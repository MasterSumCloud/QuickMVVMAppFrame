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
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.nativeclass.PlayerConfig
import com.aliyun.player.source.UrlSource
import com.demo.appframe.R
import com.demo.appframe.ext.isEmptyOrNull
import com.demo.appframe.ext.logd
import com.demo.appframe.ext.toastShort
import com.demo.appframe.util.SPUtil
import java.util.Formatter
import java.util.Locale


class EgAudioViewAli : ConstraintLayout, DefaultLifecycleObserver, View.OnClickListener {

    private var aliPlayerState: Int = 0
    private var currentPosition: Long = 0
    private lateinit var surfaceView: SurfaceView
    private lateinit var aliPlayer: AliPlayer
    private lateinit var playState: ImageView
    private lateinit var mFormatter: Formatter
    private lateinit var mFormatBuilder: StringBuilder
    private var mShowing: Boolean = false
    private lateinit var mProgress: SeekBar
    private lateinit var tvCurrentPlayTime: TextView
    private lateinit var tvTotalPlayTime: TextView
    var playStateChangeListener: PlayStateChangeListener? = null
    private var mContext: Context
    private var mDragging: Boolean = false

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

        val rootView = LayoutInflater.from(mContext).inflate(R.layout.audio_view, this)
        tvCurrentPlayTime = rootView.findViewById<TextView>(R.id.tvCurrentPlayTime)
        tvTotalPlayTime = rootView.findViewById<TextView>(R.id.tvTotalPlayTime)
        mProgress = rootView.findViewById<SeekBar>(R.id.seekBar)
        playState = rootView.findViewById<ImageView>(R.id.ivPlayState)
        playState.setOnClickListener(this)
        aliPlayer = AliPlayerFactory.createAliPlayer(mContext)
        aliPlayer.setTraceId(SPUtil.getOAID())
        aliPlayer.isAutoPlay = true

        val config: PlayerConfig = aliPlayer.getConfig()
        config.mDisableVideo = true //设置开启纯音频播放
        aliPlayer.config = config

        surfaceView = rootView.findViewById<SurfaceView>(R.id.surfaceView)
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
            }
//            "阿里${GsonUtils.toJson(it)}".logd()
        }

        aliPlayer.setOnStateChangedListener {
            aliPlayerState = it
            updatePausePlay()
            playStateChangeListener?.onPlayStateChange(it)
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
        aliPlayer.start()
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


    private val mSeekListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onStartTrackingTouch(bar: SeekBar) {
            mDragging = true
        }

        override fun onProgressChanged(bar: SeekBar, progress: Int, fromuser: Boolean) {
            if (!fromuser) {
                return
            }
            aliPlayer.seekTo(progress.toLong())
            setProgress()
        }

        override fun onStopTrackingTouch(bar: SeekBar) {
            mDragging = false
            setProgress()
            updatePausePlay()
        }
    }

    private fun updatePausePlay() {
        if (aliPlayerState == 3) {
            playState.setBackgroundResource(R.mipmap.pause_round_white)
        } else {
            playState.setBackgroundResource(R.mipmap.play_round_white)
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
                        aliPlayer.seekTo(0)
                    }
                    aliPlayer.start()
                }
                setProgress()
            }
        }
    }

    fun isPlaying(): Boolean {
        return aliPlayerState == 3
    }

    fun pause(){
        aliPlayer.pause()
    }

    interface PlayStateChangeListener {
        fun onPlayStateChange(currentPlayState: Int)
    }

    fun getDuratioin(): Int {
        return mProgress.max
    }
}