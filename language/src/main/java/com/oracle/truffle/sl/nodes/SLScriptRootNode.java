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
import java.util.ArrayList;
import java.util.List;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.NodeUtil;
import com.oracle.truffle.api.nodes.NodeVisitor;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.builtins.SLReadlnBuiltin;
import com.oracle.truffle.sl.builtins.SLReadlnBuiltinFactory;
import com.oracle.truffle.sl.nodes.controlflow.SLBlockNode;
import com.oracle.truffle.sl.nodes.controlflow.SLFunctionBodyNode;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;
import com.oracle.truffle.sl.nodes.rules.SLActionNode;
import com.oracle.truffle.sl.nodes.rules.SLRuleNode;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLGlobalRegistry;

/**
 * Runs the rules :)
 */
@NodeInfo(language = "AWK", description = "The entry point for the awk execution")
public class SLScriptRootNode extends RootNode {
    /** The function body that is executed, and specialized during execution. */
    @Children final private SLActionNode[] beginRules;
    @Children final private SLRuleNode[] rules;
    @Children final private SLActionNode[] endRules;
    @Child private SLReadlnBuiltin readLineNode;

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
        SLExpressionNode[] empty = {};
        this.readLineNode = SLReadlnBuiltinFactory.create(empty);
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        //PrintWriter output = SLContext.get(this).getOutput();
        BufferedReader input = SLContext.get(this).getInput();
        SLGlobalRegistry globals = SLContext.get(this).globalRegistry;

        assert SLContext.get(this) != null;
        // Set up our frame for BEGIN nodes
        globals.setNr(0);
        globals.setFnr(0);

        // Run all BEGIN rules
        for (SLActionNode beginRule : beginRules) {
            beginRule.executeVoid(frame);
        }

        // For each line in the input, perform all normal rules
        while (true) {
            // Update the global context :)
            //String nextLine = readLineNode.readln();
            //try {
                String nextLine = readLineNode.readln();
                // TODO This sucks lol
                if (nextLine.equals("")) {
                    break;
                }
                //String nextLine = input.readLine();
                globals.setNr(globals.getNr()+1);
                globals.setFnr(globals.getFnr()+1);
                globals.setCurrentLine(nextLine);
            //} catch (IOException e) {
                //break;
            //}

            // TODO: Make sub-node and explode/unroll it?
            // Call every god damn rule in the book
            for (SLRuleNode rule : rules ) {
                rule.executeVoid(frame);
            }
        }

        // Run all END rules.
        for (SLActionNode endRule : endRules) {
            endRule.executeVoid(frame);
        }

        return new Object();
    }

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
