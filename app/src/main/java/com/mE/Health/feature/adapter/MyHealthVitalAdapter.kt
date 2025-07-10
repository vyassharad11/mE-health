package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Observation
import com.mE.Health.databinding.ItemMyHealthVitalBinding
import com.mE.Health.utility.Constants

class MyHealthVitalAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthVitalAdapter.MyViewHolder>() {

    var itemList: List<Observation>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: Observation, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthVitalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthVitalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let { i ->
            holder.binding.tvName.text = i.code_display
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }

            if (position == 1 || position == 3) {
                holder.binding.tvName.text = "Heart Rate"
                holder.binding.tvStatus.text = "72 bmp"
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}