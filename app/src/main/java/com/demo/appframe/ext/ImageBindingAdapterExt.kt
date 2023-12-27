package com.demo.appframe.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.demo.appframe.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import java.io.File

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNotEmptyOrNull()) {
        Glide.with(imageView.context).load(imageUrl).fitCenter().dontAnimate().diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC
        ).into(imageView)
        imageView.setBackgroundResource(0)
    }
}

@BindingAdapter("localImageUrl")
fun localImageUrl(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNotEmptyOrNull()) {
        Glide.with(imageView.context).load(File(imageUrl)).fitCenter().dontAnimate()
            .diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC
            ).into(imageView)
        imageView.setBackgroundResource(0)
    }
}


@BindingAdapter("localImageRes")
fun localImageRes(imageView: ImageView, imageRes: Int?) {
    if (imageRes != 0) {
        Glide.with(imageView.context).load(imageRes).fitCenter().dontAnimate().diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC
        ).into(imageView)
        imageView.setBackgroundResource(0)
    }
}


@BindingAdapter("localResBg")
fun localResBg(imageView: ImageView, imageRes: Int?) {
    if (imageRes != null && imageRes != 0) {
        imageView.setBackgroundResource(imageRes)
    }
}

@BindingAdapter("imageGif")
fun gifImage(imageView: ImageView, res: Int?) {
    if (res != 0) {
        Glide.with(imageView.context).asGif().load(res).into(imageView)
    }
}

@BindingAdapter("imageUrlRound")
fun loadImageRound(imageView: ImageView, imageUrl: String?) {
    if (imageUrl.isNotEmptyOrNull()) {
        Glide.with(imageView.context).load(imageUrl).fitCenter().error(R.mipmap.header_ofmy_default)
            .dontAnimate().transform(CircleCrop()).diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC
            ).into(imageView)
        imageView.setBackgroundResource(0)
    } else {
        imageView.setImageResource(R.mipmap.header_ofmy_default)
    }
}

@BindingAdapter("app:imageUrl", "app:placeHolder", "app:error")
fun loadImage(
    imageView: ImageView, url: String?, holderDrawable: Int, errorDrawable: Int
) {
    Glide.with(imageView.context).load(url).placeholder(holderDrawable).error(errorDrawable)
        .into(imageView)
}

@BindingAdapter("app:imageUrl", "app:placeHolder", "app:error")
fun loadImage(
    imageView: ImageView, url: String?, holderDrawable: Drawable?, errorDrawable: Drawable?
) {
    Glide.with(imageView.context).load(url).placeholder(holderDrawable).error(errorDrawable)
        .into(imageView)
}
