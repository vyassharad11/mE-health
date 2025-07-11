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

class MyHealthConditionAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthConditionAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthConditionAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_condition, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthConditionAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}