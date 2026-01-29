package com.example.wishmate.imagegen.providers


import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.sayali.wishmate.imagegen.ImageGenerator
import kotlinx.coroutines.delay


class MockGenerator : ImageGenerator {
    override suspend fun generate(context: Context, prompt: String, overlayText: String?): Bitmap {
// Simulate network
        delay(800)
        val width = 1080
        val height = 1350 // portrait friendly for Status
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint().apply { isAntiAlias = true }


// Background gradient
        val shader = LinearGradient(0f, 0f, 0f, height.toFloat(),
            Color.parseColor("#FFF1DA"), Color.parseColor("#FFD1E3"), Shader.TileMode.CLAMP)
        paint.shader = shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null


// Overlay prompt (faint)
        paint.color = Color.argb(40, 0, 0, 0)
        paint.textSize = 28f
        canvas.drawText(prompt.take(28), 40f, height - 80f, paint)


// Main text
        overlayText?.let {
            paint.color = Color.BLACK
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 84f
            paint.setShadowLayer(12f, 0f, 8f, Color.argb(90, 0, 0, 0))
            val x = width / 2f
            val y = height / 2f
            drawMultilineCenteredText(canvas, it, x, y, paint, width - 160)
        }
        return bmp
    }


    private fun drawMultilineCenteredText(
        canvas: Canvas, text: String, cx: Float, cy: Float, paint: Paint, maxWidth: Int
    ) {
        val static = StaticLayout.Builder.obtain(text, 0, text.length, paint.asTextPaint(), maxWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()
        canvas.save()
        canvas.translate(cx - static.width/2f, cy - static.height/2f)
        static.draw(canvas)
        canvas.restore()
    }


    private fun Paint.asTextPaint(): TextPaint = TextPaint().apply {
        color = this@asTextPaint.color
        textSize = this@asTextPaint.textSize
        typeface = this@asTextPaint.typeface
        isAntiAlias = true

        // Shadow layer support (safe for all API levels)
        try {
            // if setShadowLayer was used earlier, copy it manually
            setShadowLayer(4f, 0f, 0f, android.graphics.Color.argb(90, 0, 0, 0))
        } catch (_: Exception) {
            // ignore, not critical for API < 29
        }
    }

}