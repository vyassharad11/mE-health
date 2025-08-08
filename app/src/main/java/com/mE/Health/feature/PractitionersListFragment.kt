package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Practitioner
import com.mE.Health.databinding.PractitionersListFragmentBinding
import com.mE.Health.feature.adapter.ConditionLabAdapter
import com.mE.Health.feature.adapter.ConditionMedicationAdapter
import com.mE.Health.feature.adapter.ConditionVisitAdapter
import com.mE.Health.feature.adapter.ConditionVitalAdapter
import com.mE.Health.feature.adapter.MedicationListAdapter
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.feature.adapter.PractitionersListAdapter
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

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
//        setHeaderSettingProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.back), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        val type = arguments?.getString(Constants.PN_TYPE, "")

        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())
        if (type == Constants.PRACTITIONERS) {
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
        } else if (type == Constants.VITALS) {

            binding.tvPageTitle.text = getString(R.string.list_of_vitals)
            val organizationName = arguments?.getString(Constants.PN_ORGANIZATION_NAME, "")
            val jsonList = arguments?.getString(Constants.PN_CUSTOM_LIST)
            val vitalList: List<Observation> =
                Gson().fromJson(jsonList, object : TypeToken<ArrayList<Observation?>?>() {}.type)
            val recyclerAdapter = ConditionVitalAdapter(requireActivity(), organizationName)
            recyclerAdapter.itemList = vitalList
            binding.rvAssist.adapter = recyclerAdapter
        } else if (type == Constants.LABS) {

            binding.tvPageTitle.text = getString(R.string.list_of_labs)
            val jsonList = arguments?.getString(Constants.PN_CUSTOM_LIST)
            val labList: List<DiagnosticReport> =
                Gson().fromJson(jsonList, object : TypeToken<ArrayList<DiagnosticReport?>?>() {}.type)
            val recyclerAdapter = ConditionLabAdapter(requireActivity())
            recyclerAdapter.itemList = labList
            binding.rvAssist.adapter = recyclerAdapter
        } else if (type == Constants.VISITS) {

            binding.tvPageTitle.text = getString(R.string.list_of_visits)
            if (arguments != null) {
                val jsonList = requireArguments().getString(Constants.PN_CUSTOM_LIST)
                val visitList: List<Encounter> = Gson().fromJson(
                    jsonList,
                    object : TypeToken<ArrayList<Encounter?>?>() {}.type
                )
                val recyclerAdapter = PractitionerVisitAdapter(requireActivity())
                recyclerAdapter.itemList = visitList
                binding.rvAssist.adapter = recyclerAdapter
            }
        } else if (type == Constants.CONDITION_VISIT) {

            binding.tvPageTitle.text = getString(R.string.list_of_visits)
            if (arguments != null) {
                val jsonList = requireArguments().getString(Constants.PN_CUSTOM_LIST)
                val visitList: List<Encounter> = Gson().fromJson(
                    jsonList,
                    object : TypeToken<ArrayList<Encounter?>?>() {}.type
                )
                val recyclerAdapter = ConditionVisitAdapter(requireActivity())
                recyclerAdapter.itemList = visitList
                binding.rvAssist.adapter = recyclerAdapter
            }
        } else if (type == Constants.APPOINTMENTS) {
            if (arguments != null) {
                val jsonList = requireArguments().getString(Constants.PN_CUSTOM_LIST)
                val appointmentList: List<Appointment> = Gson().fromJson(
                    jsonList,
                    object : TypeToken<ArrayList<Appointment?>?>() {}.type
                )
                binding.tvPageTitle.text = getString(R.string.list_of_appointments)
                val recyclerAdapter = PractitionerAppointmentAdapter(requireActivity())
                recyclerAdapter.itemList = appointmentList
                binding.rvAssist.adapter = recyclerAdapter
                recyclerAdapter.apply {
                    onItemClickListener = object : PractitionerAppointmentAdapter.OnClickCallback {
                        override fun onClicked(item: Appointment, position: Int) {
                        }
                    }
                }
            }
        }  else if (type == Constants.MEDICATIONS) {
            if (arguments != null) {

                binding.tvPageTitle.text = getString(R.string.list_of_medications)
                val jsonList = requireArguments().getString(Constants.PN_CUSTOM_LIST)
                val medicationList: List<MedicationRequest> = Gson().fromJson(
                    jsonList,
                    object : TypeToken<ArrayList<MedicationRequest?>?>() {}.type
                )
                val recyclerAdapter = ConditionMedicationAdapter(requireActivity())
                recyclerAdapter.itemList = medicationList
                binding.rvAssist.adapter = recyclerAdapter
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
}