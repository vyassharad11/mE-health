package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.PractitionerOrganizationWithDetails
import com.mE.Health.databinding.ItemPractitionerOrganizationBinding
import com.mE.Health.utility.Utilities

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
        val displayMetrics = Utilities.getDeviceDisplayMetrics()
        val itemWidth = displayMetrics.first/1.2


        val layoutParams = holder.binding.cvMain.layoutParams
        layoutParams.width = itemWidth.toInt()
        holder.binding.cvMain.layoutParams = layoutParams
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}