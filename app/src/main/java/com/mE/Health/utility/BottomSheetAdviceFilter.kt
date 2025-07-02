package com.mE.Health.utility

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mE.Health.R
import com.mE.Health.feature.adapter.BottomSheetFilterAdapter
import com.mE.Health.feature.adapter.MyHealthTypeAdapter
import com.mE.Health.models.MyHealthTypeModel
import com.mE.Health.utility.roundview.RoundLinearLayout
import com.mE.Health.utility.roundview.RoundTextView

data class AdviceFilterItem(val name: String, val icon: Int, var isChecked: Boolean = false)

class BottomSheetAdviceFilter(val filterList: ArrayList<AdviceFilterItem>) :
    BottomSheetDialogFragment() {

    private var onCompleteListener: OnCompleteListener? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_advice_filter,
            container, false
        )
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvList = view.findViewById<RecyclerView>(R.id.rvBottomSheetFilter)
        val tvApply = view.findViewById<RoundTextView>(R.id.tvApply)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val listAdapter = BottomSheetFilterAdapter(requireActivity(), filterList)
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rvList.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    filterList[position].isChecked = !filterList[position].isChecked
                    notifyItemChanged(position)
                }
            }
            tvApply.visibility = View.GONE
            tvApply.setOnClickListener {
                onCompleteListener?.onComplete(filterList)
                dismiss()
            }
            ivBack.setOnClickListener {
                dismiss()
            }
        }
    }

    interface OnCompleteListener {
        fun onComplete(itemList: ArrayList<AdviceFilterItem>)
    }

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    fun setOnCompleteListener(onCompleteListener: OnCompleteListener) {
        this.onCompleteListener = onCompleteListener
    }

    inner class BottomSheetFilterAdapter(
        private val mContext: Context,
        private val filterList: List<AdviceFilterItem>
    ) :
        RecyclerView.Adapter<BottomSheetFilterAdapter.MyViewHolder>() {


        var onItemClickListener: OnClickCallback? = null

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvName: TextView = itemView.findViewById(R.id.tvName)
            var ivCheckbox: ImageView = itemView.findViewById(R.id.ivCheckbox)
            var ivType: ImageView = itemView.findViewById(R.id.ivType)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BottomSheetFilterAdapter.MyViewHolder {
            var view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.bottom_sheet_advice_filter_item, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: BottomSheetFilterAdapter.MyViewHolder,
            position: Int
        ) {
            val data = filterList[position]
            holder.tvName.text = data.name
            holder.ivType.setImageResource(data.icon)
            holder.ivCheckbox.setImageResource(if (filterList[position].isChecked) R.drawable.ic_checkbox_active else R.drawable.ic_checkbox_inactive)
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    holder.itemView,
                    position
                )
            }
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return filterList.size
        }
    }
}

