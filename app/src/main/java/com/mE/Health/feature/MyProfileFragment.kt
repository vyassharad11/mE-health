package com.mE.Health.feature

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mE.Health.R
import com.mE.Health.databinding.MyProfileFragmentBinding

import com.mE.Health.utility.Constants.GENDER_FEMALE
import com.mE.Health.utility.Constants.GENDER_MALE

import com.mE.Health.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MyProfileFragment : BaseFragment() {

    private lateinit var binding: MyProfileFragmentBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var status = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
        observeViewModel()
        viewModel.fetchProfile()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = ""
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvEdit.visibility = View.VISIBLE
        binding.toolbar.tvEdit.setOnClickListener {
            // Optional: Edit click logic
        }
    }

    private fun initView() {
        binding.ivToggle.setOnClickListener {
            status = !status
            binding.ivToggle.setImageResource(
                if (status) R.drawable.toggle_on else R.drawable.toggle_off
            )
        }

        binding.anniversaryCard.setOnClickListener {
            showAnniversaryDatePicker()

        }
    }

    private fun observeViewModel() {
        viewModel.profile.observe(viewLifecycleOwner) { data ->
            binding.tvName.text = "${data.first_name} ${data.last_name}"
            binding.tvPhone.text = "${data.countryCode ?: ""} ${data.phoneNumber ?: ""}"
            binding.tvEmail.text = data.email ?: ""
            binding.tvAddress.text = data.address ?: ""

            binding.tvGender.text = when (data.gender) {
                GENDER_MALE -> "Male"
                GENDER_FEMALE -> "Female"
                else -> "Other"
            }

            binding.tvDob.text = data.dateOfBirth ?: ""
            binding.ivToggle.setImageResource(
                if (data.isMarried == true) R.drawable.toggle_on else R.drawable.toggle_off
            )
            status = data.isMarried == true

        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAnniversaryDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.my_dialog_theme,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", month + 1, dayOfMonth, year)
                binding.tvAnniversary.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setCanceledOnTouchOutside(true)
        datePickerDialog.show()
    }


}