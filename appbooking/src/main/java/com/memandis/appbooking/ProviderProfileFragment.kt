package com.memandis.appbooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.memandis.appbooking.binding.*
import com.memandis.appbooking.databinding.FragmentProfessionalBinding
import com.memandis.appbooking.vm.BookingViewModel
import com.memandis.remote.datasource.model.booking.Professional
import com.memandis.remote.datasource.model.booking.Subscription
//import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//@AndroidEntryPoint
open class ProviderProfileFragment
    : SelfBaseFragment<FragmentProfessionalBinding>() {
//    : Fragment() {

//    private var _binding: FragmentProfessionalBinding? = null

//    private val args: ProviderProfileFragmentArgs by navArgs()

    private val MIN_SCALE: Float = 0.75f//    private ViewPager2 viewPager;
//    private TabLayout designerTabLayout;

    private val bookingViewModel: BookingViewModel by viewModels()
//    private val bookingViewModel by viewModels<BookingViewModel> {  getViewModelFactory() }

//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = .inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    override fun getLayoutId(): Int {return R.layout.fragment_professional }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {

            designerProfilePager.apply {
                adapter = ProviderProfileAdapter<Professional, BookingViewModel>(
                    bookingViewModel, R.layout.item_provider_profile,
                    ProfileComparator<Professional>(),
                    ProfileListener<Professional> { _: Professional, _: Int -> }
                )
                setPageTransformer(ViewPager2.PageTransformer(
                    fun(view: View, position: Float) {
                    val pageWidth = view.width
                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        view.alpha = 0f
                    } else if (position <= 0) { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        view.alpha = 1f
                        view.translationX = 0f
                        view.translationZ = 0f
                        view.scaleX = 1f
                        view.scaleY = 1f
                    } else if (position <= 1) { // (0,1]
                        // Fade the page out.
                        view.alpha = 1 - position

                        // Counteract the default slide transition
                        view.translationX = pageWidth * -position
                        // Move it behind the left page
                        view.translationZ = -1f

                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
                        view.scaleX = scaleFactor
                        view.scaleY = scaleFactor
                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.alpha = 0f
                    }
                }))
            }

            designerBookingRecyclerView.apply {
                setHasFixedSize(true)
                adapter = SubscriptionAdapter<Subscription, BookingViewModel>(
                    bookingViewModel, R.layout.item_provider_pricing,
                    SubscriptionComparator<Subscription>(),
                    SubscriptionListener<Subscription> { item: Subscription, id: Int ->
//                        val args = Bundle().apply {
//                            putLong("designerKey", item.designer.id)
//                            putLong("projectKey" , args.projectKey)
//                            putLong("subscriptionKey", item.subscription[id].subscriptionId)
//                        }
//                        val params = item.designer.name
                        val destination = ProviderProfileFragmentDirections.
                            actionSelectionToBooking(0L,//args.userKey,
                                                    // 0L,//  item.designer.id,
                                                    // 0L,//  args.projectKey,
                                                   //  item.id,
                                                   //  " "            //params
                            )
                        with(findNavController()) {
                            currentDestination?.getAction(destination.actionId)?.let {
                                navigate(destination/*, extras*/)
                            }
                        }
                    })
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                    false)

            }

            // This is needed to subscribe to LiveData updates
            lifecycleOwner = this@ProviderProfileFragment

            viewModel = bookingViewModel

            invalidateAll()

        }

//        bookingViewModel.designerServices.observe(viewLifecycleOwner,
//            {
//                if (it != null) {
////                    Toast.makeText(  context, "Designer Profile " +
////                              " ${it[binding.designerProfilePager.currentItem].designer.imageUrl}",
////                        Toast.LENGTH_SHORT
////                    ).show()
//                    (binding.designerProfilePager.
//                       adapter as ProviderProfileAdapter<UserService, BookingViewModel>).
//                        data = it
//                }
//            }
//        )

//        bookingViewModel.designerSubscriptions.observe(viewLifecycleOwner,
//            androidx.lifecycle.Observer {
//                if (it != null) {
////                    Toast.makeText(  context, "Designer Price" +
////                           " ${it[binding.designerProfilePager.currentItem].subscription[0].name}",
////                        Toast.LENGTH_SHORT
////                    ).show()
//                }
//            })

//        //set tabs
//        TabLayoutMediator(binding.designerProfileTab, binding.designerProfilePager) {
//                tab, position -> tab.text = "${bookingViewModel.designerServices.value?.
//                                               get(position)?.designer?.firstName}"+" "+
//                                               bookingViewModel.designerServices.value?.
//                                               get(position)?.designer?.lastName
//        }.attach()

        binding.designerProfileTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                Toast.makeText(context, "Designer ${tab.text}", Toast.LENGTH_SHORT).show()
                binding.designerBookingRecyclerView.smoothScrollToPosition(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.designerProfilePager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.designerBookingRecyclerView.smoothScrollToPosition(position)
            }
        })

    }

    companion object {
        const val TAG = "ProviderProfileFragment"
    }



}

