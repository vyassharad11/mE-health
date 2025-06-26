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

class PractitionerAppointmentAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionerAppointmentAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rtvAddCalendar: RoundTextView = itemView.findViewById(R.id.rtvAddCalendar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PractitionerAppointmentAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_practitioner_appointment, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PractitionerAppointmentAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }

       holder.rtvAddCalendar.visibility =  if (position==0) View.GONE else View.VISIBLE

    }

    override fun getItemCount(): Int {
        return 2
    }
}