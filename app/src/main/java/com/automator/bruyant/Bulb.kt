package com.automator.bruyant

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Bulb @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            val paint = Paint()
            paint.color = Color.BLUE
            canvas.drawCircle(width / 2.0f, height / 2.0f, width / 2.0f, paint)
        }
        super.onDraw(canvas)
    }
}