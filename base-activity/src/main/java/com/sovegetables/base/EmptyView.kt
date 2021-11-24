package com.sovegetables.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_empty.view.*

class EmptyView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true)
    }

    fun setImage(res: Int) {
        ivIcon.setImageResource(res)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setTips(text: String) {
        tvTips.text = text
    }

    fun setButton(text: String, listener: View.OnClickListener) {
        btnSubmit.text = text
        btnSubmit.setOnClickListener(listener)
    }

    fun showNetworkError(listener: View.OnClickListener) {
        visibility = View.VISIBLE
        ivIcon.visibility = View.GONE
        tvTitle.text = "没有数据"
        tvTips.text = "请检查网络是否正常"
        btnSubmit.text = "重新加载"
        btnSubmit.setOnClickListener(listener)
    }

    fun showNetworkError(res: Int, listener: View.OnClickListener) {
        visibility = View.VISIBLE
        ivIcon.setImageResource(res)
        ivIcon.visibility = View.GONE
        tvTitle.text = "没有数据"
        tvTips.text = "请检查网络是否正常"
        btnSubmit.text = "重新加载"
        btnSubmit.setOnClickListener(listener)
    }

    fun showEmptyView(res: Int, text: String){
        visibility = View.VISIBLE
        ivIcon.setImageResource(res)
        ivIcon.visibility = View.GONE
        tvTitle.text = text
        tvTips.visibility = View.GONE
        btnSubmit.visibility = View.GONE
    }

    fun showLogin(res: Int, listener: View.OnClickListener) {
        visibility = View.VISIBLE
        tvTitle.visibility = View.VISIBLE
        ivIcon.setImageResource(res)
        ivIcon.visibility = View.GONE
        tvTitle.text = "你还没有登录"
        tvTips.visibility = View.GONE
        btnSubmit.text = "去登录"
        btnSubmit.setOnClickListener(listener)
    }
}