package com.mE.Health.feature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.AppointmentDetailFragmentBinding
import com.mE.Health.utility.BottomSheetContactUs
import com.mE.Health.utility.BottomSheetShareData
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AppointmentDetailsFragment : BaseFragment() {

    private lateinit var binding: AppointmentDetailFragmentBinding

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
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderSettingProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.appointment), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        DetailSingleton.appointment?.let { detail ->
            val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)

            val datTimePair = openCloseTime(detail.startTime, detail.endTime)
            val dateTime = "${datTimePair.first},\n${datTimePair.second}"
            binding.apply {
                tvDrName.text = detail.practitionerName
                tvSpeciality.text = detail.practitionerSpecialty
                tvDateTime.text = dateTime
                tvDate.text = datTimePair.first
                tvTime.text = datTimePair.second
                tvReason.text = reasonCodeObject.display
                tvVisitDate.text = detail.createdAt?.toDisplayDate()

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
                    binding.rtvVisitStatus.apply {
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
                    binding.tvEditAppointment.visibility = View.VISIBLE
                    binding.tvCancelAppointment.visibility = View.VISIBLE
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
                    binding.rtvVisitStatus.apply {
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
                    binding.rtvVisitStatus.apply {
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
                    binding.rtvVisitStatus.apply {
                        text = detail.status
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

        binding.llShareButton.llShareData.setOnClickListener {


            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Dummy Text to share"
            )
            startActivity(Intent.createChooser(intent, "Share via"))

//            val list =
//                ArrayList<String>().apply {
//                    add("Dr. Ashley")
//                    add("Dr. Laura kim MD")
//                    add("Dr. Susan Lee MD")
//                    add("Dr. Emily carter MD")
//                    add("Dr. Rajesh Patel")
//                    add("Dr. James o’connor")
//                    add("Dr. Laura kim MD")
//                    add("Dr. Emily carter MD")
//                }
//            val bottomSheet = BottomSheetShareData(list, "strEnquiries")
//            bottomSheet.setOnCompleteListener(object : BottomSheetShareData.OnCompleteListener {
//                override fun onComplete(item: String) {
//                }
//            })
//            bottomSheet.show(
//                requireActivity().supportFragmentManager, "BottomSheetContactUs"
//            )
        }
    }
}