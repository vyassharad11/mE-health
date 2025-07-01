package com.mE.Health.feature

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.File
import java.io.IOException
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
    private var myHealthTypeAdapter: MyHealthTypeAdapter? = null

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
        binding.ivFileUpload.setOnClickListener(this)
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

            R.id.ivFileUpload -> {
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
                if (myHealthTypeAdapter?.selectedItem == 11) showUploadDocument(onClickListener)
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
                "Date Range : ${convertTimeToDate(firstDateSelected)} - ${
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
            add(MyHealthTypeModel("Upload Documents", "6", R.drawable.ic_upload_health))
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

    private val getPhotoPicker =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri = it?.data?.data!!
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedImage
                        )
//                    binding.ivDemo.setImageBitmap(bitmap)
                    var uri = it?.data?.data!!
                    if (uri != null) {
                        picturePath = getAbsolutePath(uri)
                        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                        if (TextUtils.isEmpty(picturePath))
                            picturePath = uri.path.toString()
                        val file = File(picturePath)
                        if (!file.isFile || file.length() == 0L) {
                            Toast.makeText(
                                activity,
                                "gallery_pick_error",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return@registerForActivityResult
                        }
                        filePath = picturePath
                        Log.i(
                            javaClass.name,
                            "Gallery picturePath : $picturePath: ${file.length()}"
                        )
                        val cursor = requireActivity()!!.contentResolver
                            .query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                arrayOf(MediaStore.Images.Media._ID),
                                MediaStore.Images.Media.DATA + "=? ",
                                arrayOf(picturePath), null
                            )
                        if (cursor != null && cursor.moveToFirst()) {
                            val id =
                                cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                            uri = Uri.parse("content://media/external/images/media/$id")
                        }
                        cursor?.close()
                        val fragment = UserContentFragment()
                        val bundle = Bundle()
                        bundle.putString("IMAGE_PATH",picturePath)
                        fragment.arguments = bundle
                        addFragment(
                            R.id.fragment_container,
                            fragment,
                            "UserContentFragment",
                            "MyHealthFragment"
                        )
                        Log.i("=============", "=========picturePath: $picturePath")
                    }
                } catch (e: IOException) {
                    Log.i("TAG", "Some exception $e")
                }
            }
        }


    private val getVideoPicker =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri = it?.data?.data!!
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedImage
                        )
//                    binding.ivDemo.setImageBitmap(bitmap)
                    var uri = it?.data?.data!!
                    if (uri != null) {
                        picturePath = getAbsolutePath(uri)
                        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                        if (TextUtils.isEmpty(picturePath))
                            picturePath = uri.path.toString()
                        val file = File(picturePath)
                        if (!file.isFile || file.length() == 0L) {
                            Toast.makeText(
                                activity,
                                "gallery_pick_error",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            return@registerForActivityResult
                        }
                        filePath = picturePath
                        Log.i(
                            javaClass.name,
                            "Gallery videoPath : $picturePath: ${file.length()}"
                        )
                        val cursor = requireActivity()!!.contentResolver
                            .query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                arrayOf(MediaStore.Images.Media._ID),
                                MediaStore.Images.Media.DATA + "=? ",
                                arrayOf(picturePath), null
                            )
                        if (cursor != null && cursor.moveToFirst()) {
                            val id =
                                cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                            uri = Uri.parse("content://media/external/images/media/$id")
                        }
                        cursor?.close()
                        val fragment = UserContentFragment()
                        val bundle = Bundle()
                        bundle.putString("IMAGE_PATH",picturePath)
                        fragment.arguments = bundle
                        addFragment(
                            R.id.fragment_container,
                            UserContentFragment(),
                            "UserContentFragment",
                            "MyHealthFragment"
                        )
                        Log.i("=============", "=========videoPath: $picturePath")
                    }
                } catch (e: IOException) {
                    Log.i("TAG", "Some exception $e")
                }
            }
        }


    private var selectedPdfUri: Uri? = null

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                selectedPdfUri = it
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
                addFragment(
                    R.id.fragment_container,
                    UserContentFragment(),
                    "UserContentFragment",
                    "MyHealthFragment"
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
}