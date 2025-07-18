package com.mE.Health.feature

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mE.Health.R
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.ConnectProviderFragmentBinding
import com.mE.Health.feature.adapter.CountryStateListAdapter
import com.mE.Health.models.CountryState
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants
import com.mE.Health.viewmodels.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ConnectProviderFragment : BaseFragment() {

    private lateinit var binding: ConnectProviderFragmentBinding
    private val viewModel: ProviderViewModel by viewModels()
    private lateinit var listAdapter: CountryStateListAdapter
    private lateinit var countryStateList: ArrayList<CountryState>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConnectProviderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
        observeResponse()
        if (!isNetworkAvailable) {
            dialogOK(
                requireActivity(),
                resources.getString(R.string.whoops),
                resources.getString(R.string.network_error)
            )
            return
        }
        viewModel.getProviderList()
    }

    private fun initView() {
        countryStateList = ArrayList()
        binding.rvAssist.layoutManager = GridLayoutManager(requireActivity(), 2)
        listAdapter = CountryStateListAdapter(requireActivity(), countryStateList)
        binding.rvAssist.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : CountryStateListAdapter.OnClickCallback {
                override fun onClicked(view: View?, data: CountryState) {
                    val bundle = Bundle()
                    if (!data.country.isNullOrEmpty()) {
//                        bundle.putString(Constants.PN_TYPE, Constants.COUNTRY)
                        bundle.putString(Constants.PN_NAME, data.country)
                    } else {
//                        bundle.putString(Constants.PN_TYPE, Constants.STATE)
                        bundle.putString(Constants.PN_NAME, data.state)
                    }
                    val fragment = ProviderFragment()
                    fragment.arguments = bundle
                    DetailSingleton.providerDetailList = data.stateList as ArrayList
                    addFragment(
                        R.id.fragment_container,
                        fragment,
                        "ProviderFragment",
                        "ConnectProviderFragment"
                    )
                }
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
                else listAdapter.updateList(countryStateList)
            }
        })
    }

    private fun filterData(text: String) {
        val filterDataList = ArrayList<CountryState>()
        for (item in countryStateList) {
            val name = item.country ?: item.state
            if (name?.lowercase()?.contains(text.lowercase())!!) {
                filterDataList.add(item)
            }
        }
        listAdapter.updateList(filterDataList)
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.connect_provider)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun observeResponse() {
        viewModel.stateListData.observe(requireActivity()) {
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
                        if (it.data.data.byCountry != null) {
                            countryStateList.addAll(it.data?.data?.byCountry!!)
                        }
                        if (it.data?.data?.topStates != null) {
                            countryStateList.addAll(it.data?.data?.topStates!!)
                        }
                    }
                    listAdapter.updateList(countryStateList)
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }

        viewModel.providerList.observe(requireActivity()) {
            it.groupBy { it.state }.forEach {
                countryStateList.add(
                    CountryState(
                        state = it.key,
                        count = it.value.size.toString(),
                        stateList = it.value,
                        logo = it.value[0].logo_url
                    )
                )
            }
            listAdapter.updateList(countryStateList)
        }
    }
}