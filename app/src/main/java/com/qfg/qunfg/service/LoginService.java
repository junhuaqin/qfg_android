package com.qfg.qunfg.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by rbtq on 8/23/16.
 */
public class LoginService {
    private static final String PREF_LOGGEDIN_USER_NAME = "logged_in_username";
    private static final String PREF_LOGGEDIN_USER_TOKEN = "logged_in_token";
    private static final String PREFS_NAME = "LoginState";

    private static String USERNAME = "";
    private static String TOKEN = "";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, 0);
    }

    public static void setLoggedInUserToken(Context ctx, String name, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_NAME, name);
        editor.putString(PREF_LOGGEDIN_USER_TOKEN, token);
        USERNAME = name;
        TOKEN = token;
        editor.apply();
    }

    public static String getLoggedInUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_NAME, "");
    }

    public static String getLoggedInToken(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_TOKEN, "");
    }

    public static String getLoggedInUserName() {
        return USERNAME;
    }

    public static String getLoggedInToken() {
        return TOKEN;
    }

    public static boolean isLoggedIn(Context ctx) {
        USERNAME = getLoggedInUserName(ctx);
        TOKEN = getLoggedInToken(ctx);
        return (!TextUtils.isEmpty(USERNAME)) && (!TextUtils.isEmpty(TOKEN));
    }

    public static void clearLoggedInStatus(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_NAME);
        editor.remove(PREF_LOGGEDIN_USER_TOKEN);
        USERNAME = "";
        TOKEN = "";
        editor.apply();
    }
}
