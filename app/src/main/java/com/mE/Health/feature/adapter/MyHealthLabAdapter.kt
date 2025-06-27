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
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants
import com.mE.Health.utility.roundview.RoundLinearLayout
import com.mE.Health.utility.roundview.RoundTextView

class MyHealthLabAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthLabAdapter.MyViewHolder>() {

    private var itemList: List<ProviderDetail> = ArrayList()
    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvViewDetail: RoundTextView = itemView.findViewById(R.id.tvViewDetail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthLabAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_lab, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthLabAdapter.MyViewHolder, position: Int) {
        holder.tvViewDetail.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.tvViewDetail,
                position
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 10
    }
}