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
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.ProviderDTO
import com.mE.Health.databinding.ProviderFragmentBinding
import com.mE.Health.feature.adapter.ProviderAdapter
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
    private lateinit var providerList: ArrayList<ProviderDTO>
    private lateinit var providerRecentList: ArrayList<ProviderDTO>
    private lateinit var providerConnectedList: ArrayList<ProviderDTO>

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
        observeResponse()
    }

    private fun initView() {
        val bundle = arguments
        val name = bundle?.getString(Constants.PN_NAME, "")
        initHeader(name!!)
        binding.tvAll.setOnClickListener(this)
        binding.tvRecent.setOnClickListener(this)
        binding.tvConnected.setOnClickListener(this)

        providerList = ArrayList()
        providerList = DetailSingleton.providerDetailList!!
        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())
        providerAdapter = ProviderAdapter(requireActivity(),Constants.ALL, providerList)
        binding.rvAssist.adapter = providerAdapter
        providerAdapter.onItemClickListener = object : ProviderAdapter.OnClickCallback {
                override fun onClicked(detail: ProviderDTO, position: Int) {
                    providerList[position].isRecent = true
                    viewModel.updateUserProviderAction(true,detail.id)
                }
            }

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
                binding.etSearch.setText("")
                providerAdapter.type = Constants.ALL
                refreshButton()
                updateButton(binding.tvAll)
                providerAdapter.updateListWithTab(providerList, Constants.ALL)
            }

            R.id.tvRecent -> {
                binding.etSearch.setText("")
                providerAdapter.type = Constants.RECENT
                refreshButton()
                updateButton(binding.tvRecent)
                val list = providerList.filter { it.isRecent  }
                providerAdapter.updateListWithTab( list, Constants.RECENT)
            }

            R.id.tvConnected -> {
                binding.etSearch.setText("")
                providerAdapter.type = Constants.CONNECTED
                refreshButton()
                updateButton(binding.tvConnected)
                providerAdapter.updateListWithTab( ArrayList<ProviderDTO>(), Constants.CONNECTED)
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
//                            providerList.addAll(it.data?.data!!)
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
        val filterDataList = ArrayList<ProviderDTO>()
        for (item in getTabList()) {
            val name = item.practice_name
            if (name.lowercase().contains(text.lowercase())) {
                filterDataList.add(item)
            }
        }
        providerAdapter.updateList(filterDataList)
    }

    private fun getTabList() : ArrayList<ProviderDTO>{
        return when (providerAdapter.type) {
            Constants.ALL -> {
                providerList
            }
            Constants.RECENT -> {
                ArrayList<ProviderDTO>()
            }
            else -> {
                ArrayList<ProviderDTO>()
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
}