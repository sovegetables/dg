package com.sovegetables.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 类功能描述：软键盘管理界面</br>
 *
 * @author yuyahao
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */
public class InputManager {
    public static InputManager inputManager;
    private Context context;
    private InputMethodManager imm;

    private InputManager(Context context) {
        this.context = context;
        // 得到InputMethodManager的实例
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // totleShowSoftInput();
    }

    public static InputManager getInstances(Context context) {
        if (inputManager == null) {
            inputManager = new InputManager(context);
        }
        return inputManager;
    }

    /**
     * 切换软键盘的显示与隐藏
     */
    public void totleShowSoftInput() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 判断软键盘 弹出
     */
    public void showSoftInput() {
        if (!imm.isActive()) { //
            imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        }
    }

    public void showSoftInput(EditText editTextMessage) {
        editTextMessage.requestFocus();
        imm.showSoftInput(editTextMessage, 0);
    }

    /**
     * 关闭软键盘
     * 针对于 有一个EdtxtView
     *
     * @param input_email
     */
    public void hideSoftInput(EditText input_email) {
        if (imm.isActive()) {
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.hideSoftInputFromWindow(input_email.getWindowToken(), 0);
        }
    }

    /**
     * 针对于 有多个EdtxtView
     * 关闭所有的软键盘
     */
    public void hideALlSoftInput() {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
