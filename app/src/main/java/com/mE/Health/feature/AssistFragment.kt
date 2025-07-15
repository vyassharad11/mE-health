package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.data.helper.Resource
import com.mE.Health.data.model.assist.AssistItem
import com.mE.Health.databinding.AssistFragmentBinding
import com.mE.Health.feature.adapter.AssistAdapter
import com.mE.Health.utility.LoaderDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AssistFragment : BaseFragment() {

    private lateinit var binding: AssistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AssistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        binding.rvAssist.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AssistAdapter(requireActivity())
        binding.rvAssist.adapter = adapter
        adapter.apply {
            onItemClickListener = object : AssistAdapter.OnClickCallback {
                override fun onClicked(detail: AssistItem?, position: Int) {
                    val fragment = AssistDetailFragment()
                    val bundle = Bundle()
                    bundle.putString("Title", detail?.item)
                    fragment.arguments = bundle
                    addFragment(
                        R.id.fragment_container,
                        fragment,
                        "AssistDetailFragment",
                        "MyHealthFragment"
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                assistViewModel.assistList.collect { state ->
                    when (state) {
                        is Resource.Loading -> {
                            LoaderDialog.show(requireContext())
                        }

                        is Resource.Success -> {
                            LoaderDialog.hide()
                            val data = state.data
                            adapter.setData(data)
                        }

                        is Resource.Error -> {
                            LoaderDialog.hide()
                            // Show error
                        }

                        is Resource.Offline -> {
                            LoaderDialog.hide()
                            val data = state.data
                            adapter.setData(data)
                        }
                    }
                }
            }
        }
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.assist)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getDummyData(): ArrayList<String> {
        return ArrayList<String>().apply {
            add("Chronic Condition Detector")
            add("Preventive Care Advisor")
            add("Medication Adherence Risk Predictor")
            add("Date Range Handler")
            add("Health Goal Interpreter")
            add("Imaging Based condition Validator")
            add("Social Determinants of Health (SDoH) Estimator")
            add("Polypharmacy Risk Detector")
            add("Gaps in Care Identifier")
            add("Imaging Trend Analyzer")
            add("Behavioral Health Risk Estimator")
        }
    }
}