package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.ConnectProviderFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ConnectProviderFragment : Fragment() {

    private lateinit var binding: ConnectProviderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConnectProviderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initHeader()
    }

    private fun initView(){
        (requireContext() as HomeActivity).setBottomNavigationVisibility()
    }

    private fun initHeader(){
        binding.toolbar.tvTitle.text = getString(R.string.connect_provider)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}