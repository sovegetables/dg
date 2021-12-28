package com.sovegetables.photoselector.photo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing.utils.ImageCompressor;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bilibili.boxing_impl.ui.BoxingViewActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.sovegetables.photoselector.R;
import com.sovegetables.photoselector.photo.bean.MediaBean;
import com.sovegetables.photoselector.photo.impl.BoxingGlideLoader;
import com.sovegetables.photoselector.photo.impl.BoxingUcrop;
import com.sovegetables.photoselector.photo.ui.DiyBoxingViewActivity;

/**
 * @author shana
 * 2017/10/30
 */

public class PhotoPickerHelper {

    public static final String KEY_VIDEO_PREVIEW_CACHE = "/photoCache";
    private static final int DEFAULT = 582;
    private static Application myApplication;

    /**
     * 初始化图片加载图设置
     *
     * @param application
     */
    public static void init(Application application) {
        myApplication = application;
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
        BoxingCrop.getInstance().init(new BoxingUcrop());
    }

    /**
     * @param activity    对象
     * @param requestCode 请求码
     * @param maxCount    最大选择数 非多图选择传1
     * @param mode        媒体类型
     */
    public static void chooseMedia(Activity activity, int requestCode, int maxCount, Mode mode) {
        BoxingConfig config = mode.equals(Mode.VIDEO) ? getVideoConfig() : getImgConfig(mode, maxCount, activity, true);
        Boxing.of(config).withIntent(activity, BoxingActivity.class).start(activity, requestCode);
    }

    public static void chooseMediaWithoutCrop(Activity activity, int requestCode, int maxCount, Mode mode) {
        BoxingConfig config = mode.equals(Mode.VIDEO) ? getVideoConfig() : getImgConfig(mode, maxCount, activity, false);
        Boxing.of(config).withIntent(activity, BoxingActivity.class).start(activity, requestCode);
    }

    public static void chooseMediaWithoutCrop(Fragment fragment, int requestCode, int maxCount, Mode mode) {
        BoxingConfig config = mode.equals(Mode.VIDEO) ? getVideoConfig() : getImgConfig(mode, maxCount, fragment.getActivity(), false);
        Boxing.of(config).withIntent(fragment.getActivity(), BoxingActivity.class).start(fragment, requestCode);
    }

    public static void chooseMedia(android.app.Fragment fragment, int requestCode, int maxCount, Mode mode) {
        BoxingConfig config = mode.equals(Mode.VIDEO) ? getVideoConfig() : getImgConfig(mode, maxCount, fragment.getActivity(), true);
        Boxing.of(config).withIntent(fragment.getActivity(), BoxingActivity.class).start(fragment, requestCode);
    }

    /**
     * 图片选择配置
     *
     * @param mode     媒体类型
     * @param maxCount 最大选择数量
     * @param b
     * @return BoxingConfig
     */
    private static BoxingConfig getImgConfig(Mode mode, int maxCount, Context context, boolean b) {
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG);
        if (mode.equals(Mode.MULTI_IMG)) {
            config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG)
                    .withMaxCount(maxCount);
        } else {
            if (b) {
                //            图片裁剪配置
                String cachePath = BoxingFileHelper.getCacheDir(context);
                if (TextUtils.isEmpty(cachePath)) {
                    Toast.makeText(context.getApplicationContext(), R.string.boxing_storage_deny, Toast.LENGTH_SHORT).show();
                } else {
                    Uri destUri = new Uri.Builder()
                            .scheme("file")
                            .appendPath(cachePath)
                            .appendPath(String.format(Locale.US, "%s.png", System.currentTimeMillis()))
                            .build();
                    config.withCropOption(new BoxingCropOption(destUri));
                }
            }
        }
        config.needCamera(android.R.drawable.ic_menu_camera)
                .withMediaCheckedRes(R.drawable.ic_circle_choose_photo_select)
                .withMediaUncheckedRes(R.drawable.ic_circle_choose_photo_normal);
        return config;
    }

    /**
     * 媒体选择配置
     *
     * @return BoxingConfig
     */
    private static BoxingConfig getVideoConfig() {
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.VIDEO);
        config.withMaxCount(1);
        config.withMediaCheckedRes(R.drawable.ic_circle_choose_photo_select)
                .withMediaUncheckedRes(R.drawable.ic_circle_choose_photo_normal);
        return config;
    }

    public static void preViewPhoto(Activity activity, @NonNull List<String> imgUrls, int startPosition) {
        ArrayList<BaseMedia> medias = new ArrayList<>(imgUrls.size());
        for (int i = 0; i < imgUrls.size(); i++) {
            medias.add(new ImageMedia("default", imgUrls.get(i)));
        }
        Boxing.get().withIntent(activity, DiyBoxingViewActivity.class, medias, startPosition).start(activity, BoxingConfig.ViewMode.PREVIEW);
    }

    public static void preViewPhoto(Fragment fragment, @NonNull List<String> imgUrls, int startPosition) {
        ArrayList<BaseMedia> medias = new ArrayList<>(imgUrls.size());
        for (int i = 0; i < imgUrls.size(); i++) {
            medias.add(new ImageMedia("default", imgUrls.get(i)));
        }
        Boxing.get().withIntent(fragment.getActivity(), BoxingViewActivity.class, medias, startPosition).start(fragment, DEFAULT, BoxingConfig.ViewMode.PREVIEW);
    }

    public static void preViewPhoto(android.app.Fragment fragment, @NonNull List<String> imgUrls, int startPosition) {
        ArrayList<BaseMedia> medias = new ArrayList<>(imgUrls.size());
        for (int i = 0; i < imgUrls.size(); i++) {
            medias.add(new ImageMedia("default", imgUrls.get(i)));
        }
        Boxing.get().withIntent(fragment.getActivity(), BoxingViewActivity.class, medias, startPosition).start(fragment, DEFAULT, BoxingConfig.ViewMode.PREVIEW);
    }

    public static ArrayList<BaseMedia> getResult(Intent data) {
        ArrayList<BaseMedia> result = Boxing.getResult(data);
        return result;
    }

    /**
     * 获取压缩图
     */
    public static String[] getCompressImg(Context context, @NonNull Intent data) {
        List<BaseMedia> baseMediaList = Boxing.getResult(data);
        final List<String> imageMedias = new ArrayList<>(baseMediaList.size());
        for (int i = 0; i < baseMediaList.size(); i++) {
            BaseMedia baseMedia = baseMediaList.get(i);
            baseMedia.setSize(new File(baseMedia.getPath()).length() + "");
            if (baseMedia instanceof ImageMedia) {
                final ImageMedia imageMedia = (ImageMedia) baseMedia;
                if (imageMedia.compress(new ImageCompressor(context), 800 * 8)) {
                    imageMedia.removeExif();
                    imageMedias.add(imageMedia.getCompressPath());
                }
            }
        }
        return imageMedias.toArray(new String[]{});
    }

    /**
     * 获取压缩图
     */
    public static List<String> getCompressImgForList(Context context, @NonNull Intent data) {
        List<BaseMedia> baseMediaList = Boxing.getResult(data);
        assert baseMediaList != null;
        final List<String> imageMedias = new ArrayList<>(baseMediaList.size());
        for (int i = 0; i < baseMediaList.size(); i++) {
            BaseMedia baseMedia = baseMediaList.get(i);
            baseMedia.setSize(new File(baseMedia.getPath()).length() + "");
            if (baseMedia instanceof ImageMedia) {
                final ImageMedia imageMedia = (ImageMedia) baseMedia;
                if (imageMedia.compress(new ImageCompressor(context), 800 * 8)) {
                    imageMedia.removeExif();
                    imageMedias.add(imageMedia.getCompressPath());
                }
            }
        }
        return imageMedias;
    }

    /**
     * 获取原图
     */
    public static String[] getRawImg(@NonNull Intent data) {
        List<BaseMedia> baseMediaList = Boxing.getResult(data);
        final List<String> imageMedias = new ArrayList<>(baseMediaList.size());
        for (int i = 0; i < baseMediaList.size(); i++) {
            BaseMedia baseMedia = baseMediaList.get(i);
            imageMedias.add(baseMedia.getPath());
        }
        return imageMedias.toArray(new String[]{});
    }

    /**
     * 获取视频并设置封面
     *
     * @param result          结果集
     * @param context         上下文
     * @param targetImageView 目标imageView
     * @param maxMb           最大大小限制
     */
    public static MediaBean getVideoAndSetPreview(ArrayList<BaseMedia> result, Context context, ImageView targetImageView, int maxMb) throws IOException {
        MediaBean mediaBean = null;
        if (result != null && result.size() > 0) {
            String videoPath = result.get(0).getPath();
            String id = result.get(0).getId();
            // 限制大小
            long size = result.get(0).getSize();
            int format1 = 1024;
            int format = 1000;
            if (size < maxMb * format1 * format) {
                Log.i("videoSize", String.format("Path:%s %s MB", videoPath, size / 1024 / 1000));
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(videoPath);

                Bitmap bitmap = media.getFrameAtTime();
                //          imageView自适应图片大小
                ViewGroup.LayoutParams layoutParams = targetImageView.getLayoutParams();
                layoutParams.height = context.getResources().getDisplayMetrics().heightPixels / 3;
                targetImageView.setAdjustViewBounds(true);
                targetImageView.setLayoutParams(layoutParams);
                targetImageView.setImageBitmap(bitmap);
                mediaBean = new MediaBean(videoPath, saveImage(bitmap, id));
            } else {
                Toast.makeText(context, String.format("视频大小超过%sMB,请重新选择", maxMb), Toast.LENGTH_LONG);
            }
        }
        //超过限制的时候返回null
        return mediaBean;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bmp bitmap对象
     * @param id
     * @return 路径
     */
    private static String saveImage(Bitmap bmp, String id) throws IOException {
        if (myApplication == null) {
            throw new NullPointerException("this application is null -- from utils library photoPicker");
        }
        // 首先保存图片
        File file = new File(myApplication.getExternalCacheDir(), "cover.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return file.getAbsolutePath();
    }

    /**
     * 选择媒体类
     */
    public enum Mode {
        /**
         * 单张图片
         * 多装图片
         * 媒体
         */
        SINGLE_IMG, MULTI_IMG, VIDEO
    }

}
