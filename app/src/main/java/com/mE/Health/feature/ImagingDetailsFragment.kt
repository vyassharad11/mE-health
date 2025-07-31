package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Imaging
import com.mE.Health.databinding.ImagingDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ImagingDetailsFragment : BaseFragment() {

    private lateinit var binding: ImagingDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImagingDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.imaging),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.imaging?.let { detail ->
            binding.apply {
                tvName.text = "${detail.modality_display} (${detail.modality_code})"
                tvDescription.text = detail.description
                tvDate.text = detail.started?.toDisplayDate()
                tvStatus.text = detail.status

                val statusDetail = Utilities.getLabUIStatus(requireActivity(), detail.status ?: "")
                tvStatus.apply {
                    text = detail.status?.capitalFirstChar()
                    setTextColor(statusDetail.first)
                    delegate.backgroundColor = statusDetail.second
                }
            }
            generateShareMessage(detail)
        }

        setPreviewDetail(binding.rvPreview)

        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }

    private fun generateShareMessage(detail: Imaging) {
        shareMessage =
            "Here is my Imaging information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.modality_display} (${detail.modality_code})\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Hospital name" +
                    "Date : ${detail.started?.toDisplayDate()}" +
                    "${detail.description}\n" +
                    "ID: #HYP2022105\n" +
                    "Conditions (2)\n" +
                    "Essential Hypertension : Clinical Status: Active\n" +
                    "Type 2 Diabetic : Clinical Status: Active\n" +
                    "Procedures (1)\n" +
                    "Appendectomy : Clinical Status: Active\n\n" +
                    "Performer - Dr. David\n" +
                    "Visits Status : In-progress\n" +
                    "Start Date: 11/06/2025\n" +
                    "\n" +
                    "Thank You!!"
    }
}