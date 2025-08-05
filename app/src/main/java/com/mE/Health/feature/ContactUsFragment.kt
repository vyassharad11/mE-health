package com.mE.Health.feature

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mE.Health.R
import com.mE.Health.databinding.ContactUsFragmentBinding
import com.mE.Health.models.ContactUsRequest
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.BottomSheetContactUs
import com.mE.Health.utility.DialogOK
import com.mE.Health.viewmodels.DeleteAccountViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ContactUsFragment : BaseFragment() {

    private lateinit var binding: ContactUsFragmentBinding
    private val viewModel: DeleteAccountViewModel by viewModels()
    private var filterList = ArrayList<String>()
    private var strEnquiries = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContactUsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSession = fileViewModel.getAppSession()
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
        observeResponse()
    }

    private fun initHeader() {
        binding.toolbar.appBar.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.transparent
            )
        )
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        getSubjectList()
        binding.llEnquiries.setOnClickListener {
            val bottomSheet = BottomSheetContactUs(filterList, strEnquiries)
            bottomSheet.setOnCompleteListener(object : BottomSheetContactUs.OnCompleteListener {
                override fun onComplete(item: String) {
                    strEnquiries = item
                    binding.tvEnquiries.text = item
                }
            })
            bottomSheet.show(
                requireActivity().supportFragmentManager, "BottomSheetContactUs"
            )
        }

        binding.rtvSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (TextUtils.isEmpty(strEnquiries)) {
                Toast.makeText(requireActivity(), "Please select subject", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(message.trim())) {
                Toast.makeText(requireActivity(), "Please enter your thoughts", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val userData = appSession.getUserData()?.data
            val request = ContactUsRequest(
                user = userData?.userId!!,
                first_name = userData.firstName!!,
                last_name = userData.lastName!!,
                subject = strEnquiries,
                phone = userData.phone!!,
                address = userData.walletAddress?: "",
                email = userData.email!!,
                message = message
            )
            viewModel.makeContactUsApiCall(request)
        }
    }

    private fun getSubjectList(): ArrayList<String> {
        filterList = ArrayList()
        filterList.apply {
            add("Application Bugs")
            add("Application Feedback")
            add("Application Support")
            add("Business Claim issues")
            add("Business Claim Registration")
            add("Feature Request")
            add("Investor Enquiries")
            add("Technical Help")
            add("General Questions")
        }
        return filterList
    }

    private fun observeResponse() {
        viewModel.contactUsStateData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog()
                }

                is NetworkResult.Error -> {
                    hideProgressDialog()
                    showDialogOk(it.message!!)
                }

                is NetworkResult.Success -> {
                    hideProgressDialog()
                    DialogOK(requireActivity(), "",  it.data?.mETextRes ?: "Thank You. We will get back to you shortly.").apply {
                        onClickCallback = object : DialogOK.OkClickCallback {
                            override fun onOk() {
                                requireActivity().onBackPressed()
                            }
                        }
                    }.show()
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }
    }
}