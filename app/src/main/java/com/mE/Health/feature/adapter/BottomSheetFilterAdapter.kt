package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.CountryState
import com.mE.Health.utility.FilterItem

class BottomSheetFilterAdapter(
    private val mContext: Context,
    private val filterList: List<FilterItem>
) :
    RecyclerView.Adapter<BottomSheetFilterAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var ivCheckbox: ImageView = itemView.findViewById(R.id.ivCheckbox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetFilterAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bottom_sheet_filter_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomSheetFilterAdapter.MyViewHolder, position: Int) {
        holder.tvName.text = filterList[position].name
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