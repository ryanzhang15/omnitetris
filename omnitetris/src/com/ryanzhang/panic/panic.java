package com.ryanzhang.panic;

public class panic {

    //I literally copied this from my S1 project, because I can use it here.
    //this is very academically honest.

    //whether debug, log and error messages will be printed out respectively.
    private static final boolean DEBUG_MODE = false;
    private static final boolean LOG_MODE = false;
    private static final boolean ERROR_MODE = false;

    public static void fatalError(String msg) {
        System.out.println("FATAL ERROR: "+msg);
        System.exit(1);
    }

    public static void error(String msg) {
        if(!ERROR_MODE) return;
        System.out.println("Error: "+msg);
    }

    public static void log(String msg, String tag) {
        if(!LOG_MODE) return;
        if(tag.isEmpty()) tag = "Untagged Log";
        System.out.println("log, "+tag+": "+msg);
    }

    public static void debug(String msg) {
        if(!DEBUG_MODE) return;
        System.out.println("dbg: "+msg);
    }

}
