
package com.oracle.truffle.sl.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLBinaryNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

/**
 * This class is similar to {@link SLDivNode}.
 */
@NodeInfo(shortName = "%%")
public abstract class SLModNode extends SLBinaryNode {

    @Specialization(rewriteOn = ArithmeticException.class)
    protected long mod(long left, long right) {
        return left % right;
    }

    @Specialization
    @TruffleBoundary
    protected SLBigNumber mod(SLBigNumber left, SLBigNumber right) {
        return new SLBigNumber(left.getValue().mod(right.getValue()));
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(this, left, right);
    }

}
