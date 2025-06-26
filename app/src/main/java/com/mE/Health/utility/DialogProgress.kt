package com.mE.Health.utility

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.mE.Health.R


class DialogProgress(context: Context) : Dialog(context) {
    init {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.progress_loader)
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
            this.window!!.setBackgroundDrawable(ColorDrawable(0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
