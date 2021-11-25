package com.dougong.widget.view.combinedView.editableOptionView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.dougong.widget.R;


/**
 * Created by Weir on 2016/11/24.
 */

public class EditableOptionView extends RelativeLayout implements IEditableOptionView {

    private Context mContext;
    private EditText mEditText;

    private View mCustomLineTop;
    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageView mImageRight;
    private View mCustomLineBottom;

    private String leftText = "";
    private int leftTextSize;
    private int leftTextColor;
    private Drawable rightImage = getResources().getDrawable(R.drawable.widget_ic_arrow_right);
    private boolean showRightImage = true;
    private String rightText = "";
    private String rightHint = "";
    private int rightTextSize = 16;
    private int rightTextColor;
    private int rightHintColor;
    private int inputType = 0;
    private boolean showBottomLine;
    private boolean showTopLine;

    public EditableOptionView(Context context) {
        this(context, null);
    }

    public EditableOptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditableOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditableOptionView, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EditableOptionView_leftText) {
                leftText = a.getString(attr);

            } else if (attr == R.styleable.EditableOptionView_leftTextSize) {
                leftTextSize = a.getDimensionPixelOffset(attr, 16);

            } else if (attr == R.styleable.EditableOptionView_leftTextColor) {
                leftTextColor = a.getColor(attr, Color.parseColor("#B3B2B2"));

            } else if (attr == R.styleable.EditableOptionView_rightImageRes) {
                rightImage = a.getDrawable(attr);

            } else if (attr == R.styleable.EditableOptionView_showRightImage) {
                showRightImage = a.getBoolean(attr, true);

            } else if (attr == R.styleable.EditableOptionView_rightText) {
                rightText = a.getString(attr);

            } else if (attr == R.styleable.EditableOptionView_rightHint) {
                rightHint = a.getString(attr);

            } else if (attr == R.styleable.EditableOptionView_rightTextSize) {
                rightTextSize = a.getDimensionPixelOffset(attr, 16);

            } else if (attr == R.styleable.EditableOptionView_rightTextColor) {
                rightTextColor = a.getColor(attr, Color.parseColor("#333333"));

            } else if (attr == R.styleable.EditableOptionView_rightHintColor) {
                rightHintColor = a.getColor(attr, Color.parseColor("#999999"));

            } else if (attr == R.styleable.EditableOptionView_inputType) {
                inputType = a.getInt(attr, EditorInfo.TYPE_CLASS_TEXT);

            } else if (attr == R.styleable.EditableOptionView_showTopLine) {
                showTopLine = a.getBoolean(attr, false);

            } else if (attr == R.styleable.EditableOptionView_showBottomLine) {
                showBottomLine = a.getBoolean(attr, false);

            }
        }
        a.recycle();

        setDefaultData();
        setDefaultClick();
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.editable_option_view, this);
        findViews(v);
    }

    private void findViews(View view) {
        mCustomLineTop = view.findViewById(R.id.custom_line_top);
        mTvLeft = view.findViewById(R.id.tvLeft);
        mTvRight = view.findViewById(R.id.tvRight);
        mImageRight = view.findViewById(R.id.imageRight);
        mCustomLineBottom = view.findViewById(R.id.custom_line_bottom);
    }

    private void setDefaultData() {
        if (!leftText.isEmpty()) {
            mTvLeft.setText(leftText);
            mTvLeft.setVisibility(VISIBLE);
        } else {
            mTvLeft.setVisibility(GONE);
        }
        if (leftTextSize != 0)
            mTvLeft.setTextSize(leftTextSize);
        if (leftTextColor != 0)
            mTvLeft.setTextColor(leftTextColor);

        mImageRight.setImageDrawable(rightImage);
        mImageRight.setVisibility(showRightImage ? VISIBLE : GONE);

        if (rightText.isEmpty()) {
            mTvRight.setText(rightHint);
            if (rightHintColor != 0)
                mTvRight.setTextColor(rightHintColor);
        } else {
            mTvRight.setText(rightText);
            if (rightTextColor != 0)
                mTvRight.setTextColor(rightTextColor);
        }
        mTvRight.setTextSize(rightTextSize);

        mCustomLineTop.setVisibility(showTopLine ? VISIBLE : GONE);
        mCustomLineBottom.setVisibility(showBottomLine ? VISIBLE : GONE);
    }

    private void setDefaultClick() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText = new EditText(mContext);
                mEditText.setHint(rightHint);
                mEditText.setText(rightText);
                if (inputType != 0) {
                    mEditText.setInputType(inputType);
                }
                mEditText.setSelection(rightText.length());
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle(mTvLeft.getText())
                        .setView(mEditText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setRightText(mEditText.getText().toString());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mEditText.getLayoutParams();
                layoutParams.setMargins(20, 0, 20, 0);
                mEditText.setLayoutParams(layoutParams);
                Window window = dialog.getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
    }

    @Override
    public void setLeftText(String text) {
        mTvLeft.setText(text);
        mTvLeft.setVisibility(VISIBLE);
    }

    @Override
    public void setLeftTextColor(int color) {
        mTvLeft.setTextColor(color);
    }

    @Override
    public void setRightImage(Drawable res) {
        mImageRight.setImageDrawable(res);
    }

    @Override
    public void setRightHint(String text) {
        rightHint = text;
        if (rightText.isEmpty()) {
            mTvRight.setText(rightHint);
            if (rightHintColor != 0)
                mTvRight.setTextColor(rightHintColor);
        } else {
            mTvRight.setText(rightText);
            if (rightTextColor != 0)
                mTvRight.setTextColor(rightTextColor);
        }
    }

    @Override
    public void setRightTextColor(int res) {
        rightTextColor = res;
    }

    @Override
    public void setRightHintColor(int res) {
        rightHintColor = res;
    }

    @Override
    public void setRightInputType(int type) {
        inputType = type;
    }

    @Override
    public String getRightText() {
        return rightText.trim();
    }

    @Override
    public void setRightText(String text) {
        rightText = text;
        if (rightText.isEmpty()) {
            mTvRight.setText(rightHint);
            if (rightHintColor != 0)
                mTvRight.setTextColor(rightHintColor);
        } else {
            mTvRight.setText(rightText);
            if (rightTextColor != 0)
                mTvRight.setTextColor(rightTextColor);
        }
    }

    /**
     * 特殊情况适用 (慎用)
     *
     * @param singleLine 默认true
     */
    public void setSingleLine(boolean singleLine) {
        mTvRight.setSingleLine(singleLine);
        mTvRight.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void isShowTopLine(boolean isShow) {
        mCustomLineTop.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void isShowBottomLine(boolean isShow) {
        mCustomLineBottom.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void setEnableClick(boolean isEnable) {
        if (!isEnable) {
            setOnClickListener(null);
        }
    }

    @Override
    public void setClickView(OnClickListener listener) {
        setOnClickListener(null);
        setOnClickListener(listener);
    }
}
