package com.mE.Health.utility

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mE.Health.R
import com.mE.Health.utility.roundview.RoundLinearLayout
import java.io.File


class BottomSheetUserSavedFilePreview(
    private val mContext: Context,
    private val fileName: String,
    private val fileType: String,
    private val filePath: String
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_file_preview,
            container, false
        )
        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            BottomSheetDialogFragment.STYLE_NORMAL,
            R.style.CustomBottomSheetDialogTheme
        ) /* hack to make background transparent */
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val ivPreview = view.findViewById<ImageView>(R.id.ivPreview)
        val videoView = view.findViewById<VideoView>(R.id.videoView)
        val rllCancel = view.findViewById<RoundLinearLayout>(R.id.rllCancel)
        tvName.text = fileName
        rllCancel.setOnClickListener {
            dismiss()
        }

        if (fileType == Constants.FILE_IMAGE) {
            val displayMetrics = Utilities.getDeviceDisplayMetrics()
            val screenWidth = displayMetrics.first
            val screenHeight = displayMetrics.second
            Glide.with(mContext)
                .load(filePath)
                .override(screenWidth, screenHeight)
                .fitCenter()
                .into(ivPreview)
        } else {
            val file = File(filePath)
            val uri = Uri.fromFile(file)
            ivPreview.visibility = View.GONE
            videoView.visibility = View.VISIBLE
            val retriever = android.media.MediaMetadataRetriever()
            retriever.setDataSource(requireContext(), uri)
            val width =
                retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    ?.toIntOrNull() ?: 0
            val height =
                retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                    ?.toIntOrNull() ?: 0
            retriever.release()
            val params = videoView.layoutParams
            params.width = width
            params.height = height
            videoView.layoutParams = params
            videoView.setVideoURI(uri)
            videoView.visibility = View.VISIBLE
            val mediaController = MediaController(requireContext())
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.start()
        }
    }
}