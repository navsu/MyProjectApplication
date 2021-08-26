package com.memandis.appbooking.scheduling

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.memandis.remote.datasource.model.booking.Scheduling
import com.memandis.remote.datasource.model.booking.Slot
import com.memandis.remote.utils.bookingutils.geIdWithFormattedDateWithoutHours
import com.memandis.remote.utils.bookingutils.generateSlots
import java.text.ParseException
import java.text.SimpleDateFormat

import java.util.*
import com.google.firebase.auth.FirebaseUser

import com.memandis.remote.datasource.model.booking.Professional
import com.memandis.remote.utils.bookingutils.getNextBusinessDay


class SchedulingViewModel: ViewModel() {

    //Profile selected designer position Viewpager2
    private val _slotData: MutableLiveData<List<Slot>> = MutableLiveData()
    val slotData: LiveData<List<Slot>> get() = _slotData
    private fun setSlotData(position: List<Slot>) { _slotData.postValue(position) }

    fun getAvailableSlots(): List<Slot>? {
        setSlotData(AVAILABLE_HOURS!!)
        return AVAILABLE_HOURS
    }


    fun loadingScheduling(
//        calendar: Calendar,
//                          availableHours: List<Slot>,
//                          idProfessional: Long
        ) {

//            val calendar: Calendar? = getNextBusinessDay()
//            val idProfessional: Long = 0L
//
//            val availableHours: List<Slot>? =   AVAILABLE_HOURS
//            val dateFormatRequest = SimpleDateFormat("dd-MM-yyyy HH:mm")
//
//        FirebaseDatabase.getInstance().reference.root.child("scheduleEntry")
//            .orderByChild("idProfessionalDay")
//            .equalTo(geIdWithFormattedDateWithoutHours(calendar, idProfessional.toString()))
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (hour in availableHours!!) {
//                        true.also { hour.available = it }
//                    }
//                    for (schedulingSnapshot in dataSnapshot.children) {
//                        val scheduling = schedulingSnapshot.getValue(Scheduling::class.java)
//                        var date: Date? = null
//                        try {
//                            date = dateFormatRequest.parse(scheduling!!.date)
//                        } catch (e: ParseException) {
////                            Log.d(CompanyActivity::class.java.getSimpleName(), e.getMessage())
//                        }
//                        val instance: Calendar = Calendar.getInstance()
//                        instance.time = date
//                        val format = SimpleDateFormat("dd-MM-yyyy")
//                        if (!format.format(calendar?.getTime())
//                                .equals(format.format(instance.getTime()))) {
//                            continue
//                        }
//                        val hour = Slot( 0L,0L,0L,"",0L,"",
//                            instance.get(Calendar.HOUR_OF_DAY), true)
//                        if (availableHours.contains(hour)) {
//                            val index: Int = availableHours.indexOf(hour)
//                            availableHours[index].available = false
//                        }
//                    }
//
//                    setSlotData(availableHours)
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {}
//            })
//
//            setSlotData(availableHours!!)
//
//        }
    }

    fun pickupHour(calendar: Calendar?
                   , professional: Professional, currentUser: FirebaseUser,
        dateWithHour: String? )
    {
        FirebaseDatabase.getInstance().reference.root.child("scheduleEntry")
            .orderByChild("idUserProfessionalDay")
            .equalTo(geIdWithFormattedDateWithoutHours(calendar, currentUser.uid,
                             java.lang.String.valueOf(professional.id)))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (appleSnapshot in dataSnapshot.children) {
                        appleSnapshot.ref.removeValue()
                    }
                    val scheduling = Scheduling()
                    scheduling.date = dateWithHour
                    scheduling.idCompany = professional.idCompany
                    scheduling.idProfessional = professional.id
                    scheduling.name = currentUser.displayName
                    scheduling.uid = currentUser.uid
                    scheduling.userEmail = currentUser.email
                    scheduling.userPhone = currentUser.phoneNumber
                    scheduling.professionalName = professional.name
                    scheduling.specialization = professional.expertise
                    scheduling.idCompany_day = geIdWithFormattedDateWithoutHours( calendar,
                                               professional.idCompany.toString() ) //"10_04-09-2017"
                    scheduling.idUserProfessionalDay = geIdWithFormattedDateWithoutHours( calendar,
                                         currentUser.uid, java.lang.String.valueOf(professional.id))
                    scheduling.idProfessionalDay = geIdWithFormattedDateWithoutHours( calendar,
                                                         java.lang.String.valueOf(professional.id) )
                    scheduling.idUserDay = geIdWithFormattedDateWithoutHours(calendar, currentUser.uid)
                    val key: String? = FirebaseDatabase.getInstance().
                                      reference.root.child("scheduleEntry").push().key
                    FirebaseDatabase.getInstance().reference.root.child("scheduleEntry")
                        .child(key!!).setValue(scheduling)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e( SchedulingActivity::class.java.simpleName,
                        "onCancelled",
                        databaseError.toException()
                    )
                }
            })
    }

    init {
        _slotData.postValue(AVAILABLE_HOURS)
    }

     companion object {
        private val AVAILABLE_HOURS = generateSlots(Calendar.getInstance());
    }

}