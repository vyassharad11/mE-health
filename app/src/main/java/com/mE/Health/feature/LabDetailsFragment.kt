package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LabDetailsFragment : BaseFragment() {

    private lateinit var binding: LabDetailFragmentBinding
    private var practitionerName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LabDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderTitleProperties(getString(R.string.lab),binding.toolbar.tvTitle,true)
    }

    private fun setDetails() {
        DetailSingleton.lab?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.id, Constants.LABS)

            if (detail.performerId != null) {
                mockViewModel.getPractitionerDetail(detail.performerId)
            }
            binding.apply {
                tvLabId.text = "Lab ID: "+detail.id.uppercase()
                tvDate.text = detail.issued?.toDisplayDate()
                tvStartDate.text = "Start Date: "+detail.effectiveDate?.toDisplayDate()
                tvName.text = detail.code_display

                tvStatus.apply {
                    text = detail.status?.capitalFirstChar()
                    val statusDetail = Utilities.getLabUIStatus(requireActivity(), detail.status ?: "")
                    setTextColor(statusDetail.first)
                    delegate.backgroundColor = statusDetail.second
                }
            }
        }

        mockViewModel.practitionerDetail.observe(viewLifecycleOwner) {
            practitionerName = it.name!!
            binding.tvPractitionerName.text = it.name
        }

        binding.layoutSyncButton.llShareData.setOnClickListener {
            generateShareMessage(DetailSingleton.lab!!)
            shareRecord(message = shareMessage)
        }
    }

    private fun generateShareMessage(detail: DiagnosticReport) {
        shareMessage =
            "Here is my medical DiagnosticReport information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.code_display}\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Lab ID: ${detail.id.uppercase()}\n" +
                    "\n" +
                    "Start date\n" +
                    "${detail.issued?.toDisplayDate()}\n\n" +
                    "Results\n" +
                    "Hemoglobin : 13.5\n" +
                    "RCB        : 2500\n" +
                    "WBC        : 200\n" +
                    "Performer\n" +
                    practitionerName +
                    "Visits Status : In-progress\n" +
                    "Start Date : ${detail.effectiveDate?.toDisplayDate()}\n" +
                    "\n" +
                    "\n" +
                    "Thank You!!"
    }
}