package com.oracle.truffle.sl.nodes.rules;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;

/**
 * Rule Node - Runs an action if an expression is true
 */
@NodeInfo(shortName = "rule")
public final class SLRuleNode extends SLExpressionNode {

    @Child private SLPatternNode pattern;
    @Child private SLActionNode action;

    public SLRuleNode(SLPatternNode pattern, SLActionNode action) {
        this.pattern = pattern;
        this.action = action;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            if (pattern.executeBoolean(frame)) {
                action.executeVoid(frame);
            }
            return 1;
        } catch (UnexpectedResultException ex){
            // TODO: ?????
            // throw SLException.typeError(this, ex.getResult());
            return 0;
        }
    }
}
