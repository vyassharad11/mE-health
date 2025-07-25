package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Encounter
import com.mE.Health.databinding.VisitsDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class VisitsDetailsFragment : BaseFragment() {

    private lateinit var binding: VisitsDetailFragmentBinding
    private var shareMessage = ""

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
        setHeaderUploadProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.visit), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        DetailSingleton.visit?.let { detail ->
            binding.apply {
                tvType.text = detail.type_display
                tvStatus.text = detail.status
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