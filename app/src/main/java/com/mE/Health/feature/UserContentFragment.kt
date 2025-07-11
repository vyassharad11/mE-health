package com.mE.Health.feature

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.databinding.UserContentFragmentBinding
import com.mE.Health.feature.adapter.UploadDocFilterAdapter
import com.mE.Health.feature.adapter.UploadDocItem
import com.mE.Health.utility.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserContentFragment : BaseFragment() {

    private lateinit var binding: UserContentFragmentBinding

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
            val imgFile = fileURI?.toUri()
            if (type.equals(Constants.FILE_IMAGE)) {
                Glide.with(requireActivity())
                    .load(imgFile)
                    .into(binding.ivSelected)
                binding.ivSelected.visibility = View.VISIBLE
            } else if (type.equals(Constants.FILE_VIDEO)) {
                showVideoView(fileURI?.toUri()!!)
            } else if (type.equals(Constants.FILE_DOCUMENT)) {
                Log.i("================PATH", "=====${fileURI}")
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

        binding.tvFilterType.setOnClickListener {
            binding.rvUploadDocFilter.visibility =
                if (binding.rvUploadDocFilter.isVisible) View.GONE else View.VISIBLE
            binding.vwDivider.visibility =
                if (binding.vwDivider.isVisible) View.GONE else View.VISIBLE
        }
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.user_content)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }

        binding.toolbar.appBar.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )
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

    private fun showVideoView(videoUrl:Uri){
        binding.videoView.visibility = View.VISIBLE
        // setting uri to video view
        binding.videoView.setVideoURI(videoUrl)
        // Media controls
        val mediaController = MediaController(requireActivity())
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)

        binding.videoView.setOnPreparedListener {
            // Starting the video when ready
            binding.videoView.start()
        }

        binding.videoView.setOnErrorListener { _, what, extra ->
            // Handling video playback errors
            println("Video playback error: what=$what, extra=$extra")
            true
        }
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

    private fun openFile(url: File) {
        try {
            val uri = Uri.fromFile(url)

            val intent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip")
            } else if (url.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed")
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (url.toString().contains(".jpg") || url.toString()
                    .contains(".jpeg") || url.toString().contains(".png")
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                url.toString().contains(".mpeg") || url.toString()
                    .contains(".mpe") || url.toString().contains(".mp4") || url.toString()
                    .contains(".avi")
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            requireActivity().startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(),
                "No application found which can open the file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}