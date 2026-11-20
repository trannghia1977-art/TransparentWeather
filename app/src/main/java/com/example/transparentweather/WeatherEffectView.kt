package com.example.transparentweather

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class WeatherEffectView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private data class RainDrop(var x: Float, var y: Float, var speed: Float, var length: Float)

    private val rainDrops = List(130) {
        RainDrop(
            x = Random.nextFloat() * 1400f,
            y = Random.nextFloat() * 2600f,
            speed = Random.nextFloat() * 26f + 22f,
            length = Random.nextFloat() * 38f + 22f
        )
    }

    private val rainPaint = Paint().apply {
        color = Color.parseColor("#99B3E5FC")
        strokeWidth = 3.2f
        strokeCap = Paint.Cap.ROUND
    }

    private val lightningPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 6f
        style = Paint.Style.STROKE
        setShadowLayer(24f, 0f, 0f, Color.CYAN)
    }

    private var flashAlpha = 0
    var windTilt = 14f
    private val lightningPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (flashAlpha > 0) {
            canvas.drawColor(Color.argb(flashAlpha, 255, 255, 255))
            if (flashAlpha > 110) {
                canvas.drawPath(lightningPath, lightningPaint)
            }
            flashAlpha = (flashAlpha - 14).coerceAtLeast(0)
        }

        val w = if (width > 0) width.toFloat() else 1080f
        val h = if (height > 0) height.toFloat() else 2200f

        for (drop in rainDrops) {
            canvas.drawLine(
                drop.x, drop.y,
                drop.x - windTilt, drop.y + drop.length,
                rainPaint
            )
            drop.y += drop.speed
            drop.x -= windTilt * 0.45f

            if (drop.y > h) {
                drop.y = -drop.length
                drop.x = Random.nextFloat() * (w + 250f)
            }
        }

        if (Random.nextInt(120) < 2 && flashAlpha == 0) {
            triggerLightning()
        }

        postInvalidateOnAnimation()
    }

    fun triggerLightning() {
        val w = if (width > 0) width.toFloat() else 1080f
        val h = if (height > 0) height.toFloat() else 2200f

        flashAlpha = 190
        lightningPath.reset()
        var startX = Random.nextFloat() * (w * 0.8f) + (w * 0.1f)
        var startY = 0f
        lightningPath.moveTo(startX, startY)

        while (startY < h * 0.65f) {
            startX += Random.nextFloat() * 110f - 55f
            startY += Random.nextFloat() * 90f + 40f
            lightningPath.lineTo(startX, startY)
        }
        invalidate()
    }
}
