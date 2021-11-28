package com.sovegetables.guideline

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AbsoluteLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable
import com.sovegetables.android.logger.AppLogger
import com.sovegetables.imageloader.glide.GlideApp
import java.lang.ref.WeakReference

class AppGuideLineHelper private constructor(){

    companion object{
        const val TAG = "AppGuideLineHelper"
        val appGuideLineHelperByAuth = create()
        val appGuideLineHelperByApplyGroup = create()
        val appGuideLineHelperByTask = create()
        val appGuideLineHelperByApplyGroupMe = create()
        val appGuideLineHelperByLookTask = create()
        fun create(): AppGuideLineHelper {
            return AppGuideLineHelper()
        }
    }

    fun isShow(): Boolean{
        return guideManager.isShow()
    }

    fun hide(){
        guideManager.hide()
    }

    private val guideManager =  GuileManager()

    fun showGuildLine(context: Context?, anchorView: View, msg: String,  listener: View.OnClickListener?, imageUp: Boolean = false, @ColorInt bgColor: Int = Color.parseColor("#a0000000")){
        showGuildLine((context as Activity).window.decorView as ViewGroup, anchorView, msg, listener, imageUp, bgColor)
    }

    fun showGuildLine(parent: ViewGroup, anchorView: View, msg: String,  listener: View.OnClickListener?, imageUp: Boolean = false, @ColorInt bgColor: Int = Color.parseColor("#a0000000")){
        val guileLineTask =GuileLineTask(parent, anchorView, listener, guideManager)
        guideManager.add(guileLineTask)
        guileLineTask.showGuildLine(msg, imageUp, bgColor)
    }

    private class GuileManager{

        private val tasks = arrayListOf<GuileLineTask>()

        fun add(g: GuileLineTask){
            tasks.add(g)
        }

        fun isShow(): Boolean{
            return tasks.any { it.isShow() }
        }

        fun hide(){
            tasks.forEach {
                it.hide()
            }
            tasks.clear()
        }
    }

    private class GuileLineTask(
        parent: ViewGroup?,
        anchorView: View?,
        listener: View.OnClickListener?,
        private var guileManager: GuileManager
    ) {

        private var parent: WeakReference<ViewGroup?>? = null
        private var anchorView: WeakReference<View?>? = null
        private var guideContainer: ViewGroup? = null
        private var idleRunnable: Runnable? = null
        private var listener: View.OnClickListener? = null
        private var counter = 0
        private var showed = false

        init {
            this.parent = WeakReference(parent)
            this.anchorView = WeakReference(anchorView)
            this.listener = listener
        }

        fun isShow(): Boolean{
            return showed
        }

        fun hide(){
            if(showed && guideContainer != null && parent != null){
                parent?.get()?.let {
                    if(guideContainer != null){
                        parent?.get()?.removeView(guideContainer)
                    }
                }
                idleRunnable = null
                guideContainer = null
                showed = false
            }
        }

        fun showGuildLine(msg: String, imageUp: Boolean = false, @ColorInt bgColor: Int = Color.parseColor("#a0000000")){
            if(showed && anchorView != null && anchorView?.get()?.visibility == View.GONE){
                return
            }
            counter = 0
            val layoutListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if(!guileManager.isShow()){
                        idleRunnable = Runnable {
                            realShowGuide(this@GuileLineTask.anchorView, this@GuileLineTask.parent, msg, listener, imageUp, bgColor)
                            counter ++
                            AppLogger.i(TAG, "show: $showed counter:$counter")
                            if(!guileManager.isShow() && !showed && counter <= 2){
                                Handler().postDelayed(idleRunnable!!, 200)
                            }
                        }
                        Handler().postDelayed(idleRunnable!!, 200)
                    }
                    anchorView?.get()?.viewTreeObserver?.removeOnPreDrawListener(this)
                    return true
                }
            }
            this.anchorView?.get()?.viewTreeObserver?.addOnPreDrawListener(layoutListener)
        }

        private fun realShowGuide(
            anchorViewReference: WeakReference<View?>?,
            parentViewReference: WeakReference<ViewGroup?>?,
            msg: String,
            listener: View.OnClickListener?, imageUp: Boolean, @ColorInt bgColor: Int): Boolean {
            try {
                val anchorView = anchorViewReference?.get() ?: return false
                val parent = parentViewReference?.get() ?: return false
                val location = IntArray(2)
                anchorView.getLocationOnScreen(location)
                AppLogger.i("AppGuideLineHelper", location[0].toString() + "  " + location[1].toString())
                if (showed || (location[0] == 0 && location[1] == 0 )) {
                    return false
                }
                showed = true
                val tvMsg = TextView(parent.context)
                tvMsg.setTextColor(Color.WHITE)
                tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                tvMsg.text = msg
                tvMsg.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

                val msgH = tvMsg.measuredHeight
                val msgW = tvMsg.measuredWidth

                val measuredWidth = anchorView.measuredWidth
                val measuredHeight = anchorView.measuredHeight
                val perW = measuredWidth.coerceAtMost(measuredHeight)
                val radius = perW / 2.0f
                val de = (parent.resources.displayMetrics.density + 0.5f).toInt()
                val anchor = View(parent.context)
                val roundButtonDrawable = QMUIRoundButtonDrawable()
                roundButtonDrawable.cornerRadius = radius
                roundButtonDrawable.setStrokeData(de, ColorStateList.valueOf(Color.WHITE))
                val xCenter = (2 * location[0] + measuredWidth) / 2
                val yCenter = (2 * location[1] + measuredHeight) / 2
                val x = if (measuredWidth >= measuredHeight) { xCenter - radius.toInt() } else location[0]
                val y = if (measuredWidth >= measuredHeight) location[1] else { yCenter - radius.toInt() }
                anchor.layoutParams = AbsoluteLayout.LayoutParams(perW, perW, x, y)
                anchor.background = roundButtonDrawable
                tvMsg.layoutParams = AbsoluteLayout.LayoutParams(msgW,msgH,x - msgW - 10 * de,y - msgH - de * 10)
                val image = ImageView(parent.context)
                if(imageUp){
                    val imageW = de * 40
                    val imageH = 441 * imageW / 255
                    image.layoutParams = AbsoluteLayout.LayoutParams(imageW, imageH, xCenter - imageW / 2, y - imageH - de * 2)
                }else{
                    val imageW = de * 75
                    val imageH = de * 75
                    image.layoutParams = AbsoluteLayout.LayoutParams(imageW, imageH, xCenter - imageW / 2, y + perW / 2 + de * 2)
                }

                if(guideContainer == null){
                    guideContainer = GuileView(parent.context)
                }
                guideContainer!!.visibility = View.VISIBLE

                guideContainer!!.addView(anchor)
                guideContainer!!.addView(tvMsg)
                guideContainer!!.addView(image)

                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                guideContainer!!.layoutParams = layoutParams
                guideContainer!!.setBackgroundColor(bgColor)
                parent.addView(guideContainer)
//                anchor.setOnClickListener(listener)
                guideContainer!!.setOnClickListener {
                    listener?.onClick(it)
                    hide()
                }
                if(imageUp){
                    GlideApp.with(image).load(R.drawable.app_guide).into(image)
                }else{
                    GlideApp.with(image).load(R.drawable.guide_down).into(image)
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
            return true
        }
    }

    class GuileView: AbsoluteLayout{

        constructor(context: Context?) : super(context)

        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        )

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes)
    }


}

//data class GuideLineSaveState(var step: Int = NEW_STEP_FIRST_ME_TAB, var showed: Int = 0) {
//
//    companion object{
//        /**
//         * 首次"我的" - Flag
//         */
//        const val NEW_STEP_FIRST_ME_TAB = 10
//
//        /**
//         * 首次"实名认证" - Flag
//         */
//        const val NEW_STEP_FIRST_AUTH = 11
//
//        /**
//         * 首次"申请班组" - Flag
//         */
//        const val NEW_STEP_APPLY_GROUP = 12
//
//        /**
//         * "申请班组"后扫二维码 - Flag
//         */
//        const val NEW_STEP_QR_CODE = 13
//
//        const val NEW_STEP_TASK = 14
//
//        const val NEW_STEP_LOOK_TASK = 15
//
//        const val NEW_STEP_NOT = 16
//
//
//        private const val KEY_GUIDE_LINE = "KEY.GUIDE.LINE"
//
//        fun save(state: GuideLineSaveState){
//            SpHelper.saveData(KEY_GUIDE_LINE, state)
//        }
//
//        fun getGuideLineSaveState(): GuideLineSaveState {
//            return SpHelper.getData(
//                KEY_GUIDE_LINE,
//                GuideLineSaveState::class.java
//            )
//                ?: return GuideLineSaveState(
//                    NEW_STEP_FIRST_ME_TAB,
//                    0
//                )
//        }
//
//        fun isNeedShowFirstMeTab2(): Boolean{
//            return getGuideLineSaveState().step <= NEW_STEP_FIRST_AUTH && getGuideLineSaveState().showed == 0
//        }
//
//        fun isNeedShowFirstAuth2(): Boolean{
//            return getGuideLineSaveState().step == NEW_STEP_FIRST_AUTH && getGuideLineSaveState().showed == 0
//        }
//
//        fun isNeedShowApplyGroup(): Boolean{
//            return getGuideLineSaveState().step == NEW_STEP_APPLY_GROUP && getGuideLineSaveState().showed == 0
//        }
//
//        fun isNeedShowQRCode2(): Boolean{
//            return getGuideLineSaveState().step == NEW_STEP_QR_CODE && getGuideLineSaveState().showed == 0
//        }
//
//        fun isNeedShowTask2(): Boolean{
//            return getGuideLineSaveState().step in NEW_STEP_TASK..NEW_STEP_LOOK_TASK && getGuideLineSaveState().showed == 0
//        }
//
//        fun isNeedShowLookTask2(): Boolean{
//            return getGuideLineSaveState().step == NEW_STEP_LOOK_TASK && getGuideLineSaveState().showed == 0
//        }
//
//        fun isGuideLineNotDone2(): Boolean {
//            return getGuideLineSaveState().step < NEW_STEP_NOT
//        }
//    }
//}