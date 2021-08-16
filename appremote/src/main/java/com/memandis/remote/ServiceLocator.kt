package com.memandis.remote

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.memandis.remote.datasource.FirebaseAppRepository
import com.memandis.remote.datasource.RealtimeDatabaseRepository
import com.memandis.remote.datasource.RealtimeDatabaseRepositoryImpl
import kotlinx.coroutines.*
import java.util.*

/**
 * A Service Locator for the [MyDezignerRepository]. This is the mock version, with a
 * [MyDezignerRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()

//    // Singleton prevents multiple instances of database opening at the same time.
//    @Volatile
//    private var INSTANCE: MyDzDatabase? = null

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

        @Volatile
    var realtimeDatabaseRepository : RealtimeDatabaseRepository? = null
        @VisibleForTesting set

    //user repository
    fun provideRealtimeDatabaseRepository(context: Context): RealtimeDatabaseRepository {
        synchronized(this) {
            return realtimeDatabaseRepository ?: realtimeDatabaseRepository  ?:
            createRealtimeDatabaseRepository(context)
        }
    }

    private fun createRealtimeDatabaseRepository(context: Context): RealtimeDatabaseRepository {
        return RealtimeDatabaseRepositoryImpl(FirebaseAppRepository.userRoot)
    }

//
//    @Volatile
//    var myDezignerUserRepository: UserRepository? = null
//        @VisibleForTesting set
//
//    @Volatile
//    var myDezignerServiceRepository: ServiceRepository? = null
//        @VisibleForTesting set
//
//    @Volatile
//    var myDezignerProjectRepository: ProjectRepository? = null
//        @VisibleForTesting set
//
//    @Volatile
//    var myDezignerBookingRepository: BookingRepository? = null
//        @VisibleForTesting set
//
//    //user repository
//    fun provideMyDezignerUserRepository(context: Context): UserRepository {
//        synchronized(this) {
//            return myDezignerUserRepository ?: myDezignerUserRepository ?:
//            createMyDezignerUserRepository(context)
//        }
//    }
//
//    private fun createMyDezignerUserRepository(context: Context): UserRepository {
//        return UserRepositoryImpl(
//                createMyDezignerUserLocalDataSource(context) as UserDataLocalSource,
//                MyDezignerRemoteDataSource,
//                userMapper = UserDomainDataMapper(),
//                ioDispatcher
//        )
//    }
//
//    private fun createMyDezignerUserLocalDataSource(context: Context): UserDataLocalSource {
//        val database = getDatabase(context, applicationScope)
//        return UserLocalDataSourceImpl(database.userDao(),
//                                       userDataLocalMapper =  UserDataLocalMapper(),
//                                       ioDispatcher
//        )
//    }
//
//    //service repository
//    fun provideMyDezignerServiceRepository(context: Context): ServiceRepository {
//        synchronized(this) {
//            return myDezignerServiceRepository ?: myDezignerServiceRepository ?:
//            createMyDezignerServiceRepository(context)
//        }
//    }
//
//    private fun createMyDezignerServiceRepository(context: Context): ServiceRepository {
//        return ServiceRepositoryImpl(
//            createMyDezignerServiceLocalDataSource(context) as ServiceDataLocalSource,
//            MyDezignerRemotServicesDataSource,
//            serviceMapper = ServiceDomainDataMapper(),
//            ioDispatcher
//        )
//    }
//
//    private fun createMyDezignerServiceLocalDataSource(context: Context): ServiceDataLocalSource {
//        val database = getDatabase(context, applicationScope)
//        return ServiceLocalDataSourceImpl(database.servicesDao(),
//            serviceDataLocalMapper =  ServiceDataLocalMapper(),
//            ioDispatcher
//        )
//    }
//
//    //project repository
//    fun provideMyDezignerProjectRepository(context: Context): ProjectRepository {
//        synchronized(this) {
//            return myDezignerProjectRepository ?: myDezignerProjectRepository ?:
//            createMyDezignerProjectRepository(context)
//        }
//    }
//
//    private fun createMyDezignerProjectRepository(context: Context): ProjectRepository {
//        return ProjectRepositoryImpl(
//            createMyDezignerProjectLocalDataSource(context) as ProjectDataLocalSource,
//            MyDezignerRemoteProjectsDataSource,
//            projectMapper = ProjectDomainDataMapper(),
//            ioDispatcher
//        )
//    }
//
//    private fun createMyDezignerProjectLocalDataSource(context: Context): ProjectDataLocalSource {
//        val database = getDatabase(context, applicationScope)
//        return ProjectLocalDataSourceImpl(database.projectDao(),
//            projectDataLocalMapper =  ProjectDataLocalMapper(),
//            ioDispatcher
//        )
//    }
//
//    //booking repository
//    fun provideMyDezignerBookingRepository(context: Context): BookingRepository {
//        synchronized(this) {
//            return myDezignerBookingRepository ?: myDezignerBookingRepository ?:
//            createMyDezignerBookingRepository(context)
//        }
//    }
//
//    private fun createMyDezignerBookingRepository(context: Context): BookingRepository {
//        return BookingRepositoryImpl(
//            createMyDezignerBookingLocalDataSource(context) as BookingDataLocalSource,
//            MyDezignerRemoteBookingDataSource,
//            bookingMapper = BookingDomainDataMapper(),
//            ioDispatcher
//        )
//    }
//
//    private fun createMyDezignerBookingLocalDataSource(context: Context): BookingDataLocalSource {
//        val database = getDatabase(context, applicationScope)
//        return BookingLocalDataSourceImpl(database.appointmentDao(),
//            bookingDataLocalMapper =  BookingDataLocalMapper(),
//            ioDispatcher
//        )
//    }
//
//
//    fun getDatabase(context: Context, scope: CoroutineScope): MyDzDatabase {
//        // if the INSTANCE is not null, then return it,
//        // if it is, then create the database
//        return INSTANCE ?: synchronized(this) {
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                MyDzDatabase::class.java,
//                "MyDezigner"
//            )
//                .addCallback(UserDatabaseCallback(scope))
////                .createFromAsset("database/mydezigner.db")
//                .build()
//            INSTANCE = instance
//            // return instance
//            instance
//        }
//    }
//
//    private class UserDatabaseCallback(
//        private val scope: CoroutineScope
//    ) : RoomDatabase.Callback() {
//
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(
//                        database.userDao(),
//                        database.projectDao(),
//                        database.myImagesDao(),
//                        database.myLocationDao(),
//                        database.userDetailDao(),
//                        database.appointmentDao(),
//                        database.servicesDao(),
//                        database.subscriptionDao(),
//                        database.slotDao()
//                    )
//                }
//            }
//        }
//
//        suspend fun populateDatabase(userDao: UserDao,
//                                     projectDao: ProjectDao,
//                                     myImagesDao: MyDzImagesDao,
//                                     myLocationDao: LocationDao,
//                                     userDetailDao: UserDetailDao,
//                                     appointmentDao: BookingDao,
//                                     servicesDao: ServicesDao,
//                                     subscriptionDao: SubscriptionDao,
//                                     slotDao: SlotDao
//        ) {
//            // Delete all content here.
//            userDao.deleteAll()
//            projectDao.deleteAll()
//            myImagesDao.deleteAll()
//            myLocationDao.deleteAll()
//            userDetailDao.deleteAllServices()
//            servicesDao.deleteAll()
//            subscriptionDao.deleteAll()
//            slotDao.deleteAll()
//            appointmentDao.deleteAll()
//
//            //
//            val BASEURL: String = "https://selfless.s3-eu-west-1.amazonaws.com/data/project_"
//            var userImageUrl  = "https://selfless.s3-eu-west-1.amazonaws.com/data/user_icon.png"
//            val PBASEURL: String = "https://selfless.s3-eu-west-1.amazonaws.com/data/"
//            val placeholder =  "https://selfless.s3-eu-west-1.amazonaws.com/data/dummy_men.png"
////            val servicesurl = "https://selfless.s3-eu-west-1.amazonaws.com/data/"
//
//            val password = "password"
//            //client
//            val clientData = arrayOf("Client_1", "Client_2", "Client_3")
//            val clientNameFirst = arrayOf("cfirst1", "cfirst2", "cfirst3")
//            val clientEmail = arrayOf("client1@company.com", "client2@company.com", "client3@company.com")
//            val clientNameLast = arrayOf("clast1", "clast2", "clast3")
//            val clientDOB = arrayOf("12/01/2021", "12/01/2021", "12/01/2021")
//            val clientDOR = arrayOf("12/01/2021", "12/01/2021", "12/01/2021")
//            val clientPhone = arrayOf("9650415551", "9899395207", "9599303338")
//            val clientGender = arrayOf("Male", "Female", "Male")
//
//            //designer
//            val designerData = arrayOf("Designer_1", "Designer_2", "Designer_3")
//            val designerNameFirst = arrayOf("Sanjay", "Mahima", "Dinesh")
//            val designerEmail = arrayOf("Sanjay@company.com", "Mahima@company.com", "Dinesh@company.com")
//            val designerNameLast = arrayOf("Surya", "Surya", "Bhardwaj")
//            val designerDOB = arrayOf("12/01/2021", "12/01/2021", "12/01/2021")
//            val designerDOR = arrayOf("12/01/2021", "12/01/2021", "12/01/2021")
//            val designerPhone = arrayOf("9811275211", "9810883308", "9599303338")
//            val designerGender = arrayOf("Male", "Female", "Male")
////            var address = Address("adress0", "state0", "city0", 1)
//            //designer settings
//            val designerDomain = arrayOf("Architect", "Architect", "Architect")
//            val designerLocation = arrayOf("New delhi", "New delhi", "New delhi")
//            val designerExperience = arrayOf("22 yrs ", "17 Yrs", "45 Yrs")
//            val designerAge = arrayOf("45", "39", "66")
//            val designerExpertise = arrayOf("tech-centric ", "User-Centric", "Self-Expression")
//            //project
//            val projectName = arrayOf("project_1", "project_2", "project_3", "project_4")
//            val projectDate = arrayOf("12/01/2021","18/01/2021","20/01/2021","22/01/2021")
//            val projectDescription = arrayOf("project_1_description","project_2_description",
//                "project_3_description","project_4_description")
//            val projectComment = arrayOf("project_1_comment", "project_2_comment",
//                "project_3_comment","project_4_comment")
//            val projectLocation_lat = arrayOf(0.0, 0.0, 0.0)
//            val projectLocation_log = arrayOf(0.0, 0.0, 0.0)
//            val projectLocation_zom = arrayOf(5, 15, 20)
//
//            val projectDataSize = arrayOf("88888888", "666666", "55555", "88888888")
//            val projectDataTitle = arrayOf("image_Title", "video_Title")
//            val projectDataDescription= arrayOf( "image_desc", "video_desc")
//            val projectDataType= arrayOf("image", "video")
//
//            //service
//            val categories = arrayOf("Architecture", "Interiors", "Landscape",
//                "Design", "Vastu", "Real Estate")
//            val categoriesInfo = arrayOf("Architecture_info", "Interiors_info", "Landscape_info",
//                "Design_info", "Vastu_info", "Real Estate_info")
//
//            //subscription
//            val plans = arrayOf("Basic", "Standard", "Premium")
//            val info = arrayOf("Discuss", "Ideate", "BrainStorm")
//            val duration = arrayOf("10 Minutes", "20 Minutes", "40 minutes")
////            val currency = arrayOf("Rs", "Rs", "Rs")
//            val prices = arrayOf(
//                arrayOf("899 ", "2899", "4899"),
//                arrayOf("799 ", "2799", "4799"),
//                arrayOf("999 ", "2999", "4999")
//            )
//            //slot
//            val slotDate = arrayOf("15/05/2021", "16/05/2021", "17/05/2021",
//                "18/05/2021", "19/05/2021", "20/05/2021", "21/05/2021")
////            val slotDate_day = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
////            val slotDate_date = arrayOf("05", "06", "07", "08", "09", "10", "11")
////            val slotDate_month = arrayOf("02", "02", "02", "02", "02", "02", "02")
////            val slotDate_year = arrayOf("2021", "2021", "2021", "2021", "2021", "2021", "2021")
//
//            val basicSlotTime = arrayOf(
//                arrayOf("14:00", "14:15", "14:30", "14:45"),
//                arrayOf("14:00", "14:15", "14:30", "14:45"),
//                arrayOf("14:00", "14:15", "14:30", "14:45")
//            )
//            val standardSlotTime = arrayOf(
//                arrayOf("15:00", "15:30", "16:00", "16:30"),
//                arrayOf("15:00", "15:30", "16:00", "16:30"),
//                arrayOf("15:00", "15:30", "16:00", "16:30")
//            )
//            val premiumSlotTime = arrayOf(
//                arrayOf("17:00", "18:00"),
//                arrayOf("17:00", "18:00"),
//                arrayOf("17:00", "18:00")
//            )
//
//            val bookingName = arrayOf("MyDz_B1_C1_D1", "MyDz_B2_C2_D2", "MyDz_B3_C3_D3",
//                "MyDz_B3_C3_D3")
//            val bookingDate = arrayOf("11/02/2021","10/02/2021","08/02/2021",
//                "08/01/2021")
//            val bookingComment = arrayOf("not started","not started","not started","not started",
//                "completed")
//
//            //category insert
////           val listImages: List<Int>? = getServiceImageDataList()
//            for((c, service) in categories.withIndex()) {
//                var category = ServiceLocal(0,
//                    service,
//                    0,
////                    listImages?.get(c),
//                    getProviderCloudUrl(PBASEURL, "service$c"),
//                    getProviderCloudUrl(PBASEURL, "service$c"),
//                    categoriesInfo[c])
//                servicesDao.insert(category)
//            }
//            val cList : List<ServiceLocal> = servicesDao.loadAll()
//
//            for((i, value) in designerData.withIndex()) {
//
//                var designer= UserLocal(
//                    0L,
//                    value,
//                    designerNameFirst[i],
//                    designerNameLast[i],
//                    password,
//                    designerEmail[i],
//                    designerGender[i],
//                    designerDOB[i],
//                    getProviderCloudUrl(PBASEURL, designerNameFirst[i].toLowerCase()),
//                    getProviderCloudUrl( PBASEURL,designerNameFirst[i].toLowerCase() + "_thumbnail" ),
//                    designerDOR[i],
//                    designerPhone[i],
//                    isDesigner = true
////                    com.svayantra.appdomain.domain.entities.UserType.TYPE_DESIGNER
////                    address
//                )
//                userDao.insert(designer);
//
//                val designerId: Long = userDao.queryLastInsert()
//
//                var dservice = UserDetail(0, designerId, designerDomain[i], designerLocation[i],
//                    designerExperience[i], designerAge[i], designerExpertise[i],
//                    placeholder)
//                userDetailDao.insert(dservice)
//                //designer subscription insert
//                for((p, pValue) in plans.withIndex()) {
//
//                    var subscription = Subscription(0, designerId, pValue,
//                        info[p], duration[p], prices[i][p])
//                    subscriptionDao.insert(subscription)
//                    val subscriptionId: Long = subscriptionDao.queryLastInsert()
//
//                    //designer day time slots insert
//                    for(dValue in slotDate) {
//
//                        val sdf = SimpleDateFormat("dd/MM/yyyy")
//                        val date: Date = sdf.parse(dValue)
//                        val startDate = date.time
////                        var calendarEntity = CalendarEntity(dValue,"Mon", "08","02","2021")
//                        if(p==0) {
//                            for (bTime in basicSlotTime[i]) {
//                                slotDao.insert(  Slot(
//                                    0L, designerId, subscriptionId,
//                                    dValue, startDate,
//                                    bTime
////                                    , SlotStatus.SLOT_ACTIVE
//                                )
//                                )
//                            }
//                        } else  if(p==1) {
//                            for (sTime in standardSlotTime[i]) {
//                                slotDao.insert(
//                                    Slot(
//                                        0L, designerId, subscriptionId,
//                                        dValue, startDate,
//                                        sTime
////                                        , SlotStatus.SLOT_ACTIVE
//                                    )
//                                )
//                            }
//                        } else if(p==2) {
//                            for(prTime in premiumSlotTime[i]){
//                                slotDao.insert(
//                                    Slot(
//                                        0L, designerId, subscriptionId,
//                                        dValue, startDate,
//                                        prTime
////                                            , SlotStatus.SLOT_ACTIVE
//                                    )
//                                )}
//                        }
//
//                    }
//
//
//                }
//
//
//
//                //client info
//                var user = UserLocal(
//                    0L, clientData[i], clientNameFirst[i], clientNameLast[i],
//                    password, clientEmail[i], clientGender[i], clientDOB[i],
//                    userImageUrl, userImageUrl, clientDOR[i], clientPhone[i],
////                    com.svayantra.appdomain.domain.entities.UserType.TYPE_CLIENT
//                    isDesigner = false
//                )
//                userDao.insert(user)
//                val userId: Long = userDao.queryLastInsert()
//
//                //client services insert
//                var cservice = UserDetail(0, userId, designerDomain[i], designerLocation[i],
//                    designerExperience[i], designerAge[i], designerExpertise[i],
//                    userImageUrl)
//
//                userDetailDao.insert(cservice)
//
////                val serviceId: Long = serviceDao.queryLastInsert()
//
//                //client project
//                var project = ProjectLocal(
//                    0L,
//                    userId,
//                    isOwnerDesigner = false
////                    com.svayantra.appdomain.domain.entities.UserType.TYPE_CLIENT
//                    , designerId,
//                    cList[i].id, projectDate[i + 1], projectName[i + 1],
//                    projectDescription[i + 1], projectComment[i + 1], 10
//                )
//                projectDao.insert(project)
//
//                val projectId: Long = projectDao.queryLastInsert()
//
//                //client project data insert
//                //location
//                myLocationDao.insert(
//                    Location(0, projectId,
//                        projectLocation_lat[i], projectLocation_log[i],projectLocation_zom[i])
//                )
//
//                //images
//                for(key in projectDataSize[i + 1].indices){
//                    myImagesDao.insert(
//                        MyDzImages( 0, projectId, projectDataTitle[0], projectDataDescription[0],
//                            projectDataType[0], getImageCloudUrl(BASEURL,i + 2,
//                                key + 1) )
//                    )
//                }
//                //video
//                myImagesDao.insert(
//                    MyDzImages(0, projectId, projectDataTitle[1], projectDataDescription[1],
//                        projectDataType[1],getVideoCloudUrl(BASEURL,i + 2, 1))
//                )
//
//                val slotId: Long = slotDao.queryLastInsert()
//
//                //client booking insert
//                var appointment = BookingLocal(
//                    0,
//                    userId,
//                    projectId,
//                    slotId,
//                    bookingName[i],
//                    bookingDate[i],
//                    bookingDate[i],
////                    com.svayantra.appdomain.domain.entities.BookingStatus.BOOKING_WAITING,
//                    bookingComment[i]
//                )
//                appointmentDao.insert(appointment)
//
//            }
//
//        }
//
//        private fun getImageCloudUrl(baseURL: String, dId: Int, position: Int): String {
//            return "$baseURL$dId/images/"+"image_"+dId+"_"+position+".jpg"
//        }
//
//        private fun getVideoCloudUrl(baseURL: String, dId: Int, position: Int): String {
//            return "$baseURL$dId/videos/"+"video_"+dId+"_"+position+".mp4"
//        }
//
//        private fun getProviderCloudUrl(baseURL: String, position: String): String {
//            return "$baseURL$position.png"
//        }
//
////        private fun getServiceImageDataList(): List<Int>? {
////            val imageList: MutableList<Int> = ArrayList()
////            imageList.add(R.drawable.service1)
////            imageList.add(R.drawable.service2)
////            imageList.add(R.drawable.service3)
////            imageList.add(R.drawable.service4)
////            imageList.add(R.drawable.service5)
////            imageList.add(R.drawable.service6)
////            return imageList
////        }
//
//    }
//
//    @VisibleForTesting
//    fun resetRepository() {
//        synchronized(lock) {
//            runBlocking {
//                MyDezignerRemoteDataSource.deleteAllUsers()
////                FakeMyDezignerRemoteDataSource.deleteAllServices()
////                FakeMyDezignerRemoteDataSource.deleteAllProjects()
////                FakeMyDezignerRemoteDataSource.deleteAllMeetings()
//            }
////            // Clear all data to avoid test pollution.
////            database?.apply {
////                clearAllTables()
////                close()
////            }
////            database = null
////            myDezignerRepository = null
//        }
//    }
//
////    private fun exportDatabase(databaseName: String) {
////        try {
////            val sd = EnvironmentCompat.getExternalStorageDirectory()
////            val data = EnvironmentCompat.getDataDirectory()
////
////            if (sd.canWrite()) {
////                val currentDBPath = "//data//${activity.packageName}//databases//$databaseName"
////                val backupDBPath = "backupname.db"
////                val currentDB = File(data, currentDBPath)
////                val backupDB = File(sd, backupDBPath)
////                Log.i("TESTEST","backupDb: "+backupDB.absoluteFile)
////
////                if (currentDB.exists()) {
////                    val src = FileInputStream(currentDB).getChannel()
////                    val dst = FileOutputStream(backupDB).getChannel()
////                    dst.transferFrom(src, 0, src.size())
////                    src.close()
////                    dst.close()
////                }
////            } else {
////                Toast.makeText(activity,"Did you accept write sd card permission? ", Toast.LENGTH_LONG).show()
////            }
////        } catch (e: Exception) {
////            Log.e("TESTEST",""+e);
////        }
////
////    }

}