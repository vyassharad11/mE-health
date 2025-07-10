package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.MedicationRequest
import com.mE.Health.databinding.ItemMyHealthMedicationBinding
import com.mE.Health.utility.Constants

class MyHealthMedicationAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthMedicationAdapter.MyViewHolder>() {

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


    inner class MyViewHolder(val binding: ItemMyHealthMedicationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthMedicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvName.text = it.medicationCode_display ?: ""
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