package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.databinding.ItemMyHealthAllergiesBinding
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.toFormattedDate

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
            holder.binding.tvRecordedDate.text = "Recorded Date: ${it.recordedDate?.toFormattedDate()}"
            holder.binding.tvName.text = it.code_display
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item, position
                )
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}