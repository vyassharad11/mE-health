package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DosageInstruction
import com.mE.Health.data.model.MedicationCode
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.MedicationDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.fromJson
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MedicationDetailsFragment : BaseFragment() {

    private lateinit var binding: MedicationDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MedicationDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        setDetails()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.medication)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun setDetails() {
        DetailSingleton.medication?.let { detail ->
            binding.tvMedicationDisplay.text = detail.medicationCode_display
            binding.tvMedicationId.text =
                fromJson(detail.medicationCode, MedicationCode::class.java).code
            binding.tvDosageInstruction.text =
                fromJson(detail.dosageInstruction, DosageInstruction::class.java).text
            binding.tvReason.text = fromJson(detail.reasonCode, ReasonCode::class.java).display
            binding.tvDate.text = detail.authoredOn?.toDisplayDate()
            binding.tvStatus.apply {
                text = detail.status?.capitalFirstChar()
                val statusDetail = Utilities.getLabUIStatus(requireActivity(), detail.status ?: "")
                setTextColor(statusDetail.first)
                delegate.backgroundColor = statusDetail.second
            }
        }
    }
}