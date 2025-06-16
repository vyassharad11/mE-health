package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.AssistFragmentBinding
import com.mE.Health.databinding.AssistantFragmentBinding
import com.mE.Health.databinding.HomeFragmentBinding
import com.mE.Health.feature.adapter.AssistAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AssistFragment : BaseFragment() {

    private lateinit var binding: AssistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AssistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as HomeActivity).setBottomNavigationVisibility()
        initView()
        initHeader()
    }

    private fun initView(){
        binding.rvAssist.layoutManager = GridLayoutManager(requireActivity(), 2)
        var recyclerAdapter = AssistAdapter(requireActivity())
        binding.rvAssist.adapter = recyclerAdapter
    }

    private fun initHeader(){
        binding.toolbar.tvTitle.text = getString(R.string.assist)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}