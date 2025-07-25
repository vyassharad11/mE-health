package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.ImagingDetailFragmentBinding
import com.mE.Health.feature.adapter.ImagingPreviewAdapter
import com.mE.Health.utility.BottomSheetImagingPreview
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ImagingDetailsFragment : BaseFragment() {

    private lateinit var binding: ImagingDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImagingDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.imaging),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.imaging?.let { detail ->
            binding.apply {
                tvName.text = "${detail.modality_display} (${detail.modality_code})"
                tvDescription.text = detail.description
                tvDate.text = detail.started?.toDisplayDate()
                tvStatus.text = detail.status

                val statusDetail = Utilities.getLabUIStatus(requireActivity(), detail.status ?: "")
                tvStatus.apply {
                    text = detail.status?.capitalFirstChar()
                    setTextColor(statusDetail.first)
                    delegate.backgroundColor = statusDetail.second
                }
            }
        }

        binding.rvPreview.layoutManager = GridLayoutManager(requireActivity(), 2)
        val previewAdapter = ImagingPreviewAdapter(requireActivity())
        binding.rvPreview.adapter = previewAdapter
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

        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(Constants.IMAGING,"Share via","Text to share")
        }
    }
}