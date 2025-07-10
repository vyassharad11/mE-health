package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.utility.extractContactInfo
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
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.practitioner)
        binding.toolbar.ivCalendar.visibility = View.VISIBLE
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setPractitionerData()
    }

    private fun setPractitionerData() {
        DetailSingleton.practitioner?.let {
            mockViewModel.getOrganizationsByPractitionerId(it.id)
            mockViewModel.getAppointmentsByPractitionerId(it.id)

            binding.tvName.text = it.name
            binding.tvSpeciality.text = it.specialty

            val contactInfo = it.telecom?.extractContactInfo()
            contactInfo?.let { ci ->
                binding.tvPhone.text = ci.phone
                binding.tvEmail.text = ci.email
            }
        }

        mockViewModel.organizationList.observe(viewLifecycleOwner) {
            initView(it)
        }

        mockViewModel.practAppointmentList.observe(viewLifecycleOwner) {
            setAppointmentData(it)
        }

        mockViewModel.practVisitList.observe(viewLifecycleOwner) {
            setVisitData(it)
        }
    }

    private fun initView(itemList: List<PractitionerOrganizationWithDetails>) {
        binding.rvOrganization.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val organizationAdapter = PractitionerDetailOrganizationAdapter(requireActivity())
        organizationAdapter.itemList = itemList
        binding.rvOrganization.adapter = organizationAdapter
    }

    private fun setAppointmentData(list: List<Appointment>?) {
        binding.rvAppointments.layoutManager =
            LinearLayoutManager(requireActivity())
        val adapter = PractitionerAppointmentAdapter(requireActivity())
        adapter.itemList = list
        binding.rvAppointments.adapter = adapter
    }

    private fun setVisitData(list: List<Encounter>?) {
        binding.rvVisits.layoutManager =
            LinearLayoutManager(requireActivity())
        val adapter = PractitionerVisitAdapter(requireActivity())
        adapter.itemList = list
        binding.rvVisits.adapter = adapter
    }
}