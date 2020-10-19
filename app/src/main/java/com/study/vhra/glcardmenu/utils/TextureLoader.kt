package com.study.vhra.glcardmenu.utils

import android.content.Context
import android.opengl.GLES20
import android.util.Log

class TextureLoader (
    private val context: Context
) {
    private val loadedTextures = mutableMapOf<Int, Int>()

    fun loadTexture(resourceId: Int): Int = if (loadedTextures.contains(resourceId)) {
        loadedTextures[resourceId]!!
    } else {
        val textureHandle = TextureUtils.loadTexture(context, resourceId)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        loadedTextures[resourceId] = textureHandle
        textureHandle
    }
}