package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.databinding.ConditionDetailFragmentBinding
import com.mE.Health.feature.adapter.ConditionLabAdapter
import com.mE.Health.feature.adapter.ConditionMedicationAdapter
import com.mE.Health.feature.adapter.ConditionVisitAdapter
import com.mE.Health.feature.adapter.ConditionVitalAdapter
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
class ConditionDetailsFragment : BaseFragment(), OnClickListener {

    private lateinit var binding: ConditionDetailFragmentBinding
    private val viewModel: ConditionDataViewModel by viewModels()
    private var vitalList: ArrayList<Observation>? = ArrayList()
    private var labList: ArrayList<DiagnosticReport>? = ArrayList()
    private var visitList: ArrayList<Encounter>? = ArrayList()
    private var medicationList: ArrayList<MedicationRequest>? = ArrayList()
    private var organizationName: String = ""
    private val minimumListCount = 2
    private var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConditionDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
        observeData()
        setData()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting, true)
        setHeaderTitleProperties(getString(R.string.condition), binding.toolbar.tvTitle, true)
    }

    private fun initView() {
        binding.tvPractitionersViewAll.setOnClickListener(this)
        binding.tvMedicationViewAll.setOnClickListener(this)
        binding.tvVitalViewAll.setOnClickListener(this)
        binding.tvLabsViewAll.setOnClickListener(this)
        binding.tvVisitsViewAll.setOnClickListener(this)
        binding.layoutSyncButton.llShareData.setOnClickListener(this)
    }

    private fun setData() {
        DetailSingleton.condition?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(
                detail.id,
                Constants.CONDITIONS,
                detail.code_display!!,
                detail.recordedDate?.toDisplayDate()!!
            )
            viewModel.getPractitionerOrganizationName(detail.encounterId!!)
            viewModel.getObservationMapByEncounterId(detail.encounterId)
            viewModel.getLabsDataByEncounterId(detail.encounterId)
            viewModel.getVisitDataByEncounterId(detail.encounterId)
            viewModel.getMedicationByEncounterId(detail.encounterId)
            binding.apply {
                tvName.text = detail.code_display
                Utilities.getConditionUIStatus(requireActivity(), detail.clinicalStatus ?: "").let {
                    tvStatus.text = detail.clinicalStatus?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
                tvOnsetDate.text = detail.onsetDate?.toDisplayDate()
                tvRecordedDate.text = detail.recordedDate.toDisplayDate()
                tvConditionId.text = "#"+detail.id.uppercase()
            }
            generateShareMessage(detail)
        }
    }

    private fun observeData() {
        viewModel.practitionerData.observe(viewLifecycleOwner) {
            setPractitionerData(it)
        }
        viewModel.observationData.observe(viewLifecycleOwner) {
            setVitalData(it)
        }
        viewModel.diagnosticReportData.observe(viewLifecycleOwner) {
            setLabData(it)
        }
        viewModel.encounterData.observe(viewLifecycleOwner) {
            setVisitData(it)
        }
        viewModel.medicationData.observe(viewLifecycleOwner) {
            setMedicationData(it)
        }
    }

    private fun setPractitionerData(data: Pair<String, String>) {
        binding.tvPractitionerName.text = data.first
        binding.tvOrganizationName.text = data.second
    }

    private fun setVitalData(vitalData: Pair<String, List<Observation>>) {
        vitalList = ArrayList()
        if (vitalData.second.isNotEmpty()) {
            organizationName = vitalData.first
            vitalList!!.addAll(vitalData.second)
            binding.tvVitalViewAll.isVisible = vitalList!!.size > minimumListCount
            binding.rvVitals.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = ConditionVitalAdapter(requireActivity(), organizationName)
            adapter.itemList = if (vitalList!!.size > minimumListCount) vitalList?.subList(0, minimumListCount) else vitalList
            binding.rvVitals.adapter = adapter
        } else {
            binding.rlVitalLayout.visibility = GONE
        }
    }

    private fun setLabData(list: List<DiagnosticReport>) {
        if (list.isNotEmpty()) {
            labList = ArrayList()
            labList?.addAll(list)
            binding.tvLabsViewAll.isVisible = labList!!.size > minimumListCount
            binding.rvLabs.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = ConditionLabAdapter(requireActivity())
            adapter.itemList = if (labList!!.size > minimumListCount) labList?.subList(0, minimumListCount) else labList
            binding.rvLabs.adapter = adapter
        } else {
            binding.rlLabsLayout.visibility = GONE
        }
    }

    private fun setVisitData(list: List<Encounter>) {
        if (list.isNotEmpty()) {
            visitList = ArrayList()
            visitList?.addAll(list)
            binding.tvVisitsViewAll.isVisible = visitList!!.size > minimumListCount
            binding.rvVisits.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = ConditionVisitAdapter(requireActivity())
            adapter.itemList = if (visitList!!.size > minimumListCount) visitList?.subList(0, minimumListCount) else visitList
            binding.rvVisits.adapter = adapter
        } else {
            binding.rlVisitsLayout.visibility = GONE
        }
    }

    private fun setMedicationData(list: List<MedicationRequest>) {
        if (list.isNotEmpty()) {
            medicationList = ArrayList()
            medicationList?.addAll(list)
            binding.tvMedicationViewAll.isVisible = medicationList!!.size > minimumListCount
            binding.rvMedication.layoutManager =
                LinearLayoutManager(requireActivity())
            val adapter = ConditionMedicationAdapter(requireActivity())
            adapter.itemList = if (medicationList!!.size > minimumListCount) medicationList?.subList(0, minimumListCount) else medicationList
            binding.rvMedication.adapter = adapter
        } else {
            binding.rlMedicationsLayout.visibility = GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvPractitionersViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.PRACTITIONERS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }

            R.id.tvMedicationViewAll -> {
                bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.MEDICATIONS)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(medicationList))
                goToNext()
            }

            R.id.tvVitalViewAll -> {
                bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.VITALS)
                bundle.putString(Constants.PN_ORGANIZATION_NAME, organizationName)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(vitalList))
                goToNext()
            }

            R.id.tvLabsViewAll -> {
                bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.LABS)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(labList))
                goToNext()
            }

            R.id.tvVisitsViewAll -> {
                bundle = Bundle()
                bundle.putString(Constants.PN_TYPE, Constants.CONDITION_VISIT)
                bundle.putString(Constants.PN_CUSTOM_LIST, Gson().toJson(visitList))
                goToNext()
            }

            R.id.llShareData -> {
                shareRecord(message = shareMessage)
            }
        }
    }

    private fun goToNext(){
        val fragment = PractitionersListFragment()
        fragment.arguments = bundle
        addFragment(
            R.id.fragment_container,
            fragment,
            "PractitionersListFragment",
            "ConditionDetailsFragment"
        )
    }

    private fun generateShareMessage(detail: Condition) {
        shareMessage =
            "Here is my medical Condition information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "${detail.code_display}\n" +
                    "Status: ${detail.clinicalStatus?.capitalFirstChar()}\n" +
                    "Onset : ${detail.onsetDate?.toDisplayDate()}\n" +
                    "Recorded : ${detail.recordedDate?.toDisplayDate()}\n" +
                    "Category     : Chronic\n" +
                    "Condition ID : #HYP2022105\n" +
                    "\n" +
                    "Thank You!"
    }
}