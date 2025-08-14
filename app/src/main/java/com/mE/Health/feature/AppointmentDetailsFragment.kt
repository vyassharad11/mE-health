package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.AppointmentDetailFragmentBinding
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
class AppointmentDetailsFragment : BaseFragment() {

    private lateinit var binding: AppointmentDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AppointmentDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderTitleProperties(getString(R.string.appointment), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        DetailSingleton.appointment?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.id,Constants.APPOINTMENTS,detail.practitionerName!!,detail.createdAt?.toDisplayDate()!!)
            val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)

            val datTimePair = openCloseTime(detail.startTime, detail.endTime)
            val dateTime = "${datTimePair.first},\n${datTimePair.second}"
            generateShareMessage(detail)
            binding.apply {
                viewModel.getFirstVisitDataByEncounterId(detail.encounterId!!)
                tvDrName.text = detail.practitionerName
                tvId.text = "Id: "+detail.id
                tvDateTime.text = dateTime
                tvDate.text = datTimePair.first
                tvTime.text = datTimePair.second
                tvReason.text = reasonCodeObject.display

                if (detail.status?.lowercase() == getString(R.string.booked).lowercase()) {
                    binding.rtvStatus.apply {
                        text = getString(R.string.booked)
                        setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.color_0063F7
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.color_1A0063F7)
                    }
                    binding.tvEditAppointment.visibility = View.GONE
                    binding.tvCancelAppointment.visibility = View.GONE
                } else if (detail.status?.lowercase() == getString(R.string.completed).lowercase()) {
                    binding.rtvStatus.apply {
                        text = getString(R.string.completed)
                        setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.color_06C270
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.color_A06C270)
                    }
                } else if (detail.status?.lowercase() == getString(R.string.cancelled).lowercase()) {
                    binding.rtvStatus.apply {
                        text = getString(R.string.cancelled)
                        setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.color_F02C2C
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.color_1AF02C2C)
                    }
                } else {
                    binding.rtvStatus.apply {
                        text = detail.status?.capitalFirstChar()
                        setTextColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.color_06C270
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(requireActivity(), R.color.color_A06C270)
                    }
                }
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
            binding.tvVisitDate.text =
                getString(R.string.start_date_with_value, it.first.periodStart?.toDisplayDate())
            binding.tvSpeciality.text = it.second
        }
    }

    private fun generateShareMessage(detail: Appointment){
        val datTimePair = openCloseTime(detail.startTime, detail.endTime)
        val dateTime = "${datTimePair.first}\n${datTimePair.second}"
        val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)
        shareMessage = "Here is my medical Appointment information from mEinstein I had to share! You have to try mE!\n" +
                "https://bit.ly/4ipzMmF\n" +
                "\n" +
                "Annual well visit\n" +
                "$dateTime\n" +
                "Status: ${detail.status?.capitalFirstChar()}\n" +
                "${ detail.practitionerName}\n" +
                "Annual well visit\n" +
                "Reason\n" +
                "${reasonCodeObject.display}\n" +
                "Visits Status\n" +
                "Start Date: ${detail.createdAt?.toDisplayDate()}\n" +
                "Status: ${detail.status?.capitalFirstChar()}\n" +
                "\n" +
                "\n" +
                "Thank You!"
    }
}