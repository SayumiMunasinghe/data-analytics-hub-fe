package com.example.dataanalyticshubfe;

import java.util.ArrayList;

/**
 * Taken from sample solution given on Canvas
 * Custom exception for invalid argument of methods and program
 *
 * @author AA
 * @version 1.0.0
 */
public class InvalidArgumentException extends Exception {
    ArrayList<String> args;

    public InvalidArgumentException(String msg) {
        super(msg);
    }

    public InvalidArgumentException(String msg, ArrayList<String> args) {
        super(msg);
        this.args = args;
    }
}
