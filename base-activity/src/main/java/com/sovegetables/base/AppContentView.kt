package com.sovegetables.base

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.sovegetables.*
import com.sovegetables.imageloader.glide.GlideApp
import com.sovegetables.topnavbar.ActionBarView
import com.sovegetables.topnavbar.ITopBarAction

class AppContentView: IContentView {

    private lateinit var actionBarView: ActionBarView
    private lateinit var flContent: FrameLayout
    private lateinit var context: Context
    private lateinit var emptyViewAndLoadingViewContainer : View
    private lateinit var ivLoading: ImageView
    private lateinit var qmuiEmptyView: EmptyOrErrorView
    private lateinit var appBarContent: ViewGroup

    override fun addViewBelowTopBar(view: View) {
        appBarContent.addView(view)
    }

    override fun addViewBelowTopBar(layoutRes: Int) {
        appBarContent.addView(LayoutInflater.from(appBarContent.context).inflate(layoutRes, appBarContent, false))
    }

    override fun getEmptyController(): IEmptyController {
        return object : IEmptyController{

            override fun hideEmpty() {
                flContent.visibility = View.VISIBLE
                emptyViewAndLoadingViewContainer.visibility = View.GONE
                qmuiEmptyView.visibility = View.GONE
            }

            override fun showEmpty(msg: String?, icon: Int, model: IEmptyController.Model?) {
                flContent.visibility = View.GONE
                emptyViewAndLoadingViewContainer.visibility = View.VISIBLE
                qmuiEmptyView.visibility = View.VISIBLE
                ivLoading.visibility = View.GONE
                if(model == null){
                    if(icon > 0){
                        try {
                            qmuiEmptyView.setEmptyImage(ContextCompat.getDrawable(flContent.context, icon))
                        } catch (ignored: Exception) {
                        }
                    }
                    qmuiEmptyView.setTitleText(msg)
                }else{
                    if(!TextUtils.isEmpty(model.btnText)){
                        qmuiEmptyView.setButton(model.btnText, model.listener)
                    }
                    if(!TextUtils.isEmpty(model.title)){
                        qmuiEmptyView.setTitleText(model.title)
                    }
                    if(model.icon > 0){
                        try {
                            qmuiEmptyView.setEmptyImage(ContextCompat.getDrawable(flContent.context, model.icon))
                        } catch (ignored: Exception) {
                        }
                    }
                }

            }

        }
    }

    override fun getLoadingController(): ILoadingController {
        return object : ILoadingController{
            override fun hideLoading() {
                flContent.visibility = View.VISIBLE
                emptyViewAndLoadingViewContainer.visibility = View.GONE
            }

            override fun isLoadingShow(): Boolean {
                return emptyViewAndLoadingViewContainer.visibility == View.VISIBLE && ivLoading.visibility == View.VISIBLE
            }

            override fun showLoading() {
                flContent.visibility = View.GONE
                emptyViewAndLoadingViewContainer.visibility = View.VISIBLE
                qmuiEmptyView.visibility = View.GONE
                ivLoading.visibility = View.VISIBLE
                GlideApp.with(ivLoading)
                    .load(R.drawable.app_loading)
                    .into(ivLoading)
            }

        }
    }

    override fun getLoadingDialogController(): ILoadingDialogController {
        return QUMILoadingController(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
    }

    override fun onCreateContentView(view: View): View {
        context = view.context
        val rootView = LayoutInflater.from(view.context).inflate(R.layout.layout_activity_base, null)
        appBarContent = rootView.findViewById(R.id.ll_action_bar)
        actionBarView = rootView.findViewById(R.id.quick_action_bar)
        flContent = rootView.findViewById(R.id.fl_base_content)
        emptyViewAndLoadingViewContainer = rootView.findViewById(R.id.csl_top_view)
        ivLoading = rootView.findViewById(R.id.iv_loading)
        qmuiEmptyView = rootView.findViewById(R.id.base_emptyView)
        flContent.addView(view)
        return rootView
    }

    override fun onCreateTopBarAction(): ITopBarAction {
        return actionBarView
    }
}