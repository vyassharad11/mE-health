package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.AdviceDTO
import com.mE.Health.models.NavMenuDTO

class HomeMenuAdapter(val context: Context, private val itemList: List<NavMenuDTO>) :
    RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder>() {

    var selectedItem = -1

    var onItemClickListener: OnClickCallback? = null

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivType: ImageView = itemView.findViewById(R.id.ivNavMenu)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeMenuAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_nav_home_menu, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeMenuAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(holder.itemView,position)
        }
        holder.ivType.setImageResource(itemList[position].image)
        holder.tvTitle.text = itemList[position].name
        if (selectedItem == position) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_FF6605))
        } else {
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_333333))
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}