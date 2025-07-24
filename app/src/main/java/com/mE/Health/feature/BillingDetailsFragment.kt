package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Insurance
import com.mE.Health.databinding.BillingDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.fromJson
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class BillingDetailsFragment : BaseFragment() {

    private lateinit var binding: BillingDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BillingDetailFragmentBinding.inflate(inflater, container, false)
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
        setHeaderSettingProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.billing),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.claim?.let { detail ->
            binding.apply {

                tvBillingDate.text = detail.createdDate?.toDisplayDate()
                tvClinicName.text = detail.name
                tvBillingAmount.text = "\$ ${detail.totalAmount}"

                tvCompanyName.text =
                    fromJson(detail.insurance, Insurance::class.java).coverage?.display
                Utilities.getVisitUIStatus(requireActivity(), detail.status ?: "").let {
                    tvStatus.apply {
                        text = detail.status?.capitalFirstChar()
                        setTextColor(it.first)
                        delegate.backgroundColor = it.second
                    }
                }
            }
        }
    }
}