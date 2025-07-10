package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Encounter
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.databinding.ItemMyHealthVisitsBinding
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthVisitsAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthVisitsAdapter.MyViewHolder>() {

    var itemList: List<Encounter>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthVisitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthVisitsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvVisitName.text = it.type_display
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    holder.itemView,
                    position
                )
            }
            when (position) {
                1, 4 -> {
                    holder.binding.tvVisitStatus.apply {
                        text = "Canceled"
                        setTextColor(ContextCompat.getColor(mContext, R.color.color_F02C2C))
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_1AF02C2C)
                    }
                }

                2, 5 -> {
                    holder.binding.tvVisitStatus.apply {
                        text = "Scheduled"
                        setTextColor(ContextCompat.getColor(mContext, R.color.color_0063F7))
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_1A0063F7)
                    }
                }

                3, 6 -> {
                    holder.binding.tvVisitStatus.apply {
                        text = "Finished"
                        setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_A06C270)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}