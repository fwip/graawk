package com.oracle.truffle.sl.nodes.rules;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.nodes.SLExpressionNode;

/**
 * Pattern Node - at the moment, just delegates to the sole expression child
 */
@NodeInfo(shortName = "pattern")
public final class SLPatternNode extends SLExpressionNode {

    @Child private SLExpressionNode value;

    public SLPatternNode(SLExpressionNode value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value.executeGeneric(frame);
    }
}
