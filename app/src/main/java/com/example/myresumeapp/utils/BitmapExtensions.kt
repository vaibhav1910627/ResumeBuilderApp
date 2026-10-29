package com.example.myresumeapp.utils

import android.graphics.Bitmap

    fun Bitmap.toSoftwareBitmap(): Bitmap {
        return if (this.config == Bitmap.Config.HARDWARE) {
            this.copy(Bitmap.Config.ARGB_8888, false)
        } else {
            this
        }
    }

//writing this all to ensure that our bitmap is compatible with pdf or not
