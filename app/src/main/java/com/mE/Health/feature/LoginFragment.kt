package com.mE.Health.feature

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.LoginFragmentBinding
import com.mE.Health.viewmodels.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : BaseFragment(),View.OnClickListener {

    private lateinit var binding: LoginFragmentBinding
     private var isPasswordVisible = false
    lateinit var mainViewModel: MainViewModel

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
    }

    private fun initView(){
//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//
//        mainViewModel.productsLiveData.observe(requireActivity(), Observer {
//            val text =  it.joinToString { x -> x.title + "\n\n" }
//            Log.i("=================","===========text: $text")
//        })
        binding.ivEye.setOnClickListener (this)
        binding.tvLogin.setOnClickListener (this)
        binding.tvForgot.setOnClickListener (this)
        binding.tvAlreadyUser.setOnClickListener (this)
        binding.tvSignup.setOnClickListener (this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ivEye->{
                val selection = binding.etPassword.selectionEnd
                if (TextUtils.isEmpty(binding.etPassword.text)) return
                if (!isPasswordVisible) {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ivEye.setImageResource(R.drawable.ic_close_eye)
                    isPasswordVisible = true
                } else {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod()
                    binding.ivEye.setImageResource(R.drawable.ic_eye)
                    isPasswordVisible = false
                }
                binding.etPassword.setSelection(selection)
            }
            R.id.tvLogin->{
                requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            }
             R.id.tvForgot->{
                 replaceFragment(
                     R.id.fragment_container,
                     ForgotFragment(),
                     "ForgotFragment",
                     "LoginFragment"
                 )
            }
             R.id.tvAlreadyUser->{
                 replaceFragment(
                     R.id.fragment_container,
                     MeUserFragment(),
                     "MeUserFragment",
                     "LoginFragment"
                 )
            }
            R.id.tvSignup->{
                 replaceFragment(
                     R.id.fragment_container,
                     RegistrationFirstFragment(),
                     "RegistrationFirstFragment",
                     "LoginFragment"
                 )
            }
        }
    }
}