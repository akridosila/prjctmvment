package com.example.prjct_movmnt

import android.graphics.PointF
import kotlin.math.acos
import kotlin.math.sqrt

object PoseUtils {
    // Ypologismos gwniwn se angle metaksi twn shmeiwsewn a, b, c
    fun calculateAngle(a: PointF, b: PointF, c: PointF): Float {
        val ab = PointF(a.x - b.x, a.y - b.y)
        val cb = PointF(c.x - b.x, c.y - b.y)

        val dotProduct = ab.x * cb.x + ab.y * cb.y
        val magAb = sqrt(ab.x * ab.x + ab.y * ab.y)
        val magCb = sqrt(cb.x * cb.x + cb.y * cb.y)

        // Protect against division by zero
        if (magAb * magCb == 0f) return 0f

        val cosAngle = (dotProduct / (magAb * magCb)).coerceIn(-1f, 1f)
        return Math.toDegrees(acos(cosAngle.toDouble())).toFloat()
    }
}
