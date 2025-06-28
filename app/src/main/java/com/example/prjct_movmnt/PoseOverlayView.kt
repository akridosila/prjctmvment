package com.example.prjct_movmnt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PoseOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var pose: Pose? = null
    private var elbowAngle: Float? = null
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        textSize = 60f
    }

    fun setPose(p: Pose) {
        pose = p
        invalidate()
    }

    fun setAngle(angle: Float) {
        elbowAngle = angle
        invalidate()
    }
    val IMAGE_HEIGHT = 640
    val IMAGE_WIDTH = 480
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        pose?.allPoseLandmarks?.forEach { lm ->
            val x = lm.position.x * width  / IMAGE_WIDTH
            val y = lm.position.y * height / IMAGE_HEIGHT
            canvas.drawCircle(x, y, 10f, paint)
        }
        // If we have an angle, draw it near the elbow landmark:
        elbowAngle?.let { angle ->
            pose?.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)?.let { elbow ->
                val ex = elbow.position.x * width  / IMAGE_WIDTH
                val ey = elbow.position.y * height / IMAGE_HEIGHT
                canvas.drawText("${angle.toInt()}°", ex + 20f, ey - 20f, paint)
            }
        }
    }
}
