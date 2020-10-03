package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var positionHandle: Int = 0
    private lateinit var card: CardModel

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        val shaderLoader = ShaderLoader(context)

        card = CardModel()
        val shader = Shader(
            vertex = R.raw.vertex,
            fragment = R.raw.fragment
        )
        shaderLoader.load(shader)

        GLES20.glUseProgram(shader.id)

        positionHandle = GLES20.glGetAttribLocation(shader.id, "vPosition").also {
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glVertexAttribPointer(
            positionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            card.vertexStride,
            card.vertexBuffer
        )

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, card.vertexCount)
    }
}