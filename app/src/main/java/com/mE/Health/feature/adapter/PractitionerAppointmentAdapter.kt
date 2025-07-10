package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.Appointment
import com.mE.Health.databinding.ItemPractitionerAppointmentBinding
import com.mE.Health.utility.openCloseTime

class PractitionerAppointmentAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionerAppointmentAdapter.MyViewHolder>() {

    var itemList: List<Appointment>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class MyViewHolder(val binding: ItemPractitionerAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPractitionerAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            val datTimePair = openCloseTime(it.startTime, it.endTime)
            holder.binding.tvDate.text = datTimePair.first
            holder.binding.tvTime.text = datTimePair.second
        }
        holder.itemView.setOnClickListener {
        }

        holder.binding.rtvAddCalendar.visibility = if (position == 0) View.GONE else View.VISIBLE

    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}