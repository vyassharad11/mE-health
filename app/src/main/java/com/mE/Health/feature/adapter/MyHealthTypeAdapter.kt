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


class MyHealthTypeAdapter(private val mContext: Context,
     private val itemList: List<MyHealthTypeModel>) :
    RecyclerView.Adapter<MyHealthTypeAdapter.MyViewHolder>() {

    var selectedItem = 0

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvLayout: com.google.android.material.card.MaterialCardView = itemView.findViewById(R.id.cvLayout)
        var llMain: LinearLayout = itemView.findViewById(R.id.llMainLayout)
        var rllBottomView: RoundLinearLayout = itemView.findViewById(R.id.rllBottomView)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvCount: TextView = itemView.findViewById(R.id.tvCount)
        var ivType: ImageView = itemView.findViewById(R.id.ivType)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthTypeAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_health_view_pager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthTypeAdapter.MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val data = itemList[position]
        holder.tvName.text = data.name
        holder.tvCount.text = data.count
        holder.ivType.setImageResource(data.icon)

        if (position == selectedItem) {
            setActiveType(holder)
        } else {
            setInActiveType(holder)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    private fun setActiveType(holder: MyHealthTypeAdapter.MyViewHolder){
        val layoutParams = holder.cvLayout.layoutParams
        layoutParams.height =mContext.resources.getDimensionPixelOffset(R.dimen.dp_280)
        layoutParams.width = mContext.resources.getDimensionPixelOffset(R.dimen.dp_180)
        holder.cvLayout.layoutParams = layoutParams
        holder.llMain.setPadding(0, 0, 0, 0)
        holder.rllBottomView.delegate.backgroundColor = ContextCompat.getColor(mContext,R.color.color_FF6605)
        holder.cvLayout.strokeColor = ContextCompat.getColor(mContext,R.color.color_FF6605)
    }

    private fun setInActiveType(holder: MyHealthTypeAdapter.MyViewHolder){
        holder.llMain.setPadding(0, mContext.resources.getDimensionPixelOffset(R.dimen.dp_20), 0, 0)
        val layoutParams = holder.cvLayout.layoutParams
        layoutParams.height =mContext.resources.getDimensionPixelOffset(R.dimen.dp_240)
        layoutParams.width = mContext.resources.getDimensionPixelOffset(R.dimen.dp_180)
        holder.cvLayout.layoutParams = layoutParams
        holder.rllBottomView.delegate.backgroundColor = ContextCompat.getColor(mContext,R.color.color_6E6B78)
        holder.cvLayout.strokeColor = ContextCompat.getColor(mContext,R.color.white)
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