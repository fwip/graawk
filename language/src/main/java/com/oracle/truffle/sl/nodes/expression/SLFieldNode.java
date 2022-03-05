/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.oracle.truffle.sl.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLGlobalRegistry;
/**
 *
 * @author nelsonjs
 */
@NodeInfo(shortName = "field")
public class SLFieldNode extends SLExpressionNode {

    @Child private SLExpressionNode value;

    public SLFieldNode(SLExpressionNode value) {
        this.value = value;
    }


    @Override
    public Object executeGeneric(VirtualFrame frame) {
        SLGlobalRegistry globals = SLContext.get(this).globalRegistry;
        try {
            long fieldIndex =  value.executeLong(frame);
            if (fieldIndex == 0) {
                return globals.getCurrentLine();
            }
            if (fieldIndex < 0 || fieldIndex > Integer.MAX_VALUE) {
                // die about it
                throw new UnexpectedResultException("fieldIndex out of bounds");
            }
            return globals.getField((int) fieldIndex);
        }
        catch ( UnexpectedResultException ex) {
            // ??
            //throw new SLException(ex);

        }
        return "";

    }
    

}
