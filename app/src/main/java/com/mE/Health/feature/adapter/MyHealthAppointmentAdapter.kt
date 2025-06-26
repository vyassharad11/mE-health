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
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthAppointmentAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthAppointmentAdapter.MyViewHolder>() {

    private var itemList: List<ProviderDetail> = ArrayList()
    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int, type: String)
    }

    var onItemClickListener: OnClickCallback? = null

    init {
//        this.itemList = list
//        this.type = tabType
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvReadMore: TextView = itemView.findViewById(R.id.tvReadMore)
        var rtvStatus: RoundTextView = itemView.findViewById(R.id.rtvStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthAppointmentAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_appointment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthAppointmentAdapter.MyViewHolder, position: Int) {
//        val data = itemList[position]
        holder.tvReadMore.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.tvReadMore, position,
                Constants.READ_MORE
            )
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.tvReadMore, position,
                Constants.DETAIL
            )
        }
        if (position == 1 || position == 2) {
            holder.rtvStatus.apply {
                text = "Booked"
                setTextColor(ContextCompat.getColor(mContext, R.color.color_0063F7))
                delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_1A0063F7)
            }
        } else if (position == 3) {
            holder.rtvStatus.apply {
                text = "Cancelled"
                setTextColor(ContextCompat.getColor(mContext, R.color.color_F02C2C))
                delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_1AF02C2C)
            }
        }

//        if (type == Constants.CONNECTED) {
//            holder.tvConnect.text = mContext.getString(R.string.dis_connected)
//            holder.ivReload.visibility = View.VISIBLE
//        } else {
//            holder.tvConnect.text = mContext.getString(R.string.connected)
//            holder.ivReload.visibility = View.GONE
//        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 10
    }
}