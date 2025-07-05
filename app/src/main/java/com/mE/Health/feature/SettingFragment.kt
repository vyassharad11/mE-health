package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AdviceListFragmentBinding
import com.mE.Health.databinding.SettingFragmentBinding
import com.mE.Health.feature.adapter.AdviceAdapter
import com.mE.Health.utility.AdviceFilterItem
import com.mE.Health.utility.BottomSheetAdviceFilter
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.FilterItem
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SettingFragment : BaseFragment() {

    private lateinit var binding: SettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        var status = false
        var videoStatus = false
        binding.ivToggle.setOnClickListener {
            if (status) {
                status = false
                binding.ivToggle.setImageResource(R.drawable.toggle_off)
            } else {
                status = true
                binding.ivToggle.setImageResource(R.drawable.toggle_on)
            }
        }

        binding.ivVideoToggle.setOnClickListener {
            if (videoStatus) {
                videoStatus = false
                binding.ivVideoToggle.setImageResource(R.drawable.toggle_off)
            } else {
                videoStatus = true
                binding.ivVideoToggle.setImageResource(R.drawable.toggle_on)
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
    }
}