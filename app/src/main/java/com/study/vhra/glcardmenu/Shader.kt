package com.study.vhra.glcardmenu

import android.opengl.GLES10
import android.opengl.GLES20
import android.util.Log
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10


class Shader(
    val vertex: Int,
    val fragment: Int,
    var id: Int = 0
) {
    private val attributes: HashMap<String, Int> = hashMapOf()
    private val uniforms: HashMap<String, Int> = hashMapOf()

    fun isSuccess() = id > 0

    fun useProgram() {
        GLES20.glUseProgram(id)
    }

    fun getAttribute(attributeName: String): Int {
        return attributes[attributeName] ?: addAttribute(attributeName)
    }

    private fun addAttribute(attributeName: String): Int =
        GLES20.glGetAttribLocation(id, attributeName).also {
            GLES20.glEnableVertexAttribArray(it)
        }.also { attributes[attributeName] = it }

    fun getUniform(uniformName: String): Int {
        return GLES20.glGetUniformLocation(id, uniformName)
    }

    fun dump() {
        Log.d("devlog", "MAX Texture size: ${getMaxTextureSize()}")
        Log.d("devlog", "getActiveAttributes: ${getActiveAttributes()}")
        Log.d("devlog", "getActiveUniforms: ${getActiveUniforms()}")
    }

    private fun getActiveAttributes(): Int {
        val result = IntBuffer.allocate(1)
        GLES20.glGetProgramiv(id, GLES20.GL_ACTIVE_ATTRIBUTES, result)
        return result.get(0)
    }

    private fun getActiveUniforms(): Int {
        val result = IntBuffer.allocate(1)
        GLES20.glGetProgramiv(id, GLES20.GL_ACTIVE_UNIFORMS, result)
        return result.get(0)
    }

    private fun getMaxTextureSize(): Int {
        val maxTextureSize = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0)
        return maxTextureSize[0]
    }
}