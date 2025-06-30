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
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AdviceListFragmentBinding
import com.mE.Health.databinding.ConnectProviderFragmentBinding
import com.mE.Health.feature.adapter.AdviceAdapter
import com.mE.Health.feature.adapter.CountryStateListAdapter
import com.mE.Health.feature.adapter.MyHealthFilterAdapter
import com.mE.Health.models.CountryState
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants
import com.mE.Health.viewmodels.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AdviceListFragment : BaseFragment() {

    private lateinit var binding: AdviceListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdviceListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        binding.rvAdvice.layoutManager = LinearLayoutManager(requireActivity())
        val listAdapter = AdviceAdapter(requireActivity())
        binding.rvAdvice.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : AdviceAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    openReadMoreDialog(
                        requireActivity(),
                        "",
                        getString(R.string.appointment_description)
                    )
                }
            }
        }
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
        binding.toolbar.ivSetting.setImageResource(R.drawable.ic_filter_primary)
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }
}