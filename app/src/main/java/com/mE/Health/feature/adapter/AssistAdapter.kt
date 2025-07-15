package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.assist.AssistItem
import com.mE.Health.databinding.ItemAssistBinding

class AssistAdapter(val context: Context) :
    RecyclerView.Adapter<AssistAdapter.MyViewHolder>() {

    private var list: List<AssistItem> = emptyList()
    fun setData(list: List<AssistItem>) {
        this.list = list
        notifyDataSetChanged()
    }

    interface OnClickCallback {
        fun onClicked(detail: AssistItem?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemAssistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAssistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                item,
                position
            )
        }
        holder.binding.tvAssistName.text = list[position].item
    }


    override fun getItemCount(): Int {
        return list.size
    }
}