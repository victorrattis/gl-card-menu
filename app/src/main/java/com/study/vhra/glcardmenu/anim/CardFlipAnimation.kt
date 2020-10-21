package com.study.vhra.glcardmenu.anim

import android.opengl.Matrix
import kotlin.math.max
import kotlin.math.min

class CardFlipAnimation : Animation {
    private var play = false
    var percentage: Int = 0
    var angle: Int = 180
    var start: Int = 0

    override fun update(area: FloatArray, matrix: FloatArray) {
        if (play) {
            percentage += 2
        }

        if (percentage >= 100) {
           play = false
        }

        val width = max(area[0], area[2]) - min(area[0], area[2])
        val result = (width * percentage / 100f)

        if (start == 0) {
            Matrix.translateM(matrix, 0, (width / 2f) - (result), 0f, 0f)
            Matrix.rotateM(matrix, 0, start + (angle * percentage / 100f), 0f, -1f, 0f)
            Matrix.translateM(matrix, 0, -width / 2f, 0f, 0f)
        } else {
            Matrix.translateM(matrix, 0, (width / 2f) - (width - result), 0f, 0f)
            Matrix.rotateM(matrix, 0, start - (angle * percentage / 100f), 0f, -1f, 0f)
            Matrix.translateM(matrix, 0, -(width / 2f), 0f, 0f)
        }
    }

    override fun stop() { }

    override fun play() {
        if (play) return

        if (percentage >= 100) {
            start = if (start == 0) { 180 } else { 0 }
        }
        percentage = 0
        play = true
    }

    override fun isPlay(): Boolean = play
}