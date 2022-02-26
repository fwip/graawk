/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.oracle.truffle.sl.launcher;

import java.io.IOException;
import java.io.OutputStream;

/**
 * UnflushableOutputStream simply ignores flushing operations until close()
 * It can be useful for performance if your upstream functions flushes often.
 * @author nelsonjs
 */
public class UnflushableOutputStream extends OutputStream {
    final private OutputStream output;

    public UnflushableOutputStream(OutputStream output){
        this.output = output;
    }

    @Override
    public void flush () {
        // Do nothing
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        output.write(b, off, len);
    }
    @Override
    public void write(int n) throws IOException {
        output.write(n);
    }

    @Override
    public void close() throws IOException {
        output.flush();
        output.close();
    }
}
