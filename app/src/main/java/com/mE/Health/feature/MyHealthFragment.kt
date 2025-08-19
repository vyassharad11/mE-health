package com.mE.Health.feature

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Appointment
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.Imaging
import com.mE.Health.data.model.Immunization
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.Procedure
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.databinding.MyHealthFragmentBinding
import com.mE.Health.feature.adapter.ActionType
import com.mE.Health.feature.adapter.ClickState
import com.mE.Health.feature.adapter.MyHealthAllergiesAdapter
import com.mE.Health.feature.adapter.MyHealthAppointmentAdapter
import com.mE.Health.feature.adapter.MyHealthBillingsAdapter
import com.mE.Health.feature.adapter.MyHealthConditionAdapter
import com.mE.Health.feature.adapter.MyHealthFilterAdapter
import com.mE.Health.feature.adapter.MyHealthImagingAdapter
import com.mE.Health.feature.adapter.MyHealthImmunizationAdapter
import com.mE.Health.feature.adapter.MyHealthLabAdapter
import com.mE.Health.feature.adapter.MyHealthMedicationAdapter
import com.mE.Health.feature.adapter.MyHealthPractitionerAdapter
import com.mE.Health.feature.adapter.MyHealthProcedureAdapter
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.feature.adapter.MyHealthUploadDocAdapter
import com.mE.Health.feature.adapter.MyHealthVisitsAdapter
import com.mE.Health.feature.adapter.MyHealthVitalAdapter
import com.mE.Health.feature.adapter.TYPE
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Constants.ALLERGIES
import com.mE.Health.utility.Constants.APPOINTMENTS
import com.mE.Health.utility.Constants.BILLING
import com.mE.Health.utility.Constants.CONDITIONS
import com.mE.Health.utility.Constants.IMAGING
import com.mE.Health.utility.Constants.IMMUNIZATIONS
import com.mE.Health.utility.Constants.LABS
import com.mE.Health.utility.Constants.MEDICATIONS
import com.mE.Health.utility.Constants.PRACTITIONERS
import com.mE.Health.utility.Constants.PROCEDURES
import com.mE.Health.utility.Constants.RECORD_VAULT
import com.mE.Health.utility.Constants.VISITS
import com.mE.Health.utility.Constants.VITALS
import com.mE.Health.utility.FilterItem
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.extractContactInfo
import com.mE.Health.utility.getCalendarFromString
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.utility.toFormateCalendar
import com.mE.Health.viewmodels.FileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MyHealthFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: MyHealthFragmentBinding
    private var filterList = ArrayList<FilterItem>()
    private var firstDateSelected: Long = 0
    private var secondDateSelected: Long = 0
    private var myHealthTypeAdapter: MyHealthTypeAdapter? = null
    private var practitionerList: List<Practitioner>? = ArrayList()
    private var appointmentList: List<Appointment>? = ArrayList()
    private var conditionList: List<Condition>? = ArrayList()
    private var labList: List<DiagnosticReport>? = ArrayList()
    private var vitalsList: List<Observation>? = ArrayList()
    private var medicationList: List<MedicationRequest>? = ArrayList()
    private var visitList: List<Encounter>? = ArrayList()
    private var procedureList: List<Procedure>? = ArrayList()
    private var allergyList: List<AllergyIntolerance>? = ArrayList()
    private var immunizationList: List<Immunization>? = ArrayList()
    private var billingList: List<Claim>? = ArrayList()
    private var imagingList: List<Imaging>? = ArrayList()
    private var recordVaultList: ArrayList<UserSavedFile>? = ArrayList()
    private var practitionerAdapter: MyHealthPractitionerAdapter? = null
    private var appointmentAdapter: MyHealthAppointmentAdapter? = null
    private var conditionAdapter: MyHealthConditionAdapter? = null
    private var labAdapter: MyHealthLabAdapter? = null
    private var vitalAdapter: MyHealthVitalAdapter? = null
    private var medicationAdapter: MyHealthMedicationAdapter? = null
    private var visitsAdapter: MyHealthVisitsAdapter? = null
    private var procedureAdapter: MyHealthProcedureAdapter? = null
    private var allergiesAdapter: MyHealthAllergiesAdapter? = null
    private var immunizationAdapter: MyHealthImmunizationAdapter? = null
    private var billingsAdapter: MyHealthBillingsAdapter? = null
    private var imagingAdapter: MyHealthImagingAdapter? = null
    private var recordVaultAdapter: MyHealthUploadDocAdapter? = null
    private val recordVaultViewModel: FileViewModel by viewModels()
    private var imagingOrganizationMap = HashMap<String, String>()
    private var appointmentOrganizationMap = HashMap<String, String>()
    private var immunizationMap = HashMap<String, String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = MyHealthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        firstDateSelected = Calendar.getInstance().timeInMillis
        secondDateSelected = Calendar.getInstance().timeInMillis
        initItemList()
        initView()
        initHeader()
        addTextChangedListener()
        getStatusFilterList()
        observeRecordVaultResponse()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {
            openSetting(requireActivity())
        }
    }

    private fun getTileSelectedType(): String? {
        return myHealthTypeAdapter?.getSelectedHealthType()
    }

    private fun initView() {
        setPractitionerData()
        binding.rvType.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        myHealthTypeAdapter =
            MyHealthTypeAdapter(requireActivity(), Utilities.getAllMyHealthType(mockViewModel))
        binding.rvType.adapter = myHealthTypeAdapter
        myHealthTypeAdapter?.apply {
            onItemClickListener = object : MyHealthTypeAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    if (myHealthTypeAdapter?.selectedItem == position) return
                    myHealthTypeAdapter?.selectedItem = position
                    myHealthTypeAdapter?.notifyDataSetChanged()
                    initFilterUI()
                    hideKeyboard()
                    when (getTileSelectedType()) {
                        PRACTITIONERS -> {
                            binding.ivFilter.visibility = View.GONE
                            setPractitionerData()
                        }

                        APPOINTMENTS -> {
                            setAppointmentData()
                        }

                        CONDITIONS -> {
                            setConditionData()
                        }

                        LABS -> {
                            setLabData()
                        }

                        VITALS -> {
                            setVitalData()
                        }

                        MEDICATIONS -> {
                            setMedicationData()
                        }

                        VISITS -> {
                            setVisitsData()
                        }

                        PROCEDURES -> {
                            setProceduresData()
                        }

                        ALLERGIES -> {
                            setAllergiesData()
                        }

                        IMMUNIZATIONS -> {
                            setImmunizationData()
                        }

                        BILLING -> {
                            setBillingData()
                        }

                        IMAGING -> {
                            setImagingData()
                        }

                        RECORD_VAULT -> {
                            setUploadDocumentData()
                        }
                    }
                    setNoRecordData()
                }
            }
        }

        binding.ivSearch.setOnClickListener(this)
        binding.ivSearchCross.setOnClickListener(this)
        binding.ivFilter.setOnClickListener(this)
        binding.ivCalendarFilter.setOnClickListener(this)
        binding.ivDateCancel.setOnClickListener(this)
        binding.ivFileUpload.setOnClickListener(this)
        binding.rllUpload.setOnClickListener(this)
        binding.cvStartData.setOnClickListener(this)
        binding.cvEndData.setOnClickListener(this)
    }

    private fun initItemList() {
        practitionerList = ArrayList()
        practitionerList = mockViewModel.practitionerList.value

        appointmentList = ArrayList()
        appointmentList = mockViewModel.appointmentList.value

        conditionList = ArrayList()
        conditionList = mockViewModel.conditionList.value

        labList = ArrayList()
        labList = mockViewModel.labList.value

        vitalsList = ArrayList()
        vitalsList = mockViewModel.vitalsList.value

        medicationList = ArrayList()
        medicationList = mockViewModel.medicationList.value

        visitList = ArrayList()
        visitList = mockViewModel.visitList.value

        procedureList = ArrayList()
        procedureList = mockViewModel.procedureList.value

        allergyList = ArrayList()
        allergyList = mockViewModel.allergyList.value

        immunizationList = ArrayList()
        immunizationList = mockViewModel.immunizationList.value

        billingList = ArrayList()
        billingList = mockViewModel.claimList.value

        imagingList = ArrayList()
        imagingList = mockViewModel.imagingList.value

        recordVaultViewModel.getOrganizationNameByEncounterId(imagingList!!,appointmentList!!)
        recordVaultViewModel.getPatientDetail(immunizationList!!)
    }

    private fun addTextChangedListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChangedListener(char)
            }
        })
    }

    private fun onTextChangedListener(char: CharSequence?) {
        when (getTileSelectedType()) {
            PRACTITIONERS -> {
                filterPractitionerList(char?.toString()!!)
            }

            APPOINTMENTS -> {
                filterAppointmentList(char?.toString()!!)
            }

            CONDITIONS -> {
                filterConditionList(char?.toString()!!)
            }

            LABS -> {
                filterLabList(char?.toString()!!)
            }

            VITALS -> {
                filterVitalList(char?.toString()!!)
            }

            MEDICATIONS -> {
                filterMedicationList(char?.toString()!!)
            }

            VISITS -> {
                filterVisitList(char?.toString()!!)
            }

            PROCEDURES -> {
                filterProcedureList(char?.toString()!!)
            }

            ALLERGIES -> {
                filterAllergyList(char?.toString()!!)
            }

            IMMUNIZATIONS -> {
                filterImmunizationList(char?.toString()!!)
            }

            BILLING -> {
                filterBillingList(char?.toString()!!)
            }

            IMAGING -> {
                filterImagingList(char?.toString()!!)
            }

           RECORD_VAULT -> {
               filterRecordVaultList(char?.toString()!!)
            }
        }
        setNoRecordData()
    }

    private fun filterPractitionerList(char: String) {
        (if (char.trim().isNotEmpty()) practitionerList?.filter { item ->
            item.name?.lowercase()!!.contains(char.lowercase())
        } else practitionerList!!)?.let {
            practitionerAdapter?.updateList(
                it
            )
        }
    }

    private fun filterAppointmentList(char: String) {
        (if (char.trim().isNotEmpty()) appointmentList?.filter { item ->
            item.practitionerName?.lowercase()!!.contains(char.lowercase())
        } else appointmentList!!)?.let {
            appointmentAdapter?.updateList(
                it
            )
        }
    }

    private fun filterConditionList(char: String) {
        (if (char.trim().isNotEmpty()) conditionList?.filter { item ->
            item.code_display?.lowercase()!!.contains(char.lowercase())
        } else conditionList!!)?.let {
            conditionAdapter?.updateList(
                it
            )
        }
    }

    private fun filterLabList(char: String) {
        (if (char.trim().isNotEmpty()) labList?.filter { item ->
            item.code_display?.lowercase()!!.contains(char.lowercase())
        } else labList!!)?.let {
            labAdapter?.updateList(
                it
            )
        }
    }

    private fun filterVitalList(char: String) {
        (if (char.trim().isNotEmpty()) vitalsList?.filter { item ->
            item.code_display?.lowercase()!!.contains(char.lowercase())
        } else vitalsList!!)?.let {
            vitalAdapter?.updateList(
                it
            )
        }
    }

    private fun filterMedicationList(char: String) {
        (if (char.trim().isNotEmpty()) medicationList?.filter { item ->
            item.medicationCode_display?.lowercase()!!.contains(char.lowercase())
        } else medicationList!!)?.let {
            medicationAdapter?.updateList(
                it
            )
        }
    }

    private fun filterVisitList(char: String) {
        (if (char.trim().isNotEmpty()) visitList?.filter { item ->
            item.type_display?.lowercase()!!.contains(char.lowercase())
        } else visitList!!)?.let {
            visitsAdapter?.updateList(
                it
            )
        }
    }

    private fun filterProcedureList(char: String) {
        (if (char.trim().isNotEmpty()) procedureList?.filter { item ->
            item.code_display?.lowercase()!!.contains(char.lowercase())
        } else procedureList!!)?.let {
            procedureAdapter?.updateList(
                it
            )
        }
    }

    private fun filterAllergyList(char: String) {
        (if (char.trim().isNotEmpty()) allergyList?.filter { item ->
            item.code_display?.lowercase()!!.contains(char.lowercase())
        } else allergyList!!)?.let {
            allergiesAdapter?.updateList(
                it
            )
        }
    }

    private fun filterImmunizationList(char: String) {
        (if (char.trim().isNotEmpty()) immunizationList?.filter { item ->
            item.vaccineCode_display?.lowercase()!!.contains(char.lowercase())
        } else immunizationList!!)?.let {
            immunizationAdapter?.updateList(
                it
            )
        }
    }

    private fun filterBillingList(char: String) {
        (if (char.trim().isNotEmpty()) billingList?.filter { item ->
            item.name?.lowercase()!!.contains(char.lowercase())
        } else billingList!!)?.let {
            billingsAdapter?.updateList(
                it
            )
        }
    }

    private fun filterImagingList(char: String) {
        (if (char.trim().isNotEmpty()) imagingList?.filter { item ->
            item.modality_display?.lowercase()!!.contains(char.lowercase())
        } else imagingList!!)?.let {
            imagingAdapter?.updateList(
                it
            )
        }
    }

    private fun filterRecordVaultList(char: String) {
        (if (char.trim().isNotEmpty()) recordVaultList?.filter { item ->
            item.file_name.lowercase().contains(char.lowercase()) || item.category.lowercase().contains(char.lowercase())
        } else recordVaultList!!)?.let {
            recordVaultAdapter?.updateList(
                it
            )
        }
    }

    private fun initFilterUI() {
        binding.ivFilter.visibility = View.VISIBLE
        binding.rlDateLayout.visibility = View.GONE
        binding.rvFilter.visibility = View.GONE
        binding.rlSearchLayout.visibility = View.GONE
        binding.etSearch.setText("")
        binding.ivCalendarFilter.setColorFilter(
            ContextCompat.getColor(
                requireActivity(), R.color.text_color_primary
            )
        )
        binding.ivFilter.setColorFilter(
            ContextCompat.getColor(
                requireActivity(), R.color.text_color_primary
            )
        )
        binding.ivSearch.setColorFilter(
            ContextCompat.getColor(
                requireActivity(), R.color.text_color_primary
            )
        )
        binding.tvFilterStartDate.text = getString(R.string.dd_mm_yyyy)
        binding.tvFilterEndDate.text = getString(R.string.dd_mm_yyyy)
        firstDateSelected = Calendar.getInstance().timeInMillis
        secondDateSelected = Calendar.getInstance().timeInMillis
        startDate = ""
        endDate = ""
        binding.rlDateCalendarLayout.visibility = View.GONE

        filterStartDateCalendar = Calendar.getInstance()

        getStatusFilterList()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> {
                binding.rlSearchLayout.visibility = View.VISIBLE
                binding.etSearch.post {
                    binding.etSearch.requestFocus()
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm!!.showSoftInput(
                        binding.etSearch,
                        InputMethodManager.SHOW_IMPLICIT
                    )
                }

                binding.ivSearch.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(), R.color.color_FF6605
                    )
                )
            }

            R.id.ivSearchCross -> {
                binding.etSearch.setText("")
                hideKeyboard()
                binding.rlSearchLayout.visibility = View.GONE
                binding.ivSearch.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(), R.color.text_color_primary
                    )
                )
            }

            R.id.ivDateCancel -> {
                setCalendarFilterVisibility()
                binding.tvFilterStartDate.text = getString(R.string.dd_mm_yyyy)
                binding.tvFilterEndDate.text = getString(R.string.dd_mm_yyyy)
                binding.rlDateLayout.visibility = View.GONE
                binding.ivCalendarFilter.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(), R.color.text_color_primary
                    )
                )
                filterStartDateCalendar = Calendar.getInstance()
                setFilterWithDateRange(false)
            }

            R.id.ivFilter -> {
                val bottomSheet = BottomSheetFilter(filterList)
                bottomSheet.setOnCompleteListener(object : BottomSheetFilter.OnCompleteListener {
                    override fun onComplete(itemList: ArrayList<FilterItem>) {
                        val filterItemList: ArrayList<String> = ArrayList()
                        filterList = itemList
                        for (item in itemList) if (item.isChecked) filterItemList.add(item.name)
                        showFilterData(filterItemList)
                    }
                })
                bottomSheet.show(
                    requireActivity().supportFragmentManager, "BottomSheetFilter"
                )
            }

            R.id.ivCalendarFilter -> {
                setCalendarFilterVisibility()
            }

            R.id.ivFileUpload, R.id.rllUpload -> {
                showUploadDocumentDialog(onFileUploadListener)
            }

            R.id.cvStartData -> {
                showStartDateCalendar()
            }

            R.id.cvEndData -> {
                showEndDateCalendar()
            }
        }
    }

    private fun setCalendarFilterVisibility() {
        if (binding.rlDateCalendarLayout.isVisible) {
            binding.ivCalendarFilter.setColorFilter(
                ContextCompat.getColor(
                    requireActivity(), R.color.text_color_primary
                )
            )
            binding.rlDateCalendarLayout.visibility = View.GONE
        } else {
            binding.ivCalendarFilter.setColorFilter(
                ContextCompat.getColor(
                    requireActivity(), R.color.color_FF6605
                )
            )
            binding.rlDateCalendarLayout.visibility = View.VISIBLE
        }
    }

    private fun showFilterData(itemList: ArrayList<String>) {
        binding.rvFilter.visibility = View.VISIBLE
        binding.ivFilter.setColorFilter(
            ContextCompat.getColor(
                requireActivity(),
                if (itemList.size > 0) R.color.color_FF6605 else R.color.text_color_primary
            )
        )
        binding.rvFilter.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val myHealthFilterAdapter = MyHealthFilterAdapter(requireActivity(), itemList)
        binding.rvFilter.adapter = myHealthFilterAdapter
        myHealthFilterAdapter.apply {
            onItemClickListener = object : MyHealthFilterAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    filterList[position].isChecked = false
                    itemList.removeAt(position)
                    applyStatusFilter(itemList)
                    notifyDataSetChanged()
                    if (itemList.size == 0) {
                        binding.ivFilter.setColorFilter(
                            ContextCompat.getColor(
                                requireActivity(), R.color.text_color_primary
                            )
                        )
                    }
                }
            }
        }

        applyStatusFilter(itemList)
    }


    private fun setPractitionerData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_practitioners)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        practitionerAdapter = MyHealthPractitionerAdapter(requireActivity())
        practitionerAdapter?.itemList = practitionerList
        binding.rvList.adapter = practitionerAdapter
        practitionerAdapter?.apply {
            onItemClickListener = object : MyHealthPractitionerAdapter.OnClickCallback {
                override fun onClicked(data: Practitioner, type: ActionType) {
                    DetailSingleton.practitioner = data
                    val contactInfo = data.telecom?.extractContactInfo()
                    when (type) {
                        ActionType.DETAIL -> {
                            addFragment(
                                R.id.fragment_container,
                                PractitionerDetailsFragment(),
                                "PractitionerDetailsFragment",
                                "MyHealthFragment"
                            )
                        }

                        ActionType.EMAIL -> {
                            sendEmail(
                                contactInfo?.email!!,
                                "Practitioner Email : ${data.specialty}"
                            )
                        }

                        ActionType.PHONE -> {
                            openDialPadWithNumber(contactInfo?.phone!!)
                        }

                        ActionType.UPLOAD -> {
                            setUserSelectedDetails(
                                data.id,
                                PRACTITIONERS,
                                data.name!!,
                                data.createdAt?.toDisplayDate()!!
                            )
                            showUploadDocumentDialog(onFileUploadListener)
                        }
                    }
                }
            }
        }
    }

    private fun setAppointmentData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_appointments)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        appointmentAdapter =
            MyHealthAppointmentAdapter(requireActivity(), appointmentOrganizationMap)
        appointmentAdapter?.itemList = appointmentList
        binding.rvList.adapter = appointmentAdapter
        appointmentAdapter?.apply {
            onItemClickListener = object : MyHealthAppointmentAdapter.OnClickCallback {
                override fun onClicked(detail: Appointment, position: Int, type: String) {
                    when (type) {
                        Constants.READ_MORE -> {
                            openReadMoreDialog(
                                requireActivity(), "", getString(R.string.appointment_description)
                            )
                        }

                        Constants.DETAIL -> {
                            DetailSingleton.appointment = detail
                            val fragment = AppointmentDetailsFragment()
                            val bundle = Bundle()
                            bundle.putInt("position", position)
                            fragment.arguments = bundle
                            addFragment(
                                R.id.fragment_container,
                                fragment,
                                "AppointmentDetailsFragment",
                                "MyHealthFragment"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setConditionData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_conditions)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        conditionAdapter = MyHealthConditionAdapter(requireActivity())
        conditionAdapter?.itemList = conditionList
        binding.rvList.adapter = conditionAdapter
        conditionAdapter?.apply {
            onItemClickListener = object : MyHealthConditionAdapter.OnClickCallback {
                override fun onClicked(detail: Condition, position: Int) {
                    DetailSingleton.condition = detail
                    addFragment(
                        R.id.fragment_container,
                        ConditionDetailsFragment(),
                        "ConditionDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setLabData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_labs)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        labAdapter = MyHealthLabAdapter(requireActivity())
        labAdapter?.itemList = labList
        binding.rvList.adapter = labAdapter
        labAdapter?.apply {
            onItemClickListener = object : MyHealthLabAdapter.OnClickCallback {
                override fun onClicked(detail: DiagnosticReport, position: Int) {
                    DetailSingleton.lab = detail
                    addFragment(
                        R.id.fragment_container,
                        LabDetailsFragment(),
                        "LabDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setVitalData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_vitals)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        vitalAdapter = MyHealthVitalAdapter(requireActivity())
        vitalAdapter?.itemList = vitalsList
        binding.rvList.adapter = vitalAdapter
        vitalAdapter?.apply {
            onItemClickListener = object : MyHealthVitalAdapter.OnClickCallback {
                override fun onClicked(detail: Observation, position: Int) {
                    DetailSingleton.vital = detail
                    addFragment(
                        R.id.fragment_container,
                        VitalDetailsFragment(),
                        "VitalDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setMedicationData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_medications)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        medicationAdapter = MyHealthMedicationAdapter(requireActivity())
        medicationAdapter?.itemList = medicationList
        binding.rvList.adapter = medicationAdapter
        medicationAdapter?.apply {
            onItemClickListener = object : MyHealthMedicationAdapter.OnClickCallback {
                override fun onClicked(detail: MedicationRequest, position: Int) {
                    DetailSingleton.medication = detail
                    addFragment(
                        R.id.fragment_container,
                        MedicationDetailsFragment(),
                        "MedicationDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }


    private fun setVisitsData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_visits)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        visitsAdapter = MyHealthVisitsAdapter(requireActivity())
        visitsAdapter?.itemList = visitList
        binding.rvList.adapter = visitsAdapter
        visitsAdapter?.apply {
            onItemClickListener = object : MyHealthVisitsAdapter.OnClickCallback {
                override fun onClicked(item: Encounter?, position: Int) {
                    DetailSingleton.visit = item
                    addFragment(
                        R.id.fragment_container,
                        VisitsDetailsFragment(),
                        "VisitsDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setProceduresData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_procedures)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        procedureAdapter = MyHealthProcedureAdapter(requireActivity())
        procedureAdapter?.itemList = procedureList
        binding.rvList.adapter = procedureAdapter
        procedureAdapter?.apply {
            onItemClickListener = object : MyHealthProcedureAdapter.OnClickCallback {
                override fun onClicked(item: Procedure?, position: Int) {
                    DetailSingleton.procedure = item
                    addFragment(
                        R.id.fragment_container,
                        ProcedureDetailsFragment(),
                        "ProcedureDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setAllergiesData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_allergies)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        allergiesAdapter = MyHealthAllergiesAdapter(requireActivity())
        allergiesAdapter?.itemList = allergyList
        binding.rvList.adapter = allergiesAdapter
        allergiesAdapter?.apply {
            onItemClickListener = object : MyHealthAllergiesAdapter.OnClickCallback {
                override fun onClicked(item: AllergyIntolerance?, position: Int) {
                    DetailSingleton.allergy = item
                    addFragment(
                        R.id.fragment_container,
                        AllergiesDetailsFragment(),
                        "AllergiesDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setImmunizationData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_immunizations)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        immunizationAdapter = MyHealthImmunizationAdapter(requireActivity(),immunizationMap)
        immunizationAdapter?.itemList = immunizationList
        binding.rvList.adapter = immunizationAdapter
        immunizationAdapter?.apply {
            onItemClickListener = object : MyHealthImmunizationAdapter.OnClickCallback {
                override fun onClicked(item: Immunization?, position: Int, clickState: ClickState) {
                    when (clickState) {
                        ClickState.DETAIL -> {
                            DetailSingleton.immunization = item
                            addFragment(
                                R.id.fragment_container,
                                ImmunizationDetailsFragment(),
                                "ImmunizationDetailsFragment",
                                "MyHealthFragment"
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setBillingData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_billings)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        billingsAdapter = MyHealthBillingsAdapter(requireActivity())
        billingsAdapter?.itemList = billingList
        binding.rvList.adapter = billingsAdapter
        billingsAdapter?.apply {
            onItemClickListener = object : MyHealthBillingsAdapter.OnClickCallback {
                override fun onClicked(item: Claim?, position: Int) {
                    DetailSingleton.claim = item
                    addFragment(
                        R.id.fragment_container,
                        BillingDetailsFragment(),
                        "BillingDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun observeRecordVaultResponse() {
        recordVaultViewModel.userSavedFileList.observe(viewLifecycleOwner) {
            filterList = ArrayList()
            if (it.isNotEmpty()) {
                binding.emptyLayout.llEmptyLayout.visibility = View.GONE
                recordVaultList = ArrayList()
                recordVaultList?.addAll(it)
                recordVaultAdapter?.updateList(recordVaultList!!)
                for (item in recordVaultList!!) updateFilterList(item.category)
            } else {
                binding.emptyLayout.apply {
                    llEmptyLayout.visibility = View.VISIBLE
                    ivEmptyType.setImageResource(R.drawable.practitioner_no_record)
                    tvEmptyTitle.text = getString(R.string.blank_record)
                    tvEmptyDescription.text = ""
                }
            }
        }

        recordVaultViewModel.observationData.observe(viewLifecycleOwner) {
            imagingOrganizationMap = HashMap()
            appointmentOrganizationMap = HashMap()
            imagingOrganizationMap = it.first
            appointmentOrganizationMap = it.second
        }

       recordVaultViewModel.patientData.observe(viewLifecycleOwner) {
           immunizationMap = HashMap()
           immunizationMap = it
        }
    }

    private fun setUploadDocumentData() {
        recordVaultViewModel.getUserSavedFileList("")
        binding.tvMyHealthType.text = getString(R.string.list_of_file)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        recordVaultAdapter = MyHealthUploadDocAdapter(requireActivity())
        binding.rvList.adapter = recordVaultAdapter
        recordVaultAdapter?.apply {
            onItemClickListener = object : MyHealthUploadDocAdapter.OnClickCallback {
                override fun onClicked(type: TYPE, data: UserSavedFile, position: Int) {
                    when (type) {
                        TYPE.DELETE -> {
                            showDeleteDialog(data, position)
                        }

                        TYPE.VIEW -> {
                            if (data.file_type == Constants.FILE_IMAGE || data.file_type == Constants.FILE_VIDEO)
                                openFileView(data)
                            else if (data.file_type == Constants.FILE_DOCUMENT) openPdfView(data)
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(data: UserSavedFile, position: Int) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.delete_record))
            .setMessage(getString(R.string.you_want_delete_record))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                recordVaultViewModel.deleteFile(data.category_id)
                Toast.makeText(requireActivity(),getString(R.string.record_deleted_successfully),Toast.LENGTH_SHORT).show()
                recordVaultList?.removeAt(position)
                recordVaultAdapter?.notifyDataSetChanged()
                if (recordVaultList.isNullOrEmpty()) {
                    binding.emptyLayout.apply {
                        llEmptyLayout.visibility = View.VISIBLE
                        ivEmptyType.setImageResource(R.drawable.practitioner_no_record)
                        tvEmptyTitle.text = getString(R.string.blank_record)
                        tvEmptyDescription.text = ""
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_FF6605))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_FF6605))
    }

    private fun setImagingData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_imagings)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        imagingAdapter = MyHealthImagingAdapter(requireActivity(), imagingOrganizationMap)
        imagingAdapter?.itemList = imagingList
        binding.rvList.adapter = imagingAdapter
        imagingAdapter?.apply {
            onItemClickListener = object : MyHealthImagingAdapter.OnClickCallback {
                override fun onClicked(item: Imaging?, position: Int) {
                    DetailSingleton.imaging = item
                    addFragment(
                        R.id.fragment_container,
                        ImagingDetailsFragment(),
                        "ImagingDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private var startDate = ""
    private var endDate = ""
    private var filterStartDateCalendar = Calendar.getInstance()
    private var filterEndDateCalendar = Calendar.getInstance()

    private fun showStartDateCalendar() {
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            R.style.my_dialog_theme,
            { view, year, monthOfYear, dayOfMonth ->
                startDate = (monthOfYear + 1).toString() + "-" + dayOfMonth.toString() + "-" + year
                binding.tvFilterStartDate.text = startDate + "  "
                filterStartDateCalendar.set(Calendar.YEAR, year)
                filterStartDateCalendar.set(Calendar.MONTH, monthOfYear)
                filterStartDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if (filterStartDateCalendar > filterEndDateCalendar) {
                    endDate = ""
                    filterEndDateCalendar = Calendar.getInstance()
                    binding.tvFilterEndDate.text = getString(R.string.dd_mm_yyyy)
                }
                setDateRange()
            },
            filterStartDateCalendar.get(Calendar.YEAR),
            filterStartDateCalendar.get(Calendar.MONTH),
            filterStartDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showEndDateCalendar() {
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            R.style.my_dialog_theme,
            { view, year, monthOfYear, dayOfMonth ->
                endDate = (monthOfYear + 1).toString() + "-" + dayOfMonth.toString() + "-" + year
                binding.tvFilterEndDate.text = endDate + "  "
                filterEndDateCalendar.set(Calendar.YEAR, year)
                filterEndDateCalendar.set(Calendar.MONTH, monthOfYear)
                filterEndDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDateRange()
            },
            filterEndDateCalendar.get(Calendar.YEAR),
            filterEndDateCalendar.get(Calendar.MONTH),
            filterEndDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = filterStartDateCalendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setDateRange() {
        if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            binding.tvDateRange.text = "Date Range : $startDate - $endDate"
            binding.rlDateLayout.visibility = View.VISIBLE
            setFilterWithDateRange(true)
        }
    }

    private fun setFilterWithDateRange(isFilterList: Boolean) {
        val startDateCalendar = startDate.getCalendarFromString(Constants.DD_MM_YYYY_FORMATE)
        val endDateCalendar = endDate.getCalendarFromString(Constants.DD_MM_YYYY_FORMATE)
        when (getTileSelectedType()) {
            PRACTITIONERS -> setPractitionerDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            APPOINTMENTS -> setAppointmentDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            CONDITIONS -> setConditionDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            LABS -> setLabDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            VITALS -> setVitalDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            MEDICATIONS -> setMedicationDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            VISITS -> setVisitDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            PROCEDURES -> setProcedureDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            ALLERGIES -> setAllergyDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            IMMUNIZATIONS -> setImmunizationDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            BILLING -> setBillingDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

            IMAGING -> setImagingDateRangeFilter(
                isFilterList, startDateCalendar, endDateCalendar
            )

//            RECORD_VAULT -> setRecordVaultDateRangeFilter(
//                isFilterList, startDateCalendar, endDateCalendar
//            )
        }
        setNoRecordData()
    }

    private fun setPractitionerDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        practitionerAdapter?.updateList(if (isFilterList) practitionerList!!.filter { item ->
            val dateCalendar = item.createdAt?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else practitionerList!!)
    }

    private fun setAppointmentDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        appointmentAdapter?.updateList(if (isFilterList) appointmentList!!.filter { item ->
            val dateCalendar = item.startTime?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else appointmentList!!)
    }

    private fun setConditionDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        conditionAdapter?.updateList(if (isFilterList) conditionList!!.filter { item ->
            val dateCalendar = item.recordedDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else conditionList!!)
    }

    private fun setLabDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        labAdapter?.updateList(if (isFilterList) labList!!.filter { item ->
            val dateCalendar = item.issued?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else labList!!)
    }

    private fun setVitalDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        vitalAdapter?.updateList(if (isFilterList) vitalsList!!.filter { item ->
            val dateCalendar = item.effectiveDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else vitalsList!!)
    }

    private fun setMedicationDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        medicationAdapter?.updateList(if (isFilterList) medicationList!!.filter { item ->
            val dateCalendar = item.authoredOn?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else medicationList!!)
    }

    private fun setVisitDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        visitsAdapter?.updateList(if (isFilterList) visitList!!.filter { item ->
            val dateCalendar = item.createdAt?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else visitList!!)
    }

    private fun setProcedureDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        procedureAdapter?.updateList(if (isFilterList) procedureList!!.filter { item ->
            val dateCalendar = item.performedDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else procedureList!!)
    }

    private fun setAllergyDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        allergiesAdapter?.updateList(if (isFilterList) allergyList!!.filter { item ->
            val dateCalendar = item.recordedDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else allergyList!!)
    }

    private fun setImmunizationDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        immunizationAdapter?.updateList(if (isFilterList) immunizationList!!.filter { item ->
            val dateCalendar = item.occurrenceDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else immunizationList!!)
    }

    private fun setBillingDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        billingsAdapter?.updateList(if (isFilterList) billingList!!.filter { item ->
            val dateCalendar = item.createdDate?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else billingList!!)
    }

    private fun setImagingDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        imagingAdapter?.updateList(if (isFilterList) imagingList!!.filter { item ->
            val dateCalendar = item.started?.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
        } else imagingList!!)
    }

    private fun setRecordVaultDateRangeFilter(
        isFilterList: Boolean, startCalendar: Calendar, endCalendar: Calendar
    ) {
        recordVaultAdapter?.updateList(if (isFilterList) recordVaultList!!.filter { item ->
            val dateCalendar = item.upload_date.toFormateCalendar(
                "dd-MM-yyyy", Constants.DD_MM_YYYY_FORMATE
            )
            isDateRangeAvailable(dateCalendar, startCalendar, endCalendar)
        } else recordVaultList!!)
    }

    private fun isDateRangeAvailable(
        dateCalendar: Calendar, startCalendar: Calendar, endCalendar: Calendar
    ): Boolean {
        return (dateCalendar.timeInMillis >= startCalendar.timeInMillis && dateCalendar.timeInMillis <= endCalendar.timeInMillis)
    }

    private fun applyStatusFilter(itemList: ArrayList<String>) {
        when (getTileSelectedType()) {
            APPOINTMENTS -> {
                val list = ArrayList<Appointment>()
                for (filterText in itemList) {
                    list.addAll(appointmentList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                appointmentAdapter?.updateList(if (list.size > 0) list else appointmentList!!)
            }

            CONDITIONS -> {
                val list = ArrayList<Condition>()
                for (filterText in itemList) {
                    list.addAll(conditionList!!.filter { item -> item.clinicalStatus?.lowercase() == filterText.lowercase() })
                }
                conditionAdapter?.updateList(if (list.size > 0) list else conditionList!!)
            }

            LABS -> {
                val list = ArrayList<DiagnosticReport>()
                for (filterText in itemList) {
                    list.addAll(labList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                labAdapter?.updateList(if (list.size > 0) list else labList!!)
            }

            VITALS -> {
                val list = ArrayList<Observation>()
                for (filterText in itemList) {
                    list.addAll(vitalsList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                vitalAdapter?.updateList(if (list.size > 0) list else vitalsList!!)
            }

            MEDICATIONS -> {
                val list = ArrayList<MedicationRequest>()
                for (filterText in itemList) {
                    list.addAll(medicationList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                medicationAdapter?.updateList(if (list.size > 0) list else medicationList!!)
            }

            VISITS -> {
                val list = ArrayList<Encounter>()
                for (filterText in itemList) {
                    list.addAll(visitList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                visitsAdapter?.updateList(if (list.size > 0) list else visitList!!)
            }

            PROCEDURES -> {
                val list = ArrayList<Procedure>()
                for (filterText in itemList) {
                    list.addAll(procedureList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                procedureAdapter?.updateList(if (list.size > 0) list else procedureList!!)
            }

            ALLERGIES -> {
                val list = ArrayList<AllergyIntolerance>()
                for (filterText in itemList) {
                    list.addAll(allergyList!!.filter { item -> item.clinicalStatus?.lowercase() == filterText.lowercase() })
                }
                allergiesAdapter?.updateList(if (list.size > 0) list else allergyList!!)
            }

            IMMUNIZATIONS -> {
                val list = ArrayList<Immunization>()
                for (filterText in itemList) {
                    list.addAll(immunizationList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                immunizationAdapter?.updateList(if (list.size > 0) list else immunizationList!!)
            }

            BILLING -> {
                val list = ArrayList<Claim>()
                for (filterText in itemList) {
                    list.addAll(billingList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                billingsAdapter?.updateList(if (list.size > 0) list else billingList!!)
            }

            IMAGING -> {
                val list = ArrayList<Imaging>()
                for (filterText in itemList) {
                    list.addAll(imagingList!!.filter { item -> item.status?.lowercase() == filterText.lowercase() })
                }
                imagingAdapter?.updateList(if (list.size > 0) list else imagingList!!)
            }

            RECORD_VAULT -> {
                val list = ArrayList<UserSavedFile>()
                for (filterText in itemList) {
                    list.addAll(recordVaultList!!.filter { item -> item.category.lowercase() == filterText.lowercase() })
                }
                recordVaultAdapter?.updateList(if (list.size > 0) list else recordVaultList!!)
            }
        }
    }

    private fun getStatusFilterList() {
        filterList = ArrayList()

        when (getTileSelectedType()) {
            APPOINTMENTS -> for (item in appointmentList!!) item.status?.let { updateFilterList(it) }

            CONDITIONS -> for (item in conditionList!!) item.clinicalStatus?.let {
                updateFilterList(
                    it
                )
            }

            LABS -> for (item in labList!!) item.status?.let { updateFilterList(it) }

            VITALS -> for (item in vitalsList!!) item.status?.let { updateFilterList(it) }

            MEDICATIONS -> for (item in medicationList!!) item.status?.let { updateFilterList(it) }

            VISITS -> for (item in visitList!!) item.status?.let { updateFilterList(it) }

            PROCEDURES -> for (item in procedureList!!) item.status?.let { updateFilterList(it) }

            ALLERGIES -> for (item in allergyList!!) item.clinicalStatus?.let { updateFilterList(it) }

            IMMUNIZATIONS -> for (item in immunizationList!!) item.status?.let { updateFilterList(it) }

            BILLING -> for (item in billingList!!) item.status?.let { updateFilterList(it) }

            IMAGING -> for (item in imagingList!!) item.status?.let { updateFilterList(it) }
        }
    }

    private fun updateFilterList(status: String) {
        if (filterList.none { it.name.lowercase() == status.lowercase() }) {
            FilterItem(status.capitalFirstChar(), false).let { filterList.add(it) }
        }
    }

    private fun getFileLength(file: File): String {
        val mb = convertToMegabytes(file)
        return if (mb > 0) "$mb MB" else "${convertToKilobytes(file)} KB"
    }

    private fun convertToMegabytes(file: File): Long {
        return file.length() / (1024 * 1024)
    }

    private fun convertToKilobytes(file: File): Long {
        return file.length() / 1024
    }

    private fun setNoRecordLayout() {
        binding.emptyLayout.apply {
            llEmptyLayout.visibility = View.VISIBLE
            ivEmptyType.setImageResource(R.drawable.practitioner_no_record)
            tvEmptyTitle.text = getString(R.string.practitioner_no_record_title)
            tvEmptyDescription.text = getString(R.string.practitioner_no_record_description)
        }
    }

    private fun setNoRecordData() {
        binding.emptyLayout.llEmptyLayout.visibility = View.GONE
        when (getTileSelectedType()) {
            PRACTITIONERS -> if (practitionerAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            APPOINTMENTS -> if (appointmentAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            CONDITIONS -> if (conditionAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            LABS -> if (labAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            VITALS -> if (vitalAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            MEDICATIONS -> if (medicationAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            VISITS -> if (visitsAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            PROCEDURES -> if (procedureAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            ALLERGIES -> if (allergiesAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            IMMUNIZATIONS -> if (immunizationAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            BILLING -> if (billingsAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()

            IMAGING -> if (imagingAdapter?.itemList.isNullOrEmpty()) setNoRecordLayout()
        }
    }
}