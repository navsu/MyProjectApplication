package com.memandis.appbooking.vm;

import androidx.lifecycle.*
//import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
//import javax.inject.Inject

//@HiltViewModel
class BookingViewModel(
//@Inject constructor(
//    private val getCurrentBooking: GetBookingUseCase,
//    private val bookingMapper: Mapper<Booking, BookingEntity>,
): ViewModel() {

    //service provider profile

    //service booking time and date

//    //All designer subscriptions by user type
//    val designerSubscriptions: LiveData<List<UserSubscription>> =
//                    repository.getAllDesignerSubscriptionByUserType(UserType.TYPE_DESIGNER)
//
//    //designer services by designer id
//    val designerServices: LiveData<List<UserService>> =
//                    repository.getDesignerServicesByUserType(UserType.TYPE_DESIGNER)

//    //designer slots by designer id
//    private val _designerSlot = MutableLiveData<List<SubscriptionSlot>>().apply {value = null}
//    val designerSlot: LiveData<List<SubscriptionSlot>> = _designerSlot
//
//    fun getUserSubscriptionSlotTime(userKey: Long) {
//
//        viewModelScope.launch {
//
////            val result = repository.getUserSubscriptionSlot(userKey)
//////                .let {userInfo ->
//////                if(userInfo !=null){
////            Log.d("ViewModel_SubsSlot", "user Subs ${result.toString()}")
//////                }
//////            }
////
////            if (result is Result.Success) {
////                Log.d("userSubsSlot", "Success ${result.data[0].slot[0].time}")
//////                val userInfo = result.data.let {
//////                    UserInfo(  displayName = it.firstName, id = it.id.toString(),
//////                        email = it.email, type = it.type, imageUrl = it.imageSmallUrl )
//////                }
////                setUserSubscriptionSlotTime(result.data)
////            } else {
////                Log.d("userSubscription","failed" )
////            }
//
//        }
//
//    }
//
//    private fun setUserSubscriptionSlotTime(userSubscriptionSlot: List<SubscriptionSlot>){
//        _designerSlot.value = userSubscriptionSlot
//        _designerSlot.postValue(userSubscriptionSlot)
//    }
//
//    //designer subscriptionSlot dates as list for Calender view
//    private val _designerSubscriptionSlotDates = MutableLiveData<List<SubscriptionSlotDate>>().apply {
//        value = null
//    }
//    val designerSubscriptionSlotDates: LiveData<List<SubscriptionSlotDate>> =_designerSubscriptionSlotDates
//
//    fun getUserSubscriptionSlotDates(userKey: Long) {
//
//        viewModelScope.launch {
//
////            val result = repository.getUserSubscription(userKey)
////            Log.d("ViewModel_SubsDates", "user Subs ${result.toString()}")
////
////            if (result is Result.Success) {
////                Log.d("userSubsDates", "Success ${result.data[0].subscription.name}")
////                setUserSubscription(result.data)
////            } else {
////                Log.d("userSubscription","failed" )
////            }
//
//        }
//
//    }
//
//    private fun setUserSubscription(userSubscriptionServices: List<SubscriptionSlotDate>){
//        _designerSubscriptionSlotDates.value = userSubscriptionServices
//        _designerSubscriptionSlotDates.postValue(userSubscriptionServices)
//    }

//    val calendarWeekList = mutableListOf<CalendarEntity>()

//    val slotDate = MutableLiveData<Long>().apply { value = System.currentTimeMillis() }

    //Profile selected designer position Viewpager2
    private val _position: MutableLiveData<Int> = MutableLiveData()
    val position: LiveData<Int> get() = _position
    fun selectPosition(position: Int) {
        _position.postValue(position)
    }

    //Booking Calender Recycler View

    //date Selection
    private val _datePosition: MutableLiveData<Int> = MutableLiveData()
    val datePosition: LiveData<Int> get() = _datePosition
    fun selectedDatePosition(position: Int) {
        _datePosition.postValue(position)
    }

    //Booking Designer Slot ViewPager2

    //Booking set designer tabs text
    private val _tabItems: MutableLiveData<List<String>> = MutableLiveData()
    val tabItems: LiveData<List<String>> get() = _tabItems

    //Booking selected plan from tabs
    private val _tabPosition: MutableLiveData<Int> = MutableLiveData()
    val tabPosition: LiveData<Int> get() = _tabPosition
    fun selectTab(position: Int) {
        _tabPosition.postValue(position)
    }

//    //Booking slot date ID from calender date selection
//    private val _calenderEntity: MutableLiveData<CalendarEntity> = MutableLiveData()
//    val calenderEntity: LiveData<CalendarEntity> get() = _calenderEntity
//    fun setCalenderEntity(date: CalendarEntity) {
//        _calenderEntity.value = date
//        _calenderEntity.postValue(date)
//    }


    //Booking slot date ID from calender date selection
    private val _slotDateId: MutableLiveData<Int> = MutableLiveData()
    val slotDateId: LiveData<Int> get() = _slotDateId
    fun selectedSlotDateId(dateId: Int) {
        _slotDateId.value = dateId
        _slotDateId.postValue(dateId)
    }

    //Booking selected calender date string value for display
    private val _slotDateString: MutableLiveData<String> = MutableLiveData()
    val slotDateString: LiveData<String> get() = _slotDateString
    fun selectedSlotDate(date: String) {
        _slotDateString.value = date
        _slotDateString.postValue(date)
    }

    //Booking slots Time ID
    private val _slotSelected: MutableLiveData<Int> = MutableLiveData()
    val slotSelected: LiveData<Int> get() = _slotSelected
    fun setSlotSelected(dateId: Int) {
        _slotSelected.value = dateId
        _slotSelected.postValue(dateId)
    }

    //Booking selected slot time value for display
    private val _slotTimeString: MutableLiveData<String> = MutableLiveData()
    val slotTimeString: LiveData<String> get() = _slotTimeString
    fun setSlotTime(time: String) {
        _slotTimeString.value = time
        _slotTimeString.postValue(time)
    }

    fun navigationComplete() {

        _position.value = null

        _tabPosition.value = null
        _slotDateId.value = null
        _slotDateString.value = null
        _slotSelected.value = null
        _slotTimeString.value = null

        _datePosition.value = null



    }

    init {

//        _tabItems.postValue(TAB_ITEMS)

         _slotDateString.postValue("0")
         _slotTimeString.postValue("0")
         _slotDateString.postValue("0")
         _slotDateId.postValue(0)
         _slotSelected.postValue(0)

         _datePosition.postValue(1)
         _tabPosition.postValue(0)
         _slotSelected.postValue(-1)
    }

//    companion object {
//        private val TAB_ITEMS = listOf("1","2","3")
//    }

}