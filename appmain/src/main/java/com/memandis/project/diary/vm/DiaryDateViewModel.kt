package com.memandis.project.diary.vm

import android.util.Log
import androidx.lifecycle.*
import com.memandis.local.data.addDays
import com.memandis.project.diary.calenderview.DAY_TIME_WINDOW
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.Resource
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase.getGoodBadScoreInTimeRange
import com.memandis.remote.utils.map.zipLiveData
import org.apache.commons.lang3.time.DateUtils
import java.util.*
import kotlin.math.ceil

class DiaryDateViewModel : ViewModel() {

    var isFirstLaunch: Boolean = true
        private set
        get() {
            return if(field){
                isFirstLaunch = false
                true
            }else{
                field
            }
        }

    private val LOG_TAG = DiaryDateViewModel::class.java.simpleName

    val today = MutableLiveData(Calendar.getInstance().time)

    val dateHolders: LiveData<List<DiaryDateHolder>> = Transformations.switchMap(today) {
        Log.d(LOG_TAG, today.value.toString())

        var totalStartDate: Date = it.clone() as Date
        var totalEndDate: Date = it.clone() as Date
        val dateMap = arrayListOf<DiaryDateHolder>()

        val centerDayOfMonth = ceil(((DAY_TIME_WINDOW + 1) / 2).toDouble()).toInt()

        var countNegative = 0
        var countPositive = 0
        for(i in 0 until DAY_TIME_WINDOW + 1) {
            val dayProportion = (i / centerDayOfMonth).toFloat()
            val dayAmount = if(dayProportion < 1) {
                countNegative++
                -1 * countNegative
            } else {
                countPositive++
            }

            val time = DateUtils.truncate(it.addDays(dayAmount), Calendar.DATE)

            if(totalStartDate.time > time.time) {
                totalStartDate = time
            }
            if(totalEndDate.time < time.time) {
                totalEndDate = time
            }

            dateMap += DiaryDateHolder(time, 0)
        }

        Log.d(LOG_TAG, "Date Range calculation result: $dateMap")
        Log.d(LOG_TAG, "Today is $it, [$totalStartDate, $totalEndDate]")

        val allDiaryDateMapLiveData = MutableLiveData(dateMap)
        val allGoodBadScoreInTotalRange = getGoodBadScoreInDayRange(totalStartDate, totalEndDate)

        // Combine LiveData of DateHolders calculation and
        // Aggregated GoodBad Score retrieved from the data source
        zipLiveData<ArrayList<DiaryDateHolder>,
                    Resource<List<DiaryDateHolder>>,
                    List<DiaryDateHolder>>(allDiaryDateMapLiveData, allGoodBadScoreInTotalRange)
        {array, databaseResource ->
            array.sort()

            // Sort date holders in ascending order
            if(databaseResource.data.isNullOrEmpty()){
                return@zipLiveData array
            }

            Log.d(this::class.java.simpleName, "GoodBad Score Retrieved from DB: $databaseResource")
            for(dateHolderRes in databaseResource.data!!) {
                val targetIndex = array.indexOf(dateHolderRes)
                if(targetIndex >= 0) {
                    array[targetIndex].goodBadScore = dateHolderRes.goodBadScore
                }
            }

            array
        }
    }

    val userProfile by lazy {
        LiveDataReactiveStreams.fromPublisher(UserDiaryUseCase.getUserProfile())
    }

    private fun getGoodBadScoreInDayRange(dayStart: Date, dayEnd: Date)
    : LiveData<Resource<List<DiaryDateHolder>>> {
       return  LiveDataReactiveStreams
                .fromPublisher(getGoodBadScoreInTimeRange(dayStart, dayEnd))
    }

}

