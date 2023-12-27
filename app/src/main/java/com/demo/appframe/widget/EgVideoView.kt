package com.demo.appframe.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.source.UrlSource
import com.blankj.utilcode.util.TimeUtils
import com.demo.appframe.R
import com.demo.appframe.ext.isEmptyOrNull
import com.demo.appframe.ext.logd
import com.demo.appframe.ext.toastShort
import com.demo.appframe.util.SPUtil
import java.util.Formatter
import java.util.Locale

class EgVideoView : ConstraintLayout, DefaultLifecycleObserver, View.OnClickListener {

    private val TAG = "EgVideoView"
    private var onlyPlay: Boolean = false
    private var aliPlayerState: Int = 0
    private var currentPosition: Long = 0
    private lateinit var surfaceView: SurfaceView
    private lateinit var aliPlayer: AliPlayer
    private lateinit var controller: Group
    private lateinit var playState: ImageView
    private lateinit var mFormatter: Formatter
    private lateinit var mFormatBuilder: StringBuilder
    private var mShowing: Boolean = false
    private lateinit var mProgress: SeekBar
    private lateinit var tvCurrentPlayTime: TextView
    private lateinit var tvTotalPlayTime: TextView

    //    lateinit var mPlayer: VideoView
    private lateinit var mContext: Context
    private var mDragging: Boolean = false
    private var isPostingDismissTask = false
    private val dismissLong = 5000L
    private var clickDownTime = 0L

    var setPlayStateChangeListener: OnPlayStateChangeListener? = null
    var setPlayProgressChangeListener: OnPlayProgressChangeListener? = null

    constructor(context: Context) : super(context) {
        mContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        mContext = context
        initView()
    }

    private fun initView() {
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

        val rootView = LayoutInflater.from(mContext).inflate(R.layout.video_view, this)
        tvCurrentPlayTime = rootView.findViewById<TextView>(R.id.tvCurrentPlayTime)
        tvTotalPlayTime = rootView.findViewById<TextView>(R.id.tvTotalPlayTime)
        mProgress = rootView.findViewById<SeekBar>(R.id.seekBar)
        playState = rootView.findViewById<ImageView>(R.id.ivPlayState)
        controller = rootView.findViewById<Group>(R.id.groupController)
        playState.setOnClickListener(this)
        aliPlayer = AliPlayerFactory.createAliPlayer(mContext)
        aliPlayer.setTraceId(SPUtil.getOAID())
        aliPlayer.isAutoPlay = true
        surfaceView = rootView.findViewById<SurfaceView>(R.id.surfaceView)
        surfaceView.setOnClickListener(this)

        hide()
        initListener()
        mProgress.setOnSeekBarChangeListener(mSeekListener)
    }

    private fun initListener() {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                aliPlayer.setSurface(holder.surface)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int, width: Int, height: Int
            ) {
                aliPlayer.surfaceChanged()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                aliPlayer.setSurface(null)
            }
        })
        aliPlayer.setOnPreparedListener {
            mProgress.max = aliPlayer.duration.toInt()
            tvTotalPlayTime.text = stringForTime(aliPlayer.duration)
            aliPlayer.start()
            "vw=${aliPlayer.videoWidth} vh=${aliPlayer.videoHeight}".logd("视频信息")
        }

        aliPlayer.setOnErrorListener {
            "播放器错误-${it.code}---${it.msg}".logd()
            it.msg.toastShort()
            aliPlayer.stop()
        }

        aliPlayer.setOnInfoListener {
            if (it.code == InfoCode.CurrentPosition) {
                currentPosition = it.extraValue
                mProgress.progress = currentPosition.toInt()
                tvCurrentPlayTime.text = stringForTime(currentPosition)
                setPlayProgressChangeListener?.playChange(currentPosition)
            }
//            "阿里${GsonUtils.toJson(it)}".logd()
        }

        aliPlayer.setOnStateChangedListener {
            /*
          int idle = 0;
          int initalized = 1;
          int prepared = 2;
          int started = 3;
          int paused = 4;
          int stopped = 5;
          int completion = 6;
          int error = 7;
      */
            aliPlayerState = it
            updatePausePlay()
            setPlayStateChangeListener?.playStateChange(it)
        }
    }

    fun startPlay(videoPath: String?) {
        if (videoPath.isEmptyOrNull()) {
            return
        }
        aliPlayer.setDataSource(UrlSource().apply { uri = videoPath })
        aliPlayer.prepare()
    }

    fun start() {
        if (aliPlayerState == 6) {
            aliPlayer.seekTo(0, IPlayer.SeekMode.Accurate)
        }
        aliPlayer.start()
    }

    fun pause() {
        aliPlayer.pause()
    }

    private fun stringForTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private val dismissContorll: Runnable = object : Runnable {
        override fun run() {
            if (mDragging) {
                postDelayed(this, dismissLong)
                isPostingDismissTask = true
            } else {
                val currentTT = TimeUtils.getNowMills()
                if (currentTT - clickDownTime < dismissLong) {
                    postDelayed(this, currentTT - clickDownTime)
                    isPostingDismissTask = true
                } else {
                    hide()
                    isPostingDismissTask = false
                }
            }
        }
    }

    private val mSeekListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onStartTrackingTouch(bar: SeekBar) {
            show()
            mDragging = true
        }

        override fun onProgressChanged(bar: SeekBar, progress: Int, fromuser: Boolean) {
            if (!fromuser) {
                return
            }
            aliPlayer.seekTo(progress.toLong(), IPlayer.SeekMode.Accurate)
            setProgress()
        }

        override fun onStopTrackingTouch(bar: SeekBar) {
            mDragging = false
            setProgress()
            updatePausePlay()
            show()
        }
    }

    fun show() {
        if (!mShowing) {
            setProgress()
            if (!onlyPlay) {
                controller.visibility = VISIBLE
            }
            mShowing = true
        }
        updatePausePlay()
        if (!isPostingDismissTask) {
            postDelayed(dismissContorll, dismissLong)
            isPostingDismissTask = true
        }
    }

    private fun updatePausePlay() {
        if (aliPlayerState == 3) {
            playState.setBackgroundResource(R.mipmap.video_pause)
        } else {
            playState.setBackgroundResource(R.mipmap.video_paly)
        }
    }


    /**
     * Remove the controller from the screen.
     */
    fun hide() {
        if (mShowing) {
            if (!mDragging) {
                controller.visibility = GONE
            }
            mShowing = false
        }
    }


    private fun setProgress() {
        tvTotalPlayTime.text = stringForTime(aliPlayer.duration)
        tvCurrentPlayTime.text = stringForTime(currentPosition)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        aliPlayer.release()
    }

    override fun onClick(v: View?) {
        when (v) {
            playState -> {
                if (aliPlayerState == 3) {
                    aliPlayer.pause()
                } else {
                    if (aliPlayerState == 6) {
                        aliPlayer.seekTo(0, IPlayer.SeekMode.Accurate)
                    }
                    aliPlayer.start()
                }
                setProgress()
            }

            surfaceView -> {
                if (mShowing) {
                    hide()
                } else {
                    show()
                    clickDownTime = TimeUtils.getNowMills()
                }
            }
        }
    }

    fun isPlaying(): Boolean {
        return aliPlayerState == 3
    }

    fun setOnlyPlayMode() {
        onlyPlay = true
    }


    interface OnPlayStateChangeListener {
        fun playStateChange(state: Int)
    }

    interface OnPlayProgressChangeListener {
        fun playChange(currentProgress: Long)
    }

    fun getPlayText(): String {
        return "${tvCurrentPlayTime.text.toString()}/${tvTotalPlayTime.text.toString()}"
    }

    fun seekTo(dura: Long) {
        aliPlayer.seekTo(dura, IPlayer.SeekMode.Accurate)
        "跳跃$dura".logd(TAG)
    }

    fun getVideoWidth(): Int {
        return aliPlayer.videoWidth
    }

    fun getVideoHeight(): Int {
        return aliPlayer.videoHeight
    }

    fun getMaxDuration(): Long {
        return aliPlayer.duration
    }

}