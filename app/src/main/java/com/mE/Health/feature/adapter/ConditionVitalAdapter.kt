package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Observation
import com.mE.Health.databinding.ItemConditionVitalBinding
import com.mE.Health.utility.toDisplayDate

class ConditionVitalAdapter(val context: Context, val organizationName:String?) :
    RecyclerView.Adapter<ConditionVitalAdapter.MyViewHolder>() {

    var itemList: List<Observation>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemConditionVitalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemConditionVitalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvDate.text = item.effectiveDate?.toDisplayDate()
            holder.binding.tvHospitalName.text = organizationName
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}