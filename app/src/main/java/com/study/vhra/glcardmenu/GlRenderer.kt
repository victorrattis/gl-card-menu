package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var card: CardModel
    private lateinit var card2: CardModel

    private val models: MutableList<CardModel> = mutableListOf()

    private var projectionMatrix = FloatArray(16)

    private val shader = Shader(
        vertex = R.raw.vertex,
        fragment = R.raw.fragment
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)

        val shaderLoader = ShaderLoader(context)
        shaderLoader.load(shader)
        shader.dump()

        card = CardModel()
        card.position[0] = -1f
        card.texture = R.drawable.card_sprints
        card.setCardArea(1f, 1f)
        //card.setTexCoordArea(0f,0f, 0.5f, 1f)
        card.load(context)
        models.add(card)

        card2 = CardModel()
        card2.position[0] = 1f
        card2.texture = R.drawable.card_sprints
        card2.setCardArea(2f, 2f)
        card2.setTexCoordArea(0.5f,0f, 0.5f, 1f)
        card2.load(context)

        shader.useProgram()

        var angle = 0f
        card.setOnUpdate { matrix ->
            angle += 1
            if (angle > 360f) angle = 0f
            Matrix.rotateM(matrix, 0, angle, 0f, 1f, 0f)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

        val viewMatrix = FloatArray(16)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -6f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        Matrix.multiplyMM(projectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        models.forEach { it.update(projectionMatrix) }

        models.forEach { it.draw(shader) }
    }
}