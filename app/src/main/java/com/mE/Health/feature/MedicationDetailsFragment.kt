package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DosageInstruction
import com.mE.Health.data.model.MedicationCode
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.data.model.Value
import com.mE.Health.databinding.MedicationDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.formatIntoPrettyDate
import com.mE.Health.utility.fromJson
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MedicationDetailsFragment : BaseFragment() {

    private lateinit var binding: MedicationDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MedicationDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        setDetails()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.medication),binding.toolbar.tvTitle,true)
    }

    private fun setDetails() {
        DetailSingleton.medication?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.id, Constants.MEDICATION)

            binding.tvMedicationDisplay.text = detail.medicationCode_display
            binding.tvMedicationId.text =
                fromJson(detail.medicationCode, MedicationCode::class.java).code
            binding.tvDosageInstruction.text =
                fromJson(detail.dosageInstruction, DosageInstruction::class.java).text
            binding.tvReason.text = fromJson(detail.reasonCode, ReasonCode::class.java).display
            binding.tvDate.text = detail.authoredOn?.toDisplayDate()
            binding.tvStatus.apply {
                text = detail.status?.capitalFirstChar()
                val statusDetail = Utilities.getLabUIStatus(requireActivity(), detail.status ?: "")
                setTextColor(statusDetail.first)
                delegate.backgroundColor = statusDetail.second
            }
            generateShareMessage(detail)
        }

        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }

    private fun generateShareMessage(detail: MedicationRequest) {
        shareMessage =
            "Here is my Medication information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.medicationCode_display}\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Capsule - Oral Suspension\n" +
                    "\n" +
                    "Date\n" +
                    "${detail.authoredOn?.toDisplayDate()}\n\n" +
                    "Details\n" +
                    "Medication ID : ${fromJson(detail.medicationCode, MedicationCode::class.java).code}\n" +
                    "Label Field Notes : Take with food\n" +
                    "Dosage Instruction\n" +
                    "${fromJson(detail.dosageInstruction, DosageInstruction::class.java).text}\n" +
                    "Visit\n" +
                    "Recorded Date: 06/11/2025\n" +
                    "Reason\n" +
                    "${fromJson(detail.reasonCode, ReasonCode::class.java).display}\n" +
                    "Thank You!!"
    }
}