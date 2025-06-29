package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView

enum class ClickState {
    DETAIL, VIEW_PATIENT
}

class MyHealthImmunizationAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthImmunizationAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?,  position: Int,  clickState: ClickState)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvViewDetails: RoundTextView = itemView.findViewById(R.id.tvViewDetails)
        var tvViewPatient: RoundTextView = itemView.findViewById(R.id.tvViewPatient)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthImmunizationAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_immunization, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthImmunizationAdapter.MyViewHolder, position: Int) {
        holder.tvViewDetails.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.tvViewDetails,
                position,
                ClickState.DETAIL
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 10
    }
}