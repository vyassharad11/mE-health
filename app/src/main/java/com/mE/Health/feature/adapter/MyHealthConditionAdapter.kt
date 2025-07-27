package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.Condition
import com.mE.Health.databinding.ItemMyHealthConditionBinding
import com.mE.Health.utility.Utilities
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toDisplayDate
import java.util.Locale

class MyHealthConditionAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthConditionAdapter.MyViewHolder>() {

    var itemList: List<Condition>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(detail: Condition, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthConditionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthConditionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: Condition? = itemList?.get(position)
        item?.let {
            holder.binding.tvConditionName.text = it.code_display
            holder.binding.tvDate.text =  it.recordedDate?.toDisplayDate()
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }
            Utilities.getConditionUIStatus(mContext, it.clinicalStatus ?: "").let {statusDetail ->
                holder.binding.tvStatus.apply {
                    text = it.clinicalStatus?.capitalFirstChar()
                    setTextColor(statusDetail.first)
                    delegate.backgroundColor = statusDetail.second
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Condition>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}