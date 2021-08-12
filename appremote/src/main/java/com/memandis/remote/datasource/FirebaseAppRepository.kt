package com.memandis.remote.datasource

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A SINGLETON OBJECT that controls database reference for the user.
 */
internal object FirebaseAppRepository {

    private val LOG_TAG = this::class.java.simpleName
    //https://appservice-eb5e1-default-rtdb.asia-southeast1.firebasedatabase.app/
//    https://appservice-eb5e1-default-rtdb.firebaseio.com/projectEntry
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    internal var userRoot: DatabaseReference = database.reference.root

    internal fun setUserDatabaseReference(uid: String){
        userRoot = database.reference.child(uid)
        DiaryRepo.refreshUserRoot()
    }
}