package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Encounter
import com.mE.Health.databinding.VisitsDetailFragmentBinding
import com.mE.Health.feature.adapter.ConditionVitalAdapter
import com.mE.Health.feature.adapter.VisitAllergyAdapter
import com.mE.Health.feature.adapter.VisitConditionAdapter
import com.mE.Health.feature.adapter.VisitMedicationAdapter
import com.mE.Health.feature.adapter.VisitProcedureAdapter
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.viewmodels.ConditionDataViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class VisitsDetailsFragment : BaseFragment() {

    private lateinit var binding: VisitsDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

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
        observeData()
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.visit), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        DetailSingleton.visit?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(
                detail.id,
                Constants.VISITS,
                detail.description!!,
                detail.createdAt?.toDisplayDate()!!
            )

            binding.apply {
                viewModel.getPractitionerOrganizationName(detail.id)
                viewModel.getConditionByEncounterId(detail.id)
                viewModel.getProcedureDataByEncounterId(detail.id)
                viewModel.getMedicationByEncounterId(detail.id)
                viewModel.getAllergyDataByEncounterId(detail.id)

                tvVisitId.text = detail.id.uppercase()
                tvType.text = detail.type_display
                tvStatus.text = detail.status?.capitalFirstChar()
                generateShareMessage(detail)
                val statusDetail =
                    Utilities.getVisitUIStatus(requireActivity(), detail.status ?: "")
                tvStatus.setTextColor(statusDetail.first)
                tvStatus.delegate.backgroundColor = statusDetail.second

                tvDetailStatus.text = detail.status?.capitalFirstChar()
                tvStartDate.text = detail.periodStart?.toDisplayDate()

                val datTimePair = openCloseTime(detail.periodStart, detail.periodEnd)
                tvDate.text = datTimePair.first
                tvTime.text = datTimePair.second
            }
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }

    private fun observeData() {
        viewModel.practitionerData.observe(viewLifecycleOwner) {
            binding.tvPractitionerName.text = it.first
            binding.tvOrganizationName.text = it.second
        }
        viewModel.conditionData.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                binding.tvConditionTitle.text = "Conditions (${it.size})"
                binding.rvCondition.layoutManager =
                    LinearLayoutManager(requireActivity())
                val adapter = VisitConditionAdapter(requireActivity())
                adapter.itemList = it
                binding.rvCondition.adapter = adapter
            } else {
                binding.cvCondition.visibility = View.GONE
            }
        }
        viewModel.procedureData.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                binding.tvProcedureTitle.text = "Procedures (${it.size})"
                binding.rvProcedure.layoutManager =
                    LinearLayoutManager(requireActivity())
                val adapter = VisitProcedureAdapter(requireActivity())
                adapter.itemList = it
                binding.rvProcedure.adapter = adapter
            } else {
                binding.cvProcedure.visibility = View.GONE
            }
        }
        viewModel.medicationData.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                binding.tvMedicationTitle.text = "Medications (${it.size})"
                binding.rvMedication.layoutManager =
                    LinearLayoutManager(requireActivity())
                val adapter = VisitMedicationAdapter(requireActivity())
                adapter.itemList = it
                binding.rvMedication.adapter = adapter
            } else {
                binding.cvMedication.visibility = View.GONE
            }
        }
        viewModel.allergyData.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                binding.tvAllergyTitle.text = "Allergies (${it.size})"
                binding.rvAllergy.layoutManager =
                    LinearLayoutManager(requireActivity())
                val adapter = VisitAllergyAdapter(requireActivity())
                adapter.itemList = it
                binding.rvAllergy.adapter = adapter
            } else {
                binding.cvAllergy.visibility = View.GONE
            }
        }
    }

    private fun generateShareMessage(detail: Encounter) {
        shareMessage =
            "Here is my medical Visit information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "Visit Details\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Visit ID\n" +
                    "ENC-2023-10156\n" +
                    "\n" +
                    "Start date\n" +
                    "${detail.periodStart?.toDisplayDate()}\n" +
                    "Type ${detail.type_display}\n" +
                    "Dr. Michael Chen\n" +
                    "Hospital Name\n" +
                    "Conditions (2)\n" +
                    "Essential Hypertension\n" +
                    "Clinical Status: Active\n" +
                    "Type 2 Diabetic\n" +
                    "Clinical Status: Active\n" +
                    "Procedures (1)\n" +
                    "Appendectomy\n" +
                    "Status: Completed\n" +
                    "Medications (2)\n" +
                    "Lisinopril 10mg\n" +
                    "Clinical Status: Active\n" +
                    "Metformin 500mg\n" +
                    "Clinical Status: Active\n" +
                    "Allergies (2)\n" +
                    "Penicillin\n" +
                    "Severity: Severe\n" +
                    "Sulfa Drugs\n" +
                    "Severity: Moderate\n" +
                    "\n" +
                    "\n" +
                    "Thank You!!"
    }
}