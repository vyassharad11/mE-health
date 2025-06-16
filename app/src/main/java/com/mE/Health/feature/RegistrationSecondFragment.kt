package com.mE.Health.feature

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.RegistrationFirstFragmentBinding
import com.mE.Health.databinding.RegistrationSecondFragmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegistrationSecondFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: RegistrationSecondFragmentBinding
    private var isPasswordVisible = false
    private var isCPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationSecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initHeader()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = ""
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        binding.ivPasswordEye.setOnClickListener(this)
        binding.ivConfirmPasswordEye.setOnClickListener(this)
        binding.rllContinue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivPasswordEye->{
                val selection = binding.etPassword.selectionEnd
                if (TextUtils.isEmpty(binding.etPassword.text)) return
                if (!isPasswordVisible) {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ivPasswordEye.setImageResource(R.drawable.ic_close_eye)
                    isPasswordVisible = true
                } else {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod()
                    binding.ivPasswordEye.setImageResource(R.drawable.ic_eye)
                    isPasswordVisible = false
                }
                binding.etPassword.setSelection(selection)
            }

            R.id.ivConfirmPasswordEye -> {
                val selection = binding.etConfirmPassword.selectionEnd
                if (TextUtils.isEmpty(binding.etConfirmPassword.text)) return
                if (!isCPasswordVisible) {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_close_eye)
                    isCPasswordVisible = true
                } else {
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod()
                    binding.ivConfirmPasswordEye.setImageResource(R.drawable.ic_eye)
                    isCPasswordVisible = false
                }
                binding.etConfirmPassword.setSelection(selection)
            }

            R.id.rllContinue -> {
                requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}