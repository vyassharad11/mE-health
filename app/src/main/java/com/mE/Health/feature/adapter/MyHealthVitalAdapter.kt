package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mE.Health.R
import com.mE.Health.data.model.Observation
import com.mE.Health.data.model.Value
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
            val data = Gson().fromJson(i.value, Value::class.java)
            holder.binding.tvStatus.text = mContext.getString(R.string.value_with_unit, data.value, data.unit)
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}