package com.study.vhra.glcardmenu.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

object BufferUtils {
    fun createFloatBuffer(data: FloatArray, capacity: Int): FloatBuffer =
        ByteBuffer.allocateDirect(capacity).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(data)
                position(0)
            }
        }

    fun createShortBuffer(data: ShortArray, capacity: Int): ShortBuffer =
        ByteBuffer.allocateDirect(capacity).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(data)
                    position(0)
                }
            }
}