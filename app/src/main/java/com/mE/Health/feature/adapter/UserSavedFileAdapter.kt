package com.mE.Health.feature.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mE.Health.R
import com.mE.Health.data.model.DiagnosticReport
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.databinding.ItemImagingPreviewBinding
import com.mE.Health.databinding.ItemMyHealthLabBinding
import com.mE.Health.databinding.ItemUserSaveFileBinding
import com.mE.Health.utility.Constants

class UserSavedFileAdapter(private val mContext: Context) :
    RecyclerView.Adapter<UserSavedFileAdapter.MyViewHolder>() {

    var itemList: List<UserSavedFile>? = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickCallback {
        fun onClicked(item: UserSavedFile?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(val binding: ItemUserSaveFileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserSaveFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item: UserSavedFile? = itemList?.get(position)
        item?.let {
            holder.binding.tvName.text = "File name : "+it.file_name
            holder.itemView.setOnClickListener {
                onItemClickListener?.onClicked(
                    item,
                    position
                )
            }
            if (it.file_type == Constants.FILE_IMAGE) {
                Glide.with(mContext)
                    .load(it.file_path)
                    .into(holder.binding.ivUserSaved)
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