package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Procedure
import com.mE.Health.databinding.ItemMyHealthProcedureBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate

class MyHealthProcedureAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthProcedureAdapter.MyViewHolder>() {

    var itemList: List<Procedure>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(item: Procedure?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthProcedureBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthProcedureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHealthProcedureAdapter.MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvProcedureName.text = it.code_display
            holder.binding.tvProcedureDate.text = it.performedDate?.toDisplayDate()

            val statusDetail = Utilities.getProcedureUIStatus(mContext, it.status ?: "")
            holder.binding.tvProcedureStatus.apply {
                text = it.status?.capitalFirstChar()
                setTextColor(statusDetail.first)
                delegate.backgroundColor = statusDetail.second
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                item,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Procedure>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}