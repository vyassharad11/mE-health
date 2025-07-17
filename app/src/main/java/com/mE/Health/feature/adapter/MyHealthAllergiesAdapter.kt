package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.databinding.ItemMyHealthAllergiesBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate

class MyHealthAllergiesAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthAllergiesAdapter.MyViewHolder>() {

    var itemList: List<AllergyIntolerance>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(item: AllergyIntolerance?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthAllergiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthAllergiesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvRecordedDate.text = "Recorded Date: ${it.recordedDate?.toDisplayDate()}"
            holder.binding.tvName.text = it.code_display
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item, position
                )
            }
            holder.binding.tvStatus.text = it.clinicalStatus?.capitalFirstChar()
            Utilities.getLabUIStatus(mContext, it.clinicalStatus ?: "").let {
                holder.binding.tvStatus.setTextColor(it.first)
                holder.binding.tvStatus.delegate.backgroundColor = it.second
            }
        }

    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<AllergyIntolerance>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}