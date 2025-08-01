package com.mE.Health.utility

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mE.Health.data.model.UserSavedFile
import com.mE.Health.feature.adapter.UserSavedFileAdapter

object UserSaveFileUtil {

    fun initSaveFileLayout(
        mActivity: FragmentActivity,
        rvUserSavedFile: RecyclerView,
        userSavedFileList: List<UserSavedFile>?
    ) {
        rvUserSavedFile.layoutManager = GridLayoutManager(mActivity, 2)
        val userSavedFileAdapter = UserSavedFileAdapter(mActivity)
        userSavedFileAdapter.itemList = userSavedFileList ?: ArrayList()
        rvUserSavedFile.adapter = userSavedFileAdapter
        userSavedFileAdapter.onItemClickListener = object : UserSavedFileAdapter.OnClickCallback {
            override fun onClicked(item: UserSavedFile?, position: Int) {
                when (item?.file_type) {
                    Constants.FILE_IMAGE -> {
                        val bottomSheet = BottomSheetUserSavedFilePreview(
                            mActivity,
                            item.file_name,item.file_type, item.file_path
                        )
                        bottomSheet.show(
                            mActivity.supportFragmentManager,
                            "BottomSheetUserSavedFilePreview"
                        )
                    }

                    Constants.FILE_DOCUMENT -> {
//                                    val pdfFile = File(item.file_path) // Your file path
//                                    val pdfUri = FileProvider.getUriForFile(
//                                        requireActivity(),
//                                        requireActivity().packageName + ".fileprovider",
//                                        pdfFile
//                                    )
//                                    openPdf(
//                                        requireActivity(),
//                                        pdfUri
//                                    )
                    }

                    else -> {

                    }
                }
            }
        }
    }
}