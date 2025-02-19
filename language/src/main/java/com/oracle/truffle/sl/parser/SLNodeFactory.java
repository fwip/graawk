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
package com.oracle.truffle.sl.parser;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLRootNode;
import com.oracle.truffle.sl.nodes.SLScriptRootNode;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBlockNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBreakNode;
import com.oracle.truffle.sl.nodes.controlflow.SLContinueNode;
import com.oracle.truffle.sl.nodes.controlflow.SLDebuggerNode;
import com.oracle.truffle.sl.nodes.controlflow.SLFunctionBodyNode;
import com.oracle.truffle.sl.nodes.controlflow.SLIfNode;
import com.oracle.truffle.sl.nodes.controlflow.SLReturnNode;
import com.oracle.truffle.sl.nodes.controlflow.SLWhileNode;
import com.oracle.truffle.sl.nodes.expression.SLAddNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLBigIntegerLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLDivNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLEqualNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLFieldNode;
import com.oracle.truffle.sl.nodes.expression.SLFunctionLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLInvokeNode;
import com.oracle.truffle.sl.nodes.expression.SLLessOrEqualNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLLessThanNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLLogicalAndNode;
import com.oracle.truffle.sl.nodes.expression.SLLogicalNotNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLLogicalOrNode;
import com.oracle.truffle.sl.nodes.expression.SLLongLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLModNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLMulNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLParenExpressionNode;
import com.oracle.truffle.sl.nodes.expression.SLReadPropertyNode;
import com.oracle.truffle.sl.nodes.expression.SLReadPropertyNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLRegexLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLRegexMatchNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLStringLiteralNode;
import com.oracle.truffle.sl.nodes.expression.SLSubNodeGen;
import com.oracle.truffle.sl.nodes.expression.SLWritePropertyNode;
import com.oracle.truffle.sl.nodes.expression.SLWritePropertyNodeGen;
import com.oracle.truffle.sl.nodes.global.SLReadGlobalNode;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.nodes.local.SLReadLocalVariableNode;
import com.oracle.truffle.sl.nodes.local.SLReadLocalVariableNodeGen;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNodeGen;
import com.oracle.truffle.sl.nodes.rules.SLActionNode;
import com.oracle.truffle.sl.nodes.rules.SLPatternNode;
import com.oracle.truffle.sl.nodes.rules.SLRuleNode;
import com.oracle.truffle.sl.nodes.util.SLUnboxNodeGen;

/**
 * Helper class used by the SL {@link Parser} to create nodes. The code is factored out of the
 * automatically generated parser to keep the attributed grammar of SL small.
 */
public class SLNodeFactory {

    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, Integer> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
        }

        public Integer find(String name) {
            Integer result = locals.get(name);
            if (result != null) {
                return result;
            } else if (outer != null) {
                return outer.find(name);
            } else {
                return null;
            }
        }
    }

    /* State while parsing a source unit. */
    private final Source source;
    private final Map<String, RootCallTarget> allFunctions;
    private final List<SLActionNode> beginRules;
    private final List<SLRuleNode> rules;
    private final List<SLActionNode> endRules;

    /* State while parsing a function. */
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private FrameDescriptor.Builder frameDescriptorBuilder;
    private List<SLStatementNode> methodNodes;

    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    private final SLLanguage language;

    public SLNodeFactory(SLLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
        this.beginRules = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.endRules = new ArrayList<>();
    }

    public Map<String, RootCallTarget> getAllFunctions() {
        // Create a fake function that runs all rules
        // TODO: This feels like a hack!
        // Integer i = 0;
        // for (SLRuleNode rule : rules) {
        //     i++;
        //     String key = "rule_" + i.toString();
        //     if (!allFunctions.containsKey(key)) {
        //         SLRootNode rule_root = new SLRootNode(language, frameDescriptor, rule, source.createUnavailableSection(), key);
        //         allFunctions.put(key, Truffle.getRuntime().createCallTarget(rule_root));
        //     }
        // }
        //if (!allFunctions.containsKey("RUN")) {
        //    SLBlockNode run_rules = new SLBlockNode(rules.toArray(new SLStatementNode[rules.size()]));
        //    // TODO: Add getline() call (how?)
        //    SLWhileNode loop_node = new SLWhileNode(new SLLongLiteralNode(1), run_rules );

        //    final SLRootNode rootNode = new SLRootNode(language, frameDescriptor, functionBodyNode, functionSrc, functionName);
        //    allFunctions.put(functionName, Truffle.getRuntime().createCallTarget(rootNode));
        //    

        //}
        return allFunctions;
    }

    public List<RootCallTarget> getAllRules() {
        List<RootCallTarget> out = new ArrayList<>();
        for (SLRuleNode rule : this.rules) {
        //    out.add(rule.getCallTarget());
        }
        return out;
    }

    public RootCallTarget createScriptRoot() {
        FrameDescriptor.Builder builder = FrameDescriptor
            .newBuilder();
        // TODO: Should I be storing these somewhere...?
        builder.addSlot(FrameSlotKind.Object, "0", null);
        builder.addSlot(FrameSlotKind.Long, "NR", null);
        builder.addSlot(FrameSlotKind.Long, "NF", null);
        builder.addSlot(FrameSlotKind.Object, "fields", null);

        SLScriptRootNode root = new SLScriptRootNode(
            language,
            builder.build(),
            beginRules.toArray(new SLActionNode[beginRules.size()]),
            rules.toArray(new SLRuleNode[rules.size()]),
            endRules.toArray(new SLActionNode[endRules.size()]),
            null,
            "run_rules"
        );

        return root.getCallTarget();
    }

    public void startFunction(Token nameToken, Token bodyStartToken) {
        assert functionStartPos == 0;
        assert functionName == null;
        assert functionBodyStartPos == 0;
        assert parameterCount == 0;
        assert frameDescriptorBuilder == null;
        assert lexicalScope == null;

        functionStartPos = nameToken.getStartIndex();
        functionName = nameToken.getText();
        functionBodyStartPos = bodyStartToken.getStartIndex();
        frameDescriptorBuilder = FrameDescriptor.newBuilder();
        methodNodes = new ArrayList<>();
        startBlock();
    }

    public void addFormalParameter(Token nameToken) {
        /*
         * Method parameters are assigned to local variables at the beginning of the method. This
         * ensures that accesses to parameters are specialized the same way as local variables are
         * specialized.
         */
        final SLReadArgumentNode readArg = new SLReadArgumentNode(parameterCount);
        readArg.setSourceSection(nameToken.getStartIndex(), nameToken.getText().length());
        SLExpressionNode assignment = createAssignment(createStringLiteral(nameToken, false), readArg, parameterCount);
        methodNodes.add(assignment);
        parameterCount++;
    }

    public void finishFunction(SLStatementNode bodyNode) {
        if (bodyNode == null) {
            // a state update that would otherwise be performed by finishBlock
            lexicalScope = lexicalScope.outer;
        } else {
            methodNodes.add(bodyNode);
            final int bodyEndPos = bodyNode.getSourceEndIndex();
            final SourceSection functionSrc = source.createSection(functionStartPos, bodyEndPos - functionStartPos);
            final SLStatementNode methodBlock = finishBlock(methodNodes, parameterCount, functionBodyStartPos, bodyEndPos - functionBodyStartPos);
            assert lexicalScope == null : "Wrong scoping of blocks in parser";

            final SLFunctionBodyNode functionBodyNode = new SLFunctionBodyNode(methodBlock);
            functionBodyNode.setSourceSection(functionSrc.getCharIndex(), functionSrc.getCharLength());

            final SLRootNode rootNode = new SLRootNode(language, frameDescriptorBuilder.build(), functionBodyNode, functionSrc, functionName);
            allFunctions.put(functionName, rootNode.getCallTarget());
        }

        functionStartPos = 0;
        functionName = null;
        functionBodyStartPos = 0;
        parameterCount = 0;
        frameDescriptorBuilder = null;
        lexicalScope = null;
    }

    public void addRule(SLRuleNode rule) {
        this.rules.add(rule);
    }
    public void addBeginRule(SLActionNode rule) {
        this.beginRules.add(rule);
    }
    public void addEndRule(SLActionNode rule) {
        this.endRules.add(rule);
    }

    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
    }

    public SLStatementNode finishBlock(List<SLStatementNode> bodyNodes, int startPos, int length) {
        return finishBlock(bodyNodes, 0, startPos, length);
    }

    public SLStatementNode finishBlock(List<SLStatementNode> bodyNodes, int skipCount, int startPos, int length) {
        lexicalScope = lexicalScope.outer;

        if (containsNull(bodyNodes)) {
            return null;
        }

        List<SLStatementNode> flattenedNodes = new ArrayList<>(bodyNodes.size());
        flattenBlocks(bodyNodes, flattenedNodes);
        int n = flattenedNodes.size();
        for (int i = skipCount; i < n; i++) {
            SLStatementNode statement = flattenedNodes.get(i);
            if (statement.hasSource() && !isHaltInCondition(statement)) {
                statement.addStatementTag();
            }
        }
        SLBlockNode blockNode = new SLBlockNode(flattenedNodes.toArray(new SLStatementNode[flattenedNodes.size()]));
        blockNode.setSourceSection(startPos, length);
        return blockNode;
    }

    private static boolean isHaltInCondition(SLStatementNode statement) {
        return (statement instanceof SLIfNode) || (statement instanceof SLWhileNode);
    }

    private void flattenBlocks(Iterable<? extends SLStatementNode> bodyNodes, List<SLStatementNode> flattenedNodes) {
        for (SLStatementNode n : bodyNodes) {
            if (n instanceof SLBlockNode) {
                flattenBlocks(((SLBlockNode) n).getStatements(), flattenedNodes);
            } else {
                flattenedNodes.add(n);
            }
        }
    }

    /**
     * Returns an {@link SLDebuggerNode} for the given token.
     *
     * @param debuggerToken The token containing the debugger node's info.
     * @return A SLDebuggerNode for the given token.
     */
    public SLStatementNode createDebugger(Token debuggerToken) {
        final SLDebuggerNode debuggerNode = new SLDebuggerNode();
        srcFromToken(debuggerNode, debuggerToken);
        return debuggerNode;
    }

    /**
     * Returns an {@link SLBreakNode} for the given token.
     *
     * @param breakToken The token containing the break node's info.
     * @return A SLBreakNode for the given token.
     */
    public SLStatementNode createBreak(Token breakToken) {
        final SLBreakNode breakNode = new SLBreakNode();
        srcFromToken(breakNode, breakToken);
        return breakNode;
    }

    /**
     * Returns an {@link SLContinueNode} for the given token.
     *
     * @param continueToken The token containing the continue node's info.
     * @return A SLContinueNode built using the given token.
     */
    public SLStatementNode createContinue(Token continueToken) {
        final SLContinueNode continueNode = new SLContinueNode();
        srcFromToken(continueNode, continueToken);
        return continueNode;
    }


    public SLRuleNode createRuleNode(SLPatternNode pattern, SLActionNode action){
        return new SLRuleNode(pattern, action);
    }
    public SLPatternNode createPatternNode(SLExpressionNode expr){
        return new SLPatternNode(expr);
    }
    public SLActionNode createActionNode(SLStatementNode statements){
        return new SLActionNode(statements);
    }

    public SLRuleNode createUnconditionalRuleNode(SLActionNode action) {
        return new SLRuleNode(
            new SLPatternNode( new SLLongLiteralNode(1) ),
            action);
    }
    /* NYI
    public SLRuleNode createPrintRuleNode(SLPatternNode pattern) {
        //SLExpressionNode printNode = createCall(new SLPrintlnBuiltin(), [], 0);
        return new SLRuleNode(pattern, new SLActionNode(printNode));
    }
    */

    /**
     * Returns an {@link SLWhileNode} for the given parameters.
     *
     * @param whileToken The token containing the while node's info
     * @param conditionNode The conditional node for this while loop
     * @param bodyNode The body of the while loop
     * @return A SLWhileNode built using the given parameters. null if either conditionNode or
     *         bodyNode is null.
     */
    public SLStatementNode createWhile(Token whileToken, SLExpressionNode conditionNode, SLStatementNode bodyNode) {
        if (conditionNode == null || bodyNode == null) {
            return null;
        }

        conditionNode.addStatementTag();
        final int start = whileToken.getStartIndex();
        final int end = bodyNode.getSourceEndIndex();
        final SLWhileNode whileNode = new SLWhileNode(conditionNode, bodyNode);
        whileNode.setSourceSection(start, end - start);
        return whileNode;
    }

    /**
     * Returns an {@link SLIfNode} for the given parameters.
     *
     * @param ifToken The token containing the if node's info
     * @param conditionNode The condition node of this if statement
     * @param thenPartNode The then part of the if
     * @param elsePartNode The else part of the if (null if no else part)
     * @return An SLIfNode for the given parameters. null if either conditionNode or thenPartNode is
     *         null.
     */
    public SLStatementNode createIf(Token ifToken, SLExpressionNode conditionNode, SLStatementNode thenPartNode, SLStatementNode elsePartNode) {
        if (conditionNode == null || thenPartNode == null) {
            return null;
        }

        conditionNode.addStatementTag();
        final int start = ifToken.getStartIndex();
        final int end = elsePartNode == null ? thenPartNode.getSourceEndIndex() : elsePartNode.getSourceEndIndex();
        final SLIfNode ifNode = new SLIfNode(conditionNode, thenPartNode, elsePartNode);
        ifNode.setSourceSection(start, end - start);
        return ifNode;
    }

    /**
     * Returns an {@link SLReturnNode} for the given parameters.
     *
     * @param t The token containing the return node's info
     * @param valueNode The value of the return (null if not returning a value)
     * @return An SLReturnNode for the given parameters.
     */
    public SLStatementNode createReturn(Token t, SLExpressionNode valueNode) {
        final int start = t.getStartIndex();
        final int length = valueNode == null ? t.getText().length() : valueNode.getSourceEndIndex() - start;
        final SLReturnNode returnNode = new SLReturnNode(valueNode);
        returnNode.setSourceSection(start, length);
        return returnNode;
    }

    /**
     * Returns the corresponding subclass of {@link SLExpressionNode} for binary expressions. </br>
     * These nodes are currently not instrumented.
     *
     * @param opToken The operator of the binary expression
     * @param leftNode The left node of the expression
     * @param rightNode The right node of the expression
     * @return A subclass of SLExpressionNode using the given parameters based on the given opToken.
     *         null if either leftNode or rightNode is null.
     */
    public SLExpressionNode createBinary(Token opToken, SLExpressionNode leftNode, SLExpressionNode rightNode) {
        if (leftNode == null || rightNode == null) {
            return null;
        }
        final SLExpressionNode leftUnboxed = SLUnboxNodeGen.create(leftNode);
        final SLExpressionNode rightUnboxed = SLUnboxNodeGen.create(rightNode);

        final SLExpressionNode result;
        switch (opToken.getText()) {
            case "+":
                result = SLAddNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "*":
                result = SLMulNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "/":
                result = SLDivNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "%":
                result = SLModNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "-":
                result = SLSubNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "<":
                result = SLLessThanNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "<=":
                result = SLLessOrEqualNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case ">":
                result = SLLogicalNotNodeGen.create(SLLessOrEqualNodeGen.create(leftUnboxed, rightUnboxed));
                break;
            case ">=":
                result = SLLogicalNotNodeGen.create(SLLessThanNodeGen.create(leftUnboxed, rightUnboxed));
                break;
            case "==":
                result = SLEqualNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "!=":
                result = SLLogicalNotNodeGen.create(SLEqualNodeGen.create(leftUnboxed, rightUnboxed));
                break;
            case "&&":
                result = new SLLogicalAndNode(leftUnboxed, rightUnboxed);
                break;
            case "||":
                result = new SLLogicalOrNode(leftUnboxed, rightUnboxed);
                break;
            case "~":
                result = SLRegexMatchNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "!~":
                result = SLLogicalNotNodeGen.create(SLRegexMatchNodeGen.create(leftUnboxed, rightUnboxed));
                break;
            default:
                throw new RuntimeException("unexpected operation: " + opToken.getText());
        }

        int start = leftNode.getSourceCharIndex();
        int length = rightNode.getSourceEndIndex() - start;
        result.setSourceSection(start, length);
        result.addExpressionTag();

        return result;
    }

    /**
     * Returns an {@link SLInvokeNode} for the given parameters.
     *
     * @param functionNode The function being called
     * @param parameterNodes The parameters of the function call
     * @param finalToken A token used to determine the end of the sourceSelection for this call
     * @return An SLInvokeNode for the given parameters. null if functionNode or any of the
     *         parameterNodes are null.
     */
    public SLExpressionNode createCall(SLExpressionNode functionNode, List<SLExpressionNode> parameterNodes, Token finalToken) {
        if (functionNode == null || containsNull(parameterNodes)) {
            return null;
        }

        final SLExpressionNode result = new SLInvokeNode(functionNode, parameterNodes.toArray(new SLExpressionNode[parameterNodes.size()]));

        final int startPos = functionNode.getSourceCharIndex();
        final int endPos = finalToken.getStartIndex() + finalToken.getText().length();
        result.setSourceSection(startPos, endPos - startPos);
        result.addExpressionTag();

        return result;
    }

    /**
     * Returns an {@link SLWriteLocalVariableNode} for the given parameters.
     *
     * @param nameNode The name of the variable being assigned
     * @param valueNode The value to be assigned
     * @return An SLExpressionNode for the given parameters. null if nameNode or valueNode is null.
     */
    public SLExpressionNode createAssignment(SLExpressionNode nameNode, SLExpressionNode valueNode) {
        return createAssignment(nameNode, valueNode, null);
    }

    /**
     * Returns an {@link SLWriteLocalVariableNode} for the given parameters.
     *
     * @param nameNode The name of the variable being assigned
     * @param valueNode The value to be assigned
     * @param argumentIndex null or index of the argument the assignment is assigning
     * @return An SLExpressionNode for the given parameters. null if nameNode or valueNode is null.
     */
    public SLExpressionNode createAssignment(SLExpressionNode nameNode, SLExpressionNode valueNode, Integer argumentIndex) {
        if (nameNode == null || valueNode == null) {
            return null;
        }

        String name = ((SLStringLiteralNode) nameNode).executeGeneric(null);

        Integer frameSlot = lexicalScope.find(name);
        boolean newVariable = false;
        if (frameSlot == null) {
            frameSlot = frameDescriptorBuilder.addSlot(FrameSlotKind.Illegal, name, argumentIndex);
            lexicalScope.locals.put(name, frameSlot);
            newVariable = true;
        }
        final SLExpressionNode result = SLWriteLocalVariableNodeGen.create(valueNode, frameSlot, nameNode, newVariable);

        if (valueNode.hasSource()) {
            final int start = nameNode.getSourceCharIndex();
            final int length = valueNode.getSourceEndIndex() - start;
            result.setSourceSection(start, length);
        }
        if (argumentIndex == null) {
            result.addExpressionTag();
        }

        return result;
    }

    /**
     * Returns a {@link SLReadLocalVariableNode} if this read is a local variable or a
     * {@link SLFunctionLiteralNode} if this read is global. In SL, the only global names are
     * functions.
     *
     * @param nameNode The name of the variable/function being read
     * @return either:
     *         <ul>
     *         <li>A SLReadLocalVariableNode representing the local variable being read.</li>
     *         <li>A SLFunctionLiteralNode representing the function definition.</li>
     *         <li>null if nameNode is null.</li>
     *         </ul>
     */
    public SLExpressionNode createRead(SLExpressionNode nameNode) {
        if (nameNode == null) {
            return null;
        }

        String name = ((SLStringLiteralNode) nameNode).executeGeneric(null);
        final SLExpressionNode result;

        List<String> BUILTIN_VARS = Arrays.asList( "ARGC", "ARGV", "CONVFMT", "ENVIRON", "FILENAME", "FNR", "FS", "NF", "NR", "OFMT", "OFS", "ORS", "RLENGTH", "RS", "RSTART", "SUBSEP");
        if (BUILTIN_VARS.contains(name)) {
            // Do thing
            result = new SLReadGlobalNode(name);
        } else {
            final Integer frameSlot = lexicalScope.find(name);
            if (frameSlot != null) {
                /* Read of a local variable. */
                result = SLReadLocalVariableNodeGen.create(frameSlot);
            } else {
                /* Read of a global name. In our language, the only global names are functions. */
                result = new SLFunctionLiteralNode(name);
            }
        }
        result.setSourceSection(nameNode.getSourceCharIndex(), nameNode.getSourceLength());
        result.addExpressionTag();
        return result;
    }

    public SLExpressionNode createFieldNode(SLExpressionNode valueNode, int sourceStart) {
        SLFieldNode field = new SLFieldNode(valueNode);
        field.setSourceSection(sourceStart, valueNode.getSourceEndIndex() - sourceStart + 1);
        return field;
    }

    public SLExpressionNode createStringLiteral(Token literalToken, boolean removeQuotes) {
        /* Remove the trailing and ending " */
        String literal = literalToken.getText();
        if (removeQuotes) {
            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
            literal = literal.substring(1, literal.length() - 1);
        }

        final SLStringLiteralNode result = new SLStringLiteralNode(literal.intern());
        srcFromToken(result, literalToken);
        result.addExpressionTag();
        return result;
    }

    public SLExpressionNode createNumericLiteral(Token literalToken) {
        SLExpressionNode result;
        try {
            /* Try if the literal is small enough to fit into a long value. */
            result = new SLLongLiteralNode(Long.parseLong(literalToken.getText()));
        } catch (NumberFormatException ex) {
            /* Overflow of long value, so fall back to BigInteger. */
            result = new SLBigIntegerLiteralNode(new BigInteger(literalToken.getText()));
        }
        srcFromToken(result, literalToken);
        result.addExpressionTag();
        return result;
    }

    public SLExpressionNode createRegexLiteral(Token literalToken) {
        String literal = literalToken.getText();

        // Remove slashes around regex body
        assert literal.length() >= 2 && literal.startsWith("/") && literal.endsWith("/");
        literal = literal.substring(1, literal.length() - 1);
        final SLRegexLiteralNode result = new SLRegexLiteralNode(literal.intern());
        srcFromToken(result, literalToken);
        result.addExpressionTag();
        return result;
    }

    public SLExpressionNode createParenExpression(SLExpressionNode expressionNode, int start, int length) {
        if (expressionNode == null) {
            return null;
        }

        final SLParenExpressionNode result = new SLParenExpressionNode(expressionNode);
        result.setSourceSection(start, length);
        return result;
    }

    /**
     * Returns an {@link SLReadPropertyNode} for the given parameters.
     *
     * @param receiverNode The receiver of the property access
     * @param nameNode The name of the property being accessed
     * @return An SLExpressionNode for the given parameters. null if receiverNode or nameNode is
     *         null.
     */
    public SLExpressionNode createReadProperty(SLExpressionNode receiverNode, SLExpressionNode nameNode) {
        if (receiverNode == null || nameNode == null) {
            return null;
        }

        final SLExpressionNode result = SLReadPropertyNodeGen.create(receiverNode, nameNode);

        final int startPos = receiverNode.getSourceCharIndex();
        final int endPos = nameNode.getSourceEndIndex();
        result.setSourceSection(startPos, endPos - startPos);
        result.addExpressionTag();

        return result;
    }

    /**
     * Returns an {@link SLWritePropertyNode} for the given parameters.
     *
     * @param receiverNode The receiver object of the property assignment
     * @param nameNode The name of the property being assigned
     * @param valueNode The value to be assigned
     * @return An SLExpressionNode for the given parameters. null if receiverNode, nameNode or
     *         valueNode is null.
     */
    public SLExpressionNode createWriteProperty(SLExpressionNode receiverNode, SLExpressionNode nameNode, SLExpressionNode valueNode) {
        if (receiverNode == null || nameNode == null || valueNode == null) {
            return null;
        }

        final SLExpressionNode result = SLWritePropertyNodeGen.create(receiverNode, nameNode, valueNode);

        final int start = receiverNode.getSourceCharIndex();
        final int length = valueNode.getSourceEndIndex() - start;
        result.setSourceSection(start, length);
        result.addExpressionTag();

        return result;
    }

    /**
     * Creates source description of a single token.
     */
    private static void srcFromToken(SLStatementNode node, Token token) {
        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    /**
     * Checks whether a list contains a null.
     */
    private static boolean containsNull(List<?> list) {
        for (Object e : list) {
            if (e == null) {
                return true;
            }
        }
        return false;
    }

}
