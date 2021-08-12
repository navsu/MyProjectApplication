package com.memandis.onboarding.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding2.widget.RxTextView
//import com.memandis.remote.utils.isValidEmail
import com.memandis.onboarding.R
import com.memandis.onboarding.databinding.ButtonsLoginRegisterBinding
import com.memandis.onboarding.databinding.FragmentRegisterEmailBinding
import com.memandis.onboarding.viewmodels.RegisterViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * A Fragment that allows user to register a new account by an email
 */
class RegisterEmailFragment : Fragment() {

    private var _binding: FragmentRegisterEmailBinding? = null
    private var mergedBinding: ButtonsLoginRegisterBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterEmailBinding.inflate(inflater, container, false)
        val view = binding.root
        mergedBinding = ButtonsLoginRegisterBinding.bind(view)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerViewModel = ViewModelProvider(  this.requireActivity()).get(
                RegisterViewModel::class.java)

        // Set the page header
        binding.header.defaultHeaderText.text = getString(
                R.string.register_a_new_account)
        binding.header.subtitleTextView.text = getString(  R.string.tell_us_your_email)

        // Set the back button behavior
        mergedBinding!!.signInBackBtn.setOnClickListener { findNavController().popBackStack() }

        registerViewModel.isNewEmailStatusLiveData.observe(viewLifecycleOwner, Observer { isValid ->
//            mergedBinding!!.bottomProgressBar.visibility = View.GONE
            if(isValid == null) {
//                // Toast.makeText(this@RegisterEmailFragment.activity, "Error Retrieving Status",
//                //                Toast.LENGTH_SHORT).show()
               mergedBinding!!.signInContinueBtn.isEnabled = false
                return@Observer
            }
            if(!isValid) {
                binding.emailTextField.error = getString(
                        R.string.error_email_invalid_already_taken)
                binding.emailTextField.apply {
                    endIconDrawable = AppCompatResources.getDrawable(
                        binding.emailTextField.context,
                            R.drawable.ic_cancel_black_24dp)
                }
                mergedBinding!!.signInContinueBtn.isEnabled = false
            } else {
                mergedBinding!!.signInContinueBtn.isEnabled = false
                binding.emailTextField.error = null
                // TODO: Show valid message
                binding.emailTextField.apply {
                    endIconDrawable = AppCompatResources.getDrawable(
                        binding.emailTextField.context,
                            R.drawable.ic_check_circle_black_24dp)
                    helperText = context.getString(
                            R.string.email_is_ready)
                }
                mergedBinding!!.signInContinueBtn.isEnabled = true
            }
        })

        val a = RxTextView.textChanges( binding.emailEditText)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe {

                    Toast.makeText(context, "validate Email", Toast.LENGTH_SHORT ).show()

                    val userEmail = it.toString().trim()
//                    if(userEmail.isValidEmail()) {
                        binding.emailTextField.error = null

                        mergedBinding!!.bottomProgressBar.visibility = View.VISIBLE

                        // TODO: Proceed to validate the email with the server
                        registerViewModel.emailLiveData.value = userEmail

                        registerViewModel
                                .validateExistingEmail()
                                .observeOn(AndroidSchedulers.mainThread())
                                .onErrorReturn {throwable ->
                                    Toast.makeText(context,
                                    getString(R.string.error_email_invalid_already_taken)
                                        , Toast.LENGTH_SHORT ).show()

                                    throwable.printStackTrace()
                                    false
                                }
                                .subscribe { status, throwable ->
                                    when(throwable) {
                                        null -> registerViewModel.isNewEmailStatusLiveData.value = status
                                        else -> registerViewModel.isNewEmailStatusLiveData.value = false
                                    }
                                }
//                    }
                }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->

            // Reset the hint if it is showing that the email is valid
            if(binding.emailTextField.helperText == getString(
                            R.string.email_is_ready)) {
                binding.emailTextField.apply {
                    binding.emailTextField.endIconDrawable = null
                    binding.emailTextField.helperText = getString(
                            R.string.email_login_hint)
                }
            }

            val userEmail = text.toString().trim()
            registerViewModel.emailLiveData.value = userEmail
            registerViewModel.isNewEmailStatusLiveData.value = null

//            if(userEmail.isValidEmail()) {
                binding.emailTextField.error = null
//            } else {
//                if(binding.emailEditText.error == null) {
//                    binding.emailTextField.error = requireContext().getString(
//                            R.string.invalid_email)
//                }
//            }

        }

        mergedBinding!!.signInContinueBtn.setOnClickListener {
            val action = RegisterEmailFragmentDirections.actionRegisterEmailFragmentToRegisterPasswordFragment()
            findNavController().navigate(action)
        }

        binding.loginTextView.setOnClickListener {
            val action = RegisterEmailFragmentDirections.actionRegisterEmailFragmentToLoginEmailFragment()
            findNavController().navigate(action)
        }

    }

}