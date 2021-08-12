package com.memandis.remote.datasource.model

data class UserRemote(
   val username: String = "",
   val profileImageName: String = "",
   val email: String = "",
   val emailVerified: String = "",
   val uid: Long = 0L,
)
