package com.example.prjct_movmnt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.prjct_movmnt.data.MovementReading

class SessionChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 5f
        isAntiAlias = true
    }

    private var points: List<MovementReading> = emptyList()

    fun setReadings(data: List<MovementReading>) {
        points = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.size < 2) return

        val maxAngle = points.maxOf { it.angle }
        val scaleX = width.toFloat() / (points.size - 1)
        val scaleY = height.toFloat() / maxAngle

        for (i in 0 until points.size - 1) {
            val x1 = i * scaleX
            val y1 = height - (points[i].angle * scaleY)
            val x2 = (i + 1) * scaleX
            val y2 = height - (points[i + 1].angle * scaleY)
            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}
