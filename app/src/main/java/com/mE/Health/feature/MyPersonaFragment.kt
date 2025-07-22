package com.mE.Health.feature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mE.Health.R
import com.mE.Health.databinding.MyPersonaFragmentBinding
import com.mE.Health.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPersonaFragment : BaseFragment() {

    private lateinit var binding: MyPersonaFragmentBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var progressDialog: Dialog? = null
    private var isProfileClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyPersonaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        activeHomeMenu(requireActivity())
        initView()
        initHeader()
        observeProfileData()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        binding.rllMyProfile.setOnClickListener {
            isProfileClicked = true
            showLoader()
            viewModel.fetchProfile()
        }

        binding.rllMyHealth.setOnClickListener {
            refreshBottomMenu(requireActivity())
            replaceFragment(
                R.id.fragment_container,
                MyHealthFragment(),
                "MyHealthFragment",
                "MyPersonaFragment"
            )
        }
    }

    private fun observeProfileData() {
        viewModel.profile.observe(viewLifecycleOwner) {
            if (isProfileClicked) {
                isProfileClicked = false
                dismissLoader()
                refreshBottomMenu(requireActivity())
                replaceFragment(
                    R.id.fragment_container,
                    MyProfileFragment(),
                    "MyProfileFragment",
                    "MyPersonaFragment"
                )
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (isProfileClicked) {
                isProfileClicked = false
                dismissLoader()
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoader() {
        progressDialog = Dialog(requireContext())
        progressDialog?.setContentView(R.layout.progress_loader)
        progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }


    private fun dismissLoader() {
        progressDialog?.dismiss()
    }
}
