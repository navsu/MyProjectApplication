package com.memandis.project.diary.calenderview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.memandis.appmain.R
import com.memandis.appmain.databinding.BarDiaryCalendarViewBinding
import com.memandis.appmain.databinding.BarDiaryCalendarViewCompactBinding
import com.memandis.remote.utils.binding.toPx
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.abs

const val DAY_TIME_WINDOW = 14

class RylyToolbarView<TabType>(context: Context, attributes: AttributeSet) :
        MaterialToolbar(context, attributes) {

    private val LOG_TAG = this::class.java.simpleName

//    private var _binding1: BarDiaryCalendarViewBinding? = null
    private var _binding2: BarDiaryCalendarViewCompactBinding? = null

    private val binding get() = _binding2!!

    // val calendarRvAdapter = CalendarRvAdapter()
    lateinit var behaviorBehaviorDelegate: RylyToolbarBehaviorDelegate<TabType>

    val tabLayoutWrapper: TabLayoutWrapper
    val circleImageView: CircleImageView

    private val liveDataScrollX: MutableLiveData<Int> = MutableLiveData()

    var autoScrollingEnabled: Boolean
    private val isCompactSize: Boolean

    init {

        val attrs = context.theme.obtainStyledAttributes(attributes,
                                      R.styleable.RylyToolbarView, 0, 0)
        // Apply attributes
        val profileImageRes: Int
        attrs.apply {
            try {
                autoScrollingEnabled = getBoolean(R.styleable.RylyToolbarView_autoInitialScroll,
                                                  true)

                isCompactSize = getBoolean(R.styleable.RylyToolbarView_compactSize,
                                                  false)

                profileImageRes = getResourceId(R.styleable.RylyToolbarView_profileImage,
                                                R.drawable.ic_launcher_background)
            } finally {
                recycle()
            }
        }

//       if(!isCompactSize){
//           inflate(context, R.layout.bar_diary_calendar_view, this)
//            binding =
//                DataBindingUtil.inflate(LayoutInflater.from(context),
//                                        R.layout.bar_diary_calendar_view,
//                                        parent as ViewGroup?,
//                            false);
//           _binding1 = BarDiaryCalendarViewBinding.inflate(LayoutInflater.from(context), this)
//           binding = _binding1 as BarDiaryCalendarViewBinding
//        }else{
//           inflate(context, R.layout.bar_diary_calendar_view_compact, this)
//            binding =
//                DataBindingUtil.inflate(LayoutInflater.from(context),
//                                        R.layout.bar_diary_calendar_view_compact,
//                                        parent as ViewGroup?,
//                           false);
             _binding2 = BarDiaryCalendarViewCompactBinding.inflate(LayoutInflater.from(context), this)
//             binding = _binding2 as BarDiaryCalendarViewCompactBinding
//        }

        setContentInsetsAbsolute(0, 0)
        tabLayoutWrapper = TabLayoutWrapper(binding.root)

//        binding = root
        liveDataScrollX.postValue(tabLayoutWrapper.calendarTab.scrollX)
        tabLayoutWrapper.calendarTab.apply {
            setOnScrollChangeListener { _, scrollX, _, _, _ ->
                // val minPosition = calculateItemToDisplayMonth(v as TabLayout, scrollX)
                liveDataScrollX.postValue(scrollX)
            }
        }

        binding.profileImageView.setImageDrawable(getDrawable(context, profileImageRes))
        circleImageView = binding.profileImageView

    }

    private lateinit var onTabSelectedListener: TabLayout.OnTabSelectedListener

    fun setCircleImageButtonListener(onClick: (View) -> Unit){
        circleImageView.setOnClickListener {
            onClick(it)
        }
    }

    fun postDataUpdate(list: List<TabType>?) {
        if(list != null) {

            liveDataScrollX.removeObservers(context as LifecycleOwner)
            liveDataScrollX.observe(context as LifecycleOwner) {
                try {
                    val minPosition = calculateTabInTheMiddle(tabLayoutWrapper.calendarTab, it)
                    behaviorBehaviorDelegate.onTabScrolled(binding.defaultOverlineText,
                                                           binding.defaultHeaderText,
                                                           binding.subtitleTextView,
                                                           list[minPosition])
                } catch(e: KotlinNullPointerException) {
                    e.printStackTrace()
                }
                // println("Closest View: ${tabLayoutWrapper.calendarTab.getTabAt(minPosition)!!.text}")
            }

            if(this::onTabSelectedListener.isInitialized) {
                tabLayoutWrapper.calendarTab.removeOnTabSelectedListener(onTabSelectedListener)
            }

            onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // TODO("not implemented")
                    // To change body of created functions use File | Settings | File Templates.
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // TODO("not implemented")
                    // To change body of created functions use File | Settings | File Templates.
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    behaviorBehaviorDelegate.onTabSelected(binding.defaultOverlineText,
                                                           binding.defaultHeaderText,
                                                           binding.subtitleTextView,
                                                           list[tab!!.position])
                }
            }

            binding.defaultOverlineText.setOnClickListener {
                // Toasty.info(context!!, "${getSelectedTabIndex()}").show()
                behaviorBehaviorDelegate.onOverlineTextClickListener(it,
                                                                     list[getSelectedTabIndex()])
            }
            tabLayoutWrapper.calendarTab.addOnTabSelectedListener(onTabSelectedListener)

            tabLayoutWrapper.postUpdate(list)
        }
    }

    private fun calculateTabInTheMiddle(tabLayout: TabLayout, scrollX: Int = 0): Int {
        val tabLayoutWidthCenter = tabLayout.measuredWidth / 2 - tabLayout.getTabAt(0)!!.view.width

        var minPosition = -1
        var initialDistanceDiff = 999.0
        for(i in 0 until tabLayout.tabCount) {
            // Distance Diff between middle a child view
            val averageTagHorizontal = (tabLayout.getTabAt(i)!!.view.left + tabLayout.getTabAt(
                    i)!!.view.right) / 2.0
            val distanceDiff = abs(tabLayoutWidthCenter - averageTagHorizontal + scrollX)
            if(initialDistanceDiff > distanceDiff) {
                initialDistanceDiff = distanceDiff
                minPosition = i
            }
        }
        return minPosition
    }

    fun getSelectedTabIndex(): Int {
        return tabLayoutWrapper.getSelectedTabIndex()
    }

    private val STATE_PERSISTENT_SUPER = "sajdhaskdjkahsjkda"
    private val STATE_PERSISTENT_TAB_SELECTION = "sajdhaskdjasdsadasdpogfplgokpdfpokkahsjkda"

    override fun onSaveInstanceState(): Parcelable? {
        super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(STATE_PERSISTENT_SUPER, super.onSaveInstanceState())
            putInt(STATE_PERSISTENT_TAB_SELECTION, tabLayoutWrapper.calendarTab.selectedTabPosition)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if(newState is Bundle) {
            val selectedTab = newState.getInt(STATE_PERSISTENT_TAB_SELECTION)
            this@RylyToolbarView.post {
                Log.d(LOG_TAG, "Scrolling to the ${selectedTab}th tab")
                tabLayoutWrapper.scrollToTab(selectedTab)
            }
            newState = newState.getParcelable(STATE_PERSISTENT_SUPER)
        }

        super.onRestoreInstanceState(newState)
    }

    inner class TabLayoutWrapper(root: View) {
        val calendarTab: FadingTabLayout = root.findViewById(R.id.date_tabs)

        init {
            calendarTab.isSmoothScrollingEnabled = true
        }

        var oldTabIndex = -1
        fun postUpdate(diaryDateHolders: List<TabType>) {
            oldTabIndex = getSelectedTabIndex()
            Log.d(LOG_TAG, "postUpdate Old Selected tab: $oldTabIndex")
            calendarTab.removeAllTabs()
            for(i in diaryDateHolders.indices) {
                val tab = calendarTab.newTab().apply {
                    setCustomView(behaviorBehaviorDelegate.itemLayoutRes)
                    val dataItem = diaryDateHolders[i]
                    behaviorBehaviorDelegate.onCreateTab(this, i, dataItem)

                    val isAlwaysEmphasized: Boolean? = behaviorBehaviorDelegate
                            .isAwaysEmphasized(dataItem)
                    if(isAlwaysEmphasized != null && isAlwaysEmphasized) {
                        customView!!.findViewById<MaterialCardView>(R.id.dateRootCard).apply {
                            strokeWidth = 2.toPx
                            strokeColor = this.context.getColor(R.color.colorPrimary)
                        }
                    }
                }
                calendarTab.addTab(tab)
            }

            if(oldTabIndex !in -1 .. 0) {
                val oldAutoScroll = autoScrollingEnabled
                autoScrollingEnabled = false
                scrollToTab(oldTabIndex)
                autoScrollingEnabled = oldAutoScroll
            }

            if(autoScrollingEnabled) {
                scrollToTab()
            }
        }

        fun scrollToTab(tabIndex: Int = ((DAY_TIME_WINDOW + 1) / 2)) {
            val parent = calendarTab.getChildAt(0) as ViewGroup

            val finalTabIndex = if(tabIndex >= parent.childCount) {
                parent.childCount / 2
            } else {
                tabIndex
            }
            parent.post {
                val targetTab = parent.getChildAt(finalTabIndex) ?: parent.getChildAt(0)
                targetTab?.post {
                    val right = targetTab.right
                    calendarTab.scrollTo(right, 0)

                    val tab = try {
                        Log.d(LOG_TAG, "Trying to select the ${finalTabIndex}th tab")
                        calendarTab.getTabAt(finalTabIndex)!!
                    } catch(e: KotlinNullPointerException) {
                        e.printStackTrace()
                        Log.d(LOG_TAG, "Error!, Trying to select the ${finalTabIndex - 1}th tab")
                        calendarTab.getTabAt(finalTabIndex - 1)
                    }
                    tab?.apply {
                        select()
                        Log.d(LOG_TAG, "Selected the ${finalTabIndex}th tab named: $text")
                    }
                }
            }
        }

        internal fun getSelectedTabIndex(): Int {
            return calendarTab.selectedTabPosition
        }

    }

    class FadingTabLayout(context: Context, attributes: AttributeSet) :
            TabLayout(context, attributes) {
        override fun getLeftFadingEdgeStrength(): Float {
            return 0f
        }
    }
}

