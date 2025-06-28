package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthProcedureAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthProcedureAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvProcedureName: TextView = itemView.findViewById(R.id.tvProcedureName)
        var tvProcedureStatus: RoundTextView = itemView.findViewById(R.id.tvProcedureStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthProcedureAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_procedure, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthProcedureAdapter.MyViewHolder, position: Int) {
        when (position) {
            1,4 -> {
                holder.tvProcedureName.text = "Cardiac Catheterization"
                holder.tvProcedureStatus.apply {
                    text = "In Progress"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F09C00))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_AF09C00)
                }
            }

            2,5 -> {
                holder.tvProcedureName.text = "Colonoscopy"
                holder.tvProcedureStatus.apply {
                    text = "Completed"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }

            3,6 -> {
                holder.tvProcedureName.text = "Hip Replacement"
                holder.tvProcedureStatus.apply {
                    text = "In Progress"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_F09C00))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_AF09C00)
                }
            }
            else->{
                holder.tvProcedureName.text = "Appendectomy"
                holder.tvProcedureStatus.apply {
                    text = "Completed"
                    setTextColor(ContextCompat.getColor(mContext, R.color.color_06C270))
                    delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.color_A06C270)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}