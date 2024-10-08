package com.nacho.restaurantapplication.core.utils

import com.bumptech.glide.request.target.Target
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

object ImageLoader {
    fun loadImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        onLoadFailed: (() -> Unit)? = null,
        onResourceReady: (() -> Unit)? = null
    ) {
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.visibility = View.VISIBLE
                    onResourceReady?.invoke()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.visibility = View.GONE
                    onLoadFailed?.invoke()
                    return true
                }
            })
            .into(imageView)
    }
}