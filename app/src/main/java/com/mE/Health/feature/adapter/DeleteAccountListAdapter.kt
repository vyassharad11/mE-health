package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.data.model.Reason
import com.mE.Health.databinding.DeleteAccountListItemBinding
import com.mE.Health.utility.Constants
import com.mE.Health.utility.FilterItem

class DeleteAccountListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<DeleteAccountListAdapter.MyViewHolder>() {

    var itemList: List<Reason>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type = Constants.ALL
    var selectedItem = -1

    interface OnClickCallback {
        fun onClicked(data: Reason, position: Int)
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
        itemList?.get(position)?.let { data ->
            with(holder.binding) {
                tvName.text = data.name
                ivCheckbox.setImageResource(if (position==selectedItem) R.drawable.ic_checkbox_active else R.drawable.ic_checkbox_inactive)
                holder.itemView.setOnClickListener {
                    onItemClickListener?.onClicked(data, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<Reason>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}