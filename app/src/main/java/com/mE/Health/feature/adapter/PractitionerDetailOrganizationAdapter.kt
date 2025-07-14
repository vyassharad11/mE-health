package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.databinding.ItemPractitionerOrganizationBinding

class PractitionerDetailOrganizationAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionerDetailOrganizationAdapter.MyViewHolder>() {

    var itemList: List<PractitionerOrganizationWithDetails>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemPractitionerOrganizationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPractitionerOrganizationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvName.text = it.name
            holder.binding.tvDate.text = "Start Time: 01/01/2022"
        }
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}