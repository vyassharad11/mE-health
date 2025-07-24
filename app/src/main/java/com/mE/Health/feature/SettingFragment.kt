package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.SettingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class SettingFragment : BaseFragment(), View.OnClickListener {

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

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
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

        binding.cvDeleteAccount.setOnClickListener(this)
        binding.cvHealthCare.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cvHealthCare -> {
                addFragment(
                    R.id.fragment_container,
                    ConnectProviderFragment(),
                    "ConnectProviderFragment",
                    "SettingFragment"
                )
            }

            R.id.cvDeleteAccount -> {
                addFragment(
                    R.id.fragment_container,
                    DeleteAccountFragment(),
                    "DeleteAccountFragment",
                    "SettingFragment"
                )
            }
        }
    }
}