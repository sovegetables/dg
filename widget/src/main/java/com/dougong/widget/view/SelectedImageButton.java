package com.dougong.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;
import androidx.annotation.RequiresApi;
import com.dougong.widget.R;


public class SelectedImageButton extends ImageButton {

    private Drawable unSelectedDrawable;
    private Drawable selectedDrawable;

    public SelectedImageButton(Context context) {
        this(context, null);
    }

    public SelectedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectedImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setFocusable(true);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.SelectedImageButton, defStyleAttr, defStyleRes);
        unSelectedDrawable = a.getDrawable(R.styleable.SelectedImageButton_un_selected_src);
        selectedDrawable = a.getDrawable(R.styleable.SelectedImageButton_selected_src);
        if (unSelectedDrawable != null) {
            setImageDrawable(unSelectedDrawable);
        }
        a.recycle();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setImageDrawable(selected? selectedDrawable: unSelectedDrawable);
    }
}
