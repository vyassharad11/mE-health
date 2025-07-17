package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.Value
import com.mE.Health.databinding.VitalDetailFragmentBinding
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.formatIntoPrettyDate
import com.mE.Health.utility.toDisplayDate
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class VitalDetailsFragment : BaseFragment() {

    private lateinit var binding: VitalDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VitalDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.vital)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
        DetailSingleton.vital?.let { detail ->
            if (detail.patientId != null) {
                mockViewModel.getPatientDetail(detail.patientId)
            }
            val values = Gson().fromJson(detail.value, Value::class.java)
            binding.apply {
                tvName.text = detail.description
                tvDateTime.text = detail.effectiveDate?.formatIntoPrettyDate()
                tvStatus.text = detail.status?.capitalFirstChar()
                tvValues.text = getString(R.string.value_with_unit, values.value, values.unit)
                tvVitalId.text = detail.id.uppercase()
                tvStartDate.text =
                    getString(R.string.start_date_with_value, detail.createdAt?.toDisplayDate())
            }
        }

        mockViewModel.patientDetail.observe(viewLifecycleOwner){
            binding.tvPatientName.text = it.name
        }
    }
}