package com.memandis.project

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader

import com.google.firebase.storage.StorageReference
import java.io.InputStream


/**
 * This class enables Glide, an image loading library, to be able to retrieve images from firebase]
 * FirebaseUI provides bindings to download an image file stored in Cloud Storage from a
 * StorageReference and display it using the popular Glide library
 * To load an image from a StorageReference, first register an AppGlideModule
 */
@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference::class.java, InputStream::class.java,
                        FirebaseImageLoader.Factory())
    }
}