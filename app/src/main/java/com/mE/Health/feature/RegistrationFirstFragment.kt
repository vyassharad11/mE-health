package com.mE.Health.feature

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.databinding.RegistrationFirstFragmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegistrationFirstFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: RegistrationFirstFragmentBinding
    private lateinit var datePickerDialog : DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegistrationFirstFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initHeader()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = ""
        binding.toolbar.appBar.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.white))
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() {
        binding.tvMale.setOnClickListener(this)
        binding.tvFeMale.setOnClickListener(this)
        binding.tvOther.setOnClickListener(this)
        binding.rllContinue.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.etCountryCode.setText(countryDefault)
        initDatePicker()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvMale -> {
                refreshGender()
                updateGender(binding.tvMale)
            }

            R.id.tvFeMale -> {
                refreshGender()
                updateGender(binding.tvFeMale)
            }

            R.id.tvOther -> {
                refreshGender()
                updateGender(binding.tvOther)
            }

            R.id.etDateOfBirth -> {
                showDatePicker()
            }

            R.id.rllContinue -> {
                addFragment(
                    R.id.fragment_container,
                    RegistrationSecondFragment(),
                    "RegistrationSecondFragment",
                    "RegistrationFirstFragment"
                )
            }
        }
    }

    private fun refreshGender() {
        binding.tvMale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_bg)
        binding.tvFeMale.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_bg)
        binding.tvOther.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_bg)
        binding.tvMale.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_333333))
        binding.tvFeMale.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.color_333333
            )
        )
        binding.tvOther.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.color_333333
            )
        )
    }

    private fun updateGender(textView: TextView) {
        textView.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_bg_checked)
        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
    }

    private fun initDatePicker() {
        val calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            requireActivity(), { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.etDateOfBirth.setText(formattedDate.toString())

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
    }

    private fun showDatePicker(){
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_FF6605))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_FF6605))
    }
}