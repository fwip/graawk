package com.oracle.truffle.sl.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLBinaryNode;
import com.oracle.truffle.sl.runtime.SLPattern;

/**
 * Returns 1 if it matches, otherwise 0
 */
@NodeInfo(shortName = "~")
public abstract class SLRegexMatchNode extends SLBinaryNode {

    @Specialization
    @TruffleBoundary
    protected boolean match(String left, SLPattern right) {
        return right.getValue().matcher(left).find();
    }

    /**
     * Guard for String concatenation: returns true if either the left or the right operand is a
     * {@link String}.
     */
    protected boolean isString(Object a) {
        return a instanceof String;
    }


    @Fallback
    protected Object typeError(Object left, Object right) {
        throw SLException.typeError(this, left, right);
    }
}
