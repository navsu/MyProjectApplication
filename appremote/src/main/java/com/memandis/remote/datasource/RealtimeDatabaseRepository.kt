package com.memandis.remote.datasource

import com.memandis.remote.datasource.model.diary.DiaryEntry
import kotlinx.coroutines.flow.Flow
import java.util.*

interface RealtimeDatabaseRepository {

    fun fetchProjectEntry() : Flow<Result<List<DiaryEntry>>>

    fun fetchProjectEntryInRange(time1: Date, time2: Date) : Flow<Result<List<DiaryEntry>>>
}