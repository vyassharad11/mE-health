package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Procedure
import com.mE.Health.databinding.ItemVisitConditionBinding
import com.mE.Health.utility.capitalFirstChar

class VisitMedicationAdapter(val context: Context) :
    RecyclerView.Adapter<VisitMedicationAdapter.MyViewHolder>() {

    var itemList: List<MedicationRequest>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemVisitConditionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVisitConditionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvDisplayName.text = item.medicationCode_display
            holder.binding.tvTitle.text = "Status :"
            holder.binding.tvClinicalStatus.text = item.status?.capitalFirstChar()
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}