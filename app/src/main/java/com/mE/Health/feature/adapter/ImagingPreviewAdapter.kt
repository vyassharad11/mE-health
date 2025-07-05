package com.mE.Health.feature.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class ImagingPreviewAdapter(
    private val mContext: Context
) :
    RecyclerView.Adapter<ImagingPreviewAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
//        var ivChecked: ImageView = itemView.findViewById(R.id.ivChecked)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagingPreviewAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_imaging_preview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ImagingPreviewAdapter.MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.tvName.text = "Series ${position+1}"
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}