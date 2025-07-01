package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.HomeFragmentBinding
import com.mE.Health.viewmodels.ProviderViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: ProviderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
    }

    private fun initView() {
        binding.rllAiAssistant.setOnClickListener {
            (requireActivity() as HomeActivity).updateMenu(View.GONE)
            replaceFragment(
                R.id.fragment_container,
                AIAssistantFragment(),
                "AIAssistantFragment",
                "HomeFragment"
            )
        }
        binding.tvAIReadMore.setOnClickListener {
            openReadMoreDialog(requireActivity(), getString(R.string.me_ai_assistant), getString(R.string.ai_assistant_read_more))
        }
        binding.tvDataReadMore.setOnClickListener {
            openReadMoreDialog(requireActivity(), getString(R.string.data_marketplace), getString(R.string.market_place_read_more))
        }
        binding.tvMarketPlace.setOnClickListener {
            (requireActivity() as HomeActivity).updateMenu(View.GONE)
            replaceFragment(
                R.id.fragment_container,
                ConnectProviderFragment(),
                "ConnectProviderFragment",
                "HomeFragment"
            )
        }
    }

    fun navClickAction(){
        replaceFragment(
            R.id.fragment_container,
            MyPersonaFragment(),
            "MyPersonaFragment",
            "HomeFragment"
        )
    }
}