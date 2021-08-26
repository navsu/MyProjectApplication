package com.memandis.remote.datasource

import com.google.firebase.database.DatabaseError

import com.memandis.remote.datasource.model.booking.Scheduling

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import com.memandis.remote.datasource.model.booking.Slot
import com.memandis.remote.utils.bookingutils.geIdWithFormattedDateWithoutHours
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BookingRepo {

    private var userRoot = FirebaseAppRepository.userRoot

    private var scheduleEntryRoot = userRoot.child("scheduleEntry")

//    fun loadingScheduling(calendar: Calendar, availableHours: List<Slot>, idProfessional: Long) {
//        val dateFormatRequest = SimpleDateFormat("dd-MM-yyyy HH:mm")
//        scheduleEntryRoot .orderByChild("idProfessionalDay")
//            .equalTo(geIdWithFormattedDateWithoutHours(calendar, idProfessional.toString()))
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (hour in availableHours) {
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
//                        instance.setTime(date)
//                        val format = SimpleDateFormat("dd-MM-yyyy")
//                        if (!format.format(calendar.getTime())
//                                .equals(format.format(instance.getTime()))
//                        ) {
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
////                    return availableHours
//
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {}
//            })
//    }
//
//    public void pickupHour(final Calendar calendar, final Professional professional, final FirebaseUser currentUser, final String dateWithHour) {
//        mDatabase.child("professionalSchedule").orderByChild("idUserProfessionalDay").
//        equalTo(geIdWithFormattedDateWithoutHours(calendar, currentUser.getUid(),
//            String.valueOf(professional.getId())))
//            .addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//
//                    Scheduling scheduling = new Scheduling();
//                    scheduling.setDate(dateWithHour);
//                    scheduling.setIdCompany(professional.getIdCompany());
//                    scheduling.setIdProfessional(professional.getId());
//                    scheduling.setName(currentUser.getDisplayName());
//                    scheduling.setUid(currentUser.getUid());
//                    scheduling.setUserEmail(currentUser.getEmail());
//                    scheduling.setUserPhone(currentUser.getPhoneNumber());
//                    scheduling.setProfessionalName(professional.getName());
//                    scheduling.setSpecialization(professional.getSpecialization());
//
//                    scheduling.setIdCompany_day(
//                        geIdWithFormattedDateWithoutHours(calendar, String.valueOf(professional.getIdCompany())));//"10_04-09-2017"
//                    scheduling.setIdUserProfessionalDay(
//                        geIdWithFormattedDateWithoutHours(calendar, currentUser.getUid(), String.valueOf(professional.getId())));
//                    scheduling.setIdProfessionalDay(
//                        geIdWithFormattedDateWithoutHours(calendar, String.valueOf(professional.getId())));
//
//                    scheduling.setIdUserDay(geIdWithFormattedDateWithoutHours(calendar, currentUser.getUid()));
//
//                    String key = mDatabase.child("professionalSchedule").push().getKey();
//                    mDatabase.child("professionalSchedule").child(key).setValue(scheduling);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e(SchedulingActivity.class.getSimpleName(), "onCancelled", databaseError.toException());
//
//                }
//            });
//    }


}