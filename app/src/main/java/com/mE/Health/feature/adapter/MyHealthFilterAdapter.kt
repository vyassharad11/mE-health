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

class MyHealthFilterAdapter(
    private val mContext: Context,
    private val itemList: ArrayList<String>
) :
    RecyclerView.Adapter<MyHealthFilterAdapter.MyViewHolder>() {

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCross: ImageView = itemView.findViewById(R.id.ivCross)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthFilterAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_filter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthFilterAdapter.MyViewHolder, position: Int) {
        holder.ivCross.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.ivCross,
                position
            )
        }

        holder.tvName.text = itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}