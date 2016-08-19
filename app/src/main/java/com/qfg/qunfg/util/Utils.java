package com.qfg.qunfg.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by rbtq on 8/19/16.
 */
public class Utils {

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String convertExceptionToString(Exception e) {
        return e.getMessage();
    }
}
