package com.sovegetables.kv_cache;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.sovegetables.android.logger.AppLogger;
import com.sovegetables.util.GsonHelper;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;

public class SpHelper {

    public static void saveData(String key, Object object) {
        MMKV.defaultMMKV().encode(key, GsonHelper.toJson(object));
    }

    public static void saveData(String key, String string) {
        MMKV.defaultMMKV().encode(key, string);
    }

    public static void saveData(String key, int value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void saveData(String key, Double value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static void saveData(String key, Boolean value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public static <T> T getData(String key, Class<T> cl) {
        String s = MMKV.defaultMMKV().getString(key, "");
        if (TextUtils.isEmpty(s)) {
            return null;
        } else {
            T json = null;
            try {
                json = GsonHelper.fromJson(s, cl);
            } catch (JsonSyntaxException e) {
                AppLogger.e("getData", e);
            }
            return json;
        }
    }

    public static <T> T getData(String key, Type type) {
        String s = MMKV.defaultMMKV().getString(key, "");
        if (TextUtils.isEmpty(s)) {
            return null;
        } else {
            T json = null;
            try {
                json = GsonHelper.fromJson(s, type);
            } catch (Exception e) {
                AppLogger.e("getData", e);
            }
            return json;
        }
    }

    public static int getInt(String key) {
        return MMKV.defaultMMKV().getInt(key, 0);
    }

    public static int getInt(String key, int def) {
        return MMKV.defaultMMKV().getInt(key, def);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return MMKV.defaultMMKV().getBoolean(key, defValue);
    }

    public static Double getDouble(String key) {
        return MMKV.defaultMMKV().decodeDouble(key, 0);
    }

    public static String getString(String key) {
        return MMKV.defaultMMKV().getString(key, "");
    }

    public static String getString(String key, String def) {
        return MMKV.defaultMMKV().getString(key, def);
    }
}
