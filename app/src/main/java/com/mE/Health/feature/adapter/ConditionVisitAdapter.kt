package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.databinding.ItemConditionLabBinding
import com.mE.Health.databinding.ItemConditionVisitBinding
import com.mE.Health.databinding.ItemConditionVitalBinding

class ConditionVisitAdapter(val context: Context) :
    RecyclerView.Adapter<ConditionVisitAdapter.MyViewHolder>() {

    var itemList: List<Encounter>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemConditionVisitBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemConditionVisitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvName.text = item.organizationName
            holder.binding.tvAddress.text = item.organizationAddress
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}