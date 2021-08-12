package com.memandis.remote.domain.usecases.reactivelyfetch

import android.content.Context
import android.util.Log
import com.memandis.remote.datasource.DiaryRepo
import com.memandis.remote.datasource.model.UserRemote
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.Resource
import com.memandis.remote.utils.app.getDayRange
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

object UserDiaryUseCase {

    private val LOG_TAG = this::class.java.simpleName

    fun getUserProfile()
    : Flowable<Resource<UserRemote>>{
        return RxUseCaseProcedure(
                  DiaryRepo.reactivelyRetrieveProfileInfo(), null)
                    .proceed()
    }

    fun getDiaryEntry()
    : Flowable<Resource<List<DiaryEntry>>>{
        return RxUseCaseProcedure(
                  DiaryRepo.retrieveAllEntry(), null)
                    .proceed()
    }

    fun getDiaryEntriesInDay(day: Date)
    : Flowable<Resource<List<DiaryEntry>>> {
        val range = day.getDayRange()
        Log.d(this::class.java.simpleName, "Date Range: $range")
        return RxUseCaseProcedure(
                  DiaryRepo.reactivelyRetrieveEntriesInTimeRange(
                    range.first, range.second), null)
                      .proceed()
    }

    fun getGoodBadScoreInTimeRange(dayStart: Date, dayEnd: Date)
    : Flowable<Resource<List<DiaryDateHolder>>> {
        return RxUseCaseProcedure(
                  DiaryRepo.identifyGoodBadScoreListInRange(dayStart, dayEnd), null)
                   .proceed()
    }

    fun searchDiaryByTitle(title: String)
            : Flowable<Resource<List<DiaryEntry>>> {
        return RxUseCaseProcedure(
            DiaryRepo.reactivelyRetrieveEntryByTitle(title), null)
            .proceed()
    }

    fun deleteDiaryEntry(diaryEntry: DiaryEntry, context: Context)
    : Single<List<Resource<Boolean>>> {
        return RxSingleUseCaseProcedure(
            DiaryRepo.deleteAnEntry(diaryEntry, context), null)
            .proceed()
    }

}
