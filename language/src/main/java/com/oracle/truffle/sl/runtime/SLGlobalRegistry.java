/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.oracle.truffle.sl.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author nelsonjs
 */
public class SLGlobalRegistry {

    // All Awk builtin variables
    private long argc;
    private ArrayList<String> argv;
    private String convfmt;
    private HashMap<String, String> environ;
    private String filename; // path?
    private long fnr;
    private String fs; // Char? Regex?
    private long nf;
    private long nr;
    private String ofmt;
    private String ofs;
    private String ors;
    private long rlength;
    private String rs;  // Char?
    private long rstart;
    private char subsep; // string?

    // Line
    private String currentLine;
    private boolean lineStale;

    // Fields
    private ArrayList<String> fields;
    private boolean fieldsStale;

    public SLGlobalRegistry() {
        this.argc = 0;
        this.argv = new ArrayList();
        this.convfmt = "%.6g";
        this.environ = new HashMap<>();
        this.filename = "";
        this.fnr = 0;
        this.fs = " ";
        this.nf = 0;
        this.nr = 0;
        this.ofmt = "%.6g";
        this.ofs = " ";
        this.ors = "\n";
        this.rlength = 0;
        this.rs = "\n";
        this.rstart = 1;
        this.subsep = '\034'; // From GAWK, is the FS (file separator) ascii character

        this.currentLine = "";
        this.fields = new ArrayList<>();
    }

    public long getArgc() {
        return this.argc;
    }


    public ArrayList<String> getArgv() {
        return this.argv;
    }


    public String getConvfmt() {
        return this.convfmt;
    }


    public HashMap<String,String> getEnviron() {
        return this.environ;
    }


    public String getFilename() {
        return this.filename;
    }


    public long getFnr() {
        return this.fnr;
    }


    public String getFs() {
        return this.fs;
    }


    public long getNf() {
        if (fieldsStale) {
            this.regenerateFields();
        }
        return this.nf;
    }


    public long getNr() {
        return this.nr;
    }


    public String getOfmt() {
        return this.ofmt;
    }


    public String getOfs() {
        return this.ofs;
    }


    public String getOrs() {
        return this.ors;
    }


    public long getRlength() {
        return this.rlength;
    }


    public String getRs() {
        return this.rs;
    }


    public long getRstart() {
        return this.rstart;
    }


    public char getSubsep() {
        return this.subsep;
    }

    public void setArgc(long argc) {
        this.argc = argc;
    }
    public void setArgv(ArrayList<String> argv) {
        this.argv = argv;
    }
    public void setConvfmt(String convfmt) {
        this.convfmt = convfmt;
    }
    public void setEnviron(HashMap<String,String> environ) {
        this.environ = environ;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setFnr(long fnr) {
        this.fnr = fnr;
    }
    public void setFs(String fs) {
        this.fs = fs;
    }
    public void setNf(long nf) {
        this.nf = nf;
    }
    public void setNr(long nr) {
        this.nr = nr;
    }
    public void setOfmt(String ofmt) {
        this.ofmt = ofmt;
    }
    public void setOfs(String ofs) {
        this.ofs = ofs;
    }
    public void setOrs(String ors) {
        this.ors = ors;
    }
    public void setRlength(long rlength) {
        this.rlength = rlength;
    }
    public void setRs(String rs) {
        this.rs = rs;
    }
    public void setRstart(long rstart) {
        this.rstart = rstart;
    }
    public void setSubsep(char subsep) {
        this.subsep = subsep;
    }

    private void regenerateLine() {
        this.currentLine = String.join(this.ofs, this.fields);
        this.lineStale = false;
    }

    private void regenerateFields() {
        // This is actually a little tricky. 
        // We split by the field separator, but multiples
        String[] fieldarray = currentLine.split(this.fs + "+");
        this.fields = new ArrayList(Arrays.asList(fieldarray));
        this.fieldsStale = false;
        this.setNf(nf);
    }

    public String getCurrentLine() {
        if (lineStale) {
            regenerateLine();           
        }
        return this.currentLine;
    }

    public void setCurrentLine(String value) {
        this.currentLine = value;
        this.fieldsStale = true;
    }

    public ArrayList<String> getFields() {
        if (fieldsStale) {
            this.regenerateFields();
        }
        return this.fields;
    }

    // getField is 1-based.
    public String getField(int slot) {
        // Slot 0 is actually the current line!
        if (slot == 0) {
            return this.getCurrentLine();
        }
        if (slot < 0) {
            // Exceptions
        }
        if (fieldsStale) {
            this.regenerateFields();
        }
        if (this.fields.size() <= slot-1) {
            return "";
        }
        return this.fields.get(slot-1);
    }

    // setField is 1-based.
    public void setField(int slot, String value) {
        this.fields.ensureCapacity(slot);
        this.fields.set(slot-1, value);
        this.lineStale = true;
    }
}
