package com.memandis.appservice.main.domain.entities.user

data class UserEntity(

    val id: Long = 0L,

    val name: String,

    val firstName: String,

    val lastName: String,

    val password: String,

    val email: String,

    val gender: String,

    val dateOfBirth: String,

    val imageBigUrl: String?,

    val imageSmallUrl: String?,

    val dateOfRegistration: String,

    val phoneNumber: String,

    var isDesigner: Boolean = false
//    val type: UserType

)