package com.memandis.remote.datasource.model.booking

data class Slot (

    val id: Long = 0L,

    val userId: Long,

    val subscriptionId: Long,

    val date: String,

    val dateAndTime: Long,

    val time: String,

    val slotStatus: SlotStatus

)

enum class SlotStatus {
    SLOT_ACTIVE,
    SLOT_INACTIVE
}
