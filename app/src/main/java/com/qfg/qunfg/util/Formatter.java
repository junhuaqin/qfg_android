package com.qfg.qunfg.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by rbtq on 8/12/16.
 */
public class Formatter {
    public static String formatCurrency(float f) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        return nf.format(f);
    }

    public static float currency2fg(int money) {
        return money/100.f;
    }

    public static float currency2fg(long money) {
        return money/100.f;
    }

    public static int currency2bg(float money) {
        return (int)(money*100);
    }

}
