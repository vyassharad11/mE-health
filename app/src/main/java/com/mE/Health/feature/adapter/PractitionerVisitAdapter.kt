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
import com.mE.Health.utility.roundview.RoundTextView

class PractitionerVisitAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionerVisitAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var rtvAddCalendar: RoundTextView = itemView.findViewById(R.id.rtvAddCalendar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PractitionerVisitAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_practitioner_visit, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PractitionerVisitAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}