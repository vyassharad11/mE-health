package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Imaging
import com.mE.Health.databinding.ItemMyHealthImagingBinding
import com.mE.Health.utility.toFormattedDate

class MyHealthImagingAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthImagingAdapter.MyViewHolder>() {

    var itemList: List<Imaging>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    interface OnClickCallback {
        fun onClicked(item: Imaging?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemMyHealthImagingBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHealthImagingAdapter.MyViewHolder {
        val binding = ItemMyHealthImagingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHealthImagingAdapter.MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.apply {
                tvName.text = "${item.modality_display} (${item.modality_code})"
                tvDescription.text = item.description
                tvDate.text = item.started?.toFormattedDate()
                tvStatus.text = item.status
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                item,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}