package com.mE.Health.feature

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.Slide
import com.mE.Health.HomeActivity
import com.mE.Health.MyApplication
import com.mE.Health.R
import com.mE.Health.utility.BaseInterface
import com.mE.Health.utility.DialogOK
import com.mE.Health.utility.DialogProgress
import com.mE.Health.viewmodels.assist.AssistViewModel
import com.mE.Health.viewmodels.mockData.MockDataViewModel
import dagger.hilt.android.internal.managers.ViewComponentManager
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale
import kotlin.getValue


open class BaseFragment : Fragment() {

    val mockViewModel: MockDataViewModel by activityViewModels()
    val assistViewModel: AssistViewModel by activityViewModels()
    var dialogProgress: DialogProgress? = null
    var dialogOK: Dialog? = null


    fun replaceFragmentLogin(
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


    fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            if (updateSideNavMenuVisibility(requireActivity())){
                return
            }
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

    fun addFragmentLogin(
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

    fun addFragment(
        containerViewId: Int,
        fragment: Fragment,
        fragmentTag: String,
        backStackStateTag: String
    ) {
        try {
            if (activity == null) return
            if (updateSideNavMenuVisibility(requireActivity())){
                return
            }
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
                val response = readRawFileAsString(R.raw.country_codes)
                val array = JSONArray(response)
                for (i in 0 until array.length()) {
                    val jsonObject = array.getJSONObject(i)
                    if (countryIso.equals(
                            jsonObject.getString("alpha-2"),
                            ignoreCase = true
                        )
                    ) {
                        countryDTO = jsonObject.optString("phone-code")
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

    private fun readRawFileAsString(rawFile: Int): String {
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

    fun setBottomNavigationVisibility(context: Context) {
        (getActivity(context) as HomeActivity).setBottomNavigationVisibility()
    }

     fun refreshBottomMenu(context: Context) {
        (getActivity(context) as HomeActivity).refreshMenu()
    }

    fun activeDashboardMenu(context: Context) {
        if (!(getActivity(context) as HomeActivity).getSideNavStatus())
            (getActivity(context) as HomeActivity).activeDashboardMenu()
    }

    fun activeHomeMenu(context: Context) {
        (getActivity(context) as HomeActivity).activeHomeMenu()
    }

   fun updateSideNavStatus(context: Context) {
        (getActivity(context) as HomeActivity).updateSideNavStatus()
    }

    fun openSetting(context: Context) {
        (getActivity(context) as HomeActivity).openSetting()
    }


    private fun updateSideNavMenuVisibility(mActivity: Activity) : Boolean {
        var status  = false
        val mCurrentActivity = (mActivity.applicationContext as MyApplication).getCurrentActivity()
        if ((mCurrentActivity as HomeActivity) != null) {
            status = (mActivity as HomeActivity).hideSideNavRequired()
        }
        return status
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
        if (TextUtils.isEmpty(title)) tvTitle.visibility = View.INVISIBLE
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


    var filePath = ""
     var cameraUri: Uri? = null
     var cropPicturePath = ""
     var picturePath = ""
     var imageStoragePath = ""

    fun log(tag: String, str: String) {
        Log.i(tag, str)
    }

    fun checkPermission(
        permission: Array<String>,
        requestCode: Array<Int> = arrayOf(101)
    ): Boolean {
        var check = true
        val notGPermission: ArrayList<String> = ArrayList()
        val notGPermissionRequest: ArrayList<Int> = ArrayList()
        for (i in 0 until permission.size) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                log(javaClass.name, "Permission for : ${permission[i]} not granted")
                notGPermission.add(permission[i])
                notGPermissionRequest.add(requestCode[0])
                check = false
            } else {
                log(javaClass.name, "Permission for : ${permission[i]} granted")
            }
        }
        if (!check) {
            requestPermissions(
                notGPermission.toArray(arrayOfNulls<String>(notGPermission.size)),
                requestCode[0]
            )
        }
        return check
    }

    interface OnClickCallback {
        fun onClick( position: Int)
    }

    fun showUploadDocument( onClickCallback: OnClickCallback) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_camera_video)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.setCancelable(true)
        val tvPicture = dialog.findViewById<TextView>(R.id.tvPicture)
        val tvVideo = dialog.findViewById<TextView>(R.id.tvVideo)
        val tvDocument = dialog.findViewById<View>(R.id.tvDocument)
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel)
        tvPicture.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(1)
        })
        tvVideo.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(2)
        })
        tvDocument.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            onClickCallback.onClick(3)
        })
        tvCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }

    fun openGallery() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            requireActivity().startActivityForResult(Intent.createChooser(intent, ""), BaseInterface.GALLERY)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun openVideo() {
        try {
            var mediaChooser = Intent(Intent.ACTION_GET_CONTENT)
            mediaChooser.setType("video/*")
            requireActivity().startActivityForResult(mediaChooser, BaseInterface.TAKE_VIDEO)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
        }
    }


    private val isSDCARDMounted: Boolean
        get() {
            val status = Environment.getExternalStorageState()
            return status == Environment.MEDIA_MOUNTED
        }

    fun getAbsolutePath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = requireActivity().contentResolver.query(
            uri, projection,
            null, null, null
        )
        var filePath = ""
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            filePath = cursor.getString(columnIndex)
        }
        cursor?.close()
        return filePath
    }

    fun getImageFilePath(uri: Uri): String? {
        val file = File(uri.path)
        var res: String? = null
        val filePath =
            file.path.split(":".toRegex()).toTypedArray()
        val image_id = filePath[filePath.size - 1]
        val cursor = requireActivity().getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf(image_id), null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            res = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
        }
        return res
    }


    protected fun performCrop(activity: Activity, picUri: Uri) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            cropIntent.setDataAndType(picUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri(activity))
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            startActivityForResult(cropIntent, BaseInterface.CROP)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(
                activity,
                "crop_action_support",
                Toast.LENGTH_SHORT
            )
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                activity,
                "crop_action_support",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

     fun getTempUri(activity: Activity): Uri {
        return Uri.fromFile(getTempFile(activity))
    }

    private fun getTempFile(activity: Activity): File {
        val imageName = SimpleDateFormat("ddMMMyyyy_HHmmss", Locale.US)
            .format(Date()) + ".jpg"
        val tempFile = getNewFile(BaseInterface.IMAGE_DIRECTORY_CROP, imageName)
        cropPicturePath = tempFile.path
        return tempFile
    }


    fun getNewFile(directoryName: String, imageName: String): File {
        val file: File
        if (isSDCARDMounted) {
            val folder = File(requireActivity().getExternalFilesDir(""), directoryName)
            folder.mkdir()
            file = File(folder, imageName)
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            file = File(requireActivity().filesDir, imageName)
        }
        return file
    }


}