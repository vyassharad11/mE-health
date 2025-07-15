package com.mE.Health.utility

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import com.mE.Health.R

object LoaderDialog {

    private var dialog: AlertDialog? = null

    fun show(context: Context) {
        if (dialog?.isShowing == true) return

        val builder = AlertDialog.Builder(context)
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_loader, null)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog?.show()
    }

    fun hide() {
        dialog?.dismiss()
        dialog = null
    }
}