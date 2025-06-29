package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthVisitsAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthVisitsAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvVisitName: TextView = itemView.findViewById(R.id.tvVisitName)
        var tvVisitStatus: RoundTextView = itemView.findViewById(R.id.tvVisitStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_visits, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
        when (position) {
            1, 4 -> {
                holder.tvVisitName.text = "Dental Checkup"
                holder.tvVisitStatus.apply {
                    text = "Canceled"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F02C2C))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1AF02C2C)
                }
            }

            2, 5 -> {
                holder.tvVisitName.text = "Follow-up Visit"
                holder.tvVisitStatus.apply {
                    text = "Scheduled"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_0063F7))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_1A0063F7)
                }
            }

            3, 6 -> {
                holder.tvVisitName.text = "Laboratory Visit"
                holder.tvVisitStatus.apply {
                    text = "Finished"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor =
                        ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}