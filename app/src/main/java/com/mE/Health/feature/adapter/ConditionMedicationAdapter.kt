package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.databinding.ItemConditionLabBinding
import com.mE.Health.databinding.ItemConditionMedicationBinding
import com.mE.Health.databinding.ItemConditionVisitBinding
import com.mE.Health.databinding.ItemConditionVitalBinding
import com.mE.Health.utility.toDisplayDate

class ConditionMedicationAdapter(val context: Context) :
    RecyclerView.Adapter<ConditionMedicationAdapter.MyViewHolder>() {

    var itemList: List<MedicationRequest>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemConditionMedicationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemConditionMedicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        itemList?.get(position)?.let {
            holder.binding.tvName.text = it.medicationCode_display ?: ""
            holder.binding.tvDate.text =  "Authored: ${it.authoredOn?.toDisplayDate()}"
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}