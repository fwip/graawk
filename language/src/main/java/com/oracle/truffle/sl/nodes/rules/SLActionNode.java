
package com.oracle.truffle.sl.nodes.rules;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.nodes.SLStatementNode;

/**
 * Pattern Node - at the moment, just delegates to the sole expression child
 */
@NodeInfo(shortName = "action")
public final class SLActionNode extends SLStatementNode {

    @Child private SLStatementNode value;

    public SLActionNode(SLStatementNode value) {
        this.value = value;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        value.executeVoid(frame);
    }
}
