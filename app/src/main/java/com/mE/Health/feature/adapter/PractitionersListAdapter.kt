package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class PractitionersListAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionersListAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var tvAssistName: TextView = itemView.findViewById(R.id.tvAssistName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PractitionersListAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_practitioners_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PractitionersListAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }


    override fun getItemCount(): Int {
        return 5
    }
}