package com.dougong.widget.view.smsButton;

/**
 * 在 button 基础上加入倒计时功能
 */
public interface ISmsButtonView {
    /**
     * 开始倒计时 clickable is false
     */
    void startCountTime();

    /**
     * 停止倒计时 clickable is true
     */
    void stopCountTime();


    /**
     * 设置倒计时
     *
     * @param second 倒计时单位：秒，默认 60 秒
     */
    void setCountTime(int second);
}
