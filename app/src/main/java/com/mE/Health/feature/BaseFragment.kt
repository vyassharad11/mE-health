package com.mE.Health.feature

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.transition.Slide
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.utility.DialogOK
import com.mE.Health.utility.DialogProgress
import dagger.hilt.android.internal.managers.ViewComponentManager
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale


open class BaseFragment : Fragment(){

    var dialogProgress: DialogProgress? = null
    var dialogOK: Dialog? = null


    fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    fun addFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    fun replaceFragmentWithoutBack(containerViewId: Int, fragment: Fragment, fragmentTag: String) {
        try {
            if (activity == null) return
            fragment.enterTransition = Slide(Gravity.END)
            fragment.exitTransition = Slide(Gravity.START)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: Error) {
            e.printStackTrace()
        }
    }

    val countryDefault: String?
        get() {
            var countryDTO: String? = null
            try {
                val tm = requireActivity()
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                var countryIso = tm.networkCountryIso
                if (TextUtils.isEmpty(countryIso))
                    countryIso = Locale.getDefault().country
                val response = readRawFileAsString( R.raw.country_codes)
                val array = JSONArray(response)
                for (i in 0 until array.length()) {
                    val jsonObject = array.getJSONObject(i)
                    if (countryIso.equals(
                            jsonObject.getString("alpha-2"),
                            ignoreCase = true
                        )
                    ) {
                        countryDTO =jsonObject.optString("phone-code")
                        break
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return countryDTO
        }

    private fun readRawFileAsString( rawFile: Int): String {
        val inputStream = requireActivity().resources.openRawResource(rawFile)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuffer()
        while (true) {
            val line = reader.readLine() ?: break
            result.append(line)
        }
        reader.close()
        return result.toString()
    }

    fun showProgressDialog() {
        try {
            if (activity != null) {
                if (dialogProgress != null && dialogProgress!!.isShowing)
                    dialogProgress!!.dismiss()
                dialogProgress = DialogProgress(requireActivity())
                dialogProgress!!.show()
            }
        } catch (e: java.lang.Exception) {
        }
    }

    fun hideProgressDialog() {
        try {
            if (dialogProgress != null && dialogProgress!!.isShowing)
                dialogProgress!!.dismiss()
        } catch (e: java.lang.Exception) {

        }
    }

    fun showDialogOk(message: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_ok)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        tvMessage.text = message
        tvTitle.visibility = View.VISIBLE
        val tvOk = dialog.findViewById<View>(R.id.tvOk)
        tvOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    fun setBottomNavigationVisibility(context: Context){
        (getActivity(context) as HomeActivity).setBottomNavigationVisibility()
    }

    private fun getActivity(context: Context): Context {
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }

    fun openReadMoreDialog(context: Context, title: String, message: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_read_more)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
//        dialog.window!!.attributes.windowAnimations = R.style.animation
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        if (TextUtils.isEmpty(title))tvTitle.visibility = View.INVISIBLE
        tvTitle.text = title
        tvMessage.text = message
        val tvOk = dialog.findViewById<View>(R.id.tvOk)
        tvOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    val isNetworkAvailable: Boolean
        get() {
            try {
                val cm =
                    requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected)
                    return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


    fun dialogOK(context: Context, title: String, message: String) {
        if (context == null) return
        if (dialogOK != null && dialogOK!!.isShowing)
            dialogOK!!.dismiss()
        dialogOK = DialogOK(context, title, message)
        dialogOK!!.show()
    }
}