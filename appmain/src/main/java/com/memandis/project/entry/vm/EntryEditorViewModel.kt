package com.memandis.project.entry.vm

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.memandis.appmain.R
import com.memandis.remote.datasource.DiaryRepo
import com.memandis.remote.datasource.model.diary.DEFAULT_COLOR
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.DiaryImage
import com.memandis.remote.datasource.model.diary.DiaryTag
import com.memandis.remote.domain.usecases.reactivelyfetch.RxSingleUseCaseProcedure
//import com.memandis.remote.domain.usecases.reactivelyfetch.RxSingleUseCaseProcedure
import com.memandis.remote.utils.map.zipLiveData
import com.memandis.remote.utils.serviceutils.suspendinglyLoadBitmapToDiarylyImage
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.apache.commons.lang3.time.DateUtils
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class EntryEditorViewModel(application: Application, private val editorEntryId: String? = null,
                           initialDiaryDateHolder: Date? = Calendar.getInstance().time) :
        AndroidViewModel(application) {
    companion object {
        val EMPTY_LOCATION = Pair(0.0, 0.0)
    }

    var SESSION_DEFAULT_LOCATION = Pair(0.0, 0.0)

    val isModification = editorEntryId != null
    var initializationCompleted = false
        private set

    // private val compositeDisposable = CompositeDisposable()

    private val LOG_TAG = this::class.java.simpleName

    val title = MutableLiveData("")

    val subtitle = MutableLiveData("")
    val content = MutableLiveData("")
    val color = MutableLiveData<Int?>(null)

    val goodBad = MutableLiveData(0)

    val date = MutableLiveData(determineDate(initialDiaryDateHolder))

    val dateText = Transformations.map(date) {
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(it)
    }

    val coordinate = MutableLiveData(EMPTY_LOCATION)

    val tagArray = MutableLiveData(TreeSet<DiaryTag>())

    private val newImages = MutableLiveData<ArrayList<DiaryImage>>(arrayListOf())

    private var oldImages: MutableLiveData<List<DiaryImage>>? = MutableLiveData(arrayListOf())

    private val newDiaryImages: LiveData<List<DiaryImage>?> = Transformations.switchMap(newImages) {
        Log.d(LOG_TAG, "imageBitMap LiveData invoked")
        it ?: return@switchMap liveData<List<DiaryImage>?> {
            emit(null)
        }

        Log.d(LOG_TAG, "Loading Images...")

        val resultBitmap = suspendinglyLoadBitmapToDiarylyImage(application, it)

        return@switchMap liveData<List<DiaryImage>?> {
            if(!resultBitmap.isNullOrEmpty()) {
                emit(ArrayList(resultBitmap))
            } else {
                emit(arrayListOf())
            }
        }

    }

    init {
        Log.d(LOG_TAG, "Initializing...")
        if(editorEntryId != null && editorEntryId != application.getString(
                        R.string.debug_default_id)) {
            Log.d(LOG_TAG, "Entering Diary Entry Edit Mode... for $editorEntryId")
            val rx = RxSingleUseCaseProcedure(
                    DiaryRepo.retrieveAnEntryById(editorEntryId), null)
                    .proceed()
                    .subscribe { data, throwable ->
                        Log.d(LOG_TAG, "The result has arrived!")
                        if(data.isNullOrEmpty() || throwable != null) {
                            Log.d(LOG_TAG, "We got an error getting data from the remote source!")
                            return@subscribe
                        }
                        val e = data[0].data ?: return@subscribe

                        Log.d(LOG_TAG, "The Editor initialization has begin!")

                        title.value = e.title
                        subtitle.value = e.subtitle
                        content.value = e.content
                        color.value = e.color
                        goodBad.value = e.ratings
                        date.value = e.timeCreated
                        oldImages!!.value = e.images
                        tagArray.value = TreeSet(e.tags)

                        coordinate.value = e.location

                        initializationCompleted = true

                        Log.d(LOG_TAG, "Final Result\n${title.value!!}\n${subtitle.value!!}\n" +
                                       content.value!!)
                        Log.d(LOG_TAG, "The Editor has been successfully initialized!")
                    }

            // compositeDisposable.add(rx)
        } else {
            oldImages = MutableLiveData(arrayListOf())
        }
    }

    val allImages: LiveData<ArrayList<DiaryImage>> =
        zipLiveData(oldImages as LiveData<ArrayList<DiaryImage>>,
                    newDiaryImages as LiveData<ArrayList<DiaryImage>>) { a, b ->
        val full = ArrayList<DiaryImage>(a)
        full.addAll(b)

        return@zipLiveData full
    }

    fun saveData(): Single<Boolean> {
        var createdTime = date.value!!
        val tags = tagArray
        val diaryImage = newImages.value
        val color = color.value ?: DEFAULT_COLOR

        var id = "-1"
        if(isModification) {
            createdTime = date.value!!
            id = editorEntryId!!
        }

        Log.d(LOG_TAG,
              "Saving data...\n\t\t${title.value!!}\n\t\tAnd ${newImages.value!!.size} images")

        val entry = DiaryEntry(id,"0",
                               title.value!!,
                               subtitle.value!!,
                               content.value!!,
                               tags.value?.toList() ?: listOf(),
                               goodBad.value!!,
                               color,
                               createdTime,
                               createdTime,
                               diaryImage,
                               coordinate.value!!.first,
                               coordinate.value!!.second)
        return DiaryRepo.addNewEntry(getApplication(), entry)
                        .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateData(): Single<Boolean> {
        val nowCalendar = Calendar.getInstance()
        val tags = tagArray

        val diaryImage = newImages.value?.apply {
            if(!oldImages?.value.isNullOrEmpty()) {
                addAll(oldImages?.value!!)
            }
        }

        val color = color.value ?: DEFAULT_COLOR

        val timeModified = nowCalendar.time

        var createdTime = timeModified
        var id = "-1"
        if(isModification) {
            createdTime = date.value!!
            id = editorEntryId!!
        }

        Log.d(LOG_TAG, "Updating data...")

        val entry = DiaryEntry(id,
                              "0",
                               title.value!!,
                               subtitle.value!!,
                               content.value!!,
                               tags.value?.toList() ?: listOf(),
                               goodBad.value!!,
                               color,
                               createdTime,
                               timeModified,
                               diaryImage,
                               coordinate.value!!.first,
                               coordinate.value!!.second)
        return DiaryRepo.updateAnEntry(getApplication(), entry)
                .observeOn(
                        AndroidSchedulers.mainThread())
    }

    fun addAnImage(img: Uri) {
        val newImageArray = if(newImages.value == null) {
            arrayListOf()
        } else {
            newImages.value
        }

        newImageArray!!.add(DiaryImage(uri = img))
        newImages.value = newImageArray!!
        Log.d(LOG_TAG, "Adding a new Image; Now the array has ${newImages.value!!.size} elements.")

    }

    fun addAnVideo(vid: Uri) {
        val newVideoArray = if(newImages.value == null) {
            arrayListOf()
        } else {
            newImages.value
        }

        newVideoArray!!.add(DiaryImage(uri = vid))
        newImages.value = newVideoArray!!
        Log.d(LOG_TAG, "Adding a new Video; Now the array has ${newImages.value!!.size} elements.")

    }

    private fun determineDate(date: Date?): Date? {
        val now = Calendar.getInstance().time
        if(DateUtils.isSameDay(date, now) || date == null) {
            return now
        }
        return date

    }

    fun addDiaryTag(tagString: String) {
        val old = tagArray.value!!
        old.add(DiaryTag(tagString))
        tagArray.value = old
    }

    fun removeDiaryTag(tagString: String) {
        val old = tagArray.value!!
        for(tag in old) {
            if(tag.title == tagString) {
                old.remove(tag)
                break
            }
        }

        Log.d(LOG_TAG, "Removing: $old")
        tagArray.value = old
    }

//    private fun upload() {
//        var mReference = mStorage.child(uri.lastPathSegment)
//        try {
//            mReference.putFile(uri).addOnSuccessListener {
//                    taskSnapshot: UploadTask.TaskSnapshot? -> var url = taskSnapshot!!.downloadUrl
//                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
//                dwnTxt.text = url.toString()
//                Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
//            }
//        }catch (e: Exception) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
//        }
//
//    }
}

