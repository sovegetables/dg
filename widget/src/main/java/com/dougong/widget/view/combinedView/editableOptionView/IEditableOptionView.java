package com.dougong.widget.view.combinedView.editableOptionView;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface IEditableOptionView {

    void setLeftText(String text);

    void setLeftTextColor(int res);

    void setRightImage(Drawable res);

    void setRightHint(String text);

    void setRightTextColor(int res);

    void setRightHintColor(int res);

    void setRightInputType(int type);

    String getRightText();

    void setRightText(String text);

    void setBackgroundColor(int res);

    void isShowTopLine(boolean isShow);

    void isShowBottomLine(boolean isShow);

    void setEnableClick(boolean isEnable);

    void setClickView(View.OnClickListener listener);
}
