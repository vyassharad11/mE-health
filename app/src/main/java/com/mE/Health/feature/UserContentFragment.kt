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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.databinding.UserContentFragmentBinding
import com.mE.Health.feature.adapter.UploadDocFilterAdapter
import com.mE.Health.feature.adapter.UploadDocItem
import dagger.hilt.android.AndroidEntryPoint


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
        if (bundle?.containsKey("FILE_PATH")!!) {
            val type = bundle.getString("TYPE")
            val fileURI = bundle.getString("FILE_PATH")
            binding.tvImageSize.text = "File Size: " + bundle.getString("FILE_LENGTH")
            binding.tvImageName.text = "Name : " + bundle.getString("FILE_NAME")
            binding.tvFilterType.text = type
            val imgFile = Uri.parse(fileURI)
            // on below line we are checking if the image file exist or not.
//            if (imgFile.exists()) {
//                Log.i(
//                    "=========",
//                    "==== : $imagePath"
//                )
            // on below line we are creating an image bitmap variable
            // and adding a bitmap to it from image file.
            if (type.equals("Image")) {
                Glide.with(requireActivity())
                    .load(imgFile)
                    .into(binding.ivSelected)
                binding.ivSelected.visibility = View.VISIBLE
            } else if (type.equals("Video")) {
                val uri = Uri.parse(fileURI)
                binding.videoView.visibility = View.VISIBLE
                // setting uri to video view
                binding.videoView.setVideoURI(uri)

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
            } else if (type.equals("Pdf")) {

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
}