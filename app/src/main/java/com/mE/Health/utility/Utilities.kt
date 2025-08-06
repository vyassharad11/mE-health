package com.mE.Health.utility

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mE.Health.R
import com.mE.Health.models.MyHealthTypeModel
import com.mE.Health.utility.Constants.ALLERGIES
import com.mE.Health.utility.Constants.APPOINTMENTS
import com.mE.Health.utility.Constants.BILLING
import com.mE.Health.utility.Constants.CONDITIONS
import com.mE.Health.utility.Constants.IMAGING
import com.mE.Health.utility.Constants.IMMUNIZATIONS
import com.mE.Health.utility.Constants.LABS
import com.mE.Health.utility.Constants.MEDICATIONS
import com.mE.Health.utility.Constants.PRACTITIONERS
import com.mE.Health.utility.Constants.PROCEDURES
import com.mE.Health.utility.Constants.RECORD_VAULT
import com.mE.Health.utility.Constants.VISITS
import com.mE.Health.utility.Constants.VITALS
import com.mE.Health.viewmodels.mockData.MockDataViewModel

object Utilities {

    fun getVisitUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status.lowercase()) {
            "finished","completed","active" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "planned","in progress","paid" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            "scheduled" ->  Pair(ContextCompat.getColor(mContext, R.color.color_0063F7),ContextCompat.getColor(mContext, R.color.color_1A0063F7))
            "canceled" -> Pair(ContextCompat.getColor(mContext, R.color.color_F02C2C),ContextCompat.getColor(mContext, R.color.color_1AF02C2C))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270)) // Default case
        }
    }

    fun getProcedureUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status.lowercase()) {
            "completed" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "pending" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            "in progress" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00)) // Default case
        }
    }

    fun getLabUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status.lowercase()) {
            "active","final" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "preliminary" ->  Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_F09C00),ContextCompat.getColor(mContext, R.color.color_AF09C00)) // Default case
        }
    }

    fun getConditionUIStatus(mContext: Context,status:String): Pair<Int, Int> {
        return when (status.lowercase()) {
            "finished","completed","active" -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270))
            "resolved" ->  Pair(ContextCompat.getColor(mContext, R.color.color_8A38F5),ContextCompat.getColor(mContext, R.color.color_1A8A38F5))
            "canceled","inactive", "cancelled" -> Pair(ContextCompat.getColor(mContext, R.color.color_F02C2C),ContextCompat.getColor(mContext, R.color.color_1AF02C2C))
            else -> Pair(ContextCompat.getColor(mContext, R.color.color_06C270),ContextCompat.getColor(mContext, R.color.color_A06C270)) // Default case
        }
    }

    fun openPdf(mainActivity: Activity, fileURI: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileURI, "application/pdf")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            mainActivity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                mainActivity,
                "No application found which can open the PDF file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getVideoThumbnail(videoPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(videoPath)
            retriever.getFrameAtTime(0) // 0 = first frame
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

    fun getDeviceDisplayMetrics(): Pair<Int, Int> {
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        return Pair(screenWidth, screenHeight)
    }

    fun getAllMyHealthType(mockViewModel: MockDataViewModel): ArrayList<MyHealthTypeModel> {
        val typeList: ArrayList<MyHealthTypeModel> = ArrayList()
        typeList.apply {
            add(
                MyHealthTypeModel(
                    PRACTITIONERS,
                    mockViewModel.practitionerList.value?.size.toString(),
                    R.drawable.ic_practitioner
                )
            )
            add(
                MyHealthTypeModel(
                    APPOINTMENTS,
                    mockViewModel.appointmentList.value?.size.toString(),
                    R.drawable.ic_appoinment
                )
            )
            add(
                MyHealthTypeModel(
                    VISITS,
                    mockViewModel.visitList.value?.size.toString(),
                    R.drawable.ic_visits
                )
            )
            add(
                MyHealthTypeModel(
                    CONDITIONS,
                    mockViewModel.conditionList.value?.size.toString(),
                    R.drawable.ic_conditions_my_health
                )
            )
            add(
                MyHealthTypeModel(
                    LABS,
                    mockViewModel.labList.value?.size.toString(),
                    R.drawable.ic_labs
                )
            )
            add(
                MyHealthTypeModel(
                    VITALS,
                    mockViewModel.vitalsList.value?.size.toString(),
                    R.drawable.ic_vitals
                )
            )
            add(
                MyHealthTypeModel(
                    MEDICATIONS,
                    mockViewModel.medicationList.value?.size.toString(),
                    R.drawable.ic_medication_my_health
                )
            )
            add(
                MyHealthTypeModel(
                    IMAGING,
                    mockViewModel.imagingList.value?.size.toString(),
                    R.drawable.ic_imaging
                )
            )
            add(
                MyHealthTypeModel(
                    PROCEDURES,
                    mockViewModel.procedureList.value?.size.toString(),
                    R.drawable.ic_procedures
                )
            )
            add(
                MyHealthTypeModel(
                    ALLERGIES,
                    mockViewModel.allergyList.value?.size.toString(),
                    R.drawable.ic_allergy
                )
            )
            add(
                MyHealthTypeModel(
                    IMMUNIZATIONS,
                    mockViewModel.immunizationList.value?.size.toString(),
                    R.drawable.ic_immunization
                )
            )
            add(
                MyHealthTypeModel(
                    BILLING,
                    mockViewModel.claimList.value?.size.toString(),
                    R.drawable.ic_billing
                )
            )
            add(
                MyHealthTypeModel(
                    RECORD_VAULT,
                    "6",
                    R.drawable.ic_upload_health
                )
            )
        }
        return typeList
    }

    fun getAllMyHealthType(): ArrayList<String> {
        val typeList: ArrayList<String> = ArrayList()
        typeList.apply {
            add(PRACTITIONERS)
            add(APPOINTMENTS)
            add(VISITS)
            add(CONDITIONS)
            add(LABS )
            add(VITALS)
            add(MEDICATIONS)
            add(IMAGING )
            add(PROCEDURES )
            add(ALLERGIES )
            add(IMMUNIZATIONS)
            add(BILLING )
        }
        return typeList
    }
}