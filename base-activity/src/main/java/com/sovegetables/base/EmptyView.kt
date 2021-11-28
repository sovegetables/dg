package com.sovegetables.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sovegetables.base.databinding.ViewEmptyBinding

class EmptyView : ConstraintLayout {

    private lateinit var emptyBinding: ViewEmptyBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        emptyBinding = ViewEmptyBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setImage(res: Int) {
        emptyBinding.ivIcon.setImageResource(res)
    }

    fun setTitle(title: String) {
        emptyBinding.tvTitle.text = title
    }

    fun setTips(text: String) {
        emptyBinding.tvTips.text = text
    }

    fun setButton(text: String, listener: View.OnClickListener) {
        emptyBinding.btnSubmit.text = text
        emptyBinding.btnSubmit.setOnClickListener(listener)
    }

    fun showNetworkError(listener: View.OnClickListener) {
        visibility = View.VISIBLE
        emptyBinding.ivIcon.visibility = View.GONE
        emptyBinding.tvTitle.text = "没有数据"
        emptyBinding.tvTips.text = "请检查网络是否正常"
        emptyBinding.btnSubmit.text = "重新加载"
        emptyBinding.btnSubmit.setOnClickListener(listener)
    }

    fun showNetworkError(res: Int, listener: View.OnClickListener) {
        visibility = View.VISIBLE
        emptyBinding.ivIcon.setImageResource(res)
        emptyBinding.ivIcon.visibility = View.GONE
        emptyBinding.tvTitle.text = "没有数据"
        emptyBinding.tvTips.text = "请检查网络是否正常"
        emptyBinding.btnSubmit.text = "重新加载"
        emptyBinding.btnSubmit.setOnClickListener(listener)
    }

    fun showEmptyView(res: Int, text: String){
        visibility = View.VISIBLE
        emptyBinding.ivIcon.setImageResource(res)
        emptyBinding.ivIcon.visibility = View.GONE
        emptyBinding.tvTitle.text = text
        emptyBinding.tvTips.visibility = View.GONE
        emptyBinding.btnSubmit.visibility = View.GONE
    }

    fun showLogin(res: Int, listener: View.OnClickListener) {
        visibility = View.VISIBLE
        emptyBinding.tvTitle.visibility = View.VISIBLE
        emptyBinding.ivIcon.setImageResource(res)
        emptyBinding.ivIcon.visibility = View.GONE
        emptyBinding.tvTitle.text = "你还没有登录"
        emptyBinding.tvTips.visibility = View.GONE
        emptyBinding.btnSubmit.text = "去登录"
        emptyBinding.btnSubmit.setOnClickListener(listener)
    }
}