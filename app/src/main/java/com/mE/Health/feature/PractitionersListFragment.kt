package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Practitioner
import com.mE.Health.databinding.PractitionersListFragmentBinding
import com.mE.Health.feature.adapter.AppointmentListAdapter
import com.mE.Health.feature.adapter.LabListAdapter
import com.mE.Health.feature.adapter.MedicationListAdapter
import com.mE.Health.feature.adapter.PractitionersListAdapter
import com.mE.Health.feature.adapter.VisitListAdapter
import com.mE.Health.feature.adapter.VitalListAdapter
import com.mE.Health.utility.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PractitionersListFragment : BaseFragment() {

    private lateinit var binding: PractitionersListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = PractitionersListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        val type = arguments?.getString(Constants.PN_TYPE, "")

        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())
        if (type == Constants.PRACTITIONER) {
            binding.tvPageTitle.text = getString(R.string.list_of_practitioners)
            var recyclerAdapter = PractitionersListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.practitionerList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : PractitionersListAdapter.OnClickCallback {
                    override fun onClicked(item: Practitioner, position: Int) {
                        addFragment(
                            R.id.fragment_container,
                            PractitionersListDetailsFragment(),
                            "PractitionersListDetailsFragment",
                            "PractitionersListFragment"
                        )
                    }
                }
            }
        } else if (type == Constants.VITAL) {
            binding.tvPageTitle.text = getString(R.string.list_of_vitals)
            val recyclerAdapter = VitalListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.vitalsList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : VitalListAdapter.OnClickCallback {
                    override fun onClicked(item: Observation, position: Int) {
                    }
                }
            }
        } else if (type == Constants.LAB) {
            binding.tvPageTitle.text = getString(R.string.list_of_labs)
            val recyclerAdapter = LabListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.labList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : LabListAdapter.OnClickCallback {
                    override fun onClicked(item: DiagnosticReport, position: Int) {
                    }
                }
            }
        } else if (type == Constants.VISIT) {
            binding.tvPageTitle.text = getString(R.string.list_of_visits)
            val recyclerAdapter = VisitListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.visitList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : VisitListAdapter.OnClickCallback {
                    override fun onClicked(item: Encounter, position: Int) {
                    }
                }
            }
        } else if (type == Constants.APPOINTMENT) {
            binding.tvPageTitle.text = getString(R.string.list_of_appointments)
            val recyclerAdapter = AppointmentListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.appointmentList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : AppointmentListAdapter.OnClickCallback {
                    override fun onClicked(item: Appointment, position: Int) {
                    }
                }
            }
        } else {
            binding.tvPageTitle.text = getString(R.string.list_of_medications)
            var recyclerAdapter = MedicationListAdapter(requireActivity())
            recyclerAdapter.itemList = mockViewModel.medicationList.value
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : MedicationListAdapter.OnClickCallback {
                    override fun onClicked(item: MedicationRequest, position: Int) {
                        addFragment(
                            R.id.fragment_container,
                            PractitionersListDetailsFragment(),
                            "PractitionersListDetailsFragment",
                            "PractitionersListFragment"
                        )
                    }
                }
            }
        }
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.condition)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }
}