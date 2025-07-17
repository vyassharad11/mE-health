package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DetailSingleton
import com.mE.Health.data.model.ReasonCode
import com.mE.Health.databinding.ProcedureDetailFragmentBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ProcedureDetailsFragment : BaseFragment() {

    private lateinit var binding: ProcedureDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProcedureDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
    }

    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.procedure)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setOnClickListener {

        }
    }

    private fun initView() {
        DetailSingleton.procedure?.let { detail ->
            binding.apply {
                tvName.text = detail.code_display
                tvProcedureDate.text = detail.performedDate?.toDisplayDate()

                Utilities.getProcedureUIStatus(requireActivity(), detail.status ?: "").let {
                    tvStatus.text = detail.status?.capitalFirstChar()
                    tvStatus.setTextColor(it.first)
                    tvStatus.delegate.backgroundColor = it.second
                }
                val reasonCodeObject = Gson().fromJson(detail.reasonCode, ReasonCode::class.java)

                tvProcedureId.text = "#${reasonCodeObject.code}"
                tvReason.text = reasonCodeObject.display
            }
        }
    }
}