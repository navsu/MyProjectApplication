package com.memandis.remote.utils.binding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
//
//inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
//    block()
//    executePendingBindings()
//}
//
//@BindingAdapter("imageFromUrl","placeholder","error", "withCrossFade", "requestListener", requireAll = false)
//fun bindImageFromUrl(
//    view: ImageView,
//    imageUrl: String?,
//    placeHolder: Drawable?,
//    error: Drawable?,
//    withCrossFade: Boolean = true,
//    requestListener: RequestListener<Drawable>?
//) {
//    if (!imageUrl.isNullOrEmpty()) {
//        val transitionOptions = if (withCrossFade) {
//            DrawableTransitionOptions().crossFade()
//        } else {
//            DrawableTransitionOptions()
//        }
//        val transition = Glide.with(view.context)
//            .load(imageUrl)
////            .apply(RequestOptions().override(800, 600))
//            .error(error)
//            .placeholder(placeHolder)
//            .transition(transitionOptions)
//
//        requestListener?.let { transition.listener(it) }
//
//        transition.into(view)
//    }
//}
//
//@BindingAdapter("videoFromUrl")
//fun bindVideoFromUrl(view: VideoView, imageUrl: String?) {
//    if (!imageUrl.isNullOrEmpty()) {
//        view.setVideoURI(Uri.parse(imageUrl))
////        view.setMediaController(mediaControls)
////        view.requestFocus()
////        view.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
////            // set the media controller for video view
////            view.setMediaController(mediaControls)
//////            // set the anchor view for the video view
//////            mediaControls.setAnchorView(simpleVideoView)
//////            playVideo()
////        })
////        view.setOnCompletionListener(OnCompletionListener { mp: MediaPlayer ->
////            mp.release()
//////            releaseVideo()
////        })
////        view.setOnErrorListener(MediaPlayer.OnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
////            false
////        })
//    }
//}
//
//@BindingAdapter("imageFromUrlDefault","placeholder", "withCrossFade", "requestListener", requireAll = false)
//fun bindImageFromUrlWithDefault(
//    view: ImageView,
//    imageUrl: String?,
//    placeHolder: Drawable?,
//    withCrossFade: Boolean = true,
//    requestListener: RequestListener<Drawable>?
//) {
//    if (!imageUrl.isNullOrEmpty()) {
//        val transitionOptions = if (withCrossFade) {
//            DrawableTransitionOptions().crossFade()
//        } else {
//            DrawableTransitionOptions()
//        }
//        val transition = Glide.with(view.context)
//            .load(imageUrl)
////            .apply(RequestOptions().override(800, 600))
//            .placeholder(placeHolder)
//            .transition(transitionOptions)
//
//        requestListener?.let { transition.listener(it) }
//
//        transition.into(view)
//    } else {
//        view.setImageDrawable(placeHolder);
//    }
//}
//
//@BindingAdapter("videoFromUrl","placeholder")
//fun bindVideoFromUrlDefault(view: VideoView, imageUrl: String?, placeHolder: Drawable?) {
//    if (!imageUrl.isNullOrEmpty()) {
//        view.setVideoURI(Uri.parse(imageUrl))
////        view.setMediaController(mediaControls)
////        view.requestFocus()
////        view.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer? ->
////            // set the media controller for video view
////            view.setMediaController(mediaControls)
//////            // set the anchor view for the video view
//////            mediaControls.setAnchorView(simpleVideoView)
//////            playVideo()
////        })
////        view.setOnCompletionListener(OnCompletionListener { mp: MediaPlayer ->
////            mp.release()
//////            releaseVideo()
////        })
////        view.setOnErrorListener(MediaPlayer.OnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
////            false
////        })
//    }
//}
//
