package com.htgames.rxmvp.utils;

import android.widget.Toast;

import com.htgames.rxmvp.DemoApplication;

public class ToastUtils {
    private static Toast toast;

    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(DemoApplication.getContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
