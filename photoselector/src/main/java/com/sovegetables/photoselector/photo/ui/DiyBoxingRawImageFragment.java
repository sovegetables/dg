package com.sovegetables.photoselector.photo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.bilibili.boxing.AbsBoxingViewActivity;
import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.BoxingLog;
import com.bilibili.boxing_impl.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.sovegetables.android.logger.AppFilePaths;
import org.jetbrains.annotations.NotNull;

/**
 * show raw image with the control of finger gesture.
 *
 * @author ChenSL
 */
public class DiyBoxingRawImageFragment extends BoxingBaseFragment {
    private static final String BUNDLE_IMAGE = "com.sovegetables.photoselector.photo.ui.DiyBoxingRawImageFragment.image";
    private static final int MAX_SCALE = 15;
    private static final long MAX_IMAGE1 = 1024 * 1024L;
    private static final long MAX_IMAGE2 = 4 * MAX_IMAGE1;

    private PhotoView mImageView;
    private ProgressBar mProgress;
    private ImageMedia mMedia;
    private PhotoViewAttacher mAttacher;

    public static DiyBoxingRawImageFragment newInstance(@NonNull ImageMedia image) {
        DiyBoxingRawImageFragment fragment = new DiyBoxingRawImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMedia = getArguments().getParcelable(BUNDLE_IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_boxing_raw_image, container, false);
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgress = view.findViewById(R.id.loading);
        mImageView = view.findViewById(R.id.photo_view);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mMedia != null && !TextUtils.isEmpty(mMedia.getPath())) {
                    new AlertDialog.Builder(requireActivity())
                            .setMessage("保存当前图片到相册？")
                            .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Glide.with(requireActivity())
                                            .load(mMedia.getPath())
                                            .into(new SimpleTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                                    saveImageToGallery(getActivity(), bitmap);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                return false;
            }
        });
    }

    private static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(AppFilePaths.appCacheRootDirPath(), "Image_Download");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        Toast.makeText(context, "保存图片成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    void setUserVisibleCompat(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Point point = getResizePointer(mMedia.getSize());
            ((AbsBoxingViewActivity) getActivity()).loadRawImage(mImageView, mMedia.getPath(), point.x, point.y, new BoxingCallback(this));
        }
    }

    /**
     * resize the image or not according to size.
     *
     * @param size the size of image
     */
    private Point getResizePointer(long size) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Point point = new Point(metrics.widthPixels, metrics.heightPixels);
        if (size >= MAX_IMAGE2) {
            point.x >>= 2;
            point.y >>= 2;
        } else if (size >= MAX_IMAGE1) {
            point.x >>= 1;
            point.y >>= 1;
        } else if (size > 0) {
            // avoid some images do not have a size.
            point.x = 0;
            point.y = 0;
        }
        return point;
    }

    private void dismissProgressDialog() {
        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }
        DiyBoxingViewActivity activity = getThisActivity();
        if (activity != null && activity.mProgressBar != null) {
            activity.mProgressBar.setVisibility(View.GONE);
        }
    }

    private DiyBoxingViewActivity getThisActivity() {
        Activity activity = getActivity();
        if (activity instanceof DiyBoxingViewActivity) {
            return (DiyBoxingViewActivity) activity;
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAttacher != null) {
            mAttacher = null;
            mImageView = null;
        }
    }

    private static class BoxingCallback implements IBoxingCallback {
        private final WeakReference<DiyBoxingRawImageFragment> mWr;

        BoxingCallback(DiyBoxingRawImageFragment fragment) {
            mWr = new WeakReference<>(fragment);
        }

        @Override
        public void onSuccess() {
            if (mWr.get() == null || mWr.get().mImageView == null) {
                return;
            }
            mWr.get().dismissProgressDialog();
            Drawable drawable = mWr.get().mImageView.getDrawable();
            PhotoViewAttacher attacher = mWr.get().mAttacher;
            if (attacher != null) {
                if (drawable.getIntrinsicHeight() > (drawable.getIntrinsicWidth() << 2)) {
                    // handle the super height image.
                    int scale = drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                    scale = Math.min(MAX_SCALE, scale);
                    attacher.setMaximumScale(scale);
                    attacher.setScale(scale, true);
                }
                attacher.update();
            }
            DiyBoxingViewActivity activity = mWr.get().getThisActivity();
            if (activity != null && activity.mGallery != null) {
                activity.mGallery.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFail(Throwable t) {
            if (mWr.get() == null) {
                return;
            }
            BoxingLog.d(t != null ? t.getMessage() : "load raw image error.");
            mWr.get().dismissProgressDialog();
            mWr.get().mImageView.setImageResource(R.drawable.ic_boxing_broken_image);
            if (mWr.get().mAttacher != null) {
                mWr.get().mAttacher.update();
            }
        }
    }
}
