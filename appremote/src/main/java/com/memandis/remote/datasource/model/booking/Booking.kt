package com.svayantra.core.models

data class Booking (
    val designerId: Long,
    val subscriptionId: Long,
    val subscriptionName: String,
    val subscriptionPrice: String,
    val slotId: Long,
//    val slotType: Int,
    val fullDate: String,
    val time1: String,
    val time2: String,
    val time3: String,
    val time4: String,
    var isSelected: List<Boolean>
)