package com.study.vhra.glcardmenu.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils

object TextureUtils {
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        if (textureHandle[0] != 0) {
            val bitmap = BitmapFactory.decodeResource(
                context.resources,
                resourceId,
                BitmapFactory.Options().also { it.inScaled = false }
            )
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST
            )

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap.recycle()
        }
        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }
        return textureHandle[0]
    }
}