package com.study.vhra.glcardmenu

import android.opengl.Matrix

class Camera3D (
    val eye: FloatArray = FloatArray(3),
    val center: FloatArray = FloatArray(3),
    val up: FloatArray
) {
    fun getMatrix(): FloatArray {
        val viewMatrix = FloatArray(16)
        Matrix.setLookAtM(
            viewMatrix,
            0,
            eye[0], eye[1], eye[2],
            center[0], center[1], center[2],
            up[0], up[1], up[2]
        )
        return viewMatrix
    }
}