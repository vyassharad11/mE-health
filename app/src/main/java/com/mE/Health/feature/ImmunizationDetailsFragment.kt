package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.ImmunizationDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ImmunizationDetailsFragment : BaseFragment() {

    private lateinit var binding: ImmunizationDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImmunizationDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
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
        DetailSingleton.immunization?.let { detail ->
            binding.apply {
                tvName.text = detail.vaccineCode_display

                val text = "Recorded Date: ${detail.occurrenceDate?.toDisplayDate()}"
                tvRecordedDate.text = text
                tvStatus.text = detail.status?.capitalFirstChar()
                Utilities.getProcedureUIStatus(requireActivity(), detail.status ?: "").let {
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
            }
        }
    }
}