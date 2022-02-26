/*
 * Copyright (c) 2012, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.sl.nodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.NodeUtil;
import com.oracle.truffle.api.nodes.NodeVisitor;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.nodes.controlflow.SLBlockNode;
import com.oracle.truffle.sl.nodes.controlflow.SLFunctionBodyNode;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;
import com.oracle.truffle.sl.nodes.rules.SLActionNode;
import com.oracle.truffle.sl.nodes.rules.SLRuleNode;
import com.oracle.truffle.sl.runtime.SLContext;

/**
 * Runs the rules :)
 */
@NodeInfo(language = "SL", description = "The root of all SL execution trees")
public class SLScriptRootNode extends RootNode {
    /** The function body that is executed, and specialized during execution. */
    @Children private SLActionNode[] beginRules;
    @Children private SLRuleNode[] rules;
    @Children private SLActionNode[] endRules;

    /** The name of the function, for printing purposes only. */
    private final String name;

    private boolean isCloningAllowed;

    private final SourceSection sourceSection;

    @CompilerDirectives.CompilationFinal(dimensions = 1) private volatile SLWriteLocalVariableNode[] argumentNodesCache;

    public SLScriptRootNode(SLLanguage language, FrameDescriptor frameDescriptor, SLActionNode[] beginRuleNodes, SLRuleNode[] ruleNodes, SLActionNode endRuleNodes[], SourceSection sourceSection, String name) {
        super(language, frameDescriptor);
        //this.bodyNode = bodyNode;
        this.beginRules = beginRuleNodes;
        this.rules = ruleNodes;
        this.endRules = endRuleNodes;

        this.name = name;
        this.sourceSection = sourceSection;
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        PrintWriter output = SLContext.get(this).getOutput();

        //output.println("Beginning execute...");

        assert SLContext.get(this) != null;
        //output.println("Setting up frame");
        // Set up our frame for BEGIN nodes
        FrameSlot field_slot = frame.getFrameDescriptor().addFrameSlot("0");
        FrameSlot nf_slot = frame.getFrameDescriptor().addFrameSlot("NF");
        FrameSlot nr_slot = frame.getFrameDescriptor().addFrameSlot("NR");
        //output.println("Running BEGIN rules");
        for (SLActionNode beginRule : beginRules) {
            //output.println("running begin rule");
            beginRule.executeVoid(frame);
        }

        // For each line in the input, perform all normal rules
        BufferedReader input = SLContext.get(this).getInput();
        //output.println("entering main loop");
        while (true) {
            //output.println("Updating the frame");
            // Update the frame :)
            String nextLine;
            try {
                nextLine = input.readLine();
                if (nextLine == null) {
                    break;
                }
            } catch (IOException ex) {
                break;
            }
            String[] fields = nextLine.trim().split("\\s+");
            frame.setObject(field_slot, fields);
            frame.setInt(nf_slot, fields.length);
            try {
                frame.setLong(nr_slot, frame.getLong(nr_slot) + 1);
            } catch (FrameSlotTypeException ex) {
                // hmm.
            }


            //output.println("Running rules");
            // Call every god damn rule in the book
            for (SLRuleNode rule : rules ) {
                //output.println("Running rule");
                rule.executeVoid(frame);
            }
        }

        // Run all END rules.
        //output.println("Running END rules");
        for (SLActionNode endRule : endRules) {
            endRule.executeVoid(frame);
        }
        //output.println("done :)");

        output.flush();

        return new Object();
    }

    //public SLExpressionNode getBodyNode() {
        //return bodyNode;
    //}

    @Override
    public String getName() {
        return name;
    }

    public void setCloningAllowed(boolean isCloningAllowed) {
        this.isCloningAllowed = isCloningAllowed;
    }

    @Override
    public boolean isCloningAllowed() {
        return isCloningAllowed;
    }

    @Override
    public String toString() {
        return "root " + name;
    }

    public final SLWriteLocalVariableNode[] getDeclaredArguments() {
        SLWriteLocalVariableNode[] argumentNodes = argumentNodesCache;
        if (argumentNodes == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            argumentNodesCache = argumentNodes = findArgumentNodes();
        }
        return argumentNodes;
    }

    private SLWriteLocalVariableNode[] findArgumentNodes() {
        List<SLWriteLocalVariableNode> writeArgNodes = new ArrayList<>(4);
        NodeUtil.forEachChild(this, new NodeVisitor() {

            private SLWriteLocalVariableNode wn; // The current write node containing a slot

            @Override
            public boolean visit(Node node) {
                // When there is a write node, search for SLReadArgumentNode among its children:
                if (node instanceof InstrumentableNode.WrapperNode) {
                    return NodeUtil.forEachChild(node, this);
                }
                if (node instanceof SLWriteLocalVariableNode) {
                    wn = (SLWriteLocalVariableNode) node;
                    boolean all = NodeUtil.forEachChild(node, this);
                    wn = null;
                    return all;
                } else if (wn != null && (node instanceof SLReadArgumentNode)) {
                    writeArgNodes.add(wn);
                    return true;
                } else if (wn == null && (node instanceof SLStatementNode && !(node instanceof SLBlockNode || node instanceof SLFunctionBodyNode))) {
                    // A different SL node - we're done.
                    return false;
                } else {
                    return NodeUtil.forEachChild(node, this);
                }
            }
        });
        return writeArgNodes.toArray(new SLWriteLocalVariableNode[writeArgNodes.size()]);
    }

}
