package com.memandis.remote.datasource

import android.util.Log
import com.google.firebase.database.*
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.FirebaseDiaryEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

class RealtimeDatabaseRepositoryImpl constructor(
    private val firebaseDatabaseReference: DatabaseReference
    ): RealtimeDatabaseRepository {

    companion object {
        const val PROJECT_REFERENCE = "bookingEntry"
    }

    @ExperimentalCoroutinesApi
    override fun fetchProjectEntry() = callbackFlow<Result<List<DiaryEntry>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val list = arrayListOf<DiaryEntry>()
//                for(it in dataSnapshot.children) {
                   val items = dataSnapshot.children.map { ds ->
                                 ds.getValue(FirebaseDiaryEntry::class.java)!!.toNormalData()
//                       list.add(ds.getValue(FirebaseDiaryEntry::class.java)!!
//                           .toNormalData())

                   }
                    Log.d("DiaryRepoImpl.LOG_TAG", items.toString())
//                }
                this@callbackFlow.trySendBlocking(Result.success(items))

//                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }
        }
        firebaseDatabaseReference
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabaseReference
                .removeEventListener(postListener)
        }
    }

    // whole Firebase implementation is wrapped inside a callbackFlow()
    // this operator allows us to emit data from a different concurrency context and
    // make it accessible to a consumer,
    // awaitClose function: this function is mandatory because
    // the lambda inside it is invoked if the Flow channel is canceled or closed
    @ExperimentalCoroutinesApi
    override fun fetchProjectEntryInRange(time1: Date, time2: Date) = callbackFlow<Result<List<DiaryEntry>>> {

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = arrayListOf<DiaryEntry>()
                for(it in dataSnapshot.children) {
                    list.add(it.getValue(FirebaseDiaryEntry::class.java)!!
                        .toNormalData())
                }
                this@callbackFlow.trySendBlocking(Result.success(list))
            }
        }
        firebaseDatabaseReference
            .orderByChild("timeCreated")
            .startAt(time1.time.toDouble())
            .endAt(time2.time.toDouble())
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabaseReference
                .removeEventListener(postListener)
        }
    }

}