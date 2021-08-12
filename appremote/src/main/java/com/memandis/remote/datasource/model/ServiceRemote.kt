package com.memandis.remote.datasource.model

data class ServiceRemote (

    val id: Long = 0L,

    val  name: String,

    val image: Int? = null,

    val imageBigUrl: String?,

    val imageSmallUrl: String?,

    val description: String

)
