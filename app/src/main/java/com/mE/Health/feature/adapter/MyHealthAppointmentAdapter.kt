package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.Appointment
import com.mE.Health.databinding.ItemMyHealthAppointmentBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.openCloseTime

class MyHealthAppointmentAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthAppointmentAdapter.MyViewHolder>() {

    var itemList: List<Appointment>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: Appointment, position: Int, type: String)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemMyHealthAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: Appointment? = itemList?.get(position)
        item?.let {
            val datTimePair = openCloseTime(it.startTime, it.endTime)
            val dateTime = "${datTimePair.first},\n${datTimePair.second}"
            holder.binding.tvAppointmentTime.text = dateTime
            holder.binding.tvName.text = it.practitionerName

            holder.binding.tvReadMore.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position,
                    Constants.READ_MORE
                )
            }
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position,
                    Constants.DETAIL
                )
            }

            holder.binding.rtvStatus.apply {
                if (it.status?.lowercase() == mContext.getString(R.string.booked).lowercase()) {
                    text = mContext.getString(R.string.booked)
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_0063F7))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1A0063F7)
                } else if (it.status?.lowercase() == mContext.getString(R.string.completed).lowercase()) {
                    text = mContext.getString(R.string.completed)
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_A06C270)
                } else if (it.status?.lowercase() == mContext.getString(R.string.cancelled).lowercase()) {
                    text = mContext.getString(R.string.cancelled)
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F02C2C))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1AF02C2C)
                } else {
                    text = it.status?.capitalFirstChar()
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Appointment>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}