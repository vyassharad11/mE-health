package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class AssistAdapter(val context: Context, private val list: ArrayList<String>) :
    RecyclerView.Adapter<AssistAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAssistName: TextView = itemView.findViewById(R.id.tvAssistName)
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
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
        holder.tvAssistName.text = list[position]
    }


    override fun getItemCount(): Int {
        return list.size
    }
}