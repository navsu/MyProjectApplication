package com.memandis.remote.datasource.model.booking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Company : Parcelable {
    private val addOn: String? = null
    private val address: String? = null
    private val name: String? = null
    private val phone: String? = null
    private val id: Long = 0
}

//@Parcelize
//data class Company(
//    val addOn: String? = null
//    val address: String? = null
//    val name: String? = null
//    val phone: String? = null
//    val id: Long = 0)
//    : Parcelable {
//
//    private companion object : Parceler<Company> {
//        override fun Company.write(parcel: Parcel, flags: Int) {
//            // Custom write implementation
//            parcel.writeString(this.addOn);
//            parcel.writeString(this.address);
//            parcel.writeString(this.name);
//            parcel.writeString(this.phone);
//            parcel.writeLong(this.id);
//        }
//
//        override fun create(parcel: Parcel): Company {
//            // Custom read implementation
//        }
//    }
//
//
//}