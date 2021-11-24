package com.sovegetables.utils;

/**
 * Created by yanggangjun on 16/7/29.
 */
public class FastClickUtil {

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
