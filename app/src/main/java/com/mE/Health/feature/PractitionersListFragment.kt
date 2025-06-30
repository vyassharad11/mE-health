package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.PractitionersListFragmentBinding
import com.mE.Health.feature.adapter.MedicationListAdapter
import com.mE.Health.feature.adapter.PractitionersListAdapter
import com.mE.Health.utility.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PractitionersListFragment : BaseFragment() {

    private lateinit var binding: PractitionersListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PractitionersListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        val type = arguments?.getString(Constants.PN_TYPE, "")

        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())

        if (type == Constants.PRACTITIONER) {
            binding.tvPageTitle.text = getString(R.string.list_of_practitioners)
            var recyclerAdapter = PractitionersListAdapter(requireActivity())
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : PractitionersListAdapter.OnClickCallback {
                    override fun onClicked(view: View?, position: Int) {
                        addFragment(
                            R.id.fragment_container,
                            PractitionersListDetailsFragment(),
                            "PractitionersListDetailsFragment",
                            "PractitionersListFragment"
                        )
                    }
                }
            }
        } else {
            binding.tvPageTitle.text = getString(R.string.list_of_medication)
            var recyclerAdapter = MedicationListAdapter(requireActivity())
            binding.rvAssist.adapter = recyclerAdapter
            recyclerAdapter.apply {
                onItemClickListener = object : MedicationListAdapter.OnClickCallback {
                    override fun onClicked(view: View?, position: Int) {
                        addFragment(
                            R.id.fragment_container,
                            PractitionersListDetailsFragment(),
                            "PractitionersListDetailsFragment",
                            "PractitionersListFragment"
                        )
                    }
                }
            }
        }
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.condition)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }
}