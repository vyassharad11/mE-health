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
            if (position == 1 || position == 2) {
                holder.binding.rtvStatus.apply {
                    text = "Booked"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_0063F7))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1A0063F7)
                }
            } else if (position == 3) {
                holder.binding.rtvStatus.apply {
                    text = "Cancelled"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F02C2C))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1AF02C2C)
                }
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