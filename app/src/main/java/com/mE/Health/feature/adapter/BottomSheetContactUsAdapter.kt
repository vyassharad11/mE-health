package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.databinding.DeleteAccountListItemBinding
import com.mE.Health.utility.FilterItem

class BottomSheetContactUsAdapter(
    private val mContext: Context, private val itemList: List<String>,
    private val enquiries: String
) : RecyclerView.Adapter<BottomSheetContactUsAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(data: String, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: DeleteAccountListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeleteAccountListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        itemList[position].let { data ->
            with(holder.binding) {
                tvName.text = data
                ivCheckbox.setImageResource(if (data == enquiries) R.drawable.ic_radio_checked_button else R.drawable.ic_radio_unchecked_button)
                holder.itemView.setOnClickListener {
                    onItemClickListener?.onClicked(data, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}