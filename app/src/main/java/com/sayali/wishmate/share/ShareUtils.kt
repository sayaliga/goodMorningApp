package com.sayali.wishmate.share

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    suspend fun shareToWhatsApp(context: Context, imageUrl: String, caption: String?) {
        val bitmap = loadBitmapFromUrl(context, imageUrl)
        if (bitmap != null) {
            shareToWhatsApp(context, bitmap, caption)
        } else {
            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareToWhatsApp(context: Context, bitmap: Bitmap, caption: String?) {
        val imageUri = cacheImage(context, bitmap)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            // WhatsApp reads this as the image caption
            caption?.let { putExtra(Intent.EXTRA_TEXT, it) }
            // Grant permission for the receiving app to read the Uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // 1. Try to target standard WhatsApp first
        try {
            intent.setPackage("com.whatsapp")
            context.startActivity(intent)
        } catch (e: Exception) {
            // 2. If standard WhatsApp is not found, try WhatsApp Business
            try {
                intent.setPackage("com.whatsapp.w4b")
                context.startActivity(intent)
            } catch (e: Exception) {
                // 3. If neither is installed, show a toast or fallback to generic chooser
                Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                // Optional: Fallback to generic chooser if you want to allow other apps
                // context.startActivity(Intent.createChooser(intent, "Share via"))
            }
        }
    }

    suspend fun shareToOtherApps(
        context: Context,
        imageUrl: String,
        caption: String?
    ) {
        val bitmap = loadBitmapFromUrl(context, imageUrl)
        if (bitmap != null) {
            shareToOtherApps(context, bitmap, caption)
        } else {
            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareToOtherApps(
        context: Context,
        bitmap: Bitmap,
        caption: String?
    ) {
        val imageUri = cacheImage(context, bitmap)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            caption?.let { putExtra(Intent.EXTRA_TEXT, it) }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // 👇 Important: NO setPackage here
        val chooser = Intent.createChooser(intent, "Share via")
        context.startActivity(chooser)
    }

    private suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
        return try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Software bitmap required for file saving
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            (result as BitmapDrawable).bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun cacheImage(context: Context, bitmap: Bitmap): Uri {
        // Use 'cacheDir' to avoid cluttering the user's gallery
        val imagesFolder = File(context.cacheDir, "shared_images")
        imagesFolder.mkdirs()

        // Overwrite the same file or use a timestamp if you want history.
        // Using a fixed name prevents filling up storage with temp files.
        val file = File(imagesFolder, "share_image.png")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Ensure your authority matches the one in AndroidManifest.xml
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}