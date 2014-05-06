package com.roombooking.util;


public class LoggerUtil {
    public static String getClassName(){
       return new Throwable().getStackTrace()[1].getClassName();
    }
}
