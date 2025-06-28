package com.mE.Health.utility

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mE.Health.R

class DialogOK(context: Context, title: String, message: String) : Dialog(context) {
    var onClickCallback: OkClickCallback? = null

    init {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_ok)
            this.window!!.setBackgroundDrawable(ColorDrawable(0))
            this.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.setCanceledOnTouchOutside(true)
            this.setCancelable(true)

            val tvTitle = this.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = this.findViewById<TextView>(R.id.tvMessage)
            val tvButton = this.findViewById<TextView>(R.id.tvOk)
            tvButton.setTextColor(ContextCompat.getColor(context, R.color.text_color_orange))
            if (TextUtils.isEmpty(title))
                tvTitle!!.visibility = View.GONE
            else {
                tvTitle!!.visibility = View.VISIBLE
                tvTitle!!.text = "" + title
            }
            tvMessage!!.text = "" + message
            tvMessage!!.movementMethod = ScrollingMovementMethod()
            tvButton.setOnClickListener {
                if (onClickCallback != null) onClickCallback!!.onOk()
                dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OkClickCallback {
        fun onOk()
    }
}
