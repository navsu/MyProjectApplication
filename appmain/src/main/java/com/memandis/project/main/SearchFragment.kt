package com.memandis.project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.memandis.appmain.databinding.FragmentSearchBinding
import com.jakewharton.rxbinding4.widget.textChanges
import com.memandis.project.diary.recyclerview.DairyRvAdapter
import com.memandis.remote.datasource.model.diary.DiaryEntry
import com.memandis.remote.datasource.model.diary.Resource
import com.memandis.remote.domain.usecases.reactivelyfetch.UserDiaryUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_search, container, false)
//    }

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        val a =  binding.searchTextField.textChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.setSearchKeyword(it.toString())
                }

        val rvAdapter = DairyRvAdapter()
        binding.searchRecyclerView.apply {
            setHasFixedSize(true)
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        viewModel.searchResult.observe(viewLifecycleOwner,
            Observer<Resource<List<DiaryEntry>>> {

            if(it.status == Resource.Status.LOADING){
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.GONE
            }else{
                binding.shimmerViewContainer.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                rvAdapter.submitList(it.data)
            }

        })
    }
}

class SearchViewModel : ViewModel() {
    private val searchKeyword = MutableLiveData("")

    val searchResult = Transformations.switchMap(searchKeyword){
        LiveDataReactiveStreams.fromPublisher(UserDiaryUseCase.searchDiaryByTitle(it))
    }

    fun setSearchKeyword(searchKeyValue: String) {
        searchKeyword.value = searchKeyValue
    }
}