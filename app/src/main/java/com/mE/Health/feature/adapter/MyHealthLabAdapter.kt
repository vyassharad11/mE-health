package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.capitalFirstChar
import com.mE.Health.utility.toFormattedDate
import java.util.Locale

class MyHealthLabAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthLabAdapter.MyViewHolder>() {

    var itemList: List<DiagnosticReport>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(detail: DiagnosticReport, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthLabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthLabBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: DiagnosticReport? = itemList?.get(position)
        item?.let {
            holder.binding.tvRecordedDate.text = it.issued?.toFormattedDate()
            holder.binding.tvName.text = it.code_display
            holder.binding.tvViewDetail.setOnClickListener {
                onItemClickListener?.onClicked(item, position)
            }

            holder.binding.tvStatus.apply {
                text = it.status?.capitalFirstChar()
                when (it.status?.lowercase(Locale.ROOT)) {
                    "active","final" -> {
                        setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.color_06C270
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_A06C270)
                    }
                    "preliminary" -> {
                        setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.color_F09C00
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_AF09C00)
                    }
                    else -> {
                        setTextColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.color_06C270
                            )
                        )
                        delegate.backgroundColor =
                            ContextCompat.getColor(mContext, R.color.color_A06C270)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<DiagnosticReport>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}