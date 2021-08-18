package com.memandis.remote.datasource.model.booking

data class Professional (

    val id: Long = 0L,

    val providerId: Long,

    val domain: String?,

    val location: String?,

    val experience: String?,

    val age: String?,

    val expertise: String?,

    val imageLarge: String,

    val imageThumbnail: String,

    val  dataUrl: String?

)
