@file:Suppress("UNCHECKED_CAST")

package com.memandis.appbooking

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.memandis.appbooking.scheduling.SchedulingViewModel
import com.memandis.appbooking.vm.BookingViewModel
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import java.util.*

class AppViewModelFactory(
//    private val entryId: String? = null,
//                          private val dateHolder: DiaryDateHolder? = null,
//                          private val application: Application? = null,
//                          private val date: Date? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            SchedulingViewModel::class.java            -> SchedulingViewModel() as T
            else                                       -> BookingViewModel() as T
//            DiaryViewModel::class.java            -> DiaryViewModel() as T
//            DiaryDateViewModel::class.java        -> DiaryDateViewModel() as T
//            EntryDisplayViewModel::class.java     -> EntryDisplayViewModel(entryId!!) as T
//            EntryEditorViewModel::class.java      -> EntryEditorViewModel(application!!, entryId, date) as T
//            else                                  -> EntryActivityViewModel(application!!, entryId!!, dateHolder!!) as T
        }
    }
}

//inline fun <reified T : ViewModel?> viewModelInjectionHelper(activity: FragmentActivity,
//                                                             diaryEntryId: String? = null,
//                                                             dateHolder: DiaryDateHolder? = null,
//                                                             date: Date? = null): T {
//    return ViewModelProvider(activity, AppViewModelFactory(diaryEntryId,
//                                                           dateHolder,
//                                                           activity.application,
//                                                           date)).get(T::class.java)
//}
//
//inline fun <reified T : ViewModel?> viewModelInjectionHelper(
//    activity: FragmentActivity
////    , key: String, userId: Int = 1, diaryEntryId: String = "", dateHolder: DiaryDateHolder?= null
//): T {
//    return ViewModelProvider(activity, AppViewModelFactory(
////        diaryEntryId, dateHolder
//    )
//    ).get(key, T::class.java)
//}

inline fun <reified T : ViewModel?> viewModelInjectionHelper(
    fragment: Fragment
//    ,diaryEntryId: String? = null, dateHolder: DiaryDateHolder? = null
): T {
    return ViewModelProvider(fragment, AppViewModelFactory(
//        diaryEntryId,dateHolder
    )).get(T::class.java)
}