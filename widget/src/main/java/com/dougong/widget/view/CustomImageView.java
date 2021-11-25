package com.dougong.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.dougong.widget.R;


public class CustomImageView extends AppCompatImageView {

    private static final String TAG = "CustomImageView";
    private boolean isMatrixRote;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        boolean isMatrixRote = array.getBoolean(R.styleable.CustomImageView_is_matrix_rote, false);
        setMatrixRotate(isMatrixRote);
    }

    public void setMatrixRotate(boolean isMatrixRote){
        this.isMatrixRote = isMatrixRote;
        if(isMatrixRote){
            setScaleType(ImageView.ScaleType.MATRIX);
        }else {
            setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    private void checkNeedSetMatrixRotate(Bitmap bitmap){
        if(bitmap == null){
            return;
        }
        int dwidth = bitmap.getWidth();
        int dheight = bitmap.getHeight();
        int vwidth = getWidth();
        int vheight = getHeight();
        if(isMatrixRote){
            Matrix matrix = new Matrix();
            matrix.setTranslate(dheight, 0);
            matrix.preRotate(90f);
            float scale;
            float dx = 0, dy = 0;
            if (vwidth * dwidth > dheight * vheight) {
                scale = (float) vheight / (float) dwidth;
                dx = (dheight - vwidth * scale) * 0.5f;
            }else {
                scale = (float) vwidth / (float) dheight;
                dy = (vheight - dwidth * scale) * 0.5f;
            }
            matrix.postScale(scale, scale);
            matrix.postTranslate(Math.round(dx), Math.round(dy));
            setImageMatrix(matrix);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);

        if(drawable instanceof BitmapDrawable){
            checkNeedSetMatrixRotate(((BitmapDrawable) drawable).getBitmap());
        }
    }
}
