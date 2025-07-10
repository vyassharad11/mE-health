package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Procedure
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.databinding.ItemMyHealthProcedureBinding
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthProcedureAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthProcedureAdapter.MyViewHolder>() {

    var itemList: List<Procedure>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthProcedureBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthProcedureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHealthProcedureAdapter.MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvProcedureName.text = it.code_display
        }

        when (position) {
            1,4 -> {
                holder.binding.tvProcedureStatus.apply {
                    text = "In Progress"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F09C00))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_AF09C00)
                }
            }

            2,5 -> {
                holder.binding.tvProcedureStatus.apply {
                    text = "Completed"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }

            3,6 -> {
                holder.binding.tvProcedureStatus.apply {
                    text = "In Progress"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F09C00))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_AF09C00)
                }
            }
            else->{
                holder.binding.tvProcedureStatus.apply {
                    text = "Completed"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}