package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DosageInstruction
import com.mE.Health.data.model.MedicationCode
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.AllergiesDetailFragmentBinding
import com.mE.Health.feature.adapter.VisitConditionAdapter
import com.mE.Health.feature.adapter.VisitMedicationAdapter
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.fromJson
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.viewmodels.ConditionDataViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AllergiesDetailsFragment : BaseFragment() {

    private lateinit var binding: AllergiesDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllergiesDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.allergies),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.allergy?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )

            viewModel.getFirstVisitDataByEncounterId(detail.encounterId!!)
            viewModel.getPractitionerOrganizationName(detail.encounterId)
            viewModel.getMedicationByEncounterId(detail.encounterId)
            viewModel.getConditionByEncounterId(detail.encounterId)

            setUserSelectedDetails(detail.id,Constants.ALLERGIES,detail.code_display!!,detail.recordedDate?.toDisplayDate()!!)
            binding.apply {
                tvRecordedDate.text = detail.recordedDate.toDisplayDate()
                tvName.text = detail.code_display
                tvAllergyId.text = detail.id.uppercase()
                Utilities.getLabUIStatus(requireActivity(), detail.clinicalStatus ?: "").let {
                    tvStatus.text = detail.clinicalStatus?.capitalFirstChar()
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
            binding.tvVisitDate.text = getString(R.string.record_date_with_value, it.first.periodStart?.toDisplayDate())
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
    }

    private fun generateShareMessage(detail: AllergyIntolerance) {
        shareMessage =
            "Here is my Allergies information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.code_display}\n" +
                    "Clinical Status : ${detail.clinicalStatus?.capitalFirstChar()}\n" +
                    "Recorded Date - ${detail.recordedDate?.toDisplayDate()}\n" +
                    "Allergy ID - ALG- 2024-392\n" +
                    "\n" +
                    "Conditions (2)\n" +
                    "Essential Hypertension : Clinical Status: Active\n" +
                    "Type 2 Diabetic : Clinical Status: Active\n" +
                    "Medications (2)\n" +
                    "Lisinopril 10mg : Clinical Status: Active\n" +
                    "Metformin 500mg : Clinical Status: Active\n\n" +
                    "Thank You!!"
    }
}