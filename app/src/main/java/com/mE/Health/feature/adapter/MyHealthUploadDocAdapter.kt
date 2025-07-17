package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class MyHealthUploadDocAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthUploadDocAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        var ivFileType: ImageView = itemView.findViewById(R.id.ivFileType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthUploadDocAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_upload_doc, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthUploadDocAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }

        when (position) {
            1 -> {
                holder.tvName.text = "Report4.mp4"
                holder.tvCategory.text = "Appointment"
//                holder.tvDateTime.text = "${position + 1} days ago"
                holder.ivFileType.setImageResource(R.drawable.ic_pdf_eye_icon)
            }

            2 -> {
                holder.tvName.text = "Report.jpg"
                holder.tvCategory.text = "Labs"
//                holder.tvDateTime.text = "${position + 1} days ago"
            }

            else -> {
                holder.tvName.text = "Blood Test-01.pdf"
                holder.tvCategory.text = "Appointment"
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}