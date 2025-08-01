package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.DosageInstruction
import com.mE.Health.data.model.MedicationCode
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.AllergiesDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.fromJson
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AllergiesDetailsFragment : BaseFragment() {

    private lateinit var binding: AllergiesDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AllergiesDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.allergies),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        DetailSingleton.allergy?.let { detail ->
            setUserSelectedDetails(detail.id,Constants.ALLERGIES)
            binding.apply {
                tvRecordedDate.text = detail.recordedDate?.toDisplayDate()
                tvName.text = detail.code_display
                Utilities.getLabUIStatus(requireActivity(), detail.clinicalStatus ?: "").let {
                    tvStatus.text = detail.clinicalStatus?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
            }
            generateShareMessage(detail)
        }
        binding.layoutSyncButton.llShareData.setOnClickListener {
            shareRecord(message = shareMessage)
        }
    }

    private fun generateShareMessage(detail: AllergyIntolerance) {
        shareMessage =
            "Here is my Allergies information from mEinstein I had to share! You have to try mE!\n" +
                    "https://bit.ly/4ipzMmF\n" +
                    "\n" +
                    "\n" +
                    "${detail.code_display}\n" +
                    "Clinical Status : ${detail.clinicalStatus?.capitalFirstChar()}\n" +
                    "Recorded Date - ${detail.recordedDate?.toDisplayDate()}\n" +
                    "Allergy ID - ALG- 2024-392\n" +
                    "\n" +
                    "Conditions (2)\n" +
                    "Essential Hypertension : Clinical Status: Active\n" +
                    "Type 2 Diabetic : Clinical Status: Active\n" +
                    "Medications (2)\n" +
                    "Lisinopril 10mg : Clinical Status: Active\n" +
                    "Metformin 500mg : Clinical Status: Active\n\n" +
                    "Thank You!!"
    }
}