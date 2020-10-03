package com.study.vhra.glcardmenu

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class CardModel {
    var vertices = floatArrayOf(
        -0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f
    )

    val vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertices.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(vertices)
            position(0)
        }
    }
    val vertexStride: Int = 3 * 4
    val vertexCount: Int = vertices.size / 3
}