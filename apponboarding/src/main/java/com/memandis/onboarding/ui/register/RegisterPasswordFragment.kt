package com.memandis.onboarding.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.memandis.onboarding.R
import com.memandis.onboarding.databinding.ButtonsLoginRegisterBinding
import com.memandis.onboarding.databinding.FragmentRegisterPasswordBinding
import com.memandis.onboarding.viewmodels.RegisterViewModel
import com.memandis.remote.datasource.FirebaseUserRepository
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * A Fragment that allows users to enter a password associated with the entered email.
 *
 * @property LOG_TAG String String Tag string for showing Debugging Log
 */
class RegisterPasswordFragment : Fragment() {

    private val LOG_TAG = this::class.java.simpleName

    private var _binding: FragmentRegisterPasswordBinding? = null
    private var mergedBinding: ButtonsLoginRegisterBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        FirebaseUserRepository.attachUserToFirebaseRepositories()
        _binding = FragmentRegisterPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        mergedBinding = ButtonsLoginRegisterBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerViewModel = ViewModelProvider(this.requireActivity()).get(
                RegisterViewModel::class.java)

        // Set the page header
        binding.header.defaultHeaderText.text = getString(
                R.string.register_a_new_account)
        binding.header.subtitleTextView.text = getString(
                R.string.specify_your_password)

        // Observe the email address of the user
        registerViewModel.emailLiveData.observe(viewLifecycleOwner, Observer {
            binding.userEmailText.text = it!!
        })

        // Set the back button behavior
        mergedBinding!!.signInBackBtn.setOnClickListener { findNavController().popBackStack() }

        registerViewModel
                .arePasswordsOkayToSignUp
                .observe(viewLifecycleOwner,
                         object : Observer<Boolean?> {
                             var alreadyDisplayed = false
                             override fun onChanged(
                                     isValid: Boolean?) {
                                 mergedBinding!!.signInContinueBtn.isEnabled = isValid!!

                                 if(isValid) {
                                     if(!alreadyDisplayed) {
                                         alreadyDisplayed = true
//                                         Toasty.success(context!!,
//                                                        getString(
//                                                                R.string.password_valid),
//                                                        Toast.LENGTH_SHORT)
//                                                 .show()
                                     }
                                 } else {
                                     alreadyDisplayed = false
                                 }
                             }

                         })

        mergedBinding!!.signInContinueBtn.setOnClickListener {
            mergedBinding!!.bottomProgressBar.visibility = View.VISIBLE
            it.isEnabled = !it.isEnabled

            registerViewModel
                    .createUser()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnError { throwable ->
                        throwable.printStackTrace()
//                        Toasty.error(context!!, "Error: ${throwable.localizedMessage}", Toast.LENGTH_LONG).show()

                        // Handles the error that the Firebase Auth server throw!
                        binding.passwordTextField1.error = getString(R.string.register_error_email_badly_formatted)
                        mergedBinding!!.bottomProgressBar.visibility = View.GONE
                    }
                    .subscribe { data, throwable ->

                        if(data != null) {
                            // The user creation process is finished
//                            Toasty.info(context!!,
//                                        "${getString(R.string.logged_in)}: ${data.email}").show()

                            // Attach user credential to the application repo
                            FirebaseUserRepository.attachUserToFirebaseRepositories()

                            val action = RegisterPasswordFragmentDirections
                                    .actionRegisterPasswordFragmentToProfileCustomizeFragment(
                                            data.uid)
                            findNavController().navigate(action)
                        }

                        if(throwable != null) {
                            Log.e(LOG_TAG, throwable.message ?: "An error happens!")
                        }
                    }
        }

        registerViewModel.password1Status.observe(viewLifecycleOwner, Observer {
                // Log.d(LOG_TAG, "Pass 1's Message: $it")
                binding.passwordTextField1.error = getPasswordErrorMessage(it)
        })

        registerViewModel.password2Status.observe(viewLifecycleOwner, Observer {
            // Log.d(LOG_TAG, "Pass 2's Message: $it")
            binding.passwordTextField2.error = getPasswordErrorMessage(it)
        })

        binding.passwordEditText1.doOnTextChanged { text, _, _, _ ->
            // Log.d(LOG_TAG, "Pass 1 Text Changed: $text")
            registerViewModel.password1.value = text.toString()
        }
        binding.passwordEditText2.doOnTextChanged { text, _, _, _ ->
            // Log.d(LOG_TAG, "Pass 2 Text Changed: $text")
            registerViewModel.password2.value = text.toString()
        }

    }

    /**
     * Return an associate error message caused by a password error
     * @param validity PasswordValidity the enum for specifying the type
     * @return String? error message string
     */
    private fun getPasswordErrorMessage(validity: RegisterViewModel.PasswordValidity): String? {
        return when(validity) {
            RegisterViewModel.PasswordValidity.INVALID_FORMAT -> getString(
                    R.string.password_error_no_space)
            RegisterViewModel.PasswordValidity.NULL           -> getString(
                    R.string.password_error_null)
            RegisterViewModel.PasswordValidity.TOO_SHORT      -> getString(
                    R.string.password_error_too_short)
            RegisterViewModel.PasswordValidity.TOO_LONG       -> getString(
                    R.string.password_error_too_long)
            RegisterViewModel.PasswordValidity.VALID_FORMAT   -> null
        }
    }
}

