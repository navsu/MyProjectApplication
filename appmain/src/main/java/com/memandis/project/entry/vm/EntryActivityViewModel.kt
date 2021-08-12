package com.memandis.project.entry.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveDataReactiveStreams
import com.memandis.remote.datasource.DiaryRepo
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.Resource
import com.memandis.remote.domain.usecases.reactivelyfetch.DiaryContentUseCase.getAllDiaryEntriesByDateHolder
import com.memandis.remote.domain.usecases.reactivelyfetch.DiaryContentUseCase.getOneDiaryEntry
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase.deleteDiaryEntry
//import com.memandis.remote.domain.usecases.reactivelyfetch.DiaryContentUseCase.getAllDiaryEntriesByDateHolder
//import com.memandis.remote.domain.usecases.reactivelyfetch.DiaryContentUseCase.getOneDiaryEntry
//import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase.deleteDiaryEntry
import io.reactivex.Single

class EntryActivityViewModel(application: Application, entryId: String,
                             diaryDateHolder: DiaryDateHolder
) : AndroidViewModel(application) {

    private val LOG_TAG = this::class.java.simpleName

    val dailyDiary: LiveData<Resource<List<DiaryEntry>>> =
            LiveDataReactiveStreams.fromPublisher(getAllDiaryEntriesByDateHolder(diaryDateHolder))

    var selectedDiaryEntry = entryId
    var isFirstTime = true

    fun getSelectedDiaryEntryIndex(): Int{
        val array = dailyDiary.value!!.data!!
        Log.d(LOG_TAG, "Total Array Elements => ${array.size} => ${array.map(DiaryEntry::id)}")
        var target = 0

        for(i in array.indices){
            Log.d(LOG_TAG, "Getting index => ${array[i].id}")
            Log.d(LOG_TAG, "Comparing => ${array[i].id} vs. $selectedDiaryEntry")
            if(array[i].id.contentEquals(selectedDiaryEntry)){
                target = i
                break
            }
        }
        return target
    }

    fun removeDiaryEntry(diaryEntry: DiaryEntry): Single<List<Resource<Boolean>>> {
        return deleteDiaryEntry(diaryEntry, getApplication<Application>().applicationContext)
    }

    val userProfile  by lazy {
        LiveDataReactiveStreams.fromPublisher(DiaryRepo.reactivelyRetrieveProfileInfo())
    }

}

class EntryDisplayViewModel(val entryId: String) : ViewModel(){

    val entryContent: LiveData<Resource<DiaryEntry>> by lazy {
        LiveDataReactiveStreams.fromPublisher(getOneDiaryEntry(entryId))
    }
}