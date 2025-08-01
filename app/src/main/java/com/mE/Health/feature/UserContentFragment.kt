package com.mE.Health.feature

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.mE.Health.R
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.databinding.UserContentFragmentBinding
import com.mE.Health.feature.adapter.UploadDocItem
import com.mE.Health.utility.Constants
import com.mE.Health.utility.DialogOK
import com.mE.Health.utility.Utilities.openPdf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserContentFragment : BaseFragment() {

    private lateinit var binding: UserContentFragmentBinding

    companion object {
        var width = 0
        var height = 0
        var fileType = Constants.FILE_IMAGE
        var fileURI: Uri? = null
        var healthItemType = Constants.PRACTITIONER
        var healthItemId = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserContentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        appSession = fileViewModel.getAppSession()
        initView()
        initHeader()
    }

    private fun initView() {
        binding.tvFilterType.text= fileType
        when (fileType) {
            Constants.FILE_IMAGE -> {
                setImageView(fileURI!!)
            }

            Constants.FILE_VIDEO -> {
                setVideoView(fileURI!!)
            }

            Constants.FILE_DOCUMENT -> {
                showPDFView(fileURI!!)
            }
        }

        binding.rtvCancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.rtvSave.setOnClickListener {
            uploadFileToDatabase(fileURI!!)
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

        for (item in getAllMyHealthType()) {
            addChipToGroup(item.itemName)
        }
    }

    private fun setImageView(uri: Uri) {
        val fileInfo = getFileInfo(fileURI!!)
        val fileName = fileInfo.first

        // Convert file size to MB
        val fileSizeString = getFileSizeFromUri(fileURI!!)
        binding.tvImageSize.text = "File Size: $fileSizeString"
        binding.tvImageName.text = "Name : $fileName"

        // 3. Show image in ImageView
        Glide.with(requireContext())
            .load(uri)
            .into(binding.ivSelected)
        binding.ivSelected.visibility = View.VISIBLE
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
            add(UploadDocItem("Practitioner"))
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
        val fileInfo = getFileInfo(fileURI)
        val fileName = fileInfo.first

        // Convert file size to MB
        val fileSizeString = getFileSizeFromUri(fileURI)!!
        binding.tvImageSize.text = "File Size: $fileSizeString"
        binding.tvImageName.text = "Name : $fileName"

        binding.ivPdf.setOnClickListener {
            openPdf(requireActivity(), fileURI)
        }
    }

    private fun setVideoView(uri: Uri) {
        // Set VideoView size
        val retriever = android.media.MediaMetadataRetriever()
        retriever.setDataSource(requireContext(), uri)
        val width =
            retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                ?.toIntOrNull() ?: 0
        val height =
            retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                ?.toIntOrNull() ?: 0
        retriever.release()
        val params = binding.videoView.layoutParams
        params.width = width
        params.height = height
        binding.videoView.layoutParams = params
        binding.videoView.setVideoURI(fileURI)
        binding.videoView.visibility = View.VISIBLE
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        binding.videoView.start()

        val fileInfo = getFileInfo(fileURI!!)
        val fileName = fileInfo.first

        // Convert file size to MB
        val fileSizeString = getFileSizeFromUri(fileURI!!)
        binding.tvImageSize.text = "File Size: $fileSizeString"
        binding.tvImageName.text = "Name : $fileName"
    }

    private fun uploadFileToDatabase(uri: Uri) {
        // 1. Copy image to app-specific folder
        val appFolder = File(requireContext().filesDir, "mE-Health")
        if (!appFolder.exists()) appFolder.mkdirs()
        val fileName = generateCustomFileName()
        val destFile = File(appFolder, fileName)
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        // 2. Save image path to Room database
        val imagePath = destFile.absolutePath
        val fileSizeString = getFileSizeFromUri(uri)!!
        lifecycleScope.launch {
            val fileObject = UserSavedFile(
                user_id = appSession.getUserId(),
                health_type = healthItemType,
                health_item_id = healthItemId,
                file_name = fileName,
                size = fileSizeString,
                file_type = fileType,
                file_path = imagePath
            )
            fileViewModel.insertFile(fileObject)
        }
        val dialogOK = DialogOK(requireActivity(), "", "File saved successfully").apply {
            onClickCallback = object : DialogOK.OkClickCallback {
                override fun onOk() {
                    parentFragmentManager.setFragmentResult("UpdateView", Bundle())
                    requireActivity().onBackPressed()
                }
            }
        }
        dialogOK.show()
    }

    private fun generateCustomFileName(): String {
        val formattedTime = SimpleDateFormat(
            "MM-dd-yyyy-HH-mm-ss",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        val extension = getFileExtensionFromUri(fileURI!!, requireActivity())
        return when (fileType) {
            Constants.FILE_IMAGE -> {
                "IMG_$formattedTime.$extension"
            }

            Constants.FILE_VIDEO -> {
                "VID_$formattedTime.$extension"
            }

            Constants.FILE_DOCUMENT -> {
                "DOC_$formattedTime.$extension"
            }

            else -> {
                "FILE_$formattedTime.$extension"
            }
        }
    }
}