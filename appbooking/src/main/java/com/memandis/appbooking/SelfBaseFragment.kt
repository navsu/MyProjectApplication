package com.memandis.appbooking

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class SelfBaseFragment<B : ViewDataBinding> : Fragment() {

    protected lateinit var binding: B

//    protected lateinit var fragmentComponent: FragmentComponent

    protected open var bottomNavigationViewVisibility = View.GONE

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

//        fragmentComponent = DaggerFragmentComponent
//            .builder()
//            .appComponent(MyDezignerApplication.appComponent)
//            .build()

        setHasOptionsMenu(true)
        return binding.root
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setBottomNav()
    }

    override fun onResume() {
        super.onResume()
//        setBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }

//    private fun setBottomNav() {
//        if (activity is MainActivity) {
//            var  mainActivity = activity as MainActivity
//            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }

}