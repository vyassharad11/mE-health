package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.databinding.MedicationDetailFragmentBinding
import com.mE.Health.databinding.MyPersonaFragmentBinding
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.databinding.ProcedureDetailFragmentBinding
import com.mE.Health.databinding.VitalDetailFragmentBinding
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ProcedureDetailsFragment : BaseFragment() {

    private lateinit var binding: ProcedureDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProcedureDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.procedure)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
        DetailSingleton.procedure?.let { detail ->
            binding.apply {
                tvName.text = detail.code_display
                tvProcedureDate.text = detail.performedDate?.toFormattedDate()

                val statusDetail = Utilities.getVisitUIStatus(requireActivity(), detail.status ?: "")
                tvStatus.setTextColor(statusDetail.first)
                tvStatus.delegate.backgroundColor = statusDetail.second

                val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)

                tvProcedureId.text = "#${reasonCodeObject.code}"
                tvReason.text = reasonCodeObject.display
            }
        }
    }
}