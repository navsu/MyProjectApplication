package com.memandis.project.entry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.memandis.appmain.R
import com.memandis.appmain.databinding.FragmentEntryContentDisplayBinding
import com.memandis.project.diary.recyclerview.populateTags
import com.memandis.project.entry.vm.EntryDisplayViewModel
import com.memandis.project.entry.vm.EntryEditorViewModel.Companion.EMPTY_LOCATION
import com.memandis.project.viewModelInjectionHelper
import com.memandis.remote.datasource.model.diary.DEFAULT_ID
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.Resource

/**
 * A Fragment that displays the content of a given diary entry
 * @property LOG_TAG String Tag string for showing Debugging Log
 * @property imageAdapter ImageCarouselRvAdapter RecyclerViewAdapter for ViewPager
 */
class DiaryContentDisplayFragment : Fragment(), Observer<Resource<DiaryEntry>> {

    private val LOG_TAG = this::class.java.simpleName
    private lateinit var imageAdapter: ImageCarouselRvAdapter

    private var _binding: FragmentEntryContentDisplayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryContentDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var entryId = DEFAULT_ID
        requireArguments().apply{
            getString(BUNDLE_DIARY_ENTRY_ID).let {
                entryId = it!!
            }
        }

        imageAdapter = ImageCarouselRvAdapter()
        binding.imageCarousel.apply {
            adapter = imageAdapter
            clipToPadding = false
            binding.imageCarouselIndicator.setViewPager(this)
        }
        imageAdapter.registerAdapterDataObserver(binding.imageCarouselIndicator.adapterDataObserver)

        // TODO -> Implement this fragment to display contents!
        val viewModel = viewModelInjectionHelper<EntryDisplayViewModel>(this,
                                                                        diaryEntryId = entryId)
        viewModel.entryContent.observe(viewLifecycleOwner, this)

    }

    override fun onChanged(it: Resource<DiaryEntry>?) {

        if(it?.data == null){
            return
        }
        val data = it.data

        Log.d(LOG_TAG, "Showing Diary: $it")

        binding.entryContentTextView.apply {
            text = data!!.content
        }

        if(!data!!.images.isNullOrEmpty()) {
            imageAdapter.submitList(data!!.images)
        } else {
            binding.imageCarousel.visibility = View.GONE
        }

        binding.tagChipGroup.populateTags(data!!)

        binding.goodBadView.apply {
            setNumber(data.ratings)
            setIconBackgroundColor(data.color)
        }

        if(data.location != EMPTY_LOCATION) {
//            locationIndicatorLayout.setOnClickListener {
//                context!!.openCoordinateInGoogleMap(data.location)
//            }
//
//            context!!.getAreaNameByCoordinate(data.location).apply {
//                locationNameTextView.text = first
//                locationCoordinateTextView.text = second
//            }
        }else{
            binding.locationIndicatorLayout.visibility = View.GONE
        }
    }

    companion object{
        const val BUNDLE_DIARY_ENTRY_ID = "entryID"
        fun newInstance(entryId: String): DiaryContentDisplayFragment {
            return DiaryContentDisplayFragment().apply {
                val bundle = Bundle().apply {
                    putString(BUNDLE_DIARY_ENTRY_ID, entryId)
                }
                arguments = bundle
            }
        }

    }
}