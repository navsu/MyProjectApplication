package com.memandis.project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.memandis.appmain.databinding.FragmentServicesBinding

//import com.svayantra.core.base.SelfBaseAdaptor
//import com.svayantra.core.base.SelfBaseDiffCallback
//import com.svayantra.core.base.SelfBaseFragment
//import com.svayantra.core.base.SelfBaseListener
//import com.svayantra.core.models.Service

class ServiceFragment :  Fragment()  {
//class ServiceFragment : SelfBaseFragment<FragmentProjectCreateBinding>() {

    private val args: ServiceFragmentArgs by navArgs()

//    override var bottomNavigationViewVisibility: Int = View.GONE

//    private val projectViewModel: ProjectViewModel by viewModels()

//    private val homeViewModel by viewModels<HomeViewModel> { getViewModelFactory() }

//    override fun getLayoutId(): Int { return R.layout.fragment_project_create }

    private var _binding: FragmentServicesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

//    private lateinit var navViewModel: NavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        binding.apply {

            servicesRecyclerView.apply {
                setHasFixedSize(true)
                
                adapter = ServicesViewAdapter<ServiceRemote, ProjectViewModel>(
                    projectViewModel, R.layout.item_client_services,
                    ServiceListener<ServiceRemote>{_service, _value ->
                        navigateToBooking()
//                        val params = "Project_Key" + "-" +
//                                "Client_Key" + "-" +
//                                "Designer_key" + "-" +
//                                "booking_Key"
//                        val args = Bundle().apply {
//                            putLong("userKey", args.userKey)
//                            putLong("projectKey", 0L)
//                            putString("projectParams", params)
//                        }
//                        val navOptions = NavOptions.Builder().setPopUpTo(
//                            R.id.project_create,true ).build()
//                        with(findNavController()) {
//                            navigate(R.id.service_navigation, args, navOptions)
//                        }
                    },
                    this@ServiceFragment,
                    ServiceDataComparator(),
                    0
                )

                layoutManager = LinearLayoutManager(context)
            }
            // This is needed to subscribe to LiveData updates
            lifecycleOwner = this@ServiceFragment

            viewModel = projectViewModel

            invalidateAll()
        }
*/
//        homeViewModel.getUserProjectServices(/*args.userKey*/)
//
//        homeViewModel.userServices.observe(viewLifecycleOwner, { userServices ->
//            if(userServices !=null) {
//             Toast.makeText( context, "Navigated to project Create ${args.userKey}",
//                    Toast.LENGTH_SHORT ).show()
//                Log.d("ViewModel_Services", args.userKey.toString()+" user Services"+
//                        userServices[0].name+"_"+userServices[0].description)
//            }
//        })

    }

    private fun navigateToBooking() {
//        val params = "Project_Key"  + "-" +
//                     "Client_Key"   + "-" +
//                     "Designer_key" + "-" +
//                     "booking_Key"
//        val args = Bundle().apply {
//            putLong("userKey", args.userKey)
//            putLong("projectKey", 0L)
//            putString("projectParams", params)
//        }
//        val navOptions = NavOptions.Builder().setPopUpTo(
//            R.id.homeFragment,true ).build()
//        with(findNavController()) {
//            navigate(R.id.service_navigation, args, navOptions)
//        }
    }

    private fun navigateToUpload() {
//        val args = Bundle().apply {
//            putLong("userKey", args.userKey)
//            putLong("projectKey", 0L)
//        }
//        val navOptions = NavOptions.Builder().setPopUpTo(
//            R.id.homeFragment,true ).build()
//        with(findNavController()) {
//            navigate(R.id.upload_navigation, args, navOptions)
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.services_appbar_nav, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_selection_to_booking -> {
//                navigateToBooking()
//                true
//            }
//            R.id.action_upload-> {
//                navigateToUpload()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


//    override fun onDestroyView() {
//        super.onDestroyView()
////        _binding = null
//    }

    companion object {
        const val TAG = "ServiceFragment"
    }


}