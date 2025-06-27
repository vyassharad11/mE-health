package com.mE.Health.feature

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.ProviderFragmentBinding
import com.mE.Health.feature.adapter.ProviderAdapter
import com.mE.Health.models.ProviderDetail
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants
import com.mE.Health.viewmodels.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ProviderFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: ProviderFragmentBinding
    private val viewModel: ProviderViewModel by viewModels()
    private lateinit var providerAdapter: ProviderAdapter
    private lateinit var providerList: ArrayList<ProviderDetail>
    private lateinit var providerRecentList: ArrayList<ProviderDetail>
    private lateinit var providerConnectedList: ArrayList<ProviderDetail>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProviderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
//        observeResponse()
    }

    private fun initView() {
        val bundle = arguments
        val type = bundle?.getString(Constants.PN_TYPE, "")
        val name = bundle?.getString(Constants.PN_NAME, "")
//        viewModel.providerList(type!!, "", name!!)
        initHeader(name!!)

        binding.tvAll.setOnClickListener(this)
        binding.tvRecent.setOnClickListener(this)
        binding.tvConnected.setOnClickListener(this)


        providerList = ArrayList()
        setDummyData()
        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())
        providerAdapter = ProviderAdapter(requireActivity(),Constants.ALL, providerList)
        binding.rvAssist.adapter = providerAdapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.trim().toString().isNotEmpty())
                    filterData(s.toString())
                else  providerAdapter.updateList(getTabList())
            }
        })
    }


    private fun initHeader(title: String) {
        binding.toolbar.tvTitle.text = title
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAll -> {
                refreshButton()
                updateButton(binding.tvAll)
                providerAdapter.updateListWithTab(providerList, Constants.ALL)
            }

            R.id.tvRecent -> {
                refreshButton()
                updateButton(binding.tvRecent)
                providerAdapter.updateListWithTab(if (providerList.size>4)providerList.subList(0,4) else ArrayList<ProviderDetail>(), Constants.RECENT)
            }

            R.id.tvConnected -> {
                refreshButton()
                updateButton(binding.tvConnected)
                providerAdapter.updateListWithTab(if (providerList.size>4)providerList.subList(0,3) else ArrayList<ProviderDetail>(), Constants.CONNECTED)
            }
        }
    }


    private fun observeResponse() {
        viewModel.providerListData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog()
                }

                is NetworkResult.Error -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }

                is NetworkResult.Success -> {
                    hideProgressDialog()
                    if (it.data?.data != null) {
                        if (it.data.data != null) {
                            providerList.addAll(it.data?.data!!)
                        }
                    }
                    providerAdapter.updateList(providerList)
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }
    }

    private fun filterData(text: String) {
        val filterDataList = ArrayList<ProviderDetail>()
        for (item in getTabList()) {
            val name = item.practiceName
            if (name?.lowercase()?.contains(text.lowercase())!!) {
                filterDataList.add(item)
            }
        }
        providerAdapter.updateList(filterDataList)
    }

    private fun getTabList() : ArrayList<ProviderDetail>{
        return when (providerAdapter.type) {
            Constants.ALL -> {
                providerList
            }
            Constants.RECENT -> {
                providerRecentList
            }
            else -> {
                providerConnectedList
            }
        }
    }

    private fun updateButton(textView: TextView) {
        textView.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_bg_checked)
        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
    }

    private fun refreshButton() {
        binding.tvAll.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.transparent
            )
        )
        binding.tvRecent.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.transparent
            )
        )
        binding.tvConnected.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.transparent
            )
        )
        binding.tvAll.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_333333))
        binding.tvRecent.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.color_333333
            )
        )
        binding.tvConnected.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.color_333333
            )
        )
    }

    private fun setDummyData(){
        providerList.apply {
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cambridge University Hospitals"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cambridge University Hospitals"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cambridge University Hospitals"))
            add(ProviderDetail(practiceName ="Cambridge University Hospitals"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cleveland Clinic London"))
            add(ProviderDetail(practiceName ="Cambridge University Hospitals"))
        }

        providerRecentList = ArrayList()
        providerConnectedList = ArrayList()
        providerRecentList.addAll(providerList.subList(0,4))
        providerConnectedList.addAll(providerList.subList(0,3))
    }
}