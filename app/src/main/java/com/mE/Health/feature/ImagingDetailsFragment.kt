package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.ImagingDetailFragmentBinding
import com.mE.Health.feature.adapter.ImagingPreviewAdapter
import com.mE.Health.feature.adapter.UploadDocFilterAdapter
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.BottomSheetImagingPreview
import com.mE.Health.utility.FilterItem
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
        binding.toolbar.tvTitle.text = getString(R.string.imaging)
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
        binding.rvPreview.layoutManager = GridLayoutManager(requireActivity(), 2)
        val previewAdapter = ImagingPreviewAdapter(requireActivity())
        binding.rvPreview.adapter = previewAdapter
        previewAdapter.apply {
            onItemClickListener = object : ImagingPreviewAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    val bottomSheet = BottomSheetImagingPreview("Series ${position+1}")
                    bottomSheet.show(
                        requireActivity().supportFragmentManager,
                        "BottomSheetImagingPreview"
                    )
                }
            }
        }
    }
}