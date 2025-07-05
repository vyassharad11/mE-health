package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R

class MyHealthImagingAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthImagingAdapter.MyViewHolder>() {


    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthImagingAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_imaging, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthImagingAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }

        when (position) {
            1 -> {
                holder.tvName.text = "X-Ray (DX)"
                holder.tvDescription.text = "Chest X-ray for bronchitis"
            }
            else -> {
                holder.tvName.text = "MRI (MR)"
                holder.tvDescription.text = "Knee MRI for meniscal tear"
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}