package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
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
class PractitionerDetailsFragment : BaseFragment() {

    private lateinit var binding: PractitionerDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PractitionerDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.practitioner)
        binding.toolbar.ivCalendar.visibility = View.VISIBLE
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        binding.rvOrganization.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val organizationAdapter = PractitionerDetailOrganizationAdapter(requireActivity())
        binding.rvOrganization.adapter = organizationAdapter

        setAppointmentData()
        setVisitData()
    }

    private fun setAppointmentData(){
        binding.rvAppointments.layoutManager =
                LinearLayoutManager(requireActivity())
        val adapter = PractitionerAppointmentAdapter(requireActivity())
        binding.rvAppointments.adapter = adapter
    }

    private fun setVisitData(){
        binding.rvVisits.layoutManager =
                LinearLayoutManager(requireActivity())
        val adapter = PractitionerVisitAdapter(requireActivity())
        binding.rvVisits.adapter = adapter
    }
}