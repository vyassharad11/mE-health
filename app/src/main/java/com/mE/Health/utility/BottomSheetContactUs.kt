package com.mE.Health.utility

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Practitioner
import com.mE.Health.feature.adapter.BottomSheetContactUsAdapter
import com.mE.Health.feature.adapter.BottomSheetFilterAdapter
import com.mE.Health.utility.roundview.RoundLinearLayout
import com.mE.Health.utility.roundview.RoundTextView

class BottomSheetContactUs(val list: ArrayList<String>, val enquiries: String) :
    BottomSheetDialogFragment() {

    private var onCompleteListener: OnCompleteListener? = null
    private var itemList = ArrayList<String>()
    private var selectedSubject = ""

    init {
        this.itemList = list
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_contact_us,
            container, false
        )
        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            BottomSheetDialogFragment.STYLE_NORMAL,
            R.style.CustomBottomSheetDialogTheme
        ) /* hack to make background transparent */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvList = view.findViewById<RecyclerView>(R.id.rvBottomSheetFilter)
        val tvCancel = view.findViewById<RoundTextView>(R.id.tvCancel)
        val rllCancel = view.findViewById<RoundLinearLayout>(R.id.rllCancel)
        val listAdapter = BottomSheetContactUsAdapter(requireActivity(), itemList, enquiries)
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rvList.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : BottomSheetContactUsAdapter.OnClickCallback {
                override fun onClicked(data: String, position: Int) {
                    selectedSubject = data
                    onCompleteListener?.onComplete(selectedSubject)
                    dismiss()
                }
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
            rllCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    interface OnCompleteListener {
        fun onComplete(item: String)
    }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener) {
        this.onCompleteListener = onCompleteListener
    }
}

