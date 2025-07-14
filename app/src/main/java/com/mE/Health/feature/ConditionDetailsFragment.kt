package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AllergiesDetailFragmentBinding
import com.mE.Health.databinding.ConditionDetailFragmentBinding
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.databinding.MedicationDetailFragmentBinding
import com.mE.Health.databinding.MyPersonaFragmentBinding
import com.mE.Health.databinding.PractitionerDetailsFragmentBinding
import com.mE.Health.databinding.VisitsDetailFragmentBinding
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.feature.adapter.PractitionerAppointmentAdapter
import com.mE.Health.feature.adapter.PractitionerDetailOrganizationAdapter
import com.mE.Health.feature.adapter.PractitionerVisitAdapter
import com.mE.Health.utility.Constants
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
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
        binding.tvPractitionersViewAll.setOnClickListener(this)
        binding.tvMedicationViewAll.setOnClickListener(this)
        binding.tvVitalViewAll.setOnClickListener(this)
        binding.tvLabsViewAll.setOnClickListener(this)
        binding.tvVisitsViewAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvPractitionersViewAll -> {
                val fragment = PractitionersListFragment()
                val bundle = Bundle()
                bundle.putString(Constants.PN_TYPE,Constants.PRACTITIONER)
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
                bundle.putString(Constants.PN_TYPE,Constants.MEDICATION)
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
                bundle.putString(Constants.PN_TYPE,Constants.VITAL)
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
                bundle.putString(Constants.PN_TYPE,Constants.LAB)
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
                bundle.putString(Constants.PN_TYPE,Constants.VISIT)
                fragment.arguments = bundle
                addFragment(
                    R.id.fragment_container,
                    fragment,
                    "PractitionersListFragment",
                    "ConditionDetailsFragment"
                )
            }
        }
    }
}