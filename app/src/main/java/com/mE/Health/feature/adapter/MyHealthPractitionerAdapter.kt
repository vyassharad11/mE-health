package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Practitioner
import com.mE.Health.databinding.ItemMyHealthPractitionerBinding
import com.mE.Health.models.CountryState
import com.mE.Health.utility.Constants
import com.mE.Health.utility.extractContactInfo

class MyHealthPractitionerAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthPractitionerAdapter.MyViewHolder>() {

    var itemList: List<Practitioner>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(data: Practitioner)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemMyHealthPractitionerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthPractitionerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        itemList?.let {
            val practitioner = it[position]
            holder.binding.tvName.text = practitioner.name
            holder.binding.tvSpeciality.text = practitioner.specialty

            val contactInfo = practitioner.telecom?.extractContactInfo()
            contactInfo?.let { ci ->
                holder.binding.tvPhone.text = ci.phone
                holder.binding.tvEmail.text = ci.email
            }

            holder.binding.rllDetail.setOnClickListener {
                onItemClickListener?.onClicked(practitioner)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Practitioner>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}