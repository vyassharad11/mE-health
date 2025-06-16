package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.ForgotFragmentBinding
import com.mE.Health.databinding.MeUserFragmentBinding
import com.mE.Health.viewmodels.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MeUserFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: MeUserFragmentBinding
    lateinit var mainViewModel: MainViewModel

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
//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//
//        mainViewModel.productsLiveData.observe(requireActivity(), Observer {
//            val text =  it.joinToString { x -> x.title + "\n\n" }
//            Log.i("=================","===========text: $text")
//        })

        binding.tvBackLogin.setOnClickListener(this)
        binding.rllContinue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rllContinue -> {
                addFragment(
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