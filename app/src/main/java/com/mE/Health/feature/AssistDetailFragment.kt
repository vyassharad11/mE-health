package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AssistDetailFragmentBinding
import com.mE.Health.feature.adapter.AssistCategoryDetailAdapter
import com.mE.Health.utility.Utils
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
    private val startDateCalendar = Calendar.getInstance()

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
        dateRangePicker()
    }

    private fun dateRangePicker() {
        binding.cvEndData.isEnabled = false

        binding.cvStartData.setOnClickListener {
            Utils.showDatePicker(
                context = requireContext(),
                calendar = startDateCalendar,
                maxDate = System.currentTimeMillis(),
                onDatePicked = { day, month, year ->
                    val dd = "%02d".format(day)
                    val mm = "%02d".format(month + 1)

                    startDate = "$dd-$mm-$year"
                    binding.tvStartDate.text = startDate
                    binding.tvEndDate.text = ""

                    startDateCalendar.set(year, month, day)
                    binding.cvEndData.isEnabled = true
                },
                onCancel = {
                    binding.cvEndData.isEnabled = false
                }
            )
        }

        binding.cvEndData.setOnClickListener {
            Utils.showDatePicker(
                context = requireContext(),
                calendar = startDateCalendar,
                minDate = startDateCalendar.timeInMillis,
                maxDate = System.currentTimeMillis(),
                onDatePicked = { day, month, year ->
                    val dd = "%02d".format(day)
                    val mm = "%02d".format(month + 1)

                    endDate = "$dd-$mm-$year"
                    binding.tvEndDate.text = endDate

                    mockViewModel.getAssistDetailData(startDate, endDate)
                }
            )
        }

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AssistCategoryDetailAdapter()
        binding.rvList.adapter = adapter

        mockViewModel.assistDetailList.observe(viewLifecycleOwner) {
            adapter.startDate = startDate
            adapter.endDate = endDate
            adapter.itemList = it
            if (it.isNullOrEmpty()) {
                binding.rvList.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
            } else {
                binding.rvList.visibility = View.VISIBLE
                binding.llNoData.visibility = View.GONE
                mockViewModel.insertAssistDetail(it)
            }
        }
    }
}