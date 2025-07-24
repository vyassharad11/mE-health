package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.ContactUsFragmentBinding
import com.mE.Health.utility.BottomSheetContactUs
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.FilterItem
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ContactUsFragment : BaseFragment() {

    private lateinit var binding: ContactUsFragmentBinding
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
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
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
}