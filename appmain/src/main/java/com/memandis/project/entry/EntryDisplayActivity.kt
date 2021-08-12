package com.memandis.project.entry

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.memandis.appmain.R
import com.memandis.project.entry.vm.EntryActivityViewModel
import com.memandis.project.viewModelInjectionHelper

/**
 * A container activity for the Fragments are responsible for displaying diary entry's content
 * @property LOG_TAG String String Tag string for showing Debugging Log
 */
class EntryDisplayActivity : AppCompatActivity() {

    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        val entryActivityArgs = EntryDisplayActivityArgs.fromBundle(  intent.extras!!)

        val entryId = entryActivityArgs.entryId
        val dateHolder = entryActivityArgs.diaryDateHolder!!

        Log.d(LOG_TAG, "The activity has received EntryID => $entryId")

//         val viewModelFactory = this.let { DairyRepository.getInstance(it) }.let {
//             AppViewModelFactory(it, diaryEntryId = entryId)
//         }

//         val entryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryContentViewModel)

        val viewModel = viewModelInjectionHelper<EntryActivityViewModel>(this,
                                                                         diaryEntryId = entryId,
                                                                         dateHolder = dateHolder)
        viewModel.selectedDiaryEntry = entryId
    }


}
