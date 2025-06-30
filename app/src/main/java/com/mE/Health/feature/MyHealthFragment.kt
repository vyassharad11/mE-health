package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.mE.Health.R
import com.mE.Health.databinding.MyHealthFragmentBinding
import com.mE.Health.feature.adapter.ClickState
import com.mE.Health.feature.adapter.MyHealthAllergiesAdapter
import com.mE.Health.feature.adapter.MyHealthAppointmentAdapter
import com.mE.Health.feature.adapter.MyHealthBillingsAdapter
import com.mE.Health.feature.adapter.MyHealthConditionAdapter
import com.mE.Health.feature.adapter.MyHealthFilterAdapter
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
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyHealthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        firstDateSelected = Calendar.getInstance().timeInMillis
        secondDateSelected = Calendar.getInstance().timeInMillis
        initView()
        initHeader()
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

        }
    }

    private fun initView() {
        setPractitionerData()
        getFilterList()
        binding.rvType.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val myHealthTypeAdapter = MyHealthTypeAdapter(requireActivity(), getAllMyHealthType())
        binding.rvType.adapter = myHealthTypeAdapter
        myHealthTypeAdapter.apply {
            onItemClickListener = object : MyHealthTypeAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    if (myHealthTypeAdapter.selectedItem == position) return
                    myHealthTypeAdapter.selectedItem = position
                    myHealthTypeAdapter.notifyDataSetChanged()
                    getFilterList()
                    initFilterUI()
                    binding.rllUpload.visibility = View.GONE
                    firstDateSelected = Calendar.getInstance().timeInMillis
                    secondDateSelected = Calendar.getInstance().timeInMillis
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
                            setUploadDocumentData()
                        }
                    }
                }
            }
        }

        binding.ivSearch.setOnClickListener(this)
        binding.ivCross.setOnClickListener(this)
        binding.ivFilter.setOnClickListener(this)
        binding.ivFilterCalendar.setOnClickListener(this)
        binding.ivDateCancel.setOnClickListener(this)
    }

    private fun initFilterUI() {
        binding.rlDateLayout.visibility = View.GONE
        binding.rvFilter.visibility = View.GONE
        binding.rlSearchLayout.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> {
                binding.rlSearchLayout.visibility = View.VISIBLE
            }

            R.id.ivCross -> {
                binding.rlSearchLayout.visibility = View.GONE
            }

            R.id.ivDateCancel -> {
                binding.rlDateLayout.visibility = View.GONE
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
                    requireActivity().supportFragmentManager,
                    "BottomSheetFilter"
                )
            }

            R.id.ivFilterCalendar -> {
                openDateRangePicker()
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

    private fun openDateRangePicker() {
        val picker =
            MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.CustomDatePickerTheme)
                .setTitleText("Select Date Range")
                .setSelection(Pair(firstDateSelected, secondDateSelected))
                .build()
        picker.show(requireActivity().supportFragmentManager, "TAG")
        picker.addOnNegativeButtonClickListener { picker?.dismiss() }
        picker.addOnPositiveButtonClickListener {
            binding.rlDateLayout.visibility = View.VISIBLE
            firstDateSelected = it.first
            secondDateSelected = it.second
            binding.tvDateRange.text =
                "Date Range : ${convertTimeToDate(firstDateSelected)} - ${convertTimeToDate(secondDateSelected)}"
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
            add(MyHealthTypeModel("Practictioner", "10", R.drawable.ic_car))
            add(MyHealthTypeModel("Appointments", "6", R.drawable.ic_appoinment))
            add(MyHealthTypeModel("Conditions", "15", R.drawable.ic_conditions))
            add(MyHealthTypeModel("Labs", "6", R.drawable.ic_labs))
            add(MyHealthTypeModel("Vitals", "6", R.drawable.ic_vitals))
            add(MyHealthTypeModel("Medications", "6", R.drawable.ic_medications))
            add(MyHealthTypeModel("Visits", "6", R.drawable.ic_visits))
            add(MyHealthTypeModel("Procedures", "6", R.drawable.ic_procedures))
            add(MyHealthTypeModel("Allergies", "6", R.drawable.ic_allergy))
            add(MyHealthTypeModel("Immunizations", "6", R.drawable.ic_immunization))
            add(MyHealthTypeModel("Billings", "6", R.drawable.ic_billing))
            add(MyHealthTypeModel("Upload Documents", "6", R.drawable.ic_upload))
        }
        return typeList
    }


    private fun showFilterData(itemList: ArrayList<String>) {
        binding.rvFilter.visibility = View.VISIBLE
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
                }
            }
        }
    }

    private fun setPractitionerData() {
        binding.tvMyHealthType.text = getString(R.string.list_of_practitioner)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val practitionerAdapter = MyHealthPractitionerAdapter(requireActivity())
        binding.rvList.adapter = practitionerAdapter
        practitionerAdapter.apply {
            onItemClickListener = object : MyHealthPractitionerAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_appointment)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val providerAdapter = MyHealthAppointmentAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthAppointmentAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int, type: String) {

                    when (type) {
                        Constants.READ_MORE -> {
                            openReadMoreDialog(
                                requireActivity(),
                                "",
                                getString(R.string.appointment_description)
                            )
                        }

                        Constants.DETAIL -> {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_condition)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val providerAdapter = MyHealthConditionAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthConditionAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_lab)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val providerAdapter = MyHealthLabAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthLabAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        val adapter = MyHealthVitalAdapter(requireActivity())
        binding.rvList.adapter = adapter
        adapter.apply {
            onItemClickListener = object : MyHealthVitalAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_medication)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val providerAdapter = MyHealthMedicationAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthMedicationAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        val providerAdapter = MyHealthVisitsAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthVisitsAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        val providerAdapter = MyHealthProcedureAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthProcedureAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        val providerAdapter = MyHealthAllergiesAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthAllergiesAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_immunization)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val providerAdapter = MyHealthImmunizationAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthImmunizationAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int, clickState: ClickState) {
                    when (clickState) {
                        ClickState.DETAIL -> {
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
        val providerAdapter = MyHealthBillingsAdapter(requireActivity())
        binding.rvList.adapter = providerAdapter
        providerAdapter.apply {
            onItemClickListener = object : MyHealthBillingsAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
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
        binding.tvMyHealthType.text = getString(R.string.list_of_files)
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
}