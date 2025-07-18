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
import com.mE.Health.data.model.Condition
import com.mE.Health.data.model.ProviderDTO
import com.mE.Health.models.ProviderDetail
import com.mE.Health.utility.Constants

class ProviderAdapter(private val mContext: Context,private val tabType: String, private val list: List<ProviderDTO>) :
    RecyclerView.Adapter<ProviderAdapter.MyViewHolder>() {

    private var itemList: List<ProviderDTO> = ArrayList()
    var type = Constants.ALL
    interface OnClickCallback {
        fun onClicked(detail: ProviderDTO, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    init {
        this.itemList = list
        this.type = tabType
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivLogo: ImageView = itemView.findViewById(R.id.ivLogo)
        var tvMyChart: TextView = itemView.findViewById(R.id.tvMyChart)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvConnect: TextView = itemView.findViewById(R.id.tvConnect)
        var ivReload: ImageView = itemView.findViewById(R.id.ivReload)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProviderAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_provider, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProviderAdapter.MyViewHolder, position: Int) {
        val data = itemList[position]
        Glide.with(mContext)
            .load(data.logo_url)
            .into(holder.ivLogo)
        holder.tvName.text = data.practice_name
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                data,
                position
            )
        }
        if (type == Constants.CONNECTED) {
            holder.tvConnect.text = mContext.getString(R.string.dis_connected)
            holder.ivReload.visibility = View.VISIBLE
        } else {
            holder.tvConnect.text = mContext.getString(R.string.connected)
            holder.ivReload.visibility = View.GONE
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

    fun updateList(list: List<ProviderDTO>) {
        this.itemList = list
        notifyDataSetChanged()
    }

    fun updateListWithTab(list: List<ProviderDTO>, type: String) {
        this.type = type
        this.itemList = list
        notifyDataSetChanged()
    }
}