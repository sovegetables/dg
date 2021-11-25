package com.dougong.widget.view.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.dougong.widget.R;

import java.util.List;


public class BannerView extends RelativeLayout {

    private int BANNER_AUTO_SCROLL_TIME = 5 * 1000;
    private ConvenientBanner mBannerView;
    private int drawable_default;
    private OnItemClickListener mListener;
    private BannerImageLoader mLoader;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_banner, null);
        addView(view);
        mBannerView = view.findViewById(R.id.bannerView);
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        setBannerHeight((int) (metrics.widthPixels / 3f));
    }

    /**
     * BannerView 配置项
     *
     * @param defaultImage   默认图
     * @param autoScrollTime 自动循环滚动时间间隔
     * @param loader         图片加载器
     */
    public void setConfig(@DrawableRes int defaultImage, int autoScrollTime, BannerImageLoader loader) {
        setConfig(defaultImage, -1, -1, autoScrollTime, loader);
    }

    /**
     * BannerView 配置项
     *
     * @param defaultImage       默认图
     * @param icon_banner_normal 未选中指示图
     * @param icon_banner_sel    选中指示图
     * @param autoScrollTime     自动循环滚动时间间隔 单位：毫秒
     * @param loader             图片加载器
     */
    public void setConfig(@DrawableRes int defaultImage, @DrawableRes int icon_banner_normal
            , @DrawableRes int icon_banner_sel, int autoScrollTime, BannerImageLoader loader) {
        if (autoScrollTime > 0) {
            BANNER_AUTO_SCROLL_TIME = autoScrollTime;
        }
        drawable_default = defaultImage;
        mBannerView.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        if (icon_banner_normal != -1 && icon_banner_sel != -1) {
            mBannerView.setPageIndicator(new int[]{icon_banner_normal, icon_banner_sel});
        }
        mLoader = loader;
    }

    /**
     * 更新banner高度
     *
     * @param height view高度   单位：px
     */
    public void setBannerHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mBannerView.getLayoutParams();
        layoutParams.height = height;
        mBannerView.setLayoutParams(layoutParams);
    }

    /**
     * 设置banner点击事件
     *
     * @param listener 回调
     */
    public void setBannerClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 设置banner数据并启动
     *
     * @param autoScroll
     * @param images
     */
    public void start(boolean autoScroll, List<String> images) {
        if (mLoader == null) {
            throw new NullPointerException("请检查  BannerView.setConfig  是否已配置成功");
        }

        if (autoScroll && !mBannerView.isTurning()) {
            mBannerView.startTurning(BANNER_AUTO_SCROLL_TIME);
        }
        mBannerView.setPages(new CBViewHolderCreator() {
            @Override
            public HomeBannerHolderVew createHolder() {
                return new HomeBannerHolderVew();
            }
        }, images);
    }

    private class HomeBannerHolderVew implements Holder<String> {
        ImageView mPhoto;

        @Override
        public View createView(Context context) {
            mPhoto = new ImageView(context);
            mPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            return mPhoto;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            if (mLoader != null) {
                mLoader.loadImage(context, data, mPhoto, drawable_default);
            }
            mPhoto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
