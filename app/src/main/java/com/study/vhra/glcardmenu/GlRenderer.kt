package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.study.vhra.glcardmenu.utils.TextureLoader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlRenderer(private val context: Context) : GLSurfaceView.Renderer {
    companion object {
        const val CARD_WIDTH = 0.64f
        const val CARD_HEIGHT = 1f
    }

    private lateinit var textureLoader: TextureLoader

    private val models: MutableList<CardModel> = mutableListOf()

    private var projectionMatrix = FloatArray(16)

    private var screenSize: IntArray = intArrayOf()

    private val shader = Shader(
        vertex = R.raw.vertex,
        fragment = R.raw.fragment
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.86f, 0.86f, 0.86f, 1.0f)

        textureLoader = TextureLoader(context)

        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)

        // Enable z depth
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LESS)
        GLES20.glDepthMask(true)

        val shaderLoader = ShaderLoader(context)
        shaderLoader.load(shader)
        shader.dump()

        val textureSize = floatArrayOf(2015f, 1948f)

        models.add(CardModel("card 0").apply {
            texture = R.drawable.card_textures
            setCardArea(CARD_WIDTH, CARD_HEIGHT)
            setFrontCoordinateText(811f/textureSize[0], 1398f/textureSize[1], 1003f/textureSize[0], 1667f/textureSize[1])
            setBackCoordinateText(1213f/textureSize[0], 1f/textureSize[0], 1408f/textureSize[0], 272f/textureSize[1])
            var angle = 0f
            setOnUpdate { matrix ->
                angle += 1
                if (angle > 360f) angle = 0f
                Matrix.rotateM(matrix, 0, angle, 0f, 1f, 0f)
            }
        })

        models.add(CardModel("card 1").apply {
            texture = R.drawable.card_textures
            setCardArea(CARD_WIDTH, CARD_HEIGHT)
            setFrontCoordinateText(205f/textureSize[0], 3f/textureSize[1], 397f/textureSize[0], 272f/textureSize[1])
            setBackCoordinateText(1213f/textureSize[0], 1f/textureSize[1], 1408f/textureSize[0], 272f/textureSize[1])
            setOnUpdate { matrix ->
                Matrix.translateM(matrix, 0, 0f, 1f + 0.02f, 0f)
            }
        })

        models.add(CardModel("card 2").apply {
            texture = R.drawable.card_textures
            setCardArea(CARD_WIDTH, CARD_HEIGHT)
            setFrontCoordinateText(811f/textureSize[0], 1398f/textureSize[1], 1003f/textureSize[0], 1667f/textureSize[1])
            setBackCoordinateText(1213f/textureSize[0], 1f/textureSize[1], 1408f/textureSize[0], 272f/textureSize[1])
            var angle = 0f
            setOnUpdate { matrix ->
                angle += 1
                if (angle > 360f) angle = 0f
                Matrix.translateM(matrix, 0, CARD_WIDTH + 0.02f, 1f + 0.02f, 0f)
                Matrix.rotateM(matrix, 0, angle, 0f, -1f, 0f)
            }
        })

        models.forEach { it.load(textureLoader) }

        shader.useProgram()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        screenSize = intArrayOf(width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)

        val camera = Camera3D(
            eye = floatArrayOf(0f, 0f, -6f),
            center = floatArrayOf(0f, 0f, 0f),
            up = floatArrayOf(0f, 1f, 0f)
        )

        Matrix.multiplyMM(projectionMatrix, 0, projectionMatrix, 0, camera.getMatrix(), 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        models.forEach { it.update(projectionMatrix) }

        models.forEach { it.draw(shader) }
    }

    fun onTouchEvent(x: Float, y: Float) {
        // convert touch x and y to OpenGL coordinate system
        val glPosition = convertGlCoordinateSystem(x, y)
        Log.d("devlog", "Touch GL position: x= ${glPosition[0]}, y= ${glPosition[1]}")

        models.forEach { it.measure(glPosition, projectionMatrix) }
    }

    private fun convertGlCoordinateSystem(x: Float, y: Float): FloatArray {
        return floatArrayOf(
            (x / screenSize[0]) * 2f - 1f,
            1 - (y / screenSize[1]) * 2f
        )
    }
}