package com.memandis.remote.utils.map

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.memandis.remote.R

fun <A, B, C> zipLiveData(a: LiveData<A>, b: LiveData<B>, predicate: (A, B) -> C): LiveData<C> {
    return MediatorLiveData<C>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if(localLastA != null && localLastB != null)
                this.value = predicate(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}


fun ChipGroup.addChip(@LayoutRes chipLayoutRes: Int, @ChipType chipType: Int, chipString: String) {
    this.addView((LayoutInflater
            .from(context)
            .inflate(chipLayoutRes, null, true) as Chip)
                         .apply {
                             setChipDrawable(
                                     ChipDrawable
                                             .createFromAttributes(context,
                                                                   null, 0, chipType))
                             text = chipString
                         })
}


//@IntDef(CHIP_ACTION, CHIP_CHOICE, CHIP_ENTRY, CHIP_FILTER)
@kotlin.annotation.Retention
annotation class ChipType
//
//const val CHIP_ACTION = R.style.Widget_MaterialComponents_Chip_Action
//const val CHIP_CHOICE = R.style.Widget_MaterialComponents_Chip_Choice
//const val CHIP_ENTRY = R.style.Widget_MaterialComponents_Chip_Entry
//const val CHIP_FILTER = R.style.Widget_MaterialComponents_Chip_Filter

fun Activity.isGrantedFineLocationPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this,
                                       Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun Activity.isGrantedExternalStoragePermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this,
                                              READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}