package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.AllergiesDetailFragmentBinding
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.databinding.MedicationDetailFragmentBinding
import com.mE.Health.databinding.MyPersonaFragmentBinding
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.databinding.VisitsDetailFragmentBinding
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class VisitsDetailsFragment : BaseFragment() {

    private lateinit var binding: VisitsDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VisitsDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderSettingProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.visit),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.visit?.let { detail ->
            binding.apply {
                tvType.text = detail.type_display
                tvStatus.text = detail.status

                val statusDetail = Utilities.getVisitUIStatus(requireActivity(), detail.status ?: "")
                tvStatus.setTextColor(statusDetail.first)
                tvStatus.delegate.backgroundColor = statusDetail.second

                tvDetailStatus.text = detail.status?.capitalFirstChar()
                tvStartDate.text = detail.periodStart?.toDisplayDate()

                val datTimePair = openCloseTime(detail.periodStart, detail.periodEnd)
                tvDate.text = datTimePair.first
                tvTime.text = datTimePair.second
            }
        }
    }
}