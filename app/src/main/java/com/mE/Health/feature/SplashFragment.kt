package com.mE.Health.feature

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import com.mE.Health.R
import com.mE.Health.databinding.SplashFragmentBinding
import com.mE.Health.feature.LoginFragment

class SplashFragment  : BaseFragment() {

    private var _binding: SplashFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            replaceFragmentWithoutBack(
                R.id.fragment_container,
                LoginFragment(),
                "LoginFragment"
            )
        }, 5000L)
    }
}