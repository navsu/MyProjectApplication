package com.memandis.project.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.memandis.appmain.R
import com.memandis.appmain.databinding.FragmentDiaryListBinding
import com.memandis.local.data.DiaryRepository
import com.memandis.project.AppViewModelFactory
import com.memandis.project.GlideApp
import com.memandis.project.diary.recyclerview.DairyRvAdapter
import com.memandis.project.diary.vm.DiaryViewModel
import com.memandis.remote.datasource.model.diary.DiaryDateHolder
import com.memandis.remote.datasource.model.diary.Resource.Status

class DiaryListFragment : Fragment() {

     private var _binding: FragmentDiaryListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private lateinit var navViewModel: NavigationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiaryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dataHolder: DiaryDateHolder?
        var userId: Int?

        requireArguments().apply{
            getSerializable(BUNDLE_DIARY_DATE_HOLDER).let {
                dataHolder = it as DiaryDateHolder
            }
            getInt(BUNDLE_USER_ID).let {
                userId = it
            }
        }

        val viewModelFactory = context?.let { DiaryRepository.getInstance(it) }?.let {
            AppViewModelFactory()
        }!!

        val viewModel: DiaryViewModel = ViewModelProvider(this@DiaryListFragment,
                          viewModelFactory).get(dataHolder.toString(), DiaryViewModel::class.java)
        viewModel.setDateHolder(dataHolder!!)

        val rvAdapter = DairyRvAdapter(dataHolder!!)
        binding.diaryListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        viewModel.entryListByDateHolder.observe(viewLifecycleOwner, {
//             Toast.makeText(this@DiaryListFragment.context,
//                 it.status.toString(), Toast.LENGTH_SHORT).show()
            Log.d("EXISTING DIARY ENTRY", "Entry $it.status.toString()")
            when(it.status) {
                Status.SUCCESS -> {
                    if(!it.data.isNullOrEmpty()) {
                        successState()
                        rvAdapter.submitList(it.data)
                        Log.d("EXISTING DIARY ENTRY", "SHOWING ${it.data?.size}")
                    } else {
                        errorState()
                    }
                }
                Status.LOADING -> {
                    loadingState()
                }
                else           -> {
                    errorState()
                }
            }
        })

         viewModel.listAllEntries.observe(viewLifecycleOwner, {
             Log.d("NEW DIARY ENTRY", "Entry $it")
         })

    }

    private fun successState() {
        Log.d("SUCCESS STATE", "Entry")
        binding.emptyList.emptyList.visibility = View.GONE
        binding.shimmerViewContainer.visibility = View.GONE
        binding.shimmerViewContainer.stopShimmer()
        binding.diaryListRecyclerView.visibility = View.VISIBLE
    }

    private fun loadingState() {
        binding.emptyList.emptyList.visibility = View.GONE
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmer()
        binding.diaryListRecyclerView.visibility = View.GONE
    }

    private fun errorState() {
        binding.emptyList.emptyList.visibility = View.VISIBLE
        binding.shimmerViewContainer.visibility = View.GONE
        binding.shimmerViewContainer.stopShimmer()
        binding.diaryListRecyclerView.visibility = View.GONE

        val imageDrawableInt = arrayListOf(R.drawable.state_empty_list_1,
                                           R.drawable.state_empty_list_2,
                                           R.drawable.state_empty_list_3)
        imageDrawableInt.shuffle()

        GlideApp.with(this).asBitmap().load("").placeholder(
            imageDrawableInt[0]).into(binding.emptyList.emptyStateImage)
    }

    companion object{
        const val BUNDLE_USER_ID = "userId"
        const val BUNDLE_DIARY_DATE_HOLDER = "diaryDateHolder"

        fun newInstance(userId: Int, dateHolder: DiaryDateHolder): DiaryListFragment {
            return DiaryListFragment().apply {
                val bundle = Bundle().apply {
                    putInt(BUNDLE_USER_ID, userId)
                    putSerializable( BUNDLE_DIARY_DATE_HOLDER, dateHolder)
                }
                arguments = bundle
            }
        }
    }
}