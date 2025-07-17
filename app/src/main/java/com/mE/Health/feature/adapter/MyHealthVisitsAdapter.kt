package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Encounter
import com.mE.Health.databinding.ItemMyHealthVisitsBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate

class MyHealthVisitsAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthVisitsAdapter.MyViewHolder>() {

    var itemList: List<Encounter>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(item: Encounter?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthVisitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthVisitsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvVisitName.text = it.type_display
            holder.binding.tvDate.text = "Date: ${it.createdAt?.toDisplayDate()}"
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }
            holder.binding.tvVisitStatus.apply {
                text = it.status?.capitalFirstChar()
                val statusDetail = Utilities.getVisitUIStatus(mContext, it.status ?: "")
                setTextColor(statusDetail.first)
                delegate.backgroundColor = statusDetail.second

            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Encounter>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}