package com.memandis.appservice.main.domain.entities.user

data class UserDetailEntity (

    val id: Long = 0L,

    val  providerId: Long,

    val domain: String?,

    val location: String?,

    val experience: String?,

    val age: String?,

    val expertise: String?,

    val  dataUrl: String?

)
