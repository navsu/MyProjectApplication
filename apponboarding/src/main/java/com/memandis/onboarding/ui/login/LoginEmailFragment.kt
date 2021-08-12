package com.memandis.onboarding.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.memandis.appnavigation.Destination
import com.memandis.appnavigation.navigateToDestination
import com.memandis.onboarding.R
import com.memandis.onboarding.databinding.ButtonsLoginRegisterBinding
import com.memandis.onboarding.databinding.FragmentLoginEmailBinding
import com.memandis.remote.datasource.FirebaseUserRepository
import com.memandis.onboarding.viewmodels.LoginViewModel
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * A Fragment that hosts User Login by Email process
 */
class LoginEmailFragment: Fragment() {
//    BaseFragment<FragmentLoginEmailBinding>() {

    private var _binding: FragmentLoginEmailBinding? = null
    private var mergedBinding: ButtonsLoginRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun getLayoutId(): Int { return R.layout.fragment_login_email}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginEmailBinding.inflate(inflater, container, false)

        val view = binding.root

        mergedBinding = ButtonsLoginRegisterBinding.bind(view)

        return  view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mergedBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the page header
        binding.header.defaultHeaderText.text = getString(  R.string.login_to_mydezigner)
        binding.subtitleTextView.text = getString(   R.string.login_enter_email_password)

        binding.emailEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        binding.emailEditText.text = Editable.Factory.getInstance().
                                       newEditable("nav@memandis.com")
//                                       newEditable("clientcv1@company.com")
        binding.passwordEditText.text= Editable.Factory.getInstance().
                                       newEditable("11111111")
//                                       newEditable("111111")

        // Set the back button behavior
        mergedBinding!!.signInBackBtn.setOnClickListener { findNavController().popBackStack() }

        val loginViewModel = ViewModelProvider(this).get( LoginViewModel::class.java)

        mergedBinding?.signInContinueBtn?.setOnClickListener {
            mergedBinding?.signInContinueBtn!!.isEnabled = false
            mergedBinding!!.bottomProgressBar.visibility = View.VISIBLE
            loginViewModel.email.value = binding.emailEditText.text.toString().trim()
            loginViewModel.password.value = binding.passwordEditText.text.toString().trim()

            loginViewModel
                .loginUserAccount()
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn {throwable ->
                    Toast.makeText(context, throwable.localizedMessage!!, Toast.LENGTH_SHORT).show()
                    throwable.printStackTrace()
                    if(throwable is FirebaseAuthInvalidCredentialsException){
                        binding.emailTextField.error = throwable.localizedMessage
                        binding.passwordTextField.error = null
                    }else if(throwable is FirebaseTooManyRequestsException){
                        binding.emailTextField.error = getString(R.string.login_error_too_many_attempts)
                        binding.passwordTextField.error = getString(R.string.login_error_too_many_attempts)
                    }
                    null
                }
                .subscribe { user, _ ->
                    mergedBinding!!.signInContinueBtn.isEnabled = true
                    mergedBinding!!.bottomProgressBar.visibility = View.INVISIBLE
                    if(user == null) {
                        return@subscribe
                    }

                    // Attach user credential to the application repo
                    FirebaseUserRepository.attachUserToFirebaseRepositories()

//                    // The user creation process is finished
//                  Toast.makeText(context, "${getString(R.string.logged_in)}: ${user.email}",
//                      Toast.LENGTH_SHORT).show()
//                    val action = LoginEmailFragmentDirections.actionLoginEmailFragmentToMainActivity(user.uid)
//                    findNavController().navigate(action)
//                    requireActivity().finish()
                    val intent = Intent();
                    intent.setClassName(requireContext().packageName,
                        "com.memandis.project.main.MainActivity");
                    intent.putExtra("UserId", user.uid) // Test intent for Dynamic feature
                    startActivity(intent)
                    requireActivity().finish()

//                    navigateToDestination(Destination.HomeFlow)

                }
        }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            mergedBinding!!.signInContinueBtn.isEnabled = !(text.isNullOrEmpty() || binding.passwordEditText.text.isNullOrEmpty())
            if(binding.emailTextField.error != null){
                binding.emailTextField.error = null
            }
        }

        binding.passwordEditText.doOnTextChanged { text, _, _, _ ->
            mergedBinding!!.signInContinueBtn.isEnabled = !(text.isNullOrEmpty() || binding.emailEditText.text.isNullOrEmpty())
        }

        binding.signUpTextView.setOnClickListener {
            val action = LoginEmailFragmentDirections.actionLoginEmailFragmentToRegisterEmailFragment()
            findNavController().navigate(action)
        }

    }

}

