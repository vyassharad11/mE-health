package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.Performer
import com.mE.Health.databinding.ImagingDetailFragmentBinding
import com.mE.Health.feature.adapter.VisitConditionAdapter
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
class ImagingDetailsFragment : BaseFragment() {

    private lateinit var binding: ImagingDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

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
        observeData()
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
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            val title = "${detail.modality_display} (${detail.modality_code})"
            setUserSelectedDetails(detail.id, Constants.IMAGING,title,detail.started?.toDisplayDate()!!)
            viewModel.getPractitionerOrganizationName(detail.encounterId!!)

            val datTimePair = openCloseTime(detail.started, detail.started)

            viewModel.getConditionByEncounterId(detail.id)
            viewModel.getProcedureDataByEncounterId(detail.id)
            viewModel.getFirstVisitDataByEncounterId(detail.encounterId)

            val performerList: List<Performer> =
                Gson().fromJson(detail.performer, object : TypeToken<ArrayList<Performer?>?>() {}.type)

            binding.apply {
                if (performerList.isNotEmpty()){
                    tvPerformerName.text = performerList[0].display
                }

                tvImagingDate.text = datTimePair.first
                tvImagingTime.text = datTimePair.second

                tvImagingId.text = "#${detail.id}".uppercase()
                tvName.text = title
                tvDescription.text = detail.description
                tvDate.text = detail.started.toDisplayDate()
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

    private fun observeData() {
        viewModel.practitionerData.observe(viewLifecycleOwner) {
            binding.tvPractitionerName.text = it.first
            binding.tvOrganizationName.text = it.second
            binding.tvHospitalName.text = it.second
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

        viewModel.encounterObjectData.observe(viewLifecycleOwner) {
            val statusDetail =
                Utilities.getVisitUIStatus(requireActivity(), it.first.status ?: "")
            binding.rtvVisitStatus.text = it.first.status?.capitalFirstChar()
            binding.rtvVisitStatus.setTextColor(statusDetail.first)
            binding.rtvVisitStatus.delegate.backgroundColor = statusDetail.second
            binding.tvVisitDate.text =getString(R.string.start_date_with_value, it.first.periodStart?.toDisplayDate())
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