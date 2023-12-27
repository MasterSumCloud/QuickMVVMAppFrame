package com.demo.appframe.util

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.demo.appframe.R
import com.demo.appframe.ext.isNotEmptyOrNull
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import java.io.File


class GlideUtil {


    val mContext: Context

    constructor(mContext: Context) {
        this.mContext = mContext
    }


    fun dspImage(url: String?, imageView: ImageView) {
        if (mContext.isValid()) {
            Glide.with(mContext).load(url).fitCenter().dontAnimate().diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC
            ).into(imageView)
        }
    }

    fun dspVideo1Image(url: String?, imageView: ImageView) {
        if (mContext.isValid()) {
            Glide.with(mContext).setDefaultRequestOptions(RequestOptions().frame(0).centerCrop())
                .load(url).fitCenter().dontAnimate().diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                ).into(imageView)
        }
    }

    fun dspImageRound(url: String?, imageView: ImageView) {
        if (mContext.isValid()) {
            Glide.with(mContext).load(url).fitCenter().error(R.mipmap.header_ofmy_default)
                .dontAnimate().transform(CircleCrop()).diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                ).into(imageView)
        }
    }

    /**
     * 加载本地gif
     */
    fun dspImageGif(res: Int, imageView: ImageView) {
        if (mContext.isValid()) {
            Glide.with(mContext).asGif().load(res).diskCacheStrategy(
                DiskCacheStrategy.DATA
            ).into(imageView)
        }
    }

    /**
     * 加载String类型颜色
     */
    fun dspRoundColorWithString(color: String, radius: Float, imageView: ImageView) {
        if (TextUtils.isEmpty(color)) {
            return
        }
        val radiuspx = SizeUtils.dp2px(radius)
        val bitmap = Bitmap.createBitmap(radiuspx, radiuspx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val rect = Rect(0, 0, radiuspx, radiuspx)
        val rectF = RectF(rect)
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(color));
        canvas.drawARGB(0, 0, 0, 0);//设置画布背景透明
        canvas.drawOval(rectF, paint);//绘制圆点至Bitmap
        imageView.setImageBitmap(bitmap)
    }

    fun Context.isValid(): Boolean {
        val context = this
        if (context is Activity) {
            if (context.isFinishing || context.isDestroyed) {
                return false
            }
        }
        return true
    }


    fun dsplocalImagePath(imagePath: String?, imageView: ImageView) {
        if (mContext.isValid() && imagePath.isNotEmptyOrNull()) {
            Glide.with(imageView.context).load(File(imagePath)).fitCenter().dontAnimate()
                .diskCacheStrategy(
                    DiskCacheStrategy.NONE
                ).into(imageView)
        }
    }
}