package com.oracle.truffle.sl.runtime;

import java.util.regex.Pattern;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.sl.SLLanguage;

@ExportLibrary(InteropLibrary.class)
@SuppressWarnings("static-method")
public final class SLPattern implements TruffleObject {

    private final Pattern value;

    public SLPattern(Pattern value) {
        this.value = value;
    }

    public SLPattern(String value) {
        this.value = Pattern.compile(value);
    }

    public Pattern getValue() {
        return value;
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return value.toString();
    }

    @Override
    @TruffleBoundary
    public boolean equals(Object obj) {
        if (obj instanceof SLPattern) {
            return this.value.equals(((SLPattern) obj).getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isNumber() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInByte() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInShort() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInFloat() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInLong() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInInt() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    boolean fitsInDouble() {
        return false;
    }

    @ExportMessage
    @TruffleBoundary
    double asDouble() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    long asLong() throws UnsupportedMessageException {
            throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    byte asByte() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    int asInt() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    float asFloat() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @TruffleBoundary
    short asShort() throws UnsupportedMessageException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return SLLanguage.class;
    }

    @ExportMessage
    boolean hasMetaObject() {
        return true;
    }

    @ExportMessage
    Object getMetaObject() {
        return SLType.PATTERN;
    }

    @ExportMessage
    @TruffleBoundary
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return value.toString();
    }

}
