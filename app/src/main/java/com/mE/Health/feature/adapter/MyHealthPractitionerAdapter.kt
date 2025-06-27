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

class MyHealthPractitionerAdapter(private val mContext: Context) :
    RecyclerView.Adapter<MyHealthPractitionerAdapter.MyViewHolder>() {

    private var itemList: List<ProviderDetail> = ArrayList()
    var type = Constants.ALL

    interface OnClickCallback {
        fun onClicked(view: View?, position: Int)
    }

    var onItemClickListener: OnClickCallback? = null

    init {
//        this.itemList = list
//        this.type = tabType
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rllDetail: RoundLinearLayout = itemView.findViewById(R.id.rllDetail)
//        var tvMyChart: TextView = itemView.findViewById(R.id.tvMyChart)
//        var tvName: TextView = itemView.findViewById(R.id.tvName)
//        var tvConnect: TextView = itemView.findViewById(R.id.tvConnect)
//        var ivReload: ImageView = itemView.findViewById(R.id.ivReload)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHealthPractitionerAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_practitioner, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHealthPractitionerAdapter.MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return 10
    }
}