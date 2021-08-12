package com.memandis.onboarding.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.memandis.appnavigation.Destination
import com.memandis.appnavigation.NavigationViewModel
import com.memandis.appnavigation.navigateToDestination
import com.memandis.onboarding.databinding.FragmentLoginBinding
import com.memandis.onboarding.viewmodels.RegisterViewModel
import com.memandis.remote.datasource.FirebaseUserRepository

//import com.memandis.remote.remote.diary.FirebaseUserRepository

//import com.svayantra.apponboarding.ui.login.LoginFragment.Companion.RET_RESULT


/**
 * A Fragment that is the Landing Page of the app
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val loginFragmentArgs: LoginFragmentArgs by navArgs()

    private lateinit var navViewModel: NavigationViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewModel = ViewModelProvider(requireActivity()).get( RegisterViewModel::class.java)
        navViewModel = ViewModelProvider(requireActivity()).get(NavigationViewModel::class.java)
//        setupObservers()

        if(viewModel.isAlreadyLoggedIn) {

            // Attach user credential to the application repo
            FirebaseUserRepository.attachUserToFirebaseRepositories()

//            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(
//                viewModel.account!!.uid)
//            findNavController().navigate(action)
//            requireActivity().finish()

            val intent = Intent();
            intent.setClassName(requireContext().packageName,
                "com.memandis.project.main.MainActivity");
            intent.putExtra("UserId", viewModel.account!!.uid) // Test intent for Dynamic feature
            startActivity(intent)
            requireActivity().finish()

//            navigateToDestination(Destination.HomeFlow)

        }

        super.onViewCreated(view, savedInstanceState)

        loginFragmentArgs.msg.let {msg ->
            if (msg.isNotEmpty()) {
                Toast.makeText(requireContext(), loginFragmentArgs.msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.emailLoginBtn.apply {
            setOnClickListener {
                // Toast.makeText(this@LoginFragment.context, "Login Tapped!", Toast.LENGTH_SHORT).show()
                val action = LoginFragmentDirections.actionLoginFragmentToLoginEmailFragment()
                val navController = Navigation.findNavController(view)
                navController.navigate(action)

//                navigateToDestination(Destination.HomeFlow)

            }
        }

        binding.signUpTextView.apply {
            setOnClickListener {
                // Toast.makeText(this@LoginFragment.context, "Sign Up Tapped!", Toast.LENGTH_SHORT).show()

                val action = LoginFragmentDirections.actionLoginFragmentToRegisterEmailFragment()
                val navController = Navigation.findNavController(view)
                navController.navigate(action)

//                (requireActivity() as ToFlowNavigatable).navigateToFlow(Destination.HomeFlow)

//                navigateToDestination(Destination.LauncherFlow)

            }
        }

//        binding.logoImageView.apply {
//            setOnClickListener {
//                // Toast.makeText(this@LoginFragment.context, "Sign Up Tapped!", Toast.LENGTH_SHORT).show()
//                val action = LoginFragmentDirections.actionRegisterPasswordFragmentToProfileCustomizeFragment()
//                val navController = Navigation.findNavController(view)
//                navController.navigate(action)
//
////                (requireActivity() as ToFlowNavigatable).navigateToFlow(Destination.HomeFlow)
//
////                navigateToDestination(Destination.LauncherFlow)
//
//            }
//        }

    }



}

