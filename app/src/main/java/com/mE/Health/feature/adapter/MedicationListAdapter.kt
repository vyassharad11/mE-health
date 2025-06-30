package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class MedicationListAdapter(val context: Context) :
    RecyclerView.Adapter<MedicationListAdapter.MyViewHolder>() {

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
    ): MedicationListAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_medication_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationListAdapter.MyViewHolder, position: Int) {
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