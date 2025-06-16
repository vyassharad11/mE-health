package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.AdviceDTO

class RadioButtonListAdapter(val context: Context, private val itemList: List<String>) :
    RecyclerView.Adapter<RadioButtonListAdapter.MyViewHolder>() {

    var selectedItem = -1

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivRadioButton: ImageView = itemView.findViewById(R.id.ivRadioButton)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RadioButtonListAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_radio_button, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioButtonListAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.ivRadioButton,
                position
            )
        }
        holder.tvTitle.text = itemList[position]
        if (selectedItem == position) {
            holder.ivRadioButton.setImageResource(R.drawable.ic_radio_checked_button)
        } else {
            holder.ivRadioButton.setImageResource(R.drawable.ic_radio_unchecked_button)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}