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
import com.mE.Health.models.AdviceDTO
import com.mE.Health.models.ByCountryState
import com.mE.Health.models.CountryState
import com.mE.Health.models.CountryStateData

class CountryStateListAdapter(private val mContext: Context, private val list: List<CountryState>) :
    RecyclerView.Adapter<CountryStateListAdapter.MyViewHolder>() {

    private var itemList: List<CountryState> = ArrayList()

    interface OnClickCallback {
        fun onClicked(view: View?, data: CountryState)
    }

    var onItemClickListener: OnClickCallback? = null

    init {
        this.itemList = list
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivType: ImageView = itemView.findViewById(R.id.ivType)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvCount: TextView = itemView.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryStateListAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_country_state, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryStateListAdapter.MyViewHolder, position: Int) {
        val data = itemList[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                data
            )
        }
        Glide.with(mContext)
            .load(data.logo)
            .circleCrop()
            .into(holder.ivType)

        holder.tvName.text = data.country ?: data.state
        holder.tvCount.text = data.count
//        holder.ivType.setImageResource(data.countryLogo!!)
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

    fun updateList(list: List<CountryState>) {
        this.itemList = list
        notifyDataSetChanged()
    }
}