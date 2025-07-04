package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.MyPersonaFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MyPersonaFragment : BaseFragment() {

    private lateinit var binding: MyPersonaFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyPersonaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        activeHomeMenu(requireActivity())
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
        binding.rllMyProfile.setOnClickListener {
            refreshBottomMenu(requireActivity())
            replaceFragment(
                R.id.fragment_container,
                MyProfileFragment(),
                "MyProfileFragment",
                "MyPersonaFragment"
            )
        }

        binding.rllMyHealth.setOnClickListener {
            refreshBottomMenu(requireActivity())
            replaceFragment(
                R.id.fragment_container,
                MyHealthFragment(),
                "MyHealthFragment",
                "MyPersonaFragment"
            )
        }
    }
}