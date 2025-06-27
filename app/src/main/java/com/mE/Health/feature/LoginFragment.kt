package com.mE.Health.feature

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.Slide
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.LoginFragmentBinding
import com.mE.Health.models.LoginRequest
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants.USER_TYPE
import com.mE.Health.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class LoginFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: LoginFragmentBinding
    private var isPasswordVisible = false
    private val viewModel: LoginViewModel by viewModels()
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeResponse()
    }

    private fun initView() {
        binding.ivEye.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
        binding.tvForgot.setOnClickListener(this)
        binding.tvAlreadyUser.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.etEmail.setText("testmeuser01@yopmail.com")
        binding.etPassword.setText("12345678")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivEye -> {
                val selection = binding.etPassword.selectionEnd
                if (TextUtils.isEmpty(binding.etPassword.text)) return
                if (!isPasswordVisible) {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.ivEye.setImageResource(R.drawable.ic_close_eye)
                    isPasswordVisible = true
                } else {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod()
                    binding.ivEye.setImageResource(R.drawable.ic_eye)
                    isPasswordVisible = false
                }
                binding.etPassword.setSelection(selection)
            }

            R.id.tvLogin -> {
                if (isValid) {
                    viewModel.userLogin(
                        request = LoginRequest(
                            email,
                            password,
                            "test",
                            USER_TYPE
                        )
                    )
                }
//                requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
//                requireActivity().finish()
            }

            R.id.tvForgot -> {
                replaceFragment(
                    R.id.fragment_container,
                    ForgotFragment(),
                    "ForgotFragment",
                    "LoginFragment"
                )
            }

            R.id.tvAlreadyUser -> {
                replaceFragment(
                    R.id.fragment_container,
                    MeUserFragment(),
                    "MeUserFragment",
                    "LoginFragment"
                )
            }

            R.id.tvSignup -> {
                replaceFragment(
                    R.id.fragment_container,
                    RegistrationFirstFragment(),
                    "RegistrationFirstFragment",
                    "LoginFragment"
                )
            }
        }
    }

    private fun observeResponse() {
        viewModel.loginStateData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog()
                }

                is NetworkResult.Error -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }

                is NetworkResult.Success -> {
                    hideProgressDialog()
                    requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
                    requireActivity().finish()
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }
    }

    private val isValid: Boolean
        get() {
            var isValid = true
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                isValid = false
            }
            if (TextUtils.isEmpty(password)) {
                isValid = false
            }
            return isValid
        }
}