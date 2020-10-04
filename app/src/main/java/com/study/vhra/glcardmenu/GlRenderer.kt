package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var card: CardModel
    private lateinit var card2: CardModel

    private val shader = Shader(
        vertex = R.raw.vertex,
        fragment = R.raw.fragment
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        val shaderLoader = ShaderLoader(context)
        shaderLoader.load(shader)

        card = CardModel()
        card.position[0] = -0.6f
        card.texture = R.drawable.texture
        card.load(context)

        card2 = CardModel()
        card2.position[0] = 0.5f
        card2.texture = R.drawable.texture2
        card2.load(context)

        shader.useProgram()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        card.draw(shader)
        card2.draw(shader)
    }
}