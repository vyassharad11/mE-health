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
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.Immunization
import com.mE.Health.databinding.ItemMyHealthImmunizationBinding
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundTextView
import com.mE.Health.utility.toFormattedDate

enum class ClickState {
    DETAIL, VIEW_PATIENT
}

class MyHealthImmunizationAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthImmunizationAdapter.MyViewHolder>() {

    var itemList: List<Immunization>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int, clickState: ClickState)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthImmunizationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthImmunizationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList?.get(position)
        item?.let {
            holder.binding.tvAdministeredDate.text =
                "Administered on ${it.occurrenceDate?.toFormattedDate()}"
            holder.binding.tvName.text = it.vaccineCode_display
            holder.binding.tvViewDetails.setOnClickListener {
                onItemClickListener?.onClicked(
                    holder.binding.tvViewDetails,
                    position,
                    ClickState.DETAIL
                )
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}