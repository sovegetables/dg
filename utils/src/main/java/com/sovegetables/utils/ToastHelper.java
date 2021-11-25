package com.sovegetables.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static Toast mToast;

    public static void ShowToast(int resId, Context con) {
        if (con == null) {
            return;
        }
        Context context = con.getApplicationContext();
        String text = context.getString(resId);
        mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void ShowToast(String text, Context con) {
        if (con == null || text == null) {
            return;
        }
        Context context = con.getApplicationContext();
        mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

}
