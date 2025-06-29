package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.AssistantFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AIAssistantFragment : BaseFragment() {

    private lateinit var binding: AssistantFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AssistantFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView(){
        binding.rllAiAssist.setOnClickListener {
            addFragment(
                R.id.fragment_container,
                AssistFragment(),
                "AssistFragment",
                "AssistantFragment"
            )
        }

        binding.rllAdvice.setOnClickListener {
            addFragment(
                R.id.fragment_container,
                AdviceListFragment(),
                "AdviceListFragment",
                "AssistantFragment"
            )
        }
    }

    private fun initHeader(){
        binding.toolbar.tvTitle.text = getString(R.string.ai_assistant)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}