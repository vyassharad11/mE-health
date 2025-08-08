package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.utility.Constants
import com.mE.Health.utility.extractContactInfo
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PractitionerDetailsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: PractitionerDetailsFragmentBinding
    private var appointmentList: ArrayList<Appointment>? = ArrayList()
    private var visitList: ArrayList<Encounter>? = ArrayList()

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
        setPractitionerData()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.practitioner), binding.toolbar.tvTitle, true)

        binding.toolbar.ivCalendar.visibility = View.VISIBLE
        binding.toolbar.ivCalendar.setOnClickListener {
            binding.rlDate.visibility = if (binding.rlDate.isVisible) View.GONE else View.VISIBLE
        }
    }

    private fun setPractitionerData() {
        DetailSingleton.practitioner?.let {
            setUserSaveFileData(
                it.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(
                it.id,
                Constants.PRACTITIONERS,
                it.name!!,
                it.createdAt?.toDisplayDate()!!
            )

            mockViewModel.getOrganizationsByPractitionerId(it.id)
            mockViewModel.getAppointmentsByPractitionerId(it.id)

            binding.tvName.text = it.name
            binding.tvSpeciality.text = it.specialty

            val contactInfo = it.telecom?.extractContactInfo()
            contactInfo?.let { ci ->
                binding.tvPhone.text = ci.phone
                binding.tvEmail.text = ci.email
            }
            binding.tvStartDate.text = it.createdAt?.toDisplayDate()
            binding.tvEndDate.text = it.createdAt?.toDisplayDate()
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

        binding.tvAppointmentViewAll.setOnClickListener(this)
        binding.tvVisitViewAll.setOnClickListener(this)
        binding.rllMail.setOnClickListener(this)
        binding.rllCall.setOnClickListener(this)
        binding.llUpload.setOnClickListener(this)
    }

    private fun setAppointmentData(list: List<Appointment>?) {
        if (!list.isNullOrEmpty() && list.size > 0) {
            appointmentList = ArrayList()
            appointmentList?.addAll(list)
            binding.tvAppointmentViewAll.isVisible = appointmentList!!.size > 2
            binding.rvAppointments.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = PractitionerAppointmentAdapter(requireActivity())
            adapter.itemList = appointmentList
            binding.rvAppointments.adapter = adapter
        } else {
            binding.rlAppointment.isVisible = false
        }
    }

    private fun setVisitData(list: List<Encounter>?) {
        if (!list.isNullOrEmpty()) {
            visitList = ArrayList()
            visitList?.addAll(list)
            binding.tvVisitViewAll.isVisible = visitList!!.size > 2
            binding.rvVisits.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = PractitionerVisitAdapter(requireActivity())
            adapter.itemList =
                if (list.isNotEmpty() && list.size > 2) list.subList(0, 2) else list
            binding.rvVisits.adapter = adapter
        } else {
            binding.rlVisits.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvAppointmentViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.APPOINTMENTS)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(appointmentList))
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "PractitionerDetailsFragment"
                )
            }

            R.id.tvVisitViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.VISITS)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(visitList))
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "PractitionerDetailsFragment"
                )
            }

            R.id.rllMail -> {
                val contactInfo = DetailSingleton.practitioner?.telecom?.extractContactInfo()
                sendEmail(
                    contactInfo?.email!!,
                    "Practitioner Email : ${DetailSingleton.practitioner?.specialty}"
                )
            }

            R.id.rllCall -> {
                val contactInfo = DetailSingleton.practitioner?.telecom?.extractContactInfo()
                openDialPadWithNumber(contactInfo?.phone!!)
            }

            R.id.llUpload -> {
            }
        }
    }
}