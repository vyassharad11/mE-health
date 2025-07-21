package com.mE.Health.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.advice.AdviceInteraction
import com.mE.Health.databinding.ItemAdviceListBinding

class AdviceAdapter() :
    RecyclerView.Adapter<AdviceAdapter.MyViewHolder>() {

    var itemList: List<AdviceInteraction>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(data: AdviceInteraction, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemAdviceListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAdviceListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: AdviceInteraction? = itemList?.get(position)

        item?.let {
            holder.binding.tvTitle.text = item.title
            holder.binding.tvDescription.text = item.advice
            holder.binding.tvAdviceReadMore.setOnClickListener {
                onItemClickListener?.onClicked(item, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}