package com.memandis.remote.datasource.model.booking

data class Subscription (

    val id: Long = 0L,

    val userId: Long,

    val name: String?,

    val info: String?,

    val duration: String?,

    val price: String

)
