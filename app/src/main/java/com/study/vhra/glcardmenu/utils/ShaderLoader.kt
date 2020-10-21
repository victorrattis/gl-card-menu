package com.study.vhra.glcardmenu.utils

import android.content.Context
import android.opengl.GLES20.*
import com.study.vhra.glcardmenu.Shader
import java.io.BufferedReader
import java.io.InputStreamReader

class ShaderLoader(private val context: Context) {
    fun load(shader: Shader) {
        try {
            shader.id = createShaderProgram(
                vertexShader = getRawSourceCode(context, shader.vertex),
                fragmentShader = getRawSourceCode(context, shader.fragment)
            )
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    private fun createShaderProgram(vertexShader: String, fragmentShader: String) =
        glCreateProgram().also { programId ->
            glAttachShader(programId, loadShader(GL_VERTEX_SHADER, vertexShader))
            glAttachShader(programId, loadShader(GL_FRAGMENT_SHADER, fragmentShader))
            glLinkProgram(programId)
            if (hasLinkStatusError(programId)) {
                glGetProgramInfoLog(programId).also { message ->
                    glDeleteProgram(programId)
                    throwRuntimeException("Could not link program: $message")
                }
            }
        }

    private fun loadShader(type: Int, sourceCode: String): Int =
        glCreateShader(type).also { shaderId ->
            glShaderSource(shaderId, sourceCode)
            glCompileShader(shaderId)
            if (hasCompileErrorStatus(shaderId)) {
                glGetShaderInfoLog(shaderId).also { message ->
                    glDeleteShader(shaderId)
                    throwRuntimeException("Could not compile shader: $message")
                }
            }
        }

    private fun hasLinkStatusError(programId: Int): Boolean {
        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0)
        return linkStatus[0] != GL_TRUE
    }

    private fun hasCompileErrorStatus(shaderId: Int): Boolean {
        val compiled = IntArray(1)
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compiled, 0)
        return compiled[0] == 0
    }

    private fun throwRuntimeException(message: String) {
        throw RuntimeException(message)
    }

    private fun getRawSourceCode(context: Context, rawId: Int): String {
        var result = ""
        try {
            val inputStream = context.resources.openRawResource(rawId)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var receiveString: String? = ""
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }
            inputStream.close()
            result = stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}