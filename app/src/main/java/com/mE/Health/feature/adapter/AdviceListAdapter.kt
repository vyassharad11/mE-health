package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.AdviceDTO

class AdviceListAdapter(val context: Context, private val itemList: List<AdviceDTO>) :
    RecyclerView.Adapter<AdviceListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivType: ImageView = itemView.findViewById(R.id.ivType)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvCount: TextView = itemView.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdviceListAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_advice, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceListAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }

        holder.ivType.setImageResource(itemList[position].image)
        holder.tvName.text = itemList[position].name
        holder.tvCount.text = itemList[position].count
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