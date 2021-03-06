package com.memandis.remote.datasource

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.memandis.remote.datasource.model.diary.DiaryImage
import com.memandis.remote.datasource.model.diary.FirebaseDiaryImage.Companion.getFirebaseStoragePath
import com.memandis.remote.datasource.model.UserRemote
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream

/**
 * A SINGLETON OBJECT that contains all methods required for interacting with Firebase Storage
 * to retrieve and upload files.
 */
object FirebaseStorageRepository {

    private val LOG_TAG = this::class.java.simpleName
    private val firebaseStorage = Firebase.storage("gs://appservice-eb5e1.appspot.com/").reference
    private var userRoot: StorageReference? = null

    fun uploadDiaryEntryImages(storagePath: String,
                               nameBitmapPairs: List<Pair<String, Bitmap>>?)
    : Flowable<Pair<String, Float>>? {
        val entryFolderRef = try {
            userRoot!!.child(storagePath)
        } catch(e: UninitializedPropertyAccessException) {
            // Try reattaching the user reference to the storage
            FirebaseUserRepository.attachUserToFirebaseRepositories()
            userRoot!!.child(storagePath)
        }

        val asyncList = arrayListOf<Flowable<Pair<String, Float>>>()
        for(pair in nameBitmapPairs!!) {
            asyncList.add(uploadAnImage(entryFolderRef, "${pair.first}.jpg", pair.second))
        }
        return Flowable
                .merge(asyncList)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteDiaryEntryImages(storagePath: String,
                               filenames: List<String>?)
    : Flowable<Pair<String, Float>>? {
        val entryFolderRef = userRoot!!.child(storagePath)

        val asyncList = arrayListOf<Flowable<Pair<String, Float>>>()
        for(file in filenames!!) {
            val b = deleteAnImage(entryFolderRef, "${file}.jpg")
            asyncList.add(b)
        }
        return Flowable.merge(asyncList).observeOn(AndroidSchedulers.mainThread())
    }

    @ExperimentalCoroutinesApi
    private suspend fun uploadAnImageSuspended(storageRef: StorageReference,
                                               bitmap: Bitmap,
                                               fileName: String)
    = suspendCancellableCoroutine<Boolean> { cont ->
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        val data = outputStream.toByteArray()
        storageRef
                .child(fileName)
                .putBytes(data)
                .addOnCompleteListener {
                    cont.resume(true) {
                        it.printStackTrace()
                    }
                }
                .addOnFailureListener {
                    cont.resume(false) {
                        it.printStackTrace()
                    }
                }
    }

    private fun deleteAnImage(storageRef: StorageReference,
                              fileName: String)
    : Flowable<Pair<String, Float>> {
        val function: (emitter: FlowableEmitter<Pair<String, Float>>) -> Unit = {
                flowable -> storageRef
                    .child(fileName)
                    .delete()
                    .addOnCompleteListener {
                        Log.d(LOG_TAG, "Uploading an image ($fileName): COMPLETED!")
                        flowable.onNext(Pair(fileName, 1f))
                        flowable.onComplete()
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        flowable.onError(it)
                    }
        }
        return Flowable
                .create(function, BackpressureStrategy.BUFFER)
    }

    private fun uploadAnImage(storageRef: StorageReference,
                              fileName: String, bitmap: Bitmap)
    : Flowable<Pair<String, Float>> {
        val flowableEmitter: (emitter: FlowableEmitter<Pair<String, Float>>) -> Unit = {
                flowable ->
            val targetFileFirebaseReference = storageRef.child(fileName)
            targetFileFirebaseReference.downloadUrl.addOnSuccessListener {
                val throwable = FirebaseException(
                        "We cannot upload the file since there is a file already exist at $it.")
                if(flowable.tryOnError(throwable)) {
                    flowable.onError(throwable)
                }
            }.addOnFailureListener { _ ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                val data = outputStream.toByteArray()

                targetFileFirebaseReference
                        .putBytes(data)
                        .addOnProgressListener {
                            Log.d( LOG_TAG,"Uploading an image ($fileName)" +
                                   ": ${it.bytesTransferred / it.totalByteCount.toFloat()}...")
                            flowable.onNext(
                                    (Pair(fileName,
                                          (it.bytesTransferred / it.totalByteCount).toFloat())))
                        }
                        .addOnCompleteListener {
                            Log.d(
                                LOG_TAG,
                                  "Uploading an image ($fileName): COMPLETED!")
                            flowable.onNext(Pair(fileName, 1f))
                            flowable.onComplete()
                        }
                        .addOnFailureListener {
                            it.printStackTrace()
                            flowable.onError(it)
                        }

            }
        }
        return Flowable
                .create(flowableEmitter, BackpressureStrategy.BUFFER)
    }

    fun setUserStorageReference(uid: String) {
        userRoot = firebaseStorage.child(uid)
    }

    fun detachUserStorageReference(){
        userRoot = null
        Log.d(LOG_TAG, "Detached Firebase user storage reference! -> Result: ${userRoot == null}")
    }

    fun DiaryImage.getImageStorageReference(): StorageReference {
        val path = getFirebaseStoragePath(entryId) + this.id + ".jpg"
        Log.d(LOG_TAG, "Getting Image Path: $path")
        return userRoot!!.child(path)
    }

    fun UserRemote.getProfileImageStorageReference(): StorageReference {
        val path = userRoot!!.child("profile/${this.profileImageName}")
        Log.d(LOG_TAG, "Getting Profile Image Path: $path")
        return path
    }

}