package com.study.vhra.glcardmenu.anim

import android.opengl.Matrix

class RotateAnimation : Animation {
    var angle = 0f
    var stop = false

    override fun update(area: FloatArray, matrix: FloatArray) {
        if (!stop) {
            angle += 1
            if (angle > 360f) angle = 0f
        }
        Matrix.rotateM(matrix, 0, angle, 0f, -1f, 0f)
    }

    override fun stop() {
        stop = true
    }

    override fun play() {
        stop = false
    }

    override fun isPlay(): Boolean = !stop
}