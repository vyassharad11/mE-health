package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toFormattedDate

class MyHealthLabAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthLabAdapter.MyViewHolder>() {

    var itemList: List<DiagnosticReport>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: DiagnosticReport, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthLabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthLabBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: DiagnosticReport? = itemList?.get(position)
        item?.let {
            holder.binding.tvRecordedDate.text = it.issued?.toFormattedDate()
            holder.binding.tvName.text = it.code_display
            holder.binding.tvStatus.text = it.status?.capitalFirstChar()
            holder.binding.tvViewDetail.setOnClickListener {
                onItemClickListener?.onClicked(item, position)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}