package com.memandis.project.diary.vm

import androidx.lifecycle.*
import com.memandis.remote.datasource.RealtimeDatabaseRepository
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.Resource
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase.getDiaryEntriesInDay
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase.getDiaryEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DiaryViewModel(/*private val _realtimeDatabaseRepository: RealtimeDatabaseRepository*/) : ViewModel() {

    private val dateHolderLiveData: MutableLiveData<DiaryDateHolder> = MutableLiveData()

    val entryListByDateHolder: LiveData<Resource<List<DiaryEntry>>>
    by lazy {
        Transformations.switchMap(dateHolderLiveData) {  holder ->
            LiveDataReactiveStreams.fromPublisher(getDiaryEntriesInDay(holder.date))
        }
    }

    fun setDateHolder(dateHolder: DiaryDateHolder) {
        dateHolderLiveData.postValue(dateHolder)
    }

    val listAllEntries: LiveData<Resource<List<DiaryEntry>>>  by lazy {
        LiveDataReactiveStreams.fromPublisher(getDiaryEntry())
    }

    //################PROJECT##########################################################

    private val _userProject = MutableLiveData<List<DiaryEntry>>().apply {value = null }
    val userProject : LiveData<List<DiaryEntry>> = _userProject

//    fun getUserProject(projectKey: Long) {
////        _userServices.value = repository.getServiceByID(userKey).asLiveData().value
//        Log.d("ViewModel_Services", "user projects get...$projectKey")
////                //as MutableLiveData<List<Category>>
////        _userServices.postValue(repository.getServiceByID(userKey).asLiveData().value)
//
//        viewModelScope.launch {
////
////            val result = getProjectsUseCase(projectKey)
//////                repository.getProjectByID(1)
////
//////                .let {userInfo ->
//////                if(userInfo !=null){
//////                    Log.d("ViewModel_Services1", "user Services "+ userInfo)
//////                    setUserServices(userInfo.)
//////                }
//////            }
////
////            if (result is Result.Success) {
////                Log.d("HomeViewModel_Services", "Success ${result.data[0].project.projectName}")
//////                val userInfo = result.data.let {
//////                    UserInfo(  displayName = it.firstName, id = it.id.toString(),
//////                        email = it.email, type = it.type, imageUrl = it.imageSmallUrl )
//////                }
////                setUserProject(result.data)
////            } else {
////                Log.d("ViewModel_PResult","failed" )
////            }
//
//        }
//
//    }
//
    private fun setUserProject(projectAndImages: List<DiaryEntry>){
        _userProject.value = projectAndImages
        _userProject.postValue(projectAndImages)
    }

//    //
//    @InternalCoroutinesApi
//    @ExperimentalCoroutinesApi
//    fun fetchProjects() {
//        viewModelScope.launch {
//            // FlowCollector is used as an intermediate or a terminal collector of the flow and
//            // represents an entity that accepts values emitted by the Flow
//            // Each time we invoke the sendBlocking() from a Flow it triggers the collector
//            // that presents the result
//            _realtimeDatabaseRepository.fetchProjectEntry().collect {
//                when {
//                    it.isSuccess -> {
//                        val list = it.getOrNull()
//                        // post on a LiveData
//                        if (list != null) {
//                            setUserProject(list)
//                        }
//                    }
//                    it.isFailure -> {
//                        it.exceptionOrNull()?.printStackTrace()
//                    }
//                }
//            }
//        }
//    }
}