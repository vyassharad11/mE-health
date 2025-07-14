package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.Encounter
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.databinding.ItemVitalListBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.toFormattedDate

class MedicationListAdapter(val context: Context) :
    RecyclerView.Adapter<MedicationListAdapter.MyViewHolder>() {
    var itemList: List<MedicationRequest>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: MedicationRequest, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemVitalListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVitalListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let { it ->
            holder.binding.tvName.text = it.medicationCode_display ?: ""
            holder.binding.tvStatus.text =  "Authored: ${it.authoredOn?.toFormattedDate()}"
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}