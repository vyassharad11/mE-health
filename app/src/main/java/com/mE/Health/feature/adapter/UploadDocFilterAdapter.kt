package com.mE.Health.feature.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.R
import com.mE.Health.models.MyHealthTypeModel
import com.mE.Health.utility.roundview.RoundLinearLayout

data class UploadDocItem(val itemName: String, var isChecked: Boolean = false)
class UploadDocFilterAdapter(
    private val mContext: Context,
    private var itemList: List<UploadDocItem>
) :
    RecyclerView.Adapter<UploadDocFilterAdapter.MyViewHolder>() {

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        var ivChecked: ImageView = itemView.findViewById(R.id.ivChecked)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UploadDocFilterAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_upload_doc_filter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UploadDocFilterAdapter.MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val data = itemList[position]
        holder.tvItemName.text = data.itemName

//        holder.ivChecked.visibility = if (data.isChecked) View.VISIBLE else View.GONE
        holder.ivChecked.visibility = if (data.isChecked) View.VISIBLE else View.VISIBLE

        holder.ivChecked.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.ivChecked,
                position
            )
        }
    }

    fun updateList(itemList:List<UploadDocItem> ){
        this. itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}