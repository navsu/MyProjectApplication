package com.memandis.remote.datasource.model.booking
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Professional (

    val id: Long = 0L,

    val providerId: Long,

    val idCompany: Long,

    val name: String?,

    val domain: String?,

    val location: String?,

    val experience: String?,

    val age: String?,

    val expertise: String?,

    val imageLarge: String,

    val imageThumbnail: String,

    val  dataUrl: String?

) : Parcelable
