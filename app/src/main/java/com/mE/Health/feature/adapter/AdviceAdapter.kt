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
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundLinearLayout

class AdviceAdapter(private val mContext: Context) :
    RecyclerView.Adapter<AdviceAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAdviceReadMore: TextView = itemView.findViewById(R.id.tvAdviceReadMore)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdviceAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_advice_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceAdapter.MyViewHolder, position: Int) {
        holder.tvAdviceReadMore.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.tvAdviceReadMore,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}