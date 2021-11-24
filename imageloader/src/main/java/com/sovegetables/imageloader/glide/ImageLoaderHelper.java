package com.sovegetables.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import java.io.File;



/**
 * @author tan
 */
public class ImageLoaderHelper {


    /**
     * 处理共享元素加载图片问题
     */
    public static void loadShareBackground(Context context, String url, final ImageView imageView, int placeHolderRes, final OnLoadShareBackgroundEnd onLoadShareBackgroundEnd) {
        if (TextUtils.isEmpty(url)) {
            onLoadShareBackgroundEnd.loadDefault(imageView, placeHolderRes);
            return;
        }
        GlideApp.with(context).load(handleImageUrl(url)).dontAnimate().placeholder(placeHolderRes).error(placeHolderRes).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (onLoadShareBackgroundEnd != null) {
                    onLoadShareBackgroundEnd.loadEnd(resource, imageView);
                }
            }
        });
    }

    public interface OnLoadShareBackgroundEnd {
        void loadEnd(Drawable resource, ImageView imageView);

        void loadDefault(ImageView imageView, int placeHolderRes);
    }

    /**
     * 加载圆形头像
     *
     * @param context        上下文
     * @param url            图片地址
     * @param imageView      view
     * @param placeHolderRes 占位图
     */
    public static void loadAvatar(Context context, String url, ImageView imageView, int placeHolderRes) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(placeHolderRes);
        } else {
            GlideApp.with(context).load(handleImageUrl(url)).dontAnimate().placeholder(placeHolderRes).error(placeHolderRes).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }

    public static void loadImageForFlie(Context context, File file, ImageView imageView) {
        if (file == null) {
            return;
        }
        GlideApp.with(context).load(file).into(imageView);
    }

    /**
     * 加载图片（最好不要直接加载原图像）
     *
     * @param context   上下文
     * @param url       图片地址
     * @param imageView view
     * @param resId     占位图
     */
    public static void loadImage(Context context, String url, ImageView imageView, int resId) {
        GlideApp.with(context).load(handleImageUrl(url)).dontAnimate().placeholder(resId).error(resId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载图片（最好不要直接加载原图像）
     *
     * @param context        上下文
     * @param url            图片地址
     * @param imageView      view
     * @param resId          占位图
     * @param roundingRadius 圆角半径
     */
    public static void loadImageAndCorners(Context context, String url, ImageView imageView, int resId, int roundingRadius) {
        RequestOptions options = new RequestOptions().centerCrop().transform(new RoundedCorners(roundingRadius));
        GlideApp.with(context).load(handleImageUrl(url)).apply(options).dontAnimate().placeholder(resId).error(resId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static void loadImageAndCorners(Context context, String url, ImageView imageView, int resId, int roundingRadius, float ratio) {
        int width = getScreenWidth(context);
        int height = (int) (width / ratio);
        RequestOptions options = new RequestOptions().centerCrop().override(width, height).transform(new RoundedCorners(roundingRadius));
        GlideApp.with(context).load(handleImageUrl(url)).apply(options).dontAnimate().placeholder(resId).error(resId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载指定大小的图片
     *
     * @param context   上下文
     * @param url       图片地址
     * @param imageView view
     * @param resId     占位图
     * @param width     宽
     * @param height    高
     */
    public static void loadImage(Context context, String url, ImageView imageView, int resId, int width, int height) {
        GlideApp.with(context)
                .load(handleImageUrl(url))
                .override(width, height)
                .dontAnimate()
                .placeholder(resId)
                .error(resId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    private static final String OSS_DOMAIN = "http://yst-cloud.oss-cn-shenzhen.aliyuncs.com/";

    public static String handleImageUrl(String orignUrl) {
        if (orignUrl == null || "".equals(orignUrl)) {
            return null;
        }
        if (orignUrl.startsWith("http") || orignUrl.startsWith("/data/") || orignUrl.startsWith("file://") || orignUrl.startsWith("content") || orignUrl.startsWith("android.resource")) {
            return orignUrl;
        } else {
            return OSS_DOMAIN + orignUrl;
        }
    }
}
