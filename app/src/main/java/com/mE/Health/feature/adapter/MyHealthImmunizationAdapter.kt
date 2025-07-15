package com.mE.Health.feature.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.AllergyIntolerance
import com.mE.Health.data.model.Immunization
import com.mE.Health.databinding.ItemMyHealthImmunizationBinding
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
        fun onClicked(item: Immunization?, position: Int, clickState: ClickState)
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
            val text = "Administered on ${it.occurrenceDate?.toFormattedDate()}"
            val spannableStringBuilder = SpannableStringBuilder(text)
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(Color.BLACK),
                15, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), 15, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.binding.tvAdministeredDate.text = spannableStringBuilder
            holder.binding.tvName.text = it.vaccineCode_display
            holder.binding.tvViewDetails.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position,
                    ClickState.DETAIL
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Immunization>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}