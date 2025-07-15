package com.mE.Health.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
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
import com.mE.Health.databinding.MyHealthFragmentBinding
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
import com.mE.Health.models.MyHealthTypeModel
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.Constants
import com.mE.Health.utility.FilterItem
import com.mE.Health.utility.getCalendarFromString
import com.mE.Health.utility.toDisplayDateTime
import com.mE.Health.utility.toFormateCalendar
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


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
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {
            openSetting(requireActivity())
        }
    }

    private fun initView() {
        setPractitionerData()
        getFilterList()
        binding.rvType.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        myHealthTypeAdapter = MyHealthTypeAdapter(requireActivity(), getAllMyHealthType())
        binding.rvType.adapter = myHealthTypeAdapter
        myHealthTypeAdapter?.apply {
            onItemClickListener = object : MyHealthTypeAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    if (myHealthTypeAdapter?.selectedItem == position) return
                    myHealthTypeAdapter?.selectedItem = position
                    myHealthTypeAdapter?.notifyDataSetChanged()
                    getFilterList()
                    initFilterUI()
                    when (position) {
                        0 -> {
                            setPractitionerData()
                        }

                        1 -> {
                            setAppointmentData()
                        }

                        2 -> {
                            setConditionData()
                        }

                        3 -> {
                            setLabData()
                        }

                        4 -> {
                            setVitalData()
                        }

                        5 -> {
                            setMedicationData()
                        }

                        6 -> {
                            setVisitsData()
                        }

                        7 -> {
                            setProceduresData()
                        }

                        8 -> {
                            setAllergiesData()
                        }

                        9 -> {
                            setImmunizationData()
                        }

                        10 -> {
                            setBillingData()
                        }

                        11 -> {
                            setImagingData()
                        }

                        12 -> {
                            setUploadDocumentData()
                        }
                    }
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
        when (myHealthTypeAdapter?.selectedItem) {
            0 -> {
                filterPractitionerList(char?.toString()!!)
            }

            1 -> {
                if (char?.trim().toString().isNotEmpty()) filterAppointmentList(char?.toString()!!)
                else appointmentAdapter?.updateList(appointmentList!!)
            }

            2 -> {
                if (char?.trim().toString().isNotEmpty()) filterConditionList(char?.toString()!!)
                else conditionAdapter?.updateList(conditionList!!)
            }

            3 -> {
                if (char?.trim().toString().isNotEmpty()) filterLabList(char?.toString()!!)
                else labAdapter?.updateList(labList!!)
            }

            4 -> {
                if (char?.trim().toString().isNotEmpty()) filterVitalList(char?.toString()!!)
                else vitalAdapter?.updateList(vitalsList!!)
            }

            5 -> {
                if (char?.trim().toString().isNotEmpty()) filterMedicationList(char?.toString()!!)
                else medicationAdapter?.updateList(medicationList!!)
            }

            6 -> {
                if (char?.trim().toString().isNotEmpty()) filterVisitList(char?.toString()!!)
                else visitsAdapter?.updateList(visitList!!)
            }

            7 -> {
                if (char?.trim().toString().isNotEmpty()) filterProcedureList(char?.toString()!!)
                else procedureAdapter?.updateList(procedureList!!)
            }

            8 -> {
                if (char?.trim().toString().isNotEmpty()) filterAllergyList(char?.toString()!!)
                else allergiesAdapter?.updateList(allergyList!!)
            }

            9 -> {
                if (char?.trim().toString().isNotEmpty()) filterImmunizationList(char?.toString()!!)
                else immunizationAdapter?.updateList(immunizationList!!)
            }

            10 -> {
                if (char?.trim().toString().isNotEmpty()) filterBillingList(char?.toString()!!)
                else billingsAdapter?.updateList(billingList!!)
            }

            11 -> {
                if (char?.trim().toString().isNotEmpty()) filterImagingList(char?.toString()!!)
                else imagingAdapter?.updateList(imagingList!!)
            }
        }
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

    private fun filterAppointmentList(text: String) {
        val filterList = ArrayList<Appointment>()
        for (item in appointmentList!!) {
            if (item.practitionerName?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        appointmentAdapter?.updateList(filterList)
    }

    private fun filterConditionList(text: String) {
        val filterList = ArrayList<Condition>()
        for (item in conditionList!!) {
            if (item.code_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        conditionAdapter?.updateList(filterList)
    }

    private fun filterLabList(text: String) {
        val filterList = ArrayList<DiagnosticReport>()
        for (item in labList!!) {
            if (item.code_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        labAdapter?.updateList(filterList)
    }

    private fun filterVitalList(text: String) {
        val filterList = ArrayList<Observation>()
        for (item in vitalsList!!) {
            if (item.code_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        vitalAdapter?.updateList(filterList)
    }


    private fun filterMedicationList(text: String) {
        val filterList = ArrayList<MedicationRequest>()
        for (item in medicationList!!) {
            if (item.medicationCode_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        medicationAdapter?.updateList(filterList)
    }

    private fun filterVisitList(text: String) {
        val filterList = ArrayList<Encounter>()
        for (item in visitList!!) {
            if (item.type_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        visitsAdapter?.updateList(filterList)
    }


    private fun filterProcedureList(text: String) {
        val filterList = ArrayList<Procedure>()
        for (item in procedureList!!) {
            if (item.code_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        procedureAdapter?.updateList(filterList)
    }

    private fun filterAllergyList(text: String) {
        val filterList = ArrayList<AllergyIntolerance>()
        for (item in allergyList!!) {
            if (item.code_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        allergiesAdapter?.updateList(filterList)
    }

    private fun filterImmunizationList(text: String) {
        val filterList = ArrayList<Immunization>()
        for (item in immunizationList!!) {
            if (item.vaccineCode_display?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        immunizationAdapter?.updateList(filterList)
    }

    private fun filterBillingList(text: String) {
        val filterList = ArrayList<Claim>()
        for (item in billingList!!) {
            if (item.name?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        billingsAdapter?.updateList(filterList)
    }

    private fun filterImagingList(text: String) {
        val filterList = ArrayList<Imaging>()
        for (item in imagingList!!) {
            if (item.modality_display?.lowercase()
                    ?.contains(text.lowercase()) == true || item.description?.lowercase()
                    ?.contains(text.lowercase()) == true
            ) {
                filterList.add(item)
            }
        }
        imagingAdapter?.updateList(filterList)
    }

    private fun initFilterUI() {
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
        binding.rllUpload.visibility = View.GONE
        binding.rlDateCalendarLayout.visibility = View.GONE

        filterStartDateCalendar = Calendar.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> {
                binding.rlSearchLayout.visibility = View.VISIBLE
                binding.ivSearch.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity(), R.color.color_FF6605
                    )
                )
            }

            R.id.ivSearchCross -> {
                binding.etSearch.setText("")
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
                    override fun onComplete(filterItem: ArrayList<FilterItem>) {
                        var filterItemList: ArrayList<String> = ArrayList()
                        for (item in filterItem) if (item.isChecked) filterItemList.add(item.name)
                        showFilterData(filterItemList)
                    }
                })
                bottomSheet.show(
                    requireActivity().supportFragmentManager, "BottomSheetFilter"
                )
            }

            R.id.ivCalendarFilter -> {
//                openDateRangePicker()
                setCalendarFilterVisibility()
            }

            R.id.ivFileUpload, R.id.rllUpload -> {
                val onClickListener = object : OnClickCallback {
                    override fun onClick(position: Int) {
                        when (position) {
                            1 -> {
                                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                                photoPickerIntent.type = "image/*"
                                getPhotoPicker.launch(photoPickerIntent)
                            }

                            2 -> {
                                val pickerIntent = Intent(Intent.ACTION_PICK)
                                pickerIntent.type = "video/*"
                                getVideoPicker.launch(pickerIntent)
                            }

                            3 -> {
                                pickPdfLauncher.launch(arrayOf("application/pdf"))
                            }
                        }
                    }
                }
                showUploadDocument(onClickListener)
            }

            R.id.cvStartData -> {
                showStartDateCalendar()
            }

            R.id.cvEndData -> {
                showEndDateCalendar()
            }
        }
    }

    private fun getFilterList(): ArrayList<FilterItem> {
        filterList = ArrayList()
        filterList.apply {
            add(FilterItem("All", false))
            add(FilterItem("Today", false))
            add(FilterItem("Book", false))
            add(FilterItem("Cancelled", false))
        }
        return filterList
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

    private fun openDateRangePicker() {
        val picker =
            MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.CustomDatePickerTheme)
                .setTitleText("Select Date Range")
                .setSelection(Pair(firstDateSelected, secondDateSelected)).build()
        picker.show(requireActivity().supportFragmentManager, "TAG")
        picker.addOnNegativeButtonClickListener { picker?.dismiss() }
        picker.addOnPositiveButtonClickListener {
            binding.rlDateLayout.visibility = View.VISIBLE
            binding.ivCalendarFilter.setColorFilter(
                ContextCompat.getColor(
                    requireActivity(), R.color.color_FF6605
                )
            )
            firstDateSelected = it.first
            secondDateSelected = it.second
            binding.tvDateRange.text = "Date Range : ${convertTimeToDate(firstDateSelected)} - ${
                convertTimeToDate(
                    secondDateSelected
                )
            }"
        }
    }

    private fun convertTimeToDate(time: Long): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = time
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun getAllMyHealthType(): ArrayList<MyHealthTypeModel> {
        val typeList: ArrayList<MyHealthTypeModel> = ArrayList()
        typeList.apply {
            add(
                MyHealthTypeModel(
                    getString(R.string.practitioners),
                    mockViewModel.practitionerList.value?.size.toString(),
                    R.drawable.ic_car
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.appointments),
                    mockViewModel.appointmentList.value?.size.toString(),
                    R.drawable.ic_appoinment
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.conditions),
                    mockViewModel.conditionList.value?.size.toString(),
                    R.drawable.ic_conditions_my_health
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.labs),
                    mockViewModel.labList.value?.size.toString(),
                    R.drawable.ic_labs
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.vitals),
                    mockViewModel.vitalsList.value?.size.toString(),
                    R.drawable.ic_vitals
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.medications),
                    mockViewModel.medicationList.value?.size.toString(),
                    R.drawable.ic_medication_my_health
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.visits),
                    mockViewModel.visitList.value?.size.toString(),
                    R.drawable.ic_visits
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.procedures),
                    mockViewModel.procedureList.value?.size.toString(),
                    R.drawable.ic_procedures
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.allergies),
                    mockViewModel.allergyList.value?.size.toString(),
                    R.drawable.ic_allergy
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.immunizations),
                    mockViewModel.immunizationList.value?.size.toString(),
                    R.drawable.ic_immunization
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.billings),
                    mockViewModel.claimList.value?.size.toString(),
                    R.drawable.ic_billing
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.imagings),
                    mockViewModel.imagingList.value?.size.toString(),
                    R.drawable.ic_imaging
                )
            )
            add(
                MyHealthTypeModel(
                    getString(R.string.record_vaults), "6", R.drawable.ic_upload_health
                )
            )
        }
        return typeList
    }


    private fun showFilterData(itemList: ArrayList<String>) {
        binding.rvFilter.visibility = View.VISIBLE
        binding.ivFilter.setColorFilter(
            ContextCompat.getColor(
                requireActivity(), R.color.color_FF6605
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
    }

    private fun setPractitionerData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_practitioners)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        practitionerAdapter = MyHealthPractitionerAdapter(requireActivity())
        practitionerAdapter?.itemList = practitionerList
        binding.rvList.adapter = practitionerAdapter
        practitionerAdapter?.apply {
            onItemClickListener = object : MyHealthPractitionerAdapter.OnClickCallback {
                override fun onClicked(data: Practitioner) {
                    DetailSingleton.practitioner = data
                    addFragment(
                        R.id.fragment_container,
                        PractitionerDetailsFragment(),
                        "PractitionerDetailsFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }
    }

    private fun setAppointmentData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_appointments)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        appointmentAdapter = MyHealthAppointmentAdapter(requireActivity())
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
        immunizationAdapter = MyHealthImmunizationAdapter(requireActivity())
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

    private fun setUploadDocumentData() {
        binding.rllUpload.visibility = View.VISIBLE
        binding.tvMyHealthType.text = getString(R.string.list_of_file)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val practitionerAdapter = MyHealthUploadDocAdapter(requireActivity())
        binding.rvList.adapter = practitionerAdapter
        practitionerAdapter.apply {
            onItemClickListener = object : MyHealthUploadDocAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
//                    addFragment(
//                        R.id.fragment_container,
//                        PractitionerDetailsFragment(),
//                        "PractitionerDetailsFragment",
//                        "MyHealthFragment"
//                    )
                }
            }
        }
    }

    private fun setImagingData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_imagings)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        imagingAdapter = MyHealthImagingAdapter(requireActivity())
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

    private val getPhotoPicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = it?.data?.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, selectedImage
                )
                var uri = it?.data?.data!!
                if (uri != null) {
                    picturePath = getAbsolutePath(uri)
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") if (TextUtils.isEmpty(
                            picturePath
                        )
                    ) picturePath = uri.path.toString()
                    val file = File(picturePath)
                    if (!file.isFile || file.length() == 0L) {
                        Toast.makeText(
                            activity, "gallery_pick_error", Toast.LENGTH_LONG
                        ).show()
                        return@registerForActivityResult
                    }
                    filePath = picturePath
                    Log.i(
                        javaClass.name, "Gallery picturePath : $picturePath: ${file.length()}"
                    )


                    val cursor = requireActivity()!!.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.Media._ID),
                        MediaStore.Images.Media.DATA + "=? ",
                        arrayOf(picturePath),
                        null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                        uri = Uri.parse("content://media/external/images/media/$id")
                    }
                    cursor?.close()
                    val fileName = File(uri!!.path).name
                    val fragment = UserContentFragment()
                    val bundle = Bundle()
                    bundle.apply {
                        putString(Constants.FILE_PATH, uri.toString())
                        putString(Constants.FILE_LENGTH, getFileLength(file))
                        putString(Constants.FILE_NAME, fileName)
                        putString(Constants.FILE_TYPE, Constants.FILE_IMAGE)
                    }
                    fragment.arguments = bundle
                    addFragment(
                        R.id.fragment_container, fragment, "UserContentFragment", "MyHealthFragment"
                    )
                    Log.i("=============", "=========picturePath: $picturePath")
                }
            } catch (e: IOException) {
                Log.i("TAG", "Some exception $e")
            }
        }
    }


    @SuppressLint("Range")
    private val getVideoPicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = it?.data?.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver, selectedImage
                )
//                    binding.ivDemo.setImageBitmap(bitmap)
                var uri = it?.data?.data!!
                if (uri != null) {
                    picturePath = getAbsolutePath(uri)
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") if (TextUtils.isEmpty(
                            picturePath
                        )
                    ) picturePath = uri.path.toString()
                    val file = File(picturePath)
                    if (!file.isFile || file.length() == 0L) {
                        Toast.makeText(
                            activity, "gallery_pick_error", Toast.LENGTH_LONG
                        ).show()
                        return@registerForActivityResult
                    }
                    filePath = picturePath
                    Log.i(
                        javaClass.name, "Gallery videoPath : $picturePath: ${file.length()}"
                    )
                    val cursor = requireActivity()!!.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.Media._ID),
                        MediaStore.Images.Media.DATA + "=? ",
                        arrayOf(picturePath),
                        null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                        uri = Uri.parse("content://media/external/images/media/$id")
                    }
                    cursor?.close()
                    val fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1)
                    val fragment = UserContentFragment()
                    val bundle = Bundle()
                    bundle.apply {
                        putString(Constants.FILE_PATH, it.toString())
                        putString(Constants.FILE_LENGTH, getFileLength(file))
                        putString(Constants.FILE_NAME, fileName)
                        putString(Constants.FILE_TYPE, Constants.FILE_VIDEO)
                    }
                    fragment.arguments = bundle
                    addFragment(
                        R.id.fragment_container, fragment, "UserContentFragment", "MyHealthFragment"
                    )
                    Log.i("=============", "=========picturePath: $picturePath")
                }
            } catch (e: IOException) {
                Log.i("TAG", "Some exception $e")
            }
        }
    }


    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                // Get the file information
                val fileInfo = getFileInfo(it)
                val fileName = fileInfo.first
                val fileSize = fileInfo.second.toFloat()

                // Convert file size to MB
                val fileSizeInMB = java.lang.String.format("%.3f", fileSize / 1000000.0)

                // Update the UI with the file information
                val name =
                    "Filename - $fileName\nFile size - $fileSizeInMB MB\nFile path - ${it.path}"
                Log.i("================", "=====$name")
                Log.i("================FILE_PATH", "=====${it.toString()}")
                val filename = picturePath.substring(picturePath.lastIndexOf("/") + 1)
                val fragment = UserContentFragment()
                val bundle = Bundle()
                bundle.apply {
                    putString(Constants.FILE_PATH, it.toString())
                    putString(Constants.FILE_LENGTH, "$fileSizeInMB MB")
                    putString(Constants.FILE_NAME, fileName)
                    putString(Constants.FILE_TYPE, Constants.FILE_DOCUMENT)
                }
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container, fragment, "UserContentFragment", "MyHealthFragment"
                )
            } ?: run {
                // Handle the case where no file was selected
                Toast.makeText(requireActivity(), "No file selected", Toast.LENGTH_SHORT).show()
            }
        }

    // Function to get the file name from URI - optional
    private fun getFileInfo(uri: Uri): kotlin.Pair<String, Long> {
        // Initialize default values
        var fileName = "Unknown"
        var fileSize = 0L
        // Query the content resolver to get the file name and size
        requireActivity().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            // Move to the first row
            if (cursor.moveToFirst()) {
                // Get the display name and size columns
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                // Check if the columns exist
                if (nameIndex != -1) {
                    // Get the file name and size
                    fileName = cursor.getString(nameIndex)
                    fileSize = cursor.getLong(sizeIndex)
                }
            }
        }
        // Return the file name and size as a pair
        return kotlin.Pair(fileName, fileSize)
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
                startDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
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
            requireActivity(), R.style.my_dialog_theme, { view, year, monthOfYear, dayOfMonth ->
                endDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
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
        val startDateCalendar =
            startDate.getCalendarFromString(Constants.DD_MM_YYYY_FORMATE)
        val endDateCalendar =
            endDate.getCalendarFromString(Constants.DD_MM_YYYY_FORMATE)

        when (myHealthTypeAdapter?.selectedItem) {
            0 -> {
                setPractitionerDateRangeFilter(
                    isFilterList, startDateCalendar, endDateCalendar
                )
            }

            1 -> {
                setAppointmentDateRangeFilter(
                    isFilterList, startDateCalendar, endDateCalendar
                )
            }

            2 -> {
                setConditionDateRangeFilter(
                    isFilterList, startDateCalendar, endDateCalendar
                )
            }
        }
    }

    private fun setPractitionerDateRangeFilter(
        isFilterList: Boolean,
        startCalendar: Calendar,
        endCalendar: Calendar
    ) {
        practitionerAdapter?.updateList(
            if (isFilterList) practitionerList!!.filter { item ->
                val dateCalendar = item.createdAt?.toFormateCalendar(
                    "dd-MM-yyyy",
                    Constants.DD_MM_YYYY_FORMATE
                )
                isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
            } else practitionerList!!
        )
    }

    private fun setAppointmentDateRangeFilter(
        isFilterList: Boolean,
        startCalendar: Calendar,
        endCalendar: Calendar
    ) {
        appointmentAdapter?.updateList(
            if (isFilterList) appointmentList!!.filter { item ->
                val dateCalendar = item.startTime?.toFormateCalendar(
                    "dd-MM-yyyy",
                    Constants.DD_MM_YYYY_FORMATE
                )
                isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
            } else appointmentList!!
        )
    }

    private fun setConditionDateRangeFilter(
        isFilterList: Boolean,
        startCalendar: Calendar,
        endCalendar: Calendar
    ) {
        conditionAdapter?.updateList(
            if (isFilterList) conditionList!!.filter { item ->
                val dateCalendar = item.recordedDate?.toFormateCalendar(
                    "dd-MM-yyyy",
                    Constants.DD_MM_YYYY_FORMATE
                )
                isDateRangeAvailable(dateCalendar!!, startCalendar, endCalendar)
            } else conditionList!!
        )
    }

    private fun isDateRangeAvailable(
        dateCalendar: Calendar,
        startCalendar: Calendar,
        endCalendar: Calendar
    ): Boolean {
        return (dateCalendar.timeInMillis >= startCalendar.timeInMillis &&
                dateCalendar.timeInMillis <= endCalendar.timeInMillis)
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
}