package com.memandis.project.entry

//import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import es.dmoral.toasty.Toasty
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.memandis.remote.datasource.model.diary.DiaryTag
import com.memandis.appmain.R
import com.memandis.appmain.databinding.FragmentEntryEditBinding
import com.memandis.project.entry.vm.EntryEditorViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.*
import java.util.concurrent.Executors
import com.memandis.remote.utils.*
import com.memandis.project.diary.recyclerview.MarginItemDecoration
import com.memandis.remote.utils.app.AppConstants
import com.memandis.remote.utils.background.invokeImageSelectionIntent
import com.memandis.remote.utils.background.invokeVideoSelectionIntent
import com.memandis.remote.utils.binding.changeBackgroundColor
import com.memandis.remote.utils.map.CODE_PERMISSION_FINE_LOCATION
import com.memandis.remote.utils.map.CODE_PERMISSION_READ_STORAGE
import com.memandis.remote.utils.map.isGrantedExternalStoragePermission
import com.memandis.remote.utils.map.isGrantedFineLocationPermission

/**
 * A Fragment responsible for hosting a whole process of Editing/Creating a DiaryEntry
 * @property LOG_TAG String String Tag string for showing Debugging Log
 * @property viewModel EntryEditorViewModel Data holder for this Fragment
 */

class EntryEditFragment : Fragment()/*, OnMapReadyCallback */{

    private val LOG_TAG = this::class.java.simpleName

    private lateinit var viewModel: EntryEditorViewModel

    private var _binding: FragmentEntryEditBinding? = null

    private val binding get() = _binding!!

//    private lateinit var navViewModel: NavigationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentEntryEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(EntryEditorViewModel::class.java)

        binding.toolbar.apply {
            title = if(viewModel.isModification){
                getString(R.string.title_entry_edit)
            }else{
                getString(R.string.title_entry_create)
            }

            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        val markwon: Markwon = Markwon.create(requireContext())
        val markwonEditor = MarkwonEditor.builder(markwon).build()

        val imageAdapter = ImageCarouselRvAdapter()
        binding.imageCarousel.apply {
            adapter = imageAdapter
            clipToPadding = false
            binding.imageCarouselIndicator.setViewPager(this)
        }

        imageAdapter.registerAdapterDataObserver(binding.imageCarouselIndicator.adapterDataObserver)

        viewModel.allImages.observe(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "Image Changed: $it")
            imageAdapter.submitList(it)
//             if(it.isNullOrEmpty()) {
//                 Glide.with(this@EntryEditFragment).load(R.color.colorPrimaryDark).centerCrop()
//                         .into(heroImageView)
//                 binding.bottomSheetView.imageBtn.setChipIconResource(R.drawable.ic_image_add_black_24dp)
//             } else {
//                 Glide.with(this@EntryEditFragment).load(it.last()).centerCrop().into(heroImageView)
//                 binding.bottomSheetView.imageBtn.setChipIconResource(R.drawable.ic_image_remove_black_24dp)
//             }
        })

        viewModel.title.observe(viewLifecycleOwner, Observer {
        Log.d(LOG_TAG, "Title Changed: $it")
            binding.titleTextView.apply {
                text = if(it.isNullOrBlank()) {
                    context.getString(R.string.untitled)
                } else {
                    it
                }

                val textColorRes = when(it) {
                    null -> R.color.whiteAlpha50
                    else -> R.color.colorPrimary
                }
                setTextColor(context.getColor(textColorRes))
            }
        })

        viewModel.subtitle.observe(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "Subtitle Changed: $it")
            binding.subtitleTextView.apply {
                text = if(it.isNullOrBlank()) {
                    context.getString(R.string.untitled)
                } else {
                    it
                }

                val textColorRes = when(it) {
                    null -> R.color.whiteAlpha50
                    else -> R.color.colorPrimary
                }
                setTextColor(context.getColor(textColorRes))
            }
        })

        viewModel.dateText.observe(viewLifecycleOwner, Observer {
            binding.overlineTextView.text = it
        })

        binding.overlineTextView.setOnClickListener {
            MaterialDialog(it.context).show {

                title(res = R.string.dialog_edit_entry_date_created)
                val c = Calendar.getInstance().apply {
                    time = viewModel.date.value!!
                }

                dateTimePicker(currentDateTime = c, show24HoursView = true) { _, dateTime ->
                    viewModel.date.value = dateTime.time
                }
            }
        }

        viewModel.content.observe(viewLifecycleOwner, object : Observer<String> {
            var count = 0
            override fun onChanged(it: String?) {
                // TODO("Not yet implemented")
                Log.d(LOG_TAG, "Updating Content Text: $it")
                binding.diaryTextEditor.text = SpannableStringBuilder(it)
                count++

                if(count == 2 || !viewModel.isModification) {
                    // Configure the markdown editor
                    binding.diaryTextEditor.apply {
                        addTextChangedListener(MarkwonEditorTextWatcher.
                          withPreRender(markwonEditor,
                            Executors.newCachedThreadPool(),this))
                        doOnTextChanged { text, _, _, _ ->
                            viewModel.content.value = text.toString()
                        }
                        this.text?.let { markwonEditor.process(it) }
                    }
                    viewModel.content.removeObserver(this)
                }
            }
        })

        binding.saveBtn.setOnClickListener {
            MaterialDialog(it.context).show {
                cornerRadius(res = R.dimen.dialog_corner_radius)
                title(res = R.string.dialog_save_the_entry)
                positiveButton(android.R.string.ok) {
                    if(!viewModel.isModification) {
                        viewModel
                                .saveData()
                                .subscribe { it2, throwable ->
                                    Log.d(LOG_TAG, "Upload Data Completed: $it2")
//                                    Toasty.success(context, getString(R.string.status_saved)).show()
//                                    requireActivity().finish()
                                }
                    } else {
                        viewModel
                                .updateData()
                                .subscribe { data, throwable ->
                                    Log.d(LOG_TAG, "Saved Data Completed: $data")
//                                    Toasty.success(context, getString(R.string.status_saved)).show()
//                                    requireActivity().finish()
                                }
                    }
                }
                negativeButton(android.R.string.cancel)
            }
        }

        // TODO: Clean this up to a resource file
        val titleDialogClickListener = { _: View ->
            MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                val prefillText = if(viewModel.title.value.isNullOrEmpty()) "" else viewModel.title.value

                Log.d(LOG_TAG, "Title Dialog Prefill: ${viewModel.title.value}")

                // cornerRadius(res = R.dimen.dialog_corner_radius)
                title(R.string.dialog_edit_entry_title)
                input(waitForPositiveButton = true, prefill = prefillText,
                      hintRes = R.string.entry_title,
                      inputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
                      maxLength = 30) { _, inputText ->
                    viewModel.title.value = inputText.toString()
                }

                // onCancel { input(prefill = "") }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    this.getInputField().setTextCursorDrawable(R.drawable.edit_text_cursor)
                }

                positiveButton { R.string.submit }
                negativeButton { R.string.cancel }
            }
        }.also { listener ->
            binding.titleTextView.setOnClickListener { listener(it) }
            binding.titleEditBtn.setOnClickListener { listener(it) }
        }

        // TODO: Clean this up to a resource file
        val subtitleDialogClickListener = { _: View ->
            MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                val prefillText = if(viewModel.subtitle.value == null) "" else viewModel.subtitle.value

                // cornerRadius(res = R.dimen.dialog_corner_radius)
                title(R.string.dialog_edit_entry_subtitle)
                input(waitForPositiveButton = true, prefill = prefillText,
                      hintRes = R.string.entry_subtitle,
                      inputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
                      maxLength = 50) { _, inputText ->
                    viewModel.subtitle.value = inputText.toString()
                }

                // onCancel { input(prefill = "") }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    this.getInputField().setTextCursorDrawable(R.drawable.edit_text_cursor)
                }

                positiveButton { R.string.submit }
                negativeButton { R.string.cancel }
            }
        }.also { listener ->
            binding.subtitleTextView.setOnClickListener { listener(it) }
            binding.subtitleEditBtn.setOnClickListener { listener(it) }
        }

        val colorDialogClickListener = { _: View ->

            if(requestPermissions(CODE_PERMISSION_READ_STORAGE)) {
                invokeVideoSelectionIntent()
            }
//            MaterialDialog(requireContext()).show {
//                val colors = intArrayOf(R.color.AmberA400,
//                                        R.color.BlueA400,
//                                        R.color.CyanA400,
//                                        R.color.GreenA400,
//                                        R.color.Cyan400,
//                                        R.color.Pink400,
//                                        R.color.Brown400,
//                                        R.color.Grey400,
//                                        R.color.BlueGrey400,
//                                        R.color.RedA400).map {
//                    context.getColor(it)
//                }.toIntArray() // size = 3
//
//                title(R.string.dialog_edit_entry_color)
//
//                colorChooser(colors, allowCustomArgb = true,
//                             initialSelection = viewModel.color.value,
//                             selection = { dialog, color ->
//                                 viewModel.color.value = color
//                             })
//                positiveButton(R.string.submit)
//            }
        }.also { listener ->
            binding.bottomSheetView.videoSelectBtn
                     .setOnClickListener {
                      listener(it)
                 }
        }

        // overlayImageView.setOnClickListener { invokeImageSelectionIntent() }

        binding.bottomSheetView.apply {

            // get the bottom sheet view
            val bottomSheetLayout: MaterialCardView = binding.bottomSheetView.bottomSheetView

            // init the bottom sheet behavior
            val btmSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

            locationBtn.setOnClickListener {
                if(requestPermissions(CODE_PERMISSION_FINE_LOCATION)) {
                    btmSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    initializeLocationAndMap()
                }
            }

//            // set listener on button click
            collapseImageBtn.setOnClickListener {
                if(btmSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    btmSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                } else {
                    btmSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
            }

            btmSheetBehavior.addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffsetPercent: Float) {
                    // TODO("Not yet implemented")
                    ViewCompat.animate(collapseImageBtn)
                            .rotation(-slideOffsetPercent * 180f)
                            .withLayer()
                            .setDuration(0L)
                            // .setInterpolator(OvershootInterpolator(10.0F))
                            .start()

                    ViewCompat.animate(binding.saveBtn)
                            .translationY(
                                    -slideOffsetPercent * (bottomSheet.measuredHeight * 0.96f))
                            .withLayer()
                            .translationX(slideOffsetPercent * (bottomSheet.measuredHeight * 0.2f))
                            .alpha(1 - slideOffsetPercent)
                            .setDuration(0L)
                            .start()

                    ViewCompat.animate(locationBtn)
                            .alpha(1 - slideOffsetPercent)
                            .withLayer()
                            .withEndAction {
                                if(locationBtn.alpha == 0f) {
                                    locationBtn.visibility = View.GONE
                                } else {
                                    locationBtn.visibility = View.VISIBLE
                                }
                            }
                            .setDuration(0L)
                            .start()

                    ViewCompat.animate(binding.frameLayout)
                            .alpha(slideOffsetPercent)
                            .withLayer()
                            .setDuration(0L)
                            .start()
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if(newState == BottomSheetBehavior.STATE_EXPANDED) {
                        binding.diaryTextEditor.clearFocus()
                        bottomSheet.requestFocus()
                        binding.nestedScrollView.isNestedScrollingEnabled = false
                    } else {
                        binding.nestedScrollView.isNestedScrollingEnabled = true
                    }
                }

            })
        }

        binding.bottomSheetView.tagBtn.setOnClickListener {
            MaterialDialog(it.context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                // cornerRadius(res = R.dimen.dialog_corner_radius)
                title(R.string.dialog_add_entry_tag)
                input(waitForPositiveButton = true,
                      hintRes = R.string.entry_tag,
                      inputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
                      maxLength = 20) { _, inputText ->
                    inputText.toString().trim().also {
                        viewModel.addDiaryTag(it)
                    }
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    this.getInputField().setTextCursorDrawable(R.drawable.edit_text_cursor)
                }

                cancelOnTouchOutside(false)
                negativeButton { R.string.cancel }
            }
        }

        val chipRvAdapter = ChipRecyclerAdapter(requireContext()) { _, text ->
            viewModel.removeDiaryTag(text)
        }.apply {
            binding.tagEditContainer.adapter = this
            binding.tagEditContainer.setHasFixedSize(false)

//            val chipsLayoutManager = ChipsLayoutManager
//                    .newBuilder(context!!)
//                    // .setChildGravity(Gravity.TOP)
//                    .setScrollingEnabled(false)
//                    .setGravityResolver { Gravity.CENTER }
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .build()
//
//            binding.tagEditContainer.layoutManager = chipsLayoutManager
            binding.tagEditContainer.addItemDecoration(
                    MarginItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
        }

        viewModel.tagArray.observe(viewLifecycleOwner, object : Observer<TreeSet<DiaryTag>> {
            override fun onChanged(it: TreeSet<DiaryTag>?) {
                if(it == null) {
                    return
                }
//                chipRvAdapter.submitList(
//                        it.toList()) { viewModel.color.value = viewModel.color.value }
            }
        })

        viewModel.color.observe(viewLifecycleOwner, Observer { color ->
            if(color == null) {
//                return@observe
            } else {
              chipRvAdapter.setChipBackgroundColor = color
                binding.tagEditContainer.children.iterator().forEach {
                  (it as Chip).changeBackgroundColor(color)
               }
            binding.saveBtn.changeBackgroundColor(color)
            }
        })

        binding.bottomSheetView.goodBadBtnGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(!isChecked) {
                // Prevents the an unexpected behavior where buttons will be wrongly selected
                // When a button is selected, the button group will
                // 1. clear all selection
                // 2. Assign a selection to a button
                // These 2 process will invoke addOnButtonCheckedListenerMethod -> This is the cause of the problem!
                return@addOnButtonCheckedListener
            }
            viewModel.goodBad.value = when(checkedId) {
                R.id.goodBtn    -> 1
                R.id.badBtn     -> -1
                R.id.neutralBtn -> 0
                else            -> throw IllegalStateException("Wrong button selected!")
            }
        }

        viewModel.goodBad.observe(viewLifecycleOwner, object : Observer<Int> {
            var count = 0
            override fun onChanged(goodBad: Int) {
                binding.bottomSheetView.goodBadStatusText.text = when {
                    goodBad >= 1 -> {
                        getString(R.string.goodbad_good)
                    }
                    goodBad < 0  -> {
                        getString(R.string.goodbad_bad)
                    }
                    else         -> {
                        getString(R.string.goodbad_neutral)
                    }
                }

                count++
                // For entry editor initialization
                if(count == 2) {
                    Log.d(LOG_TAG, "Updating GoodBad Score to $goodBad")
                    val btn = when {
                        goodBad >= 1 -> {
                            R.id.goodBtn
                        }
                        goodBad < 0  -> {
                            R.id.badBtn
                        }
                        else         -> {
                            R.id.neutralBtn
                        }
                    }
                    binding.bottomSheetView.goodBadBtnGroup.check(btn)
                }
            }
        })

        binding.bottomSheetView.imageBtn.setOnClickListener {
            if(requestPermissions(CODE_PERMISSION_READ_STORAGE)) {
                invokeImageSelectionIntent()
            }
        }

        if(requireActivity().isGrantedFineLocationPermission()) {
            initializeLocationAndMap()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissions(permissionCode: Int): Boolean {
        if(permissionCode == CODE_PERMISSION_READ_STORAGE) {
            if(!requireActivity().isGrantedExternalStoragePermission()) {
//                Toasty.error(context!!, getString(R.string.permission_request_storage)).show()
                ActivityCompat.requestPermissions(requireActivity(),
                                                  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                  CODE_PERMISSION_READ_STORAGE
                )
            } else {
                return true
            }
        } else if(permissionCode == CODE_PERMISSION_FINE_LOCATION) {
            // Permission is not granted, Should we show an explanation?
            if(!requireActivity().isGrantedFineLocationPermission()) {
//                Toasty.error(context!!, getString(R.string.permission_request_location)).show()
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  CODE_PERMISSION_FINE_LOCATION
                )
            } else {
                return true
            }
        } else {
            return false
        }
        return false
    }

//    private fun submitPost() {
//
//        val userId = uid
//        database.child("users").child(userId).addListenerForSingleValueEvent(
//            object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    // Get user value
//                    val user = dataSnapshot.getValue<User>()
//
//                    if (user == null) {
//                        // User is null, error out
//                        Log.e(TAG, "User $userId is unexpectedly null")
//                        Toast.makeText(context,
//                            "Error: could not fetch user.",
//                            Toast.LENGTH_SHORT).show()
//                    } else {
//                        // Write new post
//                        writeNewPost(userId, user.username.toString(), title, body)
//                    }
//
//                    setEditingEnabled(true)
//                    findNavController().navigate(R.id.action_NewPostFragment_to_MainFragment)
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.w(TAG, "getUser:onCancelled", databaseError.toException())
//                    setEditingEnabled(true)
//                }
//            })
//    }
//
//    private fun writeNewPost(userId: String, username: String, title: String, body: String) {
//        // Create new post at /user-posts/$userid/$postid and at
//        // /posts/$postid simultaneously
//        val key = database.child("posts").push().key
//        if (key == null) {
//            Log.w(TAG, "Couldn't get push key for posts")
//            return
//        }
//
//        val post = Post(userId, username, title, body)
//        val postValues = post.toMap()
//
//        val childUpdates = hashMapOf<String, Any>(
//            "/posts/$key" to postValues,
//            "/user-posts/$userId/$key" to postValues
//        )
//
//        database.updateChildren(childUpdates)
//    }

//    @SuppressLint("MissingPermission")
    private fun initializeLocationAndMap() {
//
//        val locationManager: LocationManager = activity!!.getSystemService(
//                Context.LOCATION_SERVICE) as LocationManager
//
//        val locationListener: LocationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location?) {
//
//                progressMap?.visibility = View.GONE
//                locationEditBtn?.isEnabled = true
//                locationClearBtn?.isEnabled = true
//
//                if(location == null || viewModel.coordinate.value != EMPTY_LOCATION) {
//                    return
//                }
//
//                if(viewModel.isModification && viewModel.coordinate.value != EMPTY_LOCATION) {
//                    return
//                }
//
//                try {
//                    val initCoordinate = Pair(location.latitude, location.longitude)
//                    viewModel.SESSION_DEFAULT_LOCATION = initCoordinate
//                    viewModel.coordinate.value = initCoordinate
//                } catch(e: IOException) {
//                    e.printStackTrace()
//                } finally {
//                    locationManager.removeUpdates(this)
//                }
//            }
//
//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                // TODO("Not yet implemented")
//            }
//
//            @SuppressLint("MissingPermission")
//            override fun onProviderEnabled(provider: String?) {
////                Toasty.info(context!!, getString(R.string.gps_enabled)).show()
//
//                if(viewModel.coordinate.value == EMPTY_LOCATION) {
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300L, 0f,
//                                                           this)
//                    progressMap?.visibility = View.VISIBLE
//                    locationEditBtn?.isEnabled = false
//                    locationClearBtn?.isEnabled = false
//                }
//            }
//
//            override fun onProviderDisabled(provider: String?) {
////                Toasty.error(context!!, getString(R.string.gps_disabled)).show()
//                progressMap?.visibility = View.GONE
//            }
//        }
//
//        // Permission has already been granted
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300L, 0f,
//                                               locationListener)
//
//        // val mapFragment = map.findFragment<Fragment>() as SupportMapFragment
//        val mapFragment = this.childFragmentManager
//                .findFragmentByTag("mapPreview") as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param g GoogleMap an instance of Google Map
     */
//    override fun onMapReady(g: GoogleMap) {
//        g.apply {
//            setMinZoomPreference(15f)
//            uiSettings.setAllGesturesEnabled(false)
//        }
//
//        // Migrate the click to somewhere else
//        locationEditBtn.setOnClickListener {
//            if(activity!!.isGrantedFineLocationPermission()) {
//                val lat = viewModel.coordinate.value!!.first
//                val long = viewModel.coordinate.value!!.second
//
//                val action = EntryEditFragmentDirections.actionEntryEditFragmentToLocationActivity(
//                        lat.toFloat(), long.toFloat()).arguments
//                Log.d(LOG_TAG, "Sent Location Bundle: $lat, $long")
//
//                val intent = Intent(activity!!, LocationActivity::class.java).putExtras(action)
//                startActivityForResult(intent, REQUEST_CODE_LOCATION_PICKER)
//            }
//        }
//
//        viewModel.coordinate.observe(viewLifecycleOwner) {
//            if(it == EMPTY_LOCATION){
//                locationClearBtn.isEnabled = false
//                locationNameTextView.text = getString(R.string.location_not_selected)
//                locationCoordinateTextView.text = ""
//            }
//
//            locationClearBtn.isEnabled = true
//
//            Log.d(LOG_TAG, "Updating the location to (${it.first}, ${it.second})")
//            val position = LatLng(it.first, it.second)
//            g.clear()
//            g.addMarker(MarkerOptions().position(position))
//            g.moveCamera(CameraUpdateFactory.newLatLng(position))
//
//            val areaName = context!!.getAreaNameByCoordinate(it)
//            locationNameTextView.text = areaName.first
//            locationCoordinateTextView.text = areaName.second
//        }
//
//        locationClearBtn.setOnClickListener {
//            viewModel.coordinate.value = viewModel.SESSION_DEFAULT_LOCATION
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                AppConstants.REQUEST_CODE_GALLERY         -> {
                    //data.getData return the content URI for the selected Image
                    val selectedImage: Uri = data?.data ?: return
                    viewModel.addAnImage(selectedImage)
                }
                AppConstants.REQUEST_PICK_FROM_FILE        -> {
                    //data.getData return the content URI for the selected Image
                    val selectedVideo: Uri = data?.data ?: return
                    viewModel.addAnVideo(selectedVideo)
                }


                AppConstants.REQUEST_CODE_LOCATION_PICKER -> {
//                    Toasty.success(context!!, getString(R.string.location_saved)).show()
                    Log.d(LOG_TAG, "Returned Location Extra: ${data!!.extras?.get(
                            "lat")}, ${data.extras?.get("long")}")

                    val lat = data.extras?.get("lat")?.toString()?.toDouble()!!
                    val long = data.extras?.get("long")?.toString()?.toDouble()!!
                    // val long = data.getFloatExtra("long", viewModel.coordinate.value!!.second.toFloat())

                    Log.d(LOG_TAG, "Returned Location: $lat  $long")
                    viewModel.coordinate.value = Pair(lat, long)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            CODE_PERMISSION_READ_STORAGE  -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toasty.success(context!!, getString(R.string.permission_thanks_storage)).show()
                    invokeImageSelectionIntent()
                }
            }
            CODE_PERMISSION_FINE_LOCATION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toasty.success(context!!, getString(R.string.permission_thanks_location)).show()
                    initializeLocationAndMap()
                }
            }
        }
    }
}

/**
 * A RecyclerView adapter for Chips
 * @property onChipClickListener Function2<Chip, String, Unit> A listener specifying the behavior when a chip is clicked
 * @property setChipBackgroundColor Int the initial color of all Chips
 * @constructor default constructor
 */
class ChipRecyclerAdapter(context: Context,
                          private val onChipClickListener: (Chip, String) -> Unit) :
        ListAdapter<DiaryTag, ChipRecyclerAdapter.ViewHolder>(ChipDiffCallback()) {

    var setChipBackgroundColor: Int = context.getColor(R.color.colorPrimary)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.chip_tag_edit, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chip.apply {
            text = getItem(position).title
            changeBackgroundColor(setChipBackgroundColor)
            setOnCloseIconClickListener {
                onChipClickListener(this, text.toString())
            }
        }
    }

    class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val chip: Chip = root.findViewById(R.id.chip)
    }

    internal class ChipDiffCallback : DiffUtil.ItemCallback<DiaryTag>() {

        override fun areItemsTheSame(oldItem: DiaryTag, newItem: DiaryTag): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DiaryTag, newItem: DiaryTag): Boolean {
            return oldItem.title == newItem.title
        }

    }

}