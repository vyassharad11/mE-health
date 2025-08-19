package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.data.model.Procedure
import com.mE.Health.databinding.ItemVisitConditionBinding
import com.mE.Health.utility.capitalFirstChar

class VisitAllergyAdapter(val context: Context) :
    RecyclerView.Adapter<VisitAllergyAdapter.MyViewHolder>() {

    var itemList: List<AllergyIntolerance>? = ArrayList()
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
            holder.binding.tvDisplayName.text = item.code_display
            holder.binding.tvClinicalStatus.text = item.clinicalStatus?.capitalFirstChar()
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}