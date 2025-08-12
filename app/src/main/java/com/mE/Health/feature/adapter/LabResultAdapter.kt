package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.databinding.ItemLabResultBinding

class LabResultAdapter(val context: Context) :
    RecyclerView.Adapter<LabResultAdapter.MyViewHolder>() {

    var itemList: List<Pair<String,String>>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemLabResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLabResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvLabResultTitle.text = item.first
            holder.binding.tvLabResultValue.text = item.second
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}