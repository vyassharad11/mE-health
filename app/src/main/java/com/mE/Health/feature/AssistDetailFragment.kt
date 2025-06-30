package com.mE.Health.feature

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AssistDetailFragmentBinding
import com.mE.Health.feature.adapter.AssistCategoryDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AssistDetailFragment : BaseFragment() {

    private lateinit var binding: AssistDetailFragmentBinding
    private var startDate = ""
    private var endDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AssistDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        val title = arguments?.getString("Title")
        binding.tvAssistTitle.text = title
        val startDateCalendar = Calendar.getInstance()
        binding.cvStartData.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                R.style.my_dialog_theme,
                { view, year, monthOfYear, dayOfMonth ->
                    startDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    binding.tvStartDate.text = startDate
                    startDateCalendar.set(Calendar.YEAR, year)
                    startDateCalendar.set(Calendar.MONTH, monthOfYear)
                    startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate))
                        binding.tvApply.delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.text_color_orange)
                },
                startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        binding.cvEndData.setOnClickListener {
            val calendar = Calendar.getInstance()
            var mYear = calendar.get(Calendar.YEAR)
            var mMonth = calendar.get(Calendar.MONTH)
            var mDay = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                R.style.my_dialog_theme, { view, year, monthOfYear, dayOfMonth ->
                    endDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    binding.tvEndDate.text = endDate
                    mYear = year
                    mMonth = monthOfYear
                    mDay = dayOfMonth
                    if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate))
                        binding.tvApply.delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.text_color_orange)
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.minDate = startDateCalendar.timeInMillis;
            datePickerDialog.show()
        }

        binding.tvApply.setOnClickListener {
            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                if (title.equals("Chronic Condition Detector")) {
                    binding.llNoData.visibility = View.GONE
                    binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
                    val adapter = AssistCategoryDetailAdapter(requireActivity(), title!!)
                    binding.rvList.adapter = adapter
                } else {
                    binding.rvList.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            }
        }
    }
}