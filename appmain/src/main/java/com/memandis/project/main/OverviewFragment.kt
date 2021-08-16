package com.memandis.project.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.memandis.appmain.R
import com.memandis.appmain.databinding.FragmentOverviewBinding
import com.memandis.project.diary.vm.DiaryDateViewModel
//import java.time.YearMonth
//import java.time.temporal.WeekFields
import org.threeten.bp.YearMonth
import org.threeten.bp.temporal.WeekFields
import java.util.*

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)[DiaryDateViewModel::class.java]

        binding.calendarView.inDateStyle = InDateStyle.ALL_MONTHS
        binding.calendarView.outDateStyle = OutDateStyle.END_OF_ROW
        binding.calendarView.scrollMode = ScrollMode.PAGED
        binding.calendarView.orientation = RecyclerView.HORIZONTAL

        binding.calendarView.maxRowCount = 6
        binding.calendarView.hasBoundaries = true

        // TODO: Continue doing this lol
        //   Follow this link: https://github.com/kizitonwose/CalendarView
        binding.calendarView.dayBinder = object : DayBinder<DiaryDateViewContainer> {
            override fun bind(container: DiaryDateViewContainer, day: CalendarDay) {
                container.dateTextView.apply {
                    text = day.date.dayOfMonth.toString()

                    if (day.owner == DayOwner.THIS_MONTH) {
                        setTextColor(Color.WHITE)
                    } else {
                        setTextColor(Color.GRAY)
                    }
                }

                // container.goodBadTextView.apply {
                //
                //     val match = it.find {
                //         day.date.dayOfYear
                //
                //         val c = Calendar.getInstance().apply {
                //             time = it.date
                //         }
                //         c.get(Calendar.DAY_OF_YEAR) == day.date.dayOfYear
                //         // DateUtils.isSameDay(it.date, java.sql.Date.valueOf(day.date))
                //     }
                //
                //     text = match?.goodBadScore?.toString() ?: "0"
                // }
            }

            override fun create(view: View): DiaryDateViewContainer = DiaryDateViewContainer(view)
        }
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

        viewModel.dateHolders.observe(viewLifecycleOwner, {

            //
        })
    }
}

class DiaryDateViewContainer(view: View) : ViewContainer(view) {
    val cardContainer = view.findViewById<MaterialCardView>(R.id.dateRootCard)
                           //dateRootCard
    val dateTextView = view.findViewById<MaterialTextView>(android.R.id.text1)
    val goodBadTextView = view.findViewById<MaterialTextView>(R.id.goodnessPointTextView)
                            //goodnessPointTextView
}