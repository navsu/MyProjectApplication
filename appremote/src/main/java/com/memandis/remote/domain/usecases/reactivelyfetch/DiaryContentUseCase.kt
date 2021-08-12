package com.memandis.remote.domain.usecases.reactivelyfetch

import android.util.Log
import com.memandis.remote.datasource.DiaryRepo
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.Resource
import io.reactivex.Flowable

object DiaryContentUseCase {

    fun getOneDiaryEntry(entryId: String)
    : Flowable<Resource<DiaryEntry>> {
        return RxUseCaseProcedure(
                 DiaryRepo.reactivelyRetrieveAnEntryById(entryId), null)
                   .proceed()
    }

    fun getAllDiaryEntriesByDateHolder(dateHolder: DiaryDateHolder)
    : Flowable<Resource<List<DiaryEntry>>> {
        return RxUseCaseProcedure(
                  DiaryRepo.retrieveEntryInDay(dateHolder.date), null)
                    .proceed()
                      .doAfterNext { Log.d("getAllDiaryByDH", "Getting $it") }
    }
}