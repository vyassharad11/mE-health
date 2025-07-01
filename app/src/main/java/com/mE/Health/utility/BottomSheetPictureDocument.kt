package com.mE.Health.utility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mE.Health.R
import com.mE.Health.feature.adapter.BottomSheetFilterAdapter
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.models.MyHealthTypeModel
import com.mE.Health.utility.roundview.RoundTextView

data class Type(val name: String)

class BottomSheetPictureDocument(val filterList: ArrayList<FilterItem>) : BottomSheetDialogFragment() {

    private var onCompleteListener: OnCompleteListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.dialog_camera_video,
            container, false
        )
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvList = view.findViewById<RecyclerView>(R.id.rvBottomSheetFilter)
        val tvApply = view.findViewById<RoundTextView>(R.id.tvApply)
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
        }
    }

    interface OnCompleteListener {
        fun onComplete(itemList: ArrayList<FilterItem>)
    }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener) {
        this.onCompleteListener = onCompleteListener
    }
}

