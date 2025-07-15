package com.mE.Health.utility.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    abstract val layoutRes: Int
    abstract val viewModelClass: Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup DataBinding
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[viewModelClass]

        // Call setup hooks for child activities
        setupViews()
        observeViewModel()
    }

    open fun setupViews() {}

    open fun observeViewModel() {}
}
