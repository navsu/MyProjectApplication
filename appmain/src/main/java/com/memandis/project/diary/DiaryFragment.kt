package com.memandis.project.diary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import com.memandis.appmain.R
import com.memandis.appmain.databinding.FragmentDiaryBinding
import com.memandis.project.GlideApp
import com.memandis.project.diary.calenderview.RylyTabDateDelegate
import com.memandis.project.diary.calenderview.RylyToolbarView
import com.memandis.project.diary.vm.DiaryDateViewModel
import com.memandis.project.diary.vm.DiaryViewModel
import com.memandis.project.getViewModelFactory
import com.memandis.remote.datasource.DiaryRepo
import com.memandis.remote.datasource.FirebaseStorageRepository.getProfileImageStorageReference
import com.memandis.remote.datasource.model.UserRemote
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.Resource
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.coroutines.InternalCoroutinesApi
import org.apache.commons.lang3.time.DateUtils
import java.util.*


class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private lateinit var navViewModel: NavigationViewModel

//    private val diaryViewModel by viewModels<DiaryViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        navViewModel = ViewModelProvider(requireActivity()).get(NavigationViewModel::class.java)
//        setupObservers()

        binding.addEntryFAB.setOnClickListener {
//            (requireActivity() as ToFlowNavigatable).
//            navigateToFlow(Destination.OnBoardingFlow("From home fragment"))
            var calendar = Calendar.getInstance()
            val date: Date = calendar.time
            val action = DiaryFragmentDirections.actionDiaryFragmentToEntryEditActivity(date)
            findNavController().navigate(action)
        }

        val userId = 1

        //    private val homeViewModel by viewModels<HomeViewModel> { getViewModelFactory() }

        val viewModel: DiaryDateViewModel = ViewModelProvider(
                this.requireActivity()).get(DiaryDateViewModel::class.java)

        //custom calender data
        val calendarBar: RylyToolbarView<DiaryDateHolder> = binding.
        calendarBar.apply {

                val rylyTabDateDelegate = RylyTabDateDelegate {
                    viewModel.today.value = it.time
                }
                behaviorBehaviorDelegate = rylyTabDateDelegate
            } as RylyToolbarView<DiaryDateHolder>

        //user profile
        viewModel.userProfile.observe(viewLifecycleOwner, { profile ->

            if(profile.status == Resource.Status.ERROR){
                return@observe
            }

            val data: UserRemote = profile.data ?: return@observe

            //profile images view
            GlideApp.with(calendarBar.context).load(data.getProfileImageStorageReference())
                .placeholder(R.color.colorPrimaryDark)
                .into(calendarBar.circleImageView)

            //profile expanded (button click) view
            calendarBar.setCircleImageButtonListener {
                MaterialDialog(requireContext(),
                    BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    customView(R.layout.btm_sheet_profile)

                    val root = getCustomView()
                    root.findViewById<CircularImageView>(R.id.profileImageView).apply {
                        GlideApp.with(calendarBar.context).load(data.getProfileImageStorageReference())
                            .placeholder(R.color.colorPrimaryDark)
                            .into(this)
                    }

                    root.findViewById<MaterialTextView>(R.id.nameTextView).apply {
                        text = data.username
                    }

                    root.findViewById<MaterialTextView>(R.id.emailTextView).apply {
                        text = data.email
                    }

                    root.findViewById<FloatingActionButton>(R.id.logoutBtn).setOnClickListener {

                        MaterialDialog(it.context).show {
                            title(text = getString(R.string.dialog_logout_confirmation_ask))
                            message(text = getString(R.string.dialog_logout_confirmation_message))

                            positiveButton(R.string.confirm) {
                                // Log out user from the application!
                                DiaryRepo.logoutUser()
                                // val intent = Intent(context, LoginActivity::class.java)
                                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                // startActivity(intent)
                                // activity!!.finish()

                                val intent: Intent = requireActivity().baseContext.packageManager
                                    .getLaunchIntentForPackage(
                                        requireActivity().baseContext.packageName)!!
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)

                                requireActivity().finish()

                            }

                            negativeButton(R.string.cancel)
                        }

                    }
                }
            }

        })

//        diaryViewModel.fetchProjects()
//        diaryViewModel.userProject.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            //Diary View Pager
//            if(it != null) {
//                Log.d("DiaryRepo.VIEWMODEL_TAG", it.size.toString()+" title "+it[0].title)
//            }
//        })

        //Project Entry Data
        viewModel.dateHolders.observe(viewLifecycleOwner,  {
            //Diary View Pager
            val fragmentAdapter = DiaryDateViewPagerAdapter(userId, this, it)

            binding.diaryDateViewPager.apply {
                adapter = fragmentAdapter

                TabLayoutMediator(calendarBar.tabLayoutWrapper.calendarTab, this) { tab, _ ->
                    this.setCurrentItem(tab.position, true)
                }.attach()

                offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            }

            calendarBar.postDataUpdate(it)

            if(viewModel.isFirstLaunch) {
                binding.diaryDateViewPager.currentItem = (it.size / 2) - 1
            }

            binding.addEntryFAB.setOnClickListener {_ ->
                val action = DiaryFragmentDirections.actionDiaryFragmentToEntryEditActivity(
                                                      it[calendarBar.getSelectedTabIndex()].date)
                findNavController().navigate(action)
            }
        })

        binding.calendarBarLayout.setLiftable(true)

    }

//    companion object {
//        fun generateArgs(data : String) = Bundle().apply {
//            putString("id", data)
//        }
//    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SecondLevelFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance() =
//            SecondLevelFragment().apply {
//                arguments = Bundle().apply {}
//            }
//    }

//    private fun setupObservers() {
//        lifecycleScope.launchWhenStarted {
//            navViewModel.navEventLiveData(NavEvent.InterestingMainEvent).observe(viewLifecycleOwner) {
//                Toast.makeText(requireContext(), "Received ${it.toString()}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    @Parcelize
//    class DairyResult(val data: String) : Parcelable
//
//    companion object {
//        const val REQ_KEY = "dashboard"
//        const val RET_RESULT = "result"
//    }

}

//fun Bundle.toDairyResult(): DiaryFragment.DairyResult {
//    return getParcelable(RET_RESULT)!!
//}

class DiaryDateViewPagerAdapter(val userId: Int, fragment: Fragment,
                                private val dates: List<DiaryDateHolder>) :
        FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return dates.size
    }

    override fun createFragment(position: Int): Fragment {
        return DiaryListFragment.newInstance(
                   userId, 
                   dates[position].apply {
                      date = DateUtils.truncate( this.date, Calendar.DATE)
                      })
    }

}
