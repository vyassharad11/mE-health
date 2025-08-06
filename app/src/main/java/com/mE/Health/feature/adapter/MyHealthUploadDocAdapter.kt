package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.databinding.ItemMyHealthUploadDocBinding

enum class TYPE {
    DELETE, VIEW
}

class MyHealthUploadDocAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthUploadDocAdapter.MyViewHolder>() {

    var itemList: List<UserSavedFile>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(type: TYPE, data: UserSavedFile, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(val binding: ItemMyHealthUploadDocBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMyHealthUploadDocBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHealthUploadDocAdapter.MyViewHolder, position: Int) {
        itemList?.get(position).let { detail ->
            holder.binding.tvName.text = detail!!.file_name
            holder.binding.tvCategory.text = detail.category
            holder.binding.tvDateTime.text = detail.upload_date
            holder.binding.rllDelete.setOnClickListener {
                onItemClickListener?.onClicked(
                    TYPE.DELETE, detail,
                    position
                )
            }
            holder.binding.llData.setOnClickListener {
                onItemClickListener?.onClicked(
                    TYPE.VIEW, detail,
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    fun updateList(list: List<UserSavedFile>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}