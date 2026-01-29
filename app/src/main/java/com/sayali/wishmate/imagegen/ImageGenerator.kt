package com.sayali.wishmate.imagegen


import android.content.Context
import android.graphics.Bitmap


interface ImageGenerator {
    suspend fun generate(context: Context, prompt: String, overlayText: String? = null): Bitmap
}