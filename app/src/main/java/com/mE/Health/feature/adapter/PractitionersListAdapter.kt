package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Practitioner
import com.mE.Health.data.model.Value
import com.mE.Health.databinding.ItemPractitionersListBinding
import com.mE.Health.databinding.ItemVitalListBinding
import com.mE.Health.utility.Constants

class PractitionersListAdapter(val mContext: Context) :
    RecyclerView.Adapter<PractitionersListAdapter.MyViewHolder>() {


    var itemList: List<Practitioner>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: Practitioner, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemPractitionersListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPractitionersListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let { it ->
            holder.binding.tvName.text = it.name
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