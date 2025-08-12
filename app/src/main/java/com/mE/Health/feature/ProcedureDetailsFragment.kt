package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Procedure
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.ProcedureDetailFragmentBinding
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
class ProcedureDetailsFragment : BaseFragment() {

    private lateinit var binding: ProcedureDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

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
        observeData()
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.procedure),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.procedure?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.id, Constants.PROCEDURES,detail.code_display!!,detail.performedDate?.toDisplayDate()!!)
            viewModel.getFirstVisitDataByEncounterId(detail.encounterId!!)
            binding.apply {
                generateShareMessage(detail)
                tvName.text = detail.code_display
                tvProcedureDate.text = detail.performedDate.toDisplayDate()

                Utilities.getProcedureUIStatus(requireActivity(), detail.status ?: "").let {
                    tvStatus.text = detail.status?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
                val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)

                tvProcedureId.text = "#${reasonCodeObject.code}"
                tvReason.text = reasonCodeObject.display
            }
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }

    private fun observeData() {
        viewModel.encounterObjectData.observe(viewLifecycleOwner) {
            val statusDetail =
                Utilities.getVisitUIStatus(requireActivity(), it.first.status ?: "")
            binding.rtvVisitStatus.text = it.first.status?.capitalFirstChar()
            binding.rtvVisitStatus.setTextColor(statusDetail.first)
            binding.rtvVisitStatus.delegate.backgroundColor = statusDetail.second
            binding.tvVisitDate.text =getString(R.string.start_date_with_value, it.first.periodStart?.toDisplayDate())
        }
    }

    private fun generateShareMessage(detail: Procedure) {
        val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)
        shareMessage =
            "Here is my medical Procedure information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.code_display}\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "${detail.performedDate?.toDisplayDate()}\n" +
                    "\n" +
                    "Procedure Details\n" +
                    "Procedure ID : #${reasonCodeObject.code}\n" +
                    "Reason        : ${reasonCodeObject.display}\n" +
                    "Visits Status : Active\n" +
                    "Recorded Date: 06/11/2025\n" +
                    "\n" +
                    "\n" +
                    "Thank You!!"
    }
}