package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.MeUserFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MeUserFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: MeUserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MeUserFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.tvBackLogin.setOnClickListener(this)
        binding.rllContinue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rllContinue -> {
                addFragmentLogin(
                    R.id.fragment_container,
                    MeUserSecondFragment(),
                    "MeUserSecondFragment",
                    "MeUserFragment"
                )
            }

            R.id.tvBackLogin -> {
                requireActivity().onBackPressed()
            }
        }
    }
}