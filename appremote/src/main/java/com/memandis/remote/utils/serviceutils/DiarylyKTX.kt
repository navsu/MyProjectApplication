package com.memandis.remote.utils.serviceutils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memandis.remote.datasource.model.diary.DiaryImage
import com.memandis.remote.utils.app.AppConstants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun ViewModel.suspendinglyLoadBitmapToDiarylyImage(
        application: Application,
        it: List<DiaryImage>): List<DiaryImage> {
    val deferred = arrayListOf<Pair<Uri, Deferred<Bitmap>>>()
    for(image in it) {

        val descriptor = application
                .applicationContext!!
                .contentResolver
                .openAssetFileDescriptor(image.uri,  AppConstants.FILE_READ_ONLY)!!
        viewModelScope.launch {
            deferred.add(Pair(image.uri, async {
                BitmapFactory.decodeFileDescriptor(
                        descriptor.fileDescriptor, null, null)
            }))
        }
        descriptor.close()
    }

    val resultBitmap = arrayListOf<DiaryImage>()
    for(defer in deferred) {
        viewModelScope.launch {
            resultBitmap.add(
                DiaryImage(
                    imageBitmap = defer.second.await(), uri = defer.first)
            )
        }
    }
    Log.d("suspendLdBitmapFromImg",
          "Deferred: ${deferred.size} \tLoaded: ${resultBitmap.size} Bitmaps")
    return resultBitmap
}


fun ViewModel.suspendinglyLoadBitmapFromUri(
        application: Application,
        it: List<Uri>): List<Pair<Uri, Bitmap>> {
    val deferred = arrayListOf<Pair<Uri, Deferred<Bitmap>>>()
    for(uri in it) {

        val descriptor = application
                .applicationContext!!
                .contentResolver
                .openAssetFileDescriptor(uri, com.memandis.remote.utils.app.AppConstants.FILE_READ_ONLY)!!
//            .openAssetFileDescriptor(uri, EntryEditFragment.FILE_READ_ONLY)!!
        viewModelScope.launch {
            deferred.add(Pair(uri, async {
                BitmapFactory.decodeFileDescriptor(
                        descriptor.fileDescriptor, null, null)
            }))
        }
        descriptor.close()
    }

    val resultBitmap = arrayListOf<Pair<Uri, Bitmap>>()
    for(defer in deferred) {
        viewModelScope.launch {
            resultBitmap.add(Pair(defer.first, defer.second.await()))
        }
    }
    Log.d("suspoadBitmapFromUri",
          "Deferred: ${deferred.size} \tLoaded: ${resultBitmap.size} Bitmaps")
    return resultBitmap.toList()
}