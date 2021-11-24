package com.sovegetables.kv_cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sovegetables.android.logger.AppLogger;
import com.tencent.mmkv.MMKV;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class DataInterface {

    public static Context context;

    private static final String KEY_DEVICES_ID = "key_devices_id";

    public static void init(Context context) {
        DataInterface.context = context.getApplicationContext();
    }

    public static String getDeviceId(){
        MMKV mmkv = MMKV.defaultMMKV();
        String deviceId = mmkv.decodeString(KEY_DEVICES_ID);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String currentTime = formatter.format(new Date());

        Date date = null;
        try {
            date = formatter.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        if(TextUtils.isEmpty(deviceId)){
            deviceId = "Android" + ts;
            mmkv.encode(KEY_DEVICES_ID, deviceId);
        }
        AppLogger.i(KEY_DEVICES_ID, deviceId);
        return deviceId;
    }
}

