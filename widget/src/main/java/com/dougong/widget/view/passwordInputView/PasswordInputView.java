package com.dougong.widget.view.passwordInputView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import androidx.core.content.ContextCompat;
import com.dougong.widget.R;


/**
 * <com.dougong.widget.view.passwordInputView.PasswordInputView
 * xmlns:app="http://schemas.android.com/apk/res-auto"
 * android:layout_width="match_parent"
 * android:layout_height="48dp"
 * android:id="@+id/editTextView"
 * 自定义控制参数
 * app:fontSize="14sp"
 * app:hintTextColor="@color/gray1"
 * app:textColor="@color/black"
 * app:paddingLeft="@dimen/margin_medium"
 * app:paddingRight="@dimen/margin_medium"
 * 自定义密码显示控制按钮的图标
 * app:showButtonImage="@drawable/icon_password_show_tips"
 * />
 * <p>
 * 显示密码按钮的图标是采用背景方式处理，
 * 所以为了保证图片居中，如果要另外设置背景需要使用 layer-list 方式处理
 * <?xml version="1.0" encoding="utf-8"?>
 * <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
 * <item android:gravity="center">
 * <selector xmlns:android="http://schemas.android.com/apk/res/android">
 * <item android:drawable="@drawable/icon_dont_show" android:state_checked="false"/>
 * <item android:drawable="@drawable/icon_show" android:state_checked="true"/>
 * </selector>
 * </item>
 * </layer-list>
 */
public class PasswordInputView extends LinearLayout {
    private EditText mPasswordInputView;
    private ToggleButton mPasswordShowToggleButton;

    public PasswordInputView(Context context) {
        super(context);
        init(context);
    }

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttrs(context, attrs);
    }

    public PasswordInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(context, attrs);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.password_input_view, this);
        findViews(v);

        mPasswordShowToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPasswordInputView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mPasswordInputView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mPasswordInputView.setSelection(mPasswordInputView.length());
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordInputView);

        int textColor = typedArray.getColor(R.styleable.PasswordInputView_textColor, ContextCompat.getColor(context, android.R.color.black));
        int hintTextColor = typedArray.getColor(R.styleable.PasswordInputView_hintTextColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        float textSize = typedArray.getDimensionPixelSize(R.styleable.PasswordInputView_fontSize, 16);
        String hint = typedArray.getString(R.styleable.PasswordInputView_hintText);
        mPasswordInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mPasswordInputView.setTextColor(textColor);
        mPasswordInputView.setHintTextColor(hintTextColor);

        Drawable drawable = typedArray.getDrawable(R.styleable.PasswordInputView_drawable_left);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mPasswordInputView.setCompoundDrawables(drawable, null, null, null);
        }

        int drawablePadding = typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_drawable_padding, 0);
        mPasswordInputView.setCompoundDrawablePadding(drawablePadding);
        mPasswordInputView.setHint(hint);

        int paddingLeft = typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_paddingLeft, 0);
        int paddingRight = typedArray.getDimensionPixelOffset(R.styleable.PasswordInputView_paddingRight, 0);
        this.setPadding(paddingLeft, 0, paddingRight, 0);

        int imageRes = typedArray.getResourceId(R.styleable.PasswordInputView_showButtonImage, R.drawable.icon_password_show_tips);
        mPasswordShowToggleButton.setBackgroundResource(imageRes);
    }

    /**
     * 返回 editText
     *
     * @return
     */
    public EditText getPasswordInputView() {
        return mPasswordInputView;
    }

    public ToggleButton getPasswordRightButton() {

        return mPasswordShowToggleButton;
    }

    private void findViews(View v) {
        mPasswordInputView = v.findViewById(R.id.passwordInputView);
        mPasswordShowToggleButton = v.findViewById(R.id.passwordShowToggleButton);
    }
}
