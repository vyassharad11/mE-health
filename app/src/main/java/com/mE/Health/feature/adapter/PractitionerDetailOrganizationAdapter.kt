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

class PractitionerDetailOrganizationAdapter(val context: Context) :
    RecyclerView.Adapter<PractitionerDetailOrganizationAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var ivType: ImageView = itemView.findViewById(R.id.ivType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PractitionerDetailOrganizationAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_practitioner_organization, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PractitionerDetailOrganizationAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }

    }

    override fun getItemCount(): Int {
        return 5
    }
}