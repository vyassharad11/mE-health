package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.AllergiesDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

private const val s = Constants.ALLERGIES

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AllergiesDetailsFragment : BaseFragment() {

    private lateinit var binding: AllergiesDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllergiesDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderTitleProperties(getString(R.string.allergies),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.allergy?.let { detail ->
            binding.apply {
                tvRecordedDate.text = detail.recordedDate?.toDisplayDate()
                tvName.text = detail.code_display
                Utilities.getLabUIStatus(requireActivity(), detail.clinicalStatus ?: "").let {
                    tvStatus.text = detail.clinicalStatus?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
            }
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(Constants.ALLERGIES,"Share via","Text to share")
        }
    }
}