package com.mE.Health.utility

interface BaseInterface {
    companion object {


        /**
         * Image Storage Path
         */
        const val FILE_DIRECTORY = ".downloadedFile"
        const val IMAGE_DIRECTORY_CROP = ".downloadedFile"
        const val IMAGE_DIRECTORY_COMPRESS = ".downloadedFile"
        const val DOWNLOADED_FILE_DIRECTORY = ".downloadedFile"

        const val PERMISSIONS_REQUEST = 1001
        const val GALLERY = 111
        const val CAMERA = 112
        const val CROP = 113
        const val FILE = 114
        const val GALLERY_MULTIPLE = 115
        const val TAKE_VIDEO = 118
        val PDF_SELECTION = 116
        val DOC_SELECTION = 117
    }
}
