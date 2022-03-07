
package com.oracle.truffle.sl.nodes.global;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLGlobalRegistry;


public class SLReadGlobalNode extends SLExpressionNode {

    private final String name;

    public SLReadGlobalNode(String name) {
        this.name = name;
    }

        //List<String> BUILTIN_VARS = Arrays.asList( "ARGC", "ARGV", "CONVFMT", "ENVIRON", "FILENAME", "FNR", "FS", "NF", "NR", "OFMT", "OFS", "ORS", "RLENGTH", "RS", "RSTART", "SUBSEP");
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        SLGlobalRegistry globals = SLContext.get(this).globalRegistry;
        switch (name) {
            case "ARGC":      return globals.getArgc();
            case "ARGV":      return globals.getArgv();
            case "CONVFMT":   return globals.getConvfmt();
            case "ENVIRON":   return globals.getEnviron(); // TODO: Will this work? Returns hashmap.
            case "FILENAME":  return globals.getFilename();
            case "FNR":       return globals.getFnr();
            case "FS":        return globals.getFs();
            case "NF":        return globals.getNf();
            case "NR":        return globals.getNr();
            case "OFMT":      return globals.getOfmt();
            case "OFS":       return globals.getOfs();
            case "ORS":       return globals.getOrs();
            case "RLENGTH":   return globals.getRlength();
            case "RS":        return globals.getRs();
            case "RSTART":    return globals.getRstart();
            case "SUBSEP":    return globals.getSubsep();
        }
        return null;
    }
}