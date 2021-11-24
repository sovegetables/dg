package com.sovegetables.player

import android.content.Context
import android.os.Bundle
import com.sovegetables.android.logger.AppFilePaths
import com.sovegetables.android.logger.AppLogger
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLiveConstants.RENDER_ROTATION_LANDSCAPE
import com.tencent.rtmp.TXLiveConstants.RENDER_ROTATION_PORTRAIT
import com.tencent.rtmp.TXVodPlayConfig
import com.tencent.rtmp.TXVodPlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import java.io.File

open class VideoPlayer(val context: Context){

    interface OnPlayerListener{
        fun onPlayEnd()
        fun onPlayProgress(current : Int, duration: Int)
        fun onPlayStart()
        fun onPlayPause()
        fun onPlayResume()
        fun onPlayStop()
        fun onPlayFirstStart()
        fun onPlayRotationChange(vertical: Boolean)
        fun onPlayTitleChange(title: String?)
        fun onPlayWithPdfFile(pdfFileUrl: String)
        fun onNetErr()
        fun onNetStatus(player: Any?, bundle: Bundle?)
        fun onLoading()
        fun onLoadingEnd()
        fun onPrePlayStop()
    }

    open class OnSimplePlayerListener: OnPlayerListener {
        override fun onPlayTitleChange(title: String?) {
        }

        override fun onNetErr() {
        }

        override fun onNetStatus(player: Any?, bundle: Bundle?) {
        }

        override fun onLoading() {
        }

        override fun onLoadingEnd() {
        }

        override fun onPrePlayStop() {
        }

        override fun onPlayWithPdfFile(pdfFileUrl: String) {
        }

        override fun onPlayEnd() {
        }

        override fun onPlayProgress(current: Int, duration: Int) {
        }

        override fun onPlayStart() {
        }

        override fun onPlayPause() {
        }

        override fun onPlayResume() {
        }

        override fun onPlayStop() {
        }

        override fun onPlayFirstStart() {
        }

        override fun onPlayRotationChange(vertical: Boolean) {
        }

    }

    private var mVodPlayer: TXVodPlayer = TXVodPlayer(context)
    private var duration = 0
    private var progress = 0
    private var isWatchEnd = 0
    private var listener: OnPlayerListener? = null
    private var isFirst = true

    fun setOnPlayerListener(l : OnPlayerListener?){
        listener = l
    }

    companion object{
        const val TAG = "VideoPlayer"
    }


    init {
        val mConfig = TXVodPlayConfig()
        val filePath = AppFilePaths.videoCacheFolderDirPath()
        val file = File(filePath)
        val boolean = com.sovegetables.kv_cache.SpHelper.getBoolean("key.VideoPlayer3", true)
        if(boolean && file.exists()){
            file.deleteRecursively()
            com.sovegetables.kv_cache.SpHelper.saveData("key.VideoPlayer3", false)
        }
        mConfig.setCacheFolderPath(filePath)
        mConfig.setMaxCacheItems(5)
        mVodPlayer.setConfig(mConfig)
//        mVodPlayer.enableHardwareDecode(true)
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION)
        mVodPlayer.setVodListener(object : ITXVodPlayListener {
            override fun onPlayEvent(player: TXVodPlayer?, event: Int, data: Bundle?) {
                AppLogger.i(TAG, "onPlayEvent:$event")
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    if (duration > 0 && progress > 0) {
                        progress = duration
                        isWatchEnd = 1
                        isFirst = true
                        AppLogger.i("playFragment", "stop,upload record")
                        listener?.onPlayEnd()
                    }
                    duration = 0
                    player?.seek(0)
                    player?.resume()
                }

                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    if(isFirst){
                        isFirst = false
                        listener?.onPlayFirstStart()
                    }
                    listener?.onPlayStart()
                    listener?.onPlayResume()
                    listener?.onPlayProgress(progress, duration)
                }

                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    // 播放进度, 单位是秒
                    val current = data?.getInt(TXLiveConstants.EVT_PLAY_PROGRESS) ?: 0
                    if (current > 0) {
                        progress = current
                    }
                    if( duration == 0){
                        duration = data?.getInt(TXLiveConstants.EVT_PLAY_DURATION) ?: 0
                    }
                    AppLogger.i("TaskVideo progress / duration: ",  "$current  / $duration")
                    if(current >= 0 && duration > 0){
                        listener?.onPlayProgress(current, duration)
                    }
                }

                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    //网络错误，断链
                    listener?.onNetErr()
                }else if(event == TXLiveConstants.PLAY_EVT_PLAY_LOADING){
                    listener?.onLoading()
                } else if(event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END){
                    listener?.onLoadingEnd()
                }
            }
            override fun onNetStatus(player: TXVodPlayer?, p1: Bundle?) {
                listener?.onNetStatus(player, p1)
            }
        })
    }

    open fun prepare(txCloudVideoView: TXCloudVideoView){
        mVodPlayer.setPlayerView(txCloudVideoView)
    }

    open fun preload(url : String){
        mVodPlayer.setAutoPlay(false)
        mVodPlayer.startPlay(url)
    }

    open fun getProgress() :Int{
        return progress
    }

    open fun getDuration() : Int{
        return duration
    }

    open fun isPlaying(): Boolean {
        return mVodPlayer.isPlaying
    }

    fun setRenderRotation(renderRotationLandscape: Int) {
        mVodPlayer.setRenderRotation(RENDER_ROTATION_LANDSCAPE)
        listener?.onPlayRotationChange(false)
    }

    open fun play(url: String?, title: String?, isVertical : Boolean = true){
        AppLogger.i(TAG, "play:$url")
        progress = 0
        duration = 0
        isWatchEnd = 0
        mVodPlayer.setAutoPlay(true)
        mVodPlayer.startPlay(url)
        mVodPlayer.setRenderRotation(if(isVertical) RENDER_ROTATION_PORTRAIT else RENDER_ROTATION_LANDSCAPE)
        listener?.onPlayRotationChange(isVertical)
        listener?.onPlayTitleChange(title)
    }

    open fun seekTo(position: Int){
        mVodPlayer.seek(position)
    }

    open fun resume() {
        mVodPlayer.resume()
        listener?.onPlayResume()
    }

    fun isWatchEnd(): Int{
        return isWatchEnd
    }

    open fun pause(){
        if(isPlaying()){
            mVodPlayer.pause()
            listener?.onPlayPause()
        }
    }

    open fun stop(){
        listener?.onPrePlayStop()
        mVodPlayer.stopPlay(true)
        isFirst = true
        listener?.onPlayStop()
    }

    fun speedPlay(d: Float) {
        mVodPlayer.setRate(d)
    }

}

