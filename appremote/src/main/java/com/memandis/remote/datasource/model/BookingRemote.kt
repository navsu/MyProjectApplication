package com.memandis.remote.datasource.model

data class BookingRemote (

    val id: Long = 0L,

    val customerId: Long,

    val projectId: Long,

    val slotId: Long,

    val bookingName: String,

    val bookingDate: String,

    val bookingTime: String,

//    val status: com.svayantra.appdomain.domain.entities.BookingStatus,

    val comment: String?

)