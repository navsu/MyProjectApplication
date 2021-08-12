package com.memandis.remote.datasource.model

data class ProjectRemote(
    val id: Long = 0L,
//    @SerializedName("dt")
    val ownerId: Long?,

//    @SerializedName("dt")
//    val owner: com.svayantra.appdomain.domain.entities.UserType,
    val isOwnerDesigner: Boolean,

//    @SerializedName("dt")
    val provider: Long?,

//    @SerializedName("dt")
    val serviceId: Long?,

//    @SerializedName("dt")
    val projectDate: String,

//    @SerializedName("dt")
    val projectName: String?,

//    @SerializedName("dt")
    val projectDescription: String?,

//    @SerializedName("dt")
    val projectComment: String?,

//    @SerializedName("dt")
    val projectRating: Int = -1

)