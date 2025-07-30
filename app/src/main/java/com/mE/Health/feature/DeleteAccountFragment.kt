package com.mE.Health.feature

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.MainActivity
import com.mE.Health.R
import com.mE.Health.data.model.Reason
import com.mE.Health.databinding.DeleteAccountFragmentBinding
import com.mE.Health.feature.adapter.DeleteAccountListAdapter
import com.mE.Health.retrofit.NetworkResult
import com.mE.Health.utility.Constants
import com.mE.Health.utility.FilterItem
import com.mE.Health.utility.roundview.RoundLinearLayout
import com.mE.Health.utility.roundview.RoundTextView
import com.mE.Health.viewmodels.DeleteAccountViewModel
import com.mE.Health.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment() {

    private lateinit var binding: DeleteAccountFragmentBinding
    private val viewModel: DeleteAccountViewModel by viewModels()
    private var accountAdapter: DeleteAccountListAdapter? = null
    private var selectedReason = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DeleteAccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initHeader()
        initView()
        viewModel.getReasonData()
        observeResponse()
    }

    private fun initHeader() {
        binding.toolbar.appBar.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        )
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbar.ivSetting.visibility = View.GONE
    }

    private fun initView() {
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        accountAdapter = DeleteAccountListAdapter(requireActivity())
        binding.rvList.adapter = accountAdapter
        accountAdapter?.apply {
            onItemClickListener = object : DeleteAccountListAdapter.OnClickCallback {
                override fun onClicked(data: Reason, position: Int) {
                    accountAdapter?.selectedItem = position
                    selectedReason = data.id
                    notifyDataSetChanged()
                    if (data.name.lowercase() == "other" || itemList?.get(
                            position
                        )?.name?.lowercase() == "others"
                    ) {
                        binding.llInput.visibility = View.VISIBLE
                    } else {
                        binding.llInput.visibility = View.GONE
                    }
                }
            }
        }

        binding.rtvDeleteAccount.setOnClickListener {
            // Handle delete account action
            if (selectedReason.isNullOrEmpty()) Toast.makeText(
                requireActivity(),
                "Please select a reason for deleting your account",
                Toast.LENGTH_SHORT
            ).show()
            else
                showUserInputDialog()
        }
    }

    private fun showUserInputDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_user_input)
        dialog.window?.setBackgroundDrawable(0.toDrawable())
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        val etUserInput = dialog.findViewById<EditText>(R.id.etUserInput)
        val llCancel = dialog.findViewById<RoundLinearLayout>(R.id.llCancel)
        val rtvDeleteAccount = dialog.findViewById<RoundTextView>(R.id.rtvDeleteAccount)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        val tvPolicy = dialog.findViewById<TextView>(R.id.tvPolicy)

        var text = getString(R.string.deleting_account_permanently_description)
        var sIndex = text.indexOf("permanently")
        val eIndex = text.indexOf("information")
        var spannableStringBuilder = SpannableStringBuilder(text)
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.color_F02C2C)),
            sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            sIndex,
            eIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvMessage.text = spannableStringBuilder

        text = getString(R.string.confirm_this_description)
        sIndex = text.indexOf("DELETE")
        spannableStringBuilder = SpannableStringBuilder(text)
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireActivity(), R.color.color_F02C2C)),
            sIndex, spannableStringBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            sIndex,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvPolicy.text = spannableStringBuilder
        rtvDeleteAccount.setOnClickListener {
            if (!etUserInput.text.isNullOrEmpty() && etUserInput.text.toString()
                    .lowercase() == "delete"
            ) {
                dialog.dismiss()
                viewModel.deleteUserAccount(
                    selectedReason,
                    Constants.source,
                    "3",
                    "security issue"
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Please type 'DELETE' to confirm",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        llCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun observeResponse() {
        viewModel.reasonStateData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog()
                }

                is NetworkResult.Error -> {
                    hideProgressDialog()
                    showDialogOk(it.message!!)
                }

                is NetworkResult.Success -> {
                    hideProgressDialog()
                    if (it.data?.data.isNullOrEmpty()) {
                        Toast.makeText(
                            requireActivity(),
                            "No reasons available",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        accountAdapter?.updateList(it.data?.data ?: emptyList())
                    }
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }

        viewModel.deleteStateData.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    showProgressDialog()
                }

                is NetworkResult.Error -> {
                    hideProgressDialog()
                    showDialogOk(it.message!!)
                }

                is NetworkResult.Success -> {
                    hideProgressDialog()
                    Toast.makeText(
                        requireActivity(),
                        it.data?.mETextRes ?: "Account successfully deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(
                        R.anim.enter_from_bottom,
                        android.R.anim.fade_out
                    )
                    requireActivity().finish()
                }

                else -> {
                    hideProgressDialog()
                    showDialogOk(it?.message!!)
                }
            }
        }
    }
}