package com.htgames.rxmvp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.htgames.rxmvp.DemoApplication;

/**
 * Created by yudenghao on 2017/9/15.
 */

public class PreferencesUtils {
    private final static PreferencesUtils preferencesUtils = new PreferencesUtils(DemoApplication.getContext());
    private final static String PREFERENCE = "APP_PREFERENCE";

    private PreferencesUtils() {
        throw new RuntimeException("please user getInstance()");
    }

    private SharedPreferences sharedPreferences;

    public PreferencesUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public static PreferencesUtils getInstance() {
        return preferencesUtils;
    }
}
