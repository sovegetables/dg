package com.sovegetables.android.logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.sovegetables.logger.ILog;
import com.sovegetables.logger.Logger;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

public class AppLogger {

    private AppLogger(){}
    private static final String TAG_FORMAT = "%s - %s";

    static {
        try {
            System.loadLibrary("marsxlog");
            System.loadLibrary("c++_shared");
        } catch (Exception ignored) {
        }
    }

    private final static long K_MAX_LOG_ALIVE_TIME = 3 * 24 * 60 * 60;

    private static void initXlog(String namePrefix) {
        final String logPath = AppFilePaths.xLogDirPath();
        final String cachePath = AppFilePaths.xLogCacheDirPath();
        Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, namePrefix, 0, "");
        Xlog.setConsoleLogOpen(false);
        Xlog.setMaxAliveTime(K_MAX_LOG_ALIVE_TIME);
        Log.setLogImp(new Xlog());
    }

    private static class DebugLog extends ILog{

        @Override
        public void println(LEVEL level, String tag, String message) {
            tag = tag == null? "null": tag;
            message = message == null? "null": message;
            tag = String.format(TAG_FORMAT, sTag, tag);
            if(level == LEVEL.VERBOSE){
                android.util.Log.v(tag, message);
            }else if(level == LEVEL.INFO){
                android.util.Log.i(tag, message);
            }else if(level == LEVEL.WARN){
                android.util.Log.w(tag, message);
            }else if(level == LEVEL.DEBUG){
                android.util.Log.d(tag, message);
            }else {
                android.util.Log.e(tag, message);
            }
        }
    }

    private static class XLogDelegate extends ILog{

        @Override
        public void println(LEVEL level, String tag, String message) {
            tag = tag == null? "null": tag;
            message = message == null? "null": message;
            tag = String.format(TAG_FORMAT, sTag, tag);
            if(level == LEVEL.VERBOSE){
                Log.v(tag, message);
                flush();
            }else if(level == LEVEL.INFO){
                Log.i(tag, message);
                flush();
            }else if(level == LEVEL.WARN){
                Log.w(tag, message);
                flush();
            }else if(level == LEVEL.DEBUG){
                Log.d(tag, message);
                flush();
            }else {
                Log.e(tag, message);
                flush();
            }
        }
    }

    private static String sTag = "AppLogger";

    public static void init(Context context, String tag, boolean debug) {
        Context applicationContext = context.getApplicationContext();
        sTag = tag;
        initXlog(tag);
        Logger.Delegates logImpl = new Logger.Delegates();
        if(debug){
            logImpl.addDelegate(new DebugLog());
        }
        logImpl.addDelegate(new XLogDelegate());
        Logger.Companion.setDelegate(logImpl);
        i("App VersionCode: ", String.valueOf(getVersionCode(applicationContext)));
        i("App Version: ", String.valueOf(getVersionName(applicationContext)));
        i("Device sdk version: ", String.valueOf(Build.VERSION.SDK_INT));
        i("Device Board: ", Build.BOARD);
        i("Product Device: ", Build.DEVICE);
        i("Device Product Name: ", Build.PRODUCT);
        i("Manufacturer: ", Build.MANUFACTURER);
        i("Model: ", Build.MODEL);
    }

    public static void flush(){
        Log.appenderFlush(true);
    }

    public static void close(){
        Log.appenderClose();
    }


    private static long getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return 0;
    }

    private static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    public static void i(final String tag, final String msg){
        Logger.Companion.i(tag, msg);
    }

    public static void i(final String tag, final Object arg){
        Logger.Companion.i(tag, arg == null? "null": arg.toString());
    }

    public static void d(final String tag, final String msg){
        Logger.Companion.d(tag, msg);
    }

    public static void d(final String tag, final Object arg){
        Logger.Companion.d(tag, arg == null? "null": arg.toString());
    }

    public static void e(final String tag, final Throwable throwable){
        Logger.Companion.e(tag, "", throwable);
    }

    public static void e(final String tag, final String msg){
        Logger.Companion.e(tag, msg);
    }

    public static void e(final Throwable throwable, final String msg){
        Logger.Companion.e("", msg, throwable);
    }

    public static void e(final Throwable throwable, final String tag, final String msg){
        Logger.Companion.e(tag, msg, throwable);
    }
}
