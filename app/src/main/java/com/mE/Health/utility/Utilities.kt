package com.mE.Health.utility

import android.content.Context
import androidx.core.content.ContextCompat
import com.mE.Health.R

object Utilities {

    fun getVisitUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status) {
            "finished" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "planned" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            "scheduled" ->  Pair(ContextCompat.getColor(mContext, R.color.color_0063F7),ContextCompat.getColor(mContext, R.color.color_1A0063F7))
            "Canceled" -> Pair(ContextCompat.getColor(mContext, R.color.color_F02C2C),ContextCompat.getColor(mContext, R.color.color_1AF02C2C))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270)) // Default case
        }
    }

    fun getProcedureUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status) {
            "completed" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "Pending" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00)) // Default case
        }
    }
}