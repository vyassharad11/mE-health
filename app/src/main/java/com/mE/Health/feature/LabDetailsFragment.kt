package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.databinding.LabDetailFragmentBinding
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class LabDetailsFragment : BaseFragment() {

    private lateinit var binding: LabDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LabDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        setDetails()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.lab)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun setDetails() {
        DetailSingleton.lab?.let { detail ->
            if (detail.performerId != null) {
                mockViewModel.getPractitionerDetail(detail.performerId)
            }
            binding.apply {
                tvLabId.text = detail.id.uppercase()
                tvDate.text = detail.issued?.toFormattedDate()
                tvStartDate.text = detail.effectiveDate?.toFormattedDate()
                tvName.text = detail.code_display

                tvStatus.apply {
                    text = detail.status?.capitalFirstChar()
                    when (detail.status?.lowercase(Locale.ROOT)) {
                        "active","final" -> {
                            setTextColor(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.color_06C270
                                )
                            )
                            delegate.backgroundColor =
                                ContextCompat.getColor(requireActivity(), R.color.color_A06C270)
                        }
                        "preliminary" -> {
                            setTextColor(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.color_F09C00
                                )
                            )
                            delegate.backgroundColor =
                                ContextCompat.getColor(requireActivity(), R.color.color_AF09C00)
                        }
                        else -> {
                            setTextColor(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.color_06C270
                                )
                            )
                            delegate.backgroundColor =
                                ContextCompat.getColor(requireActivity(), R.color.color_A06C270)
                        }
                    }
                }
            }
        }

        mockViewModel.practitionerDetail.observe(viewLifecycleOwner) {
            binding.tvPractitionerName.text = it.name
        }
    }
}