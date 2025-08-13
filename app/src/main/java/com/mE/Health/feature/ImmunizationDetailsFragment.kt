package com.mE.Health.feature

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.data.model.Vaccine
import com.mE.Health.databinding.ImmunizationDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.viewmodels.ConditionDataViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ImmunizationDetailsFragment : BaseFragment() {

    private lateinit var binding: ImmunizationDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImmunizationDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        observeData()
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.immunization), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        DetailSingleton.immunization?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(
                detail.id,
                Constants.IMMUNIZATIONS,
                detail.vaccineCode_display!!,
                detail.occurrenceDate?.toDisplayDate()!!
            )
            if (detail.patientId != null) {
                viewModel.getPatientDetail(detail.patientId)
            }
            viewModel.getFirstVisitDataByEncounterId(detail.encounterId!!)
            viewModel.getPractitionerOrganizationName(detail.encounterId!!)
            val reasonObject = Gson().fromJson(detail.vaccineCode, Vaccine::class.java)
            binding.apply {
                tvName.text = detail.vaccineCode_display
                tvImmunizationId.text = "#" + detail.id.uppercase()
                tvDescription.text =  reasonObject?.display

//                val text = "Recorded Date: ${detail.occurrenceDate.toDisplayDate()}"
//                tvRecordedDate.text = text
                tvStatus.text = detail.status?.capitalFirstChar()
                Utilities.getProcedureUIStatus(requireActivity(), detail.status ?: "").let {
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
            }
            generateShareMessage(detail)
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }


    private fun observeData() {
        viewModel.patientDetail.observe(viewLifecycleOwner) {
            binding.tvPatientName.text = it.name
        }
        viewModel.practitionerData.observe(viewLifecycleOwner) {
            binding.tvPractitionerName.text = it.first
            binding.tvOrganizationName.text = it.second
        }
        viewModel.encounterObjectData.observe(viewLifecycleOwner) {
            val statusDetail =
                Utilities.getVisitUIStatus(requireActivity(), it.first.status ?: "")
            binding.rtvVisitStatus.text = it.first.status?.capitalFirstChar()
            binding.rtvVisitStatus.setTextColor(statusDetail.first)
            binding.rtvVisitStatus.delegate.backgroundColor = statusDetail.second
            binding.tvVisitDate.text =getString(R.string.start_date_with_value, it.first.periodStart?.toDisplayDate())
        }
    }

    private fun generateShareMessage(detail: Immunization) {
        shareMessage =
            "Here is my Immunization information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.vaccineCode_display}\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Sarah Parker\n" +
                    "Initial Consultation\n" +
                    "\n" +
                    "Record Details\n" +
                    "Immunization ID : #IMM78901\n" +
                    "Location : Apollo Hospital\n" +
                    "Provider : Dr. Sarah Johnson\n\n" +
                    "Thank You!!"
    }
}