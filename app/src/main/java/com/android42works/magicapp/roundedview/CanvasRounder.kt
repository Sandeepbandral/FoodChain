package com.android42works.magicapp.roundedview

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF

internal class CanvasRounder(cornerRadius: Float) {
    private val path = android.graphics.Path()
    private var rectF: RectF = RectF(0f, 0f, 0f, 0f)
    var cornerRadius: Float = cornerRadius
        set(value) {
            field = value
            resetPath()
        }

    fun round(canvas: Canvas, drawFunction: (Canvas) -> Unit) {
        val save = canvas.save()
        canvas.clipPath(path)
        drawFunction(canvas)
        canvas.restoreToCount(save)
    }

    fun updateSize(currentWidth: Int, currentHeight: Int) {
        rectF = android.graphics.RectF(0f, 0f, currentWidth.toFloat(), currentHeight.toFloat())
        resetPath()
    }

    private fun resetPath() {
        path.reset()
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
        path.close()
    }
}