package com.memandis.onboarding.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.widget.RxTextView
import com.memandis.onboarding.R
import com.memandis.onboarding.databinding.FragmentProfileCustomizeBinding
import com.memandis.remote.utils.map.CODE_PERMISSION_READ_STORAGE
import com.memandis.remote.utils.background.invokeImageSelectionIntent
import com.memandis.remote.utils.map.isGrantedExternalStoragePermission
import com.memandis.onboarding.viewmodels.RegisterViewModel
import com.memandis.remote.datasource.FirebaseUserRepository
import com.memandis.remote.utils.app.StringUtils.Companion.isValidUsername
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * A fragment that is responsible for the customization of
 * the user profile including username and profile image.
 *
 * @property LOG_TAG String a string that is used for log message
 * @property viewModel RegisterViewModel view model that is responsible for managing data in this fragment
 */
class ProfileCustomizeFragment : Fragment() {

    val REQUEST_CODE_GALLERY = 100

    private val LOG_TAG: String = this::class.java.simpleName

    private lateinit var viewModel: RegisterViewModel

    private var _binding: FragmentProfileCustomizeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inject the user to both app and storage references
        // since we will have to use them for the customization
        FirebaseUserRepository.attachUserToFirebaseRepositories()

        _binding = FragmentProfileCustomizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the user ID from the previous fragment.
        val userId = ProfileCustomizeFragmentArgs.fromBundle(requireArguments()).uid

        // Retrieve the ViewModel from the activity
        viewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)

        // Set the Page Header text
        binding.header.defaultHeaderText.text = getString(R.string.customize_your_account)
        binding.header.subtitleTextView.text = getString(R.string.set_your_username_and_image)

        binding.usernameText.text = viewModel.emailLiveData.value!!

        viewModel.isUsernameAvailableStatusLiveData.observe(viewLifecycleOwner) { isValid ->

            // Set the progress bar to GONE since the password validity check is completed.
            binding.bottomProgressBar.visibility = View.GONE
            if(isValid == null) {
                binding.continueBtn.isEnabled = false
                return@observe
            }

            // If the password is not valid.
            if(!isValid) {
                // Show the error message.
                binding.usernameTextForm.error = getString(
                        R.string.error_username_invalid_already_taken)
            } else {
                // Show a valid message
                binding.usernameTextForm.error = null
                binding.usernameTextForm.apply {
                    endIconDrawable = AppCompatResources.getDrawable(
                            binding.usernameTextForm.context,
                            R.drawable.ic_check_circle_black_24dp)
                    helperText = context.getString(
                            R.string.username_is_ready)
                }
                binding.continueBtn.isEnabled = true
            }
        }

        binding.continueBtn.setOnClickListener {

            // If we do not know the status of the Username, we will not allow the user to continue...
            if(viewModel.isUsernameAvailableStatusLiveData.value == null) {
                return@setOnClickListener
            }

            // If we do know that it is invalid, we also do not allow the user to continue.
            if(!viewModel.isUsernameAvailableStatusLiveData.value!!) {
                return@setOnClickListener
            }

            viewModel
                    .saveCustomizationData(requireContext())
                    ?.subscribe { data, _ ->
                        // If both status booleans (E.g. for username and user profile status) is true.
                        if(data[0] && data[1]) {
                            // Proceed to the MainActivity as specified in the graph...
//                            val action = ProfileCustomizeFragmentDirections.
//                                          actionProfileCustomizeFragmentToMainActivity(userId)
//                            findNavController().navigate(action)
//                            navigateToDestination(Destination.HomeFlow)
//                            startActivity(Intent(context, ServiceActivity::class.java) )

                            val intent = Intent();
                            intent.setClassName(requireContext().packageName,
                                "com.memandis.project.main.MainActivity");
                            intent.putExtra("UserId", userId) // Test intent for Dynamic feature
                            startActivity(intent)

                        } else {
//                            Toasty.error(context!!, getString(R.string.customize_error_updating))
                        }
                    }

        }
        viewModel.profileImageBitmap.observe(viewLifecycleOwner, Observer {
            // If the incoming image is NULL
            if(it == null) {
                // Make the image add button visible
                binding.addImageButton.visibility = View.VISIBLE
                return@Observer
            }

            // Otherwise, make it disappear.
            binding.addImageButton.visibility = View.GONE

            // Show a toast to notify the user
//            Toasty.info(context!!, getString(R.string.customize_image_selected)).show()

            // Load the image into it.
            Glide.with(requireContext()).load(it[0].second).into(binding.heroProfileView)
        })

        val addImageClickListener: (v: View) -> Unit = {
            // When clicking the image button

            // Check for the external storage permission.
            if(requireActivity().isGrantedExternalStoragePermission()) {
                // If the permission is already granted, open the image selector.
                Log.d(LOG_TAG, getString(R.string.permission_granted_gallery))
                invokeImageSelectionIntent()
//                val previewRequest =
//                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                        if (it.resultCode == RESULT_OK) {
//                            val list = it.data
//                            // do whatever with the data in the callback
//                        }
//                    }
            } else {
                // If not granted, ask the system for the permission.
                requestPermissions(CODE_PERMISSION_READ_STORAGE).also {
                    if(!it) {
                        Log.d(LOG_TAG, getString(R.string.permission_denied_gallery))
                    }
                }
            }
        }

        // Add the same click listener to both ImageView and the add image button.
        binding.heroProfileView.setOnClickListener(addImageClickListener)
        binding.addImageButton.setOnClickListener(addImageClickListener)

        // Use the text change event to an Observable of CharSequence
        val a = RxTextView.textChanges(binding.usernameEditText)
                .observeOn(AndroidSchedulers.mainThread())          // Do observe it the Main thread, since we must receive events from the UI.
                .subscribeOn(AndroidSchedulers.mainThread())        // Do subscribe it the Main thread, since we must interact with the UI.
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    // Remove unnecessary white spaces from the username
                    val username = it.toString().trim()

                    // Validate the format of the username
                    if(username.isValidUsername()) {
                        // If it is valid, begins to validate with the server.
                        // We validate whether the username has already been used or not...
                        binding.usernameTextForm.error = null
                        binding.bottomProgressBar.visibility = View.VISIBLE

                        // Set the username to be the value in the text field
                        viewModel.username.value = username

                        // Proceed to validate the username with the server
                        viewModel
                                .validateExistingUsername()
                                .onErrorReturn { throwable ->
                                    // If an error is returned, notify the user.
//                                    Toasty.error(context!!, getString(
//                                            R.string.error_username_invalid_already_taken)).show()
                                    throwable.printStackTrace()

                                    // Make it invalid by returning false
                                    false
                                }
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe { status, throwable ->
                                    when(throwable) {
                                        null -> viewModel.isUsernameAvailableStatusLiveData.value = status
                                        else -> viewModel.isUsernameAvailableStatusLiveData.value = false
                                    }
                                }
                    }
                }

        binding.usernameEditText.doOnTextChanged { text, _, _, _ ->

            // Reset the hint if it is showing that the username is valid
            if(binding.usernameTextForm.helperText == getString(R.string.username_is_ready)) {
                binding.usernameTextForm.apply {
                    binding.usernameTextForm.endIconDrawable = null
                    binding.usernameTextForm.helperText = getString(R.string.username_login_hint)
                }
            }

            val username = text.toString().trim()
            viewModel.username.value = username
            viewModel.isUsernameAvailableStatusLiveData.value = null

            if(username.isValidUsername()) {
                binding.usernameTextForm.error = null
            } else {
                if(binding.usernameEditText.error == null) {
                    binding.usernameTextForm.error = requireContext().getString(
                            R.string.invalid_username)
                }
            }

        }
    }

    /**
     * Request the permission from the system accordingly to the parameter
     * @param permissionCode Int custom request code
     * @return Boolean the result of the permission -> true if granted
     */
    private fun requestPermissions(permissionCode: Int): Boolean {

        // If the request code is for reading storage
        if(permissionCode == CODE_PERMISSION_READ_STORAGE) {
            // And the app is not granted the permission storage
            if(!requireActivity().isGrantedExternalStoragePermission()) {
                // Request a permission for that
                ActivityCompat.requestPermissions(requireActivity(),
                                                  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                  CODE_PERMISSION_READ_STORAGE
                )
            } else {
                return true
            }
        } else {
            return false
        }
        return false
    }

    /**
     * Handle fragment behavior after returning from a system permission dialog
     * @param requestCode Int custom request code
     * @param permissions Array<out String> array of permission
     * @param grantResults IntArray the result of the permission request
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d(LOG_TAG, "Receiving Permission Result (code = $requestCode)")
        when(requestCode) {
            CODE_PERMISSION_READ_STORAGE -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toasty.success(context!!, getString(R.string.permission_thanks_storage)).show()
                    invokeImageSelectionIntent()
                }
            }
        }
    }

    /**
     * Handle fragment behavior after returning from an intent
     * @param requestCode Int custom request code
     * @param resultCode Int  the status of the result OK or not Ok
     * @param data Intent? the data returning from another activity
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(LOG_TAG, "Receiving Activity Result (code = $requestCode)")

        // If the result is OK
        if(resultCode == Activity.RESULT_OK) {
            Log.d(LOG_TAG, "The Result is OKAY!)")
            when(requestCode) {
                // And the request is for the gallery
//                EntryEditFragment.
                REQUEST_CODE_GALLERY -> {
                    Log.d(LOG_TAG, "The Result is related to GALLERY!!")
//                    Toasty.info(context!!, "A profile image selected!").show()

                    //data.getData return the content URI for the selected Image
                    val selectedImage: Uri = data?.data ?: return
                    Log.d(LOG_TAG, "The Result contains ${selectedImage}!")

                    // Set it to the ViewModel for further UI update.
                    viewModel.profileImageUri.value = selectedImage
                }
            }
        }
    }

}
