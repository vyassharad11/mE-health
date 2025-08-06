package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.ConditionDetailFragmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ConditionDetailsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: ConditionDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConditionDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
        setData()
    }

    private fun initHeader() {
        setHeaderBackProperties(binding.toolbar.ivBack)
        setHeaderUploadProperties(binding.toolbar.ivSetting,true)
        setHeaderTitleProperties(getString(R.string.condition),binding.toolbar.tvTitle,true)
    }

    private fun initView() {
        binding.tvPractitionersViewAll.setOnClickListener(this)
        binding.tvMedicationViewAll.setOnClickListener(this)
        binding.tvVitalViewAll.setOnClickListener(this)
        binding.tvLabsViewAll.setOnClickListener(this)
        binding.tvVisitsViewAll.setOnClickListener(this)
        binding.layoutSyncButton.llShareData.setOnClickListener(this)
    }

    private fun setData() {
        DetailSingleton.condition?.let { detail ->
            setUserSaveFileData(
                detail.id,
                binding.userSavedFileLayout.rvFile,
                binding.userSavedFileLayout.llFileLayout
            )
            setUserSelectedDetails(detail.id,Constants.CONDITIONS,detail.code_display!!,detail.recordedDate?.toDisplayDate()!!)

            binding.apply {
                tvName.text = detail.code_display
                Utilities.getConditionUIStatus(requireActivity(), detail.clinicalStatus ?: "").let {
                    tvStatus.text = detail.clinicalStatus?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
                tvOnsetDate.text = detail.onsetDate?.toDisplayDate()
                tvRecordedDate.text = detail.recordedDate?.toDisplayDate()
            }
            generateShareMessage(detail)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvPractitionersViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.PRACTITIONERS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
            R.id.tvMedicationViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.MEDICATIONS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
            R.id.tvVitalViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.VITALS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
             R.id.tvLabsViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.LABS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
             R.id.tvVisitsViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.VISITS)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
            R.id.llShareData -> {
                shareRecord(message = shareMessage)
            }
        }
    }

    private fun generateShareMessage(detail: Condition){
        shareMessage = "Here is my medical Condition information from mEinstein I had to share! You have to try mE!\n" +
                "https://bit.ly/4ipzMmF\n" +
                "\n" +
                "${detail.code_display}\n" +
                "Status: ${detail.clinicalStatus?.capitalFirstChar()}\n" +
                "Onset : ${detail.onsetDate?.toDisplayDate()}\n" +
                "Recorded : ${detail.recordedDate?.toDisplayDate()}\n" +
                "Category     : Chronic\n" +
                "Condition ID : #HYP2022105\n" +
                "\n" +
                "Thank You!"
    }
}