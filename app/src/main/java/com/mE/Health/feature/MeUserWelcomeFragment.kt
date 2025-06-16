package com.mE.Health.feature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.MeUserSecondFragmentBinding
import com.mE.Health.databinding.MeUserWelcomeFragmentBinding
import com.mE.Health.feature.adapter.RadioButtonListAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MeUserWelcomeFragment : BaseFragment() {

    private lateinit var binding: MeUserWelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MeUserWelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.continueLayout.rtvButton.text = getString(R.string.all_set)
        binding.continueLayout.rllContinue.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }
}