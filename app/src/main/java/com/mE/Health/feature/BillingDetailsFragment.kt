package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.Claim
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Insurance
import com.mE.Health.databinding.BillingDetailFragmentBinding
import com.mE.Health.utility.Constants
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
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.billing),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.claim?.let { detail ->
            setUserSaveFileData(
                detail.claimId,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.claimId,Constants.BILLING,detail.name!!,detail.createdDate?.toDisplayDate()!!)
            binding.apply {
                tvBillingDate.text = detail.createdDate.toDisplayDate()
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
            generateShareMessage(detail)
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
        binding.tvViewInvoice.setOnClickListener {
            openPdfFromRaw(requireActivity())
        }
    }

    private fun generateShareMessage(detail: Claim) {
        shareMessage =
            "Here is my Billing information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.name}\n" +
                    "Status : ${detail.status?.capitalFirstChar()}\n" +
                    "Date : ${detail.createdDate?.toDisplayDate()}\n" +
                    "Amount : \$ ${detail.totalAmount}\n" +
                    "Insurance Details\n" +
                    "Insurance Company - ${fromJson(detail.insurance, Insurance::class.java).coverage?.display}\n" +
                    "Coverage Type - Primary\n" +
                    "Plan ID - BCBS-2023-456\n" +
                    "\n" +
                    "Thank You!!"
    }
}