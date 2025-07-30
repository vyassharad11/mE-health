package com.mE.Health.feature

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.mE.Health.R
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.data.model.UserSavedImages
import com.mE.Health.databinding.UserContentFragmentBinding
import com.mE.Health.feature.adapter.UploadDocFilterAdapter
import com.mE.Health.feature.adapter.UploadDocItem
import com.mE.Health.utility.Constants
import com.mE.Health.viewmodels.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserContentFragment : BaseFragment() {

    private lateinit var binding: UserContentFragmentBinding
    private val viewModel: ProviderViewModel by viewModels()
    private var list = ArrayList<UserSavedImages>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserContentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        appSession = viewModel.getAppSession()
        initView()
        initHeader()
    }

    private fun initView() {
        val bundle = arguments
        if (bundle?.containsKey(Constants.FILE_PATH)!!) {
            val type = bundle.getString(Constants.FILE_TYPE)
            val fileURI = bundle.getString(Constants.FILE_PATH)
            binding.tvImageSize.text = "File Size: ${bundle.getString(Constants.FILE_LENGTH)}"
            binding.tvImageName.text = "Name : ${bundle.getString(Constants.FILE_NAME)}"
            binding.tvFilterType.text = type
            list.apply {
                add(
                    UserSavedImages(
                        bundle.getString(Constants.FILE_NAME) ?: "",
                        bundle.getString(Constants.FILE_LENGTH) ?: "",
                        type ?: "",
                        fileURI ?: ""
                    )
                )
            }
            val imgFile = fileURI?.toUri()
            if (type.equals(Constants.FILE_IMAGE)) {
                Glide.with(requireActivity())
                    .load(imgFile)
                    .into(binding.ivSelected)
                binding.ivSelected.visibility = View.VISIBLE
            } else if (type.equals(Constants.FILE_VIDEO)) {
                setVideoView()
            } else if (type.equals(Constants.FILE_DOCUMENT)) {
                showPDFView(fileURI?.toUri()!!)
            }
        }

        val itemList = getAllMyHealthType()
        binding.rvUploadDocFilter.layoutManager = GridLayoutManager(requireActivity(), 2)
        val docFilterAdapter = UploadDocFilterAdapter(requireActivity(), itemList)
        binding.rvUploadDocFilter.adapter = docFilterAdapter
        docFilterAdapter.apply {
            onItemClickListener = object : UploadDocFilterAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    itemList[position].isChecked = !itemList[position].isChecked
                    updateList(itemList)
                }
            }
        }

        binding.llFilterType.setOnClickListener {
            if (binding.chipsGroup.isVisible) {
                binding.ivArrow.rotation = 180.0f
                binding.chipsGroup.visibility = View.GONE
            } else {
                binding.ivArrow.rotation = 0.0f
                binding.chipsGroup.visibility = View.VISIBLE
            }
            binding.vwDivider.visibility =
                if (binding.vwDivider.isVisible) View.GONE else View.VISIBLE
        }

        for (item in itemList) {
            addChipToGroup(item.itemName)
        }

        binding.rtvSave.setOnClickListener {
//            pickImageFromStorage()
//            viewModel.updateFile(list,"pract1")
//            pickVideoFromStorage()
//            pickImageFromStorage()

//            val imagePath = getPathFromUri( fileURI)
//            if (imagePath != null) {
//                // 3. Store in Room
//                lifecycleScope.launch {
//                    val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "app_db").build()
//                    db.imageDao().insertImage(ImageEntity(imagePath = imagePath))
//                }
//            }
        }
    }

    private fun initHeader() {
        setHeaderTitleProperties(getString(R.string.user_content), binding.toolbar.tvTitle, true)
        setHeaderSettingProperties(binding.toolbar.ivSetting, true)
        binding.toolbar.appBar.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )
    }

    private fun addChipToGroup(text: String) {
        val chip = Chip(requireActivity())
        chip.text = text
        chip.closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tick_orange)
        chip.isCloseIconVisible = true
        chip.isClickable = true
        chip.isCheckable = false
        binding.chipsGroup.addView(chip as View)
        chip.setOnClickListener {
            chip.isCloseIconVisible = !chip.isCloseIconVisible
        }
    }

    private fun getAllMyHealthType(): ArrayList<UploadDocItem> {
        val typeList: ArrayList<UploadDocItem> = ArrayList()
        typeList.apply {
            add(UploadDocItem("Practictioner"))
            add(UploadDocItem("Appointments"))
            add(UploadDocItem("Conditions"))
            add(UploadDocItem("Labs"))
            add(UploadDocItem("Vitals"))
            add(UploadDocItem("Medications"))
            add(UploadDocItem("Visits"))
            add(UploadDocItem("Procedures"))
            add(UploadDocItem("Allergies"))
            add(UploadDocItem("Immunizations"))
            add(UploadDocItem("Billings"))
            add(UploadDocItem("Upload Documents"))
        }
        return typeList
    }

    private fun showPDFView(fileURI: Uri) {
        binding.ivPdf.visibility = View.VISIBLE
        binding.ivPdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileURI, "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireActivity(),
                    "No application found which can open the PDF file",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        var width = 0
        var height = 0
        var videoURI: Uri? = null
    }

    private fun setVideoView() {
        // Set VideoView size
        val params = binding.videoView.layoutParams
        params.width = width
        params.height = height
        binding.videoView.layoutParams = params
        binding.videoView.setVideoURI(videoURI)
        binding.videoView.visibility = View.VISIBLE
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.start()
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // 1. Copy image to app-specific folder
                val appFolder = File(requireContext().filesDir, "mE-Health")
                if (!appFolder.exists()) appFolder.mkdirs()
                val formattedTime = SimpleDateFormat(
                    "MM-dd-yyyy-HH-mm-ss",
                    Locale.getDefault()
                ).format(Calendar.getInstance().time)
                val fileName = "IMG_$formattedTime.jpg"
                val destFile = File(appFolder, fileName)
                requireContext().contentResolver.openInputStream(it)?.use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                // 2. Save image path to Room database
                val imagePath = destFile.absolutePath
                val fileSizeBytes = destFile.length()
                val fileSizeString = when {
                    fileSizeBytes >= 1024 * 1024 -> String.format(
                        "%.2f MB",
                        fileSizeBytes / (1024.0 * 1024.0)
                    )

                    fileSizeBytes >= 1024 -> String.format("%.2f KB", fileSizeBytes / 1024.0)
                    else -> "$fileSizeBytes Bytes"
                }
                lifecycleScope.launch {
                    val fileObject = UserSavedFile(
                        user_id = appSession.getUserId(),
                        health_type = Constants.PRACTITIONER,
                        file_name = fileName,
                        size = fileSizeString,
                        file_type = Constants.FILE_IMAGE,
                        file_path = imagePath
                    )
                    viewModel.insertFile(fileObject)
                }

                binding.tvImageSize.text = "File Size: $fileSizeString"
                binding.tvImageName.text = "Name : ${getFileNameFromUri(it,requireActivity())}"
                // 3. Show image in ImageView
                Glide.with(requireContext())
                    .load(destFile)
                    .into(binding.ivSelected)
                binding.ivSelected.visibility = View.VISIBLE
            }
        }


    // Call this function to open the image picker
    private fun pickImageFromStorage() {
        pickImageLauncher.launch("image/*")
    }

    fun getFileNameFromUri(uri: Uri, context: Context): String? {
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
}