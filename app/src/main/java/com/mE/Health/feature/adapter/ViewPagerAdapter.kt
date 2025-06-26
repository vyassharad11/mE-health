package com.mE.Health.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mE.Health.R
import com.mE.Health.models.AdviceDTO

class ViewPagerAdapter(val context: Context, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var ivType: ImageView = itemView.findViewById(R.id.ivType)
//        var tvName: TextView = itemView.findViewById(R.id.tvName)
//        var tvCount: TextView = itemView.findViewById(R.id.tvCount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.MyViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_health_view_pager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.MyViewHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//        }
//
//        holder.ivType.setImageResource(itemList[position].image)
//        holder.tvName.text = itemList[position].name
//        holder.tvCount.text = itemList[position].count
    }

    private val runnable = Runnable {
//        itemList.addAll(imageList)
//        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return 10
    }
}