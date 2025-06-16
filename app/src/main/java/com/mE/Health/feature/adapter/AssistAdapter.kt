package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class AssistAdapter (val context: Context) :
    RecyclerView.Adapter<AssistAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //item initalization
        var tv_office: TextView = itemView.findViewById(R.id.tv_office)
        var tv_device: TextView = itemView.findViewById(R.id.tv_device)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssistAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_assist, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssistAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {

            //is selected is used for selecting the position of item
//            if (dataList[position].isSelected) {
//                dataList[position].setIsSelected(false)
//                holder.ll_design.setBackgroundColor(Color.GREEN)
//            } else {
//                dataList[position].setIsSelected(true)
//
//                holder.ll_design.setBackgroundColor(Color.WHITE)
//            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 12
    }
}