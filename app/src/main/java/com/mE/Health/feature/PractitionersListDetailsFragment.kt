package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.databinding.PractitionerListDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PractitionersListDetailsFragment : BaseFragment() {

    private lateinit var binding: PractitionerListDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PractitionerListDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
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
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
    }

}