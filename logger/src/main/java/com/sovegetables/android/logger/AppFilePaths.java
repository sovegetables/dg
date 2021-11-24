package com.sovegetables.android.logger;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppFilePaths {

    private static boolean sFirstly = true;
    private static String APP_ROOT_DIR_NAME;
    private static Context application;

    private AppFilePaths() {
        //no instance
    }

    public static void init(Context context, String rootDirName) {
        APP_ROOT_DIR_NAME = rootDirName;
        application = context.getApplicationContext();
    }

    public static String eventTrackPath() {
        return appCacheRootDirPath() + "/eventTrack.log";
    }

    public static String videoWatchLogDirPath() {
        return appCacheRootDirPath() + "/video-watch";
    }

    private static final String ORC_DIR = appCacheRootDirPath() + "/orc";

    public static String orcFrontPath() {
        File file = new File(ORC_DIR);
        if(!file.exists()){
            file.mkdir();
        }
        return appCacheRootDirPath() + "/orc/front.jpg";
    }

    public static String orcBackPath() {
        File file = new File(ORC_DIR);
        if(!file.exists()){
            file.mkdir();
        }
        return appCacheRootDirPath() + "/orc/back.jpg";
    }

    public static String pdfCacheDirPath() {
        return appCacheRootDirPath() + "/pdf/";
    }

    public static String videoCacheFolderDirPath() {
        return appCacheRootDirPath() + "/dougong/txcache";
    }

    public static String qqBrowserDirPath() {
        return appCacheRootDirPath() + "/TbsReaderTemp";
    }

    public static String apkUpdateCacheDirPath() {
        return appCacheRootDirPath() + "/update/";
    }

    public static String faceCacheDirPath() {
        return AppFilePaths.appCacheRootDirPath() + "/download/faces/";
    }

    public static String xLogDirPath() {
        return AppFilePaths.appCacheRootDirPath() + "/xlog";
    }

    public static String xLogCacheZipPath() {
        String zipDir = AppFilePaths.appCacheRootDirPath() + "/zip";
        File file = new File(zipDir);
        if(!file.exists()){
            file.mkdir();
        }
        return zipDir;
    }

    public static String xLogCacheDirPath() {
        return AppFilePaths.appCacheRootDirPath() + "/xlog_cache";
    }

    public static List<File> getCanCleanCacheDirs() {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(pdfCacheDirPath()));
        files.add(new File(videoCacheFolderDirPath()));
        files.add(new File(apkUpdateCacheDirPath()));
        files.add(new File(faceCacheDirPath()));
        files.add(new File(xLogDirPath()));
        files.add(new File(xLogCacheDirPath()));
        return files;
    }

    private static void cleanCacheFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    cleanCacheFile(f);
                }
            }
        }
    }

    public static void cleanCache() {
        List<File> cacheDirs = getCanCleanCacheDirs();
        for (File f : cacheDirs) {
            cleanCacheFile(f);
        }
    }

    public static int getCanCleanCacheSize() {
        List<File> cacheDirs = getCanCleanCacheDirs();
        int size = 0;
        for (File f : cacheDirs) {
            size += getFileSize(f);
        }
        return size;
    }

    //递归打印获得文件夹大小
    private static int getFileSize(File f) {
        int sum = 0;
        if (null != f && f.exists()) {
            if (f.isFile()) {
                //是文件才能获得大小
                sum += f.length();
            } else if (f.isDirectory()) {
                File[] ss = f.listFiles();
                for (File r : ss) {
                    sum += getFileSize(r);
                }
            }
        }
        return sum;
    }

    public static String appCacheRootDirPath() {
        String root;
        try {
            File externalCacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!externalCacheDir.exists()) {
                root = application.getCacheDir().getAbsolutePath() + "/" + APP_ROOT_DIR_NAME;
            } else {
                root = externalCacheDir.getAbsolutePath() + "/" + APP_ROOT_DIR_NAME;
            }
        } catch (Exception e) {
            root = application.getCacheDir().getAbsolutePath() + "/" + APP_ROOT_DIR_NAME;
        }
        if (sFirstly) {
            sFirstly = false;
            try {
                File file = new File(root);
                if (!file.exists()) {
                    file.mkdir();
                }
            } catch (Exception ignored) {
            }
        }
        return root;
    }


}
