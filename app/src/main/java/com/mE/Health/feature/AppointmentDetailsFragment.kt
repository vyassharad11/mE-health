package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AppointmentDetailFragmentBinding
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.databinding.MyPersonaFragmentBinding
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
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
        binding.toolbar.tvTitle.text = getString(R.string.appointments)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
        val bundle = arguments
        var type = 0
        if (bundle != null) {
            type = bundle.getInt("position", 0)
        }
        if (type == 1 || type == 2) {
            binding.rtvStatus.apply {
                text = "Booked"
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_0063F7))
                delegate.backgroundColor =
                    ContextCompat.getColor(requireActivity(), R.color.color_1A0063F7)
            }

            binding.rtvVisitStatus.apply {
                text = "Booked"
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_0063F7))
                delegate.backgroundColor =
                    ContextCompat.getColor(requireActivity(), R.color.color_1A0063F7)
            }

            binding.tvEditAppointment.visibility = View.VISIBLE
            binding.tvCancelAppointment.visibility = View.VISIBLE
        } else if (type == 3) {
            binding.rtvStatus.apply {
                text = "Cancelled"
                setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_F02C2C))
                delegate.backgroundColor =
                    ContextCompat.getColor(requireActivity(), R.color.color_1AF02C2C)
            }
        }
    }
}