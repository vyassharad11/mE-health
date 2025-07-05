package com.mE.Health.utility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mE.Health.R
import com.mE.Health.feature.adapter.BottomSheetFilterAdapter
import com.mE.Health.utility.roundview.RoundLinearLayout
import com.mE.Health.utility.roundview.RoundTextView


data class FilterItem(val name: String, var isChecked: Boolean = false)

class BottomSheetFilter(val filterList: ArrayList<FilterItem>) : BottomSheetDialogFragment() {

    private var onCompleteListener: OnCompleteListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_filter,
            container, false
        )
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme) /* hack to make background transparent */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvList = view.findViewById<RecyclerView>(R.id.rvBottomSheetFilter)
        val tvApply = view.findViewById<RoundTextView>(R.id.tvApply)
        val rllCancel = view.findViewById<RoundLinearLayout>(R.id.rllCancel)
        val listAdapter = BottomSheetFilterAdapter(requireActivity(), filterList)
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rvList.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : BottomSheetFilterAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    filterList[position].isChecked = !filterList[position].isChecked
                    notifyItemChanged(position)
                }
            }
            tvApply.setOnClickListener {
                onCompleteListener?.onComplete(filterList)
                dismiss()
            }
            rllCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    interface OnCompleteListener {
        fun onComplete(itemList: ArrayList<FilterItem>)
    }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener) {
        this.onCompleteListener = onCompleteListener
    }
}

