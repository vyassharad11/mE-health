package com.mE.Health.feature

import android.app.Dialog
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.DeleteAccountFragmentBinding
import com.mE.Health.feature.adapter.DeleteAccountListAdapter
import com.mE.Health.utility.FilterItem
import com.mE.Health.utility.roundview.RoundTextView
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.graphics.drawable.toDrawable
import com.mE.Health.utility.roundview.RoundLinearLayout

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment() {

    private lateinit var binding: DeleteAccountFragmentBinding
    private var itemList = ArrayList<FilterItem>()

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
        getDummyList()
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = DeleteAccountListAdapter(requireActivity())
        adapter.itemList = itemList
        binding.rvList.adapter = adapter
        adapter.apply {
            onItemClickListener = object : DeleteAccountListAdapter.OnClickCallback {
                override fun onClicked(data: FilterItem, position: Int) {
                    itemList?.get(position)?.isChecked = !itemList?.get(position)?.isChecked!!
                    notifyItemChanged(position)
                    if (itemList?.get(itemList?.size!! - 1)?.isChecked!!) {
                        binding.llInput.visibility = View.VISIBLE
                    } else {
                        binding.llInput.visibility = View.GONE
                    }
                }
            }
        }

        binding.rtvDeleteAccount.setOnClickListener {
            // Handle delete account action
            showUserInputDialog()
        }
    }

    private fun getDummyList(): ArrayList<FilterItem> {
        itemList = ArrayList()
        itemList.apply {
            add(FilterItem("I don’t find this app useful", false))
            add(FilterItem("I don’t have time to use the app right now", false))
            add(FilterItem("I had technical issues or bugs", false))
            add(FilterItem("I didn’t find mEinstein useful", false))
            add(FilterItem("I don’t have time to use the app right now", false))
            add(FilterItem("I’m switching to another app/service", false))
            add(FilterItem("Other", false))
        }
        return itemList
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
}