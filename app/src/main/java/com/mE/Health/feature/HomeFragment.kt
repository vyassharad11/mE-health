package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.HomeFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as HomeActivity).setBottomNavigationVisibility()
        initView()
    }

    private fun initView(){
        binding.rllAiAssistant.setOnClickListener {
            (requireContext() as HomeActivity).updateNavMenuVisibility(View.GONE)
            replaceFragment(
                R.id.fragment_container,
                AssistantFragment(),
                "AssistantFragment",
                "HomeFragment"
            )
        }
    }
}