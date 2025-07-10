package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Claim
import com.mE.Health.databinding.ItemMyHealthBillingsBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.toFormattedDate

class MyHealthBillingsAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthBillingsAdapter.MyViewHolder>() {

    var itemList: List<Claim>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthBillingsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthBillingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvDate.text = item.createdDate?.toFormattedDate()
            holder.binding.tvName.text = item.name
            holder.binding.tvAmount.text = "${item.totalCurrency}${item.totalAmount}"
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    holder.itemView,
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}