package com.mE.Health.feature

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.mE.Health.HomeActivity
import com.mE.Health.MyApplication
import com.mE.Health.R
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.feature.adapter.ImagingPreviewAdapter
import com.mE.Health.feature.adapter.UserSavedFileAdapter
import com.mE.Health.utility.AppSession
import com.mE.Health.utility.BottomSheetImagingPreview
import com.mE.Health.utility.BottomSheetUserSavedFilePreview
import com.mE.Health.utility.Constants
import com.mE.Health.utility.DialogOK
import com.mE.Health.utility.DialogProgress
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.Utilities.openPdf
import com.mE.Health.viewmodels.FileViewModel
import com.mE.Health.viewmodels.assist.AssistViewModel
import com.mE.Health.viewmodels.mockData.MockDataViewModel
import dagger.hilt.android.internal.managers.ViewComponentManager
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URLDecoder
import java.util.Locale
import javax.inject.Inject


open class BaseFragment : Fragment() {

    val mockViewModel: MockDataViewModel by activityViewModels()
    val assistViewModel: AssistViewModel by activityViewModels()
    val fileViewModel: FileViewModel by activityViewModels()
    private var dialogProgress: DialogProgress? = null
    private var dialogOK: Dialog? = null
    var shareMessage = ""

    @Inject
    lateinit var appSession: AppSession

    fun replaceFragmentLogin(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }


    fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            if (updateSideNavMenuVisibility(requireActivity())) {
                return
            }
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    fun addFragmentLogin(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    fun addFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            if (updateSideNavMenuVisibility(requireActivity())) {
                return
            }
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }


    fun replaceFragmentWithoutBack(containerViewId: Int, fragment: Fragment, fragmentTag: String) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    val countryDefault: String?
        get() {
            var countryDTO: String? = null
            try {
                val tm = requireActivity()
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                var countryIso = tm.networkCountryIso
                if (TextUtils.isEmpty(countryIso))
                    countryIso = Locale.getDefault().country
                val response = readRawFileAsString(R.raw.country_codes)
                val array = JSONArray(response)
                for (i in 0 until array.length()) {
                    val jsonObject = array.getJSONObject(i)
                    if (countryIso.equals(
                            jsonObject.getString("alpha-2"),
                            ignoreCase = true
                        )
                    ) {
                        countryDTO = jsonObject.optString("phone-code")
                        break
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return countryDTO
        }

    private fun readRawFileAsString(rawFile: Int): String {
        val inputStream = requireActivity().resources.openRawResource(rawFile)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuffer()
        while (true) {
            val line = reader.readLine() ?: break
            result.append(line)
        }
        reader.close()
        return result.toString()
    }

    fun showProgressDialog() {
        try {
            if (activity != null) {
                if (dialogProgress != null && dialogProgress!!.isShowing)
                    dialogProgress!!.dismiss()
                dialogProgress = DialogProgress(requireActivity())
                dialogProgress!!.show()
            }
        } catch (e: java.lang.Exception) {
        }
    }

    fun hideProgressDialog() {
        try {
            if (dialogProgress != null && dialogProgress!!.isShowing)
                dialogProgress!!.dismiss()
        } catch (e: java.lang.Exception) {

        }
    }

    fun showDialogOk(message: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_ok)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        tvMessage.text = message
        tvTitle.visibility = View.VISIBLE
        val tvOk = dialog.findViewById<TextView>(R.id.tvOk)
        tvOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setBottomNavigationVisibility(context: Context) {
        (getActivity(context) as HomeActivity).setBottomNavigationVisibility()
    }

    fun refreshBottomMenu(context: Context) {
        (getActivity(context) as HomeActivity).refreshMenu()
    }

    fun activeDashboardMenu(context: Context) {
        if (!(getActivity(context) as HomeActivity).getSideNavStatus())
            (getActivity(context) as HomeActivity).activeDashboardMenu()
    }

    fun activeHomeMenu(context: Context) {
        (getActivity(context) as HomeActivity).activeHomeMenu()
    }

    fun updateSideNavStatus(context: Context) {
        (getActivity(context) as HomeActivity).updateSideNavStatus()
    }

    fun openSetting(context: Context) {
        (getActivity(context) as HomeActivity).openSetting()
    }

    fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    fun setHeaderBackProperties(
        ivBack: ImageView
    ) {
        ivBack.apply {
            setOnClickListener {
                onBackPressed()
            }
        }
    }

    fun setHeaderTitleProperties(
        title: String,
        tvTitle: TextView,
        isClickable: Boolean = false
    ) {
        tvTitle.apply {
            text = title
            if (isClickable) setOnClickListener {
                onBackPressed()
            }
        }
    }

    fun setHeaderSettingProperties(
        ivSetting: ImageView,
        isVisible: Boolean = false
    ) {
        ivSetting.apply {
            visibility = if (isVisible) View.VISIBLE else View.GONE
            setOnClickListener {
                openSetting(requireActivity())
            }
        }
    }

    fun setHeaderUploadProperties(
        ivSetting: ImageView,
        isVisible: Boolean = false
    ) {
        ivSetting.apply {
            setImageResource(R.drawable.ic_upload_orange)
            visibility = if (isVisible) View.VISIBLE else View.GONE
            setOnClickListener {
                showUploadDocumentDialog(onFileUploadListener)
            }
        }
    }

    private fun updateSideNavMenuVisibility(mActivity: Activity): Boolean {
        var status = false
        val mCurrentActivity = (mActivity.applicationContext as MyApplication).getCurrentActivity()
        if ((mCurrentActivity as HomeActivity) != null) {
            status = (mActivity as HomeActivity).hideSideNavRequired()
        }
        return status
    }

    private fun getActivity(context: Context): Context {
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    fun openReadMoreDialog(context: Context, title: String, message: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_read_more)
        dialog.window?.setBackgroundDrawable(0.toDrawable())
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
//        dialog.window!!.attributes.windowAnimations = R.style.animation
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        if (TextUtils.isEmpty(title)) tvTitle.visibility = View.INVISIBLE
        tvTitle.text = title
        tvMessage.text = message
        val tvOk = dialog.findViewById<View>(R.id.tvOk)
        tvOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    val isNetworkAvailable: Boolean
        get() {
            try {
                val cm =
                    requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected)
                    return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


    fun dialogOK(context: Context, title: String, message: String) {
        if (context == null) return
        if (dialogOK != null && dialogOK!!.isShowing)
            dialogOK!!.dismiss()
        dialogOK = DialogOK(context, title, message)
        dialogOK!!.show()
    }



    fun log(tag: String, str: String) {
        Log.i(tag, str)
    }

    fun checkPermission(
        permission: Array<String>,
        requestCode: Array<Int> = arrayOf(101)
    ): Boolean {
        var check = true
        val notGPermission: ArrayList<String> = ArrayList()
        val notGPermissionRequest: ArrayList<Int> = ArrayList()
        for (i in 0 until permission.size) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                log(javaClass.name, "Permission for : ${permission[i]} not granted")
                notGPermission.add(permission[i])
                notGPermissionRequest.add(requestCode[0])
                check = false
            } else {
                log(javaClass.name, "Permission for : ${permission[i]} granted")
            }
        }
        if (!check) {
            requestPermissions(
                notGPermission.toArray(arrayOfNulls<String>(notGPermission.size)),
                requestCode[0]
            )
        }
        return check
    }

    interface OnClickCallback {
        fun onClick(position: Int)
    }

    fun showUploadDocumentDialog(onClickCallback: OnClickCallback) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_camera_video)
        dialog.window?.setBackgroundDrawable(0.toDrawable())
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.setCancelable(true)
        val tvPicture = dialog.findViewById<TextView>(R.id.tvPicture)
        val tvVideo = dialog.findViewById<TextView>(R.id.tvVideo)
        val tvDocument = dialog.findViewById<View>(R.id.tvDocument)
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel)
        tvPicture.setOnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(1)
        }
        tvVideo.setOnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(2)
        }
        tvDocument.setOnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(3)
        }
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener("UpdateView", viewLifecycleOwner) { key, bundle ->
            fileViewModel.getUserSavedFileList(UserContentFragment.healthItemId)
        }
    }

    fun setUserSelectedDetails(itemId: String, itemType: String) {
        UserContentFragment.healthItemId = itemId
        UserContentFragment.healthItemType = itemType
    }

    val onFileUploadListener = object : OnClickCallback {
        override fun onClick(position: Int) {
            when (position) {
                1 -> {
                    UserContentFragment.fileType = Constants.FILE_IMAGE
                    pickFileFromStorage("image/*")
                }

                2 -> {
                    UserContentFragment.fileType = Constants.FILE_VIDEO
                    pickFileFromStorage("video/*")
                }

                3 -> {
                    UserContentFragment.fileType = Constants.FILE_DOCUMENT
                    pickPdfLauncher.launch(arrayOf("application/pdf"))
                }
            }
        }
    }

    private fun pickFileFromStorage(input:String) {
        pickFileLauncher.launch(input)
    }

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            UserContentFragment.fileURI = uri
            addFragment(
                R.id.fragment_container, UserContentFragment(), "UserContentFragment", "MyHealthFragment"
            )
        }

    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                UserContentFragment.fileURI = it
                addFragment(
                    R.id.fragment_container, UserContentFragment(), "UserContentFragment", "MyHealthFragment"
                )
            } ?: run {
                // Handle the case where no file was selected
                Toast.makeText(requireActivity(), "No file selected", Toast.LENGTH_SHORT).show()
            }
        }

    // Function to get the file name from URI - optional
    fun getFileInfo(uri: Uri): kotlin.Pair<String, Long> {
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

    fun shareRecord(message: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            message
        )
        startActivity(Intent.createChooser(intent, "title"))
    }

    fun setPreviewDetail(rvPreview: RecyclerView) {
        rvPreview.layoutManager = GridLayoutManager(requireActivity(), 2)
        val previewAdapter = ImagingPreviewAdapter(requireActivity())
        rvPreview.adapter = previewAdapter
        previewAdapter.apply {
            onItemClickListener = object : ImagingPreviewAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    val bottomSheet = BottomSheetImagingPreview("Series ${position + 1}")
                    bottomSheet.show(
                        requireActivity().supportFragmentManager,
                        "BottomSheetImagingPreview"
                    )
                }
            }
        }
    }

    fun openDialPadWithNumber(phoneNumber: String) {
        requireActivity().startActivity(Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        })
    }

    fun sendEmail(email: String, subject: String) {
        requireActivity().startActivity(Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            putExtra(Intent.EXTRA_SUBJECT, subject)
        })
    }

    fun getFileExtensionFromUri(uri: Uri, context: Context): String? {
        // Try to get extension from file name
        val fileName = getFileNameFromUri(uri, context)
        fileName?.let {
            val dotIndex = it.lastIndexOf('.')
            if (dotIndex != -1 && dotIndex < it.length - 1) {
                return it.substring(dotIndex + 1)
            }
        }
        // Fallback: get extension from MIME type
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
    }

    private fun getFileNameFromUri(uri: Uri, context: Context): String? {
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    return cursor.getString(nameIndex)
                }
            }
        }
        // Fallback for file:// or unknown schemes
        uri.path?.let { path ->
            val cut = path.lastIndexOf('/')
            if (cut != -1 && cut + 1 < path.length) {
                return URLDecoder.decode(path.substring(cut + 1), "UTF-8")
            }
        }
        return null
    }

    fun getFileSizeFromUri(uri: Uri): String? {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (sizeIndex != -1 && it.moveToFirst()) {
                val fileSizeBytes = it.getLong(sizeIndex)
                val fileSizeString = when {
                    fileSizeBytes >= 1024 * 1024 -> String.format(
                        "%.2f MB",
                        fileSizeBytes / (1024.0 * 1024.0)
                    )

                    fileSizeBytes >= 1024 -> String.format("%.2f KB", fileSizeBytes / 1024.0)
                    else -> "$fileSizeBytes Bytes"
                }
                return fileSizeString
            }
        }
        return null
    }

    fun setUserSaveFileData(
        id: String,
        rvUserSavedFile: RecyclerView,
        llFileLayout: LinearLayout
    ) {
        fileViewModel.userSavedFileList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                llFileLayout.visibility = View.VISIBLE
                initSaveFileLayout(requireActivity(), rvUserSavedFile, it)
            } else {
                llFileLayout.visibility = View.GONE
            }
        }
        fileViewModel.getUserSavedFileList(id)
    }

    private fun initSaveFileLayout(
        mActivity: Activity,
        rvUserSavedFile: RecyclerView,
        userSavedFileList: List<UserSavedFile>?
    ) {
        rvUserSavedFile.layoutManager = GridLayoutManager(mActivity, 2)
        val userSavedFileAdapter = UserSavedFileAdapter(mActivity)
        userSavedFileAdapter.itemList = userSavedFileList ?: ArrayList()
        rvUserSavedFile.adapter = userSavedFileAdapter
        userSavedFileAdapter.onItemClickListener = onSaveFileItemClickListener
    }

     private val onSaveFileItemClickListener = object : UserSavedFileAdapter.OnClickCallback {
        override fun onClicked(item: UserSavedFile?, position: Int) {
            when (item?.file_type) {
                Constants.FILE_IMAGE,Constants.FILE_VIDEO -> {
                    val bottomSheet = BottomSheetUserSavedFilePreview(
                        requireActivity(),
                        item.file_name, item.file_type, item.file_path
                    )
                    bottomSheet.show(
                        requireActivity().supportFragmentManager,
                        "BottomSheetUserSavedFilePreview"
                    )
                }

                Constants.FILE_DOCUMENT -> {
                    val pdfFile = File(item.file_path)
                    if (!pdfFile.exists()) {
                        Log.e("PDF Error", "File does not exist: ${pdfFile.absolutePath}")
                        return
                    }
                    val pdfUri = FileProvider.getUriForFile(
                        requireActivity(),
                        requireActivity().packageName + ".provider",
                        pdfFile
                    )
                    openPdf(
                        requireActivity(),
                        pdfUri
                    )
                }

                else -> {

                }
            }
        }
    }

    fun openPdfFromRaw(context: Context) {
        val fileName = "sample.pdf" // file name in res/raw
        val outFile = File(context.cacheDir, fileName)
        val path = FileProvider.getUriForFile(
            requireActivity(),
            "com.mE.Health.provider", // must match manifest authority exactly
            outFile
        )
        openPdf(
            requireActivity(),
            path
        )
    }
}