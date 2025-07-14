package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Value
import com.mE.Health.databinding.ItemMyHealthVitalBinding
import com.mE.Health.databinding.ItemVitalListBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.toFormattedDate

class LabListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<LabListAdapter.MyViewHolder>() {

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


    inner class MyViewHolder(val binding: ItemVitalListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVitalListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let { it ->
            holder.binding.tvName.text = it.code_display
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }
            holder.binding.tvStatus.text = it.issued?.toFormattedDate()
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}