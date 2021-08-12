package com.memandis.remote.utils.app

import androidx.lifecycle.MutableLiveData

object App {
    class User(val id: String, val name: String, val isAdmin: Boolean)

    var currentUser: User?
        get() = currentUserLiveData.value
        set(value) {
            currentUserLiveData.postValue(value)
        }

    val currentUserLiveData = MutableLiveData<User?>()
}