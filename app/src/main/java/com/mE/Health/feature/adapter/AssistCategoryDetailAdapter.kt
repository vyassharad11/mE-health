package com.mE.Health.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.AssistDetailEntity
import com.mE.Health.databinding.ItemAssistDetailBinding

class AssistCategoryDetailAdapter() :
    RecyclerView.Adapter<AssistCategoryDetailAdapter.MyViewHolder>() {

    var itemList: List<AssistDetailEntity>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var startDate = ""
    var endDate = ""

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemAssistDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAssistDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }

        item?.let {
            holder.binding.tvDateRange.text = "$startDate - $endDate"
            holder.binding.tvCategory.text = it.title
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}