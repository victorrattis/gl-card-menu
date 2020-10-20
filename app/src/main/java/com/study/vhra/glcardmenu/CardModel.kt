package com.study.vhra.glcardmenu

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.study.vhra.glcardmenu.utils.BufferUtils
import com.study.vhra.glcardmenu.utils.TextureLoader
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.max
import kotlin.math.min

class CardModel constructor(private val name: String = "default") {
    companion object {
        const val COORDINATE_FOR_VERTEX = 2
        private const val COORDINATE_BYTES = 4
        const val VERTEX_BYTES = COORDINATE_FOR_VERTEX * COORDINATE_BYTES
    }

    private lateinit var vertexBuffer: FloatBuffer
    private var vertices = floatArrayOf(
        -0.5f,  0.5f,
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f,  0.5f,
        -0.5f,  0.5f,
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f,  0.5f
    )

    private lateinit var textCoordBuffer: FloatBuffer
    private var textCoordinates = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        0.5f, 1.0f,
        0.5f, 0.0f,
        1.0f, 0.0f,
        1.0f, 1.0f,
        0.5f, 1.0f,
        0.5f, 0.0f
    )

    private lateinit var indexBuffer: ShortBuffer
    private val indices = shortArrayOf(
        0, 1, 2,
        0, 2, 3,
        4, 6, 5,
        4, 7, 6
    )

    private var mvp: FloatArray = FloatArray(16)

    private val numberOfVertices: Int = vertices.size / COORDINATE_FOR_VERTEX

    private fun getVertexCapacity(): Int = vertices.size * COORDINATE_BYTES

    var position: FloatArray = FloatArray(3)
    var texture: Int = 0

    private var frontCoordText = floatArrayOf(0f, 0f, 0.5f, 1f)
    private var backCoordText = floatArrayOf(0.5f, 0f, 1f, 1f)

    var mTextureDataHandle: Int = 0

    private var area = floatArrayOf()

    fun setCardArea(width: Float, height: Float) {
        val halfWidth = width / 2.0f
        val halfHeight = height / 2.0f
        val startX = -halfWidth
        val startY = -halfHeight

        area = floatArrayOf(startX, startY, startX + width, startY + height)

        vertices = floatArrayOf(
            startX,  startY + height,
            startX, startY,
            startX + width, startY,
            startX + width,  startY + height,

            startX,  startY + height,
            startX, startY,
            startX + width, startY,
            startX + width,  startY + height
        )
    }

    fun setFrontCoordinateText(startX: Float, startY: Float, endX: Float, endY: Float) {
        frontCoordText = floatArrayOf(startX, startY, endX, endY)
    }

    fun setBackCoordinateText(startX: Float, startY: Float, endX: Float, endY: Float) {
        backCoordText = floatArrayOf(startX, startY, endX, endY)
    }

    private fun loadCoordinateTexture() {
        textCoordinates = floatArrayOf(
            // back
            backCoordText[2], backCoordText[1],
            backCoordText[2], backCoordText[3],
            backCoordText[0], backCoordText[3],
            backCoordText[0], backCoordText[1],

            // front
            frontCoordText[2], frontCoordText[1],
            frontCoordText[2], frontCoordText[3],
            frontCoordText[0], frontCoordText[3],
            frontCoordText[0], frontCoordText[1]
        )
    }

    fun load(textureLoader: TextureLoader) {
        loadCoordinateTexture()

        vertexBuffer = BufferUtils.createFloatBuffer(vertices, getVertexCapacity())
        indexBuffer = BufferUtils.createShortBuffer(indices, indices.size * 2)
        textCoordBuffer = BufferUtils.createFloatBuffer(textCoordinates, textCoordinates.size * 4)

        mTextureDataHandle = textureLoader.loadTexture(texture)
    }

    var onUpdateCallback: (matrix: FloatArray) -> Unit = {}
    fun setOnUpdate(callback: (matrix: FloatArray) -> Unit) {
        onUpdateCallback = callback
    }

    fun update(projection: FloatArray) {
        Matrix.setIdentityM(mvp, 0)

        if (selected) {
            Matrix.translateM(mvp, 0, 0f, 0f, -1f)
            onUpdateCallback(mvp)
            Matrix.scaleM(mvp, 0, 1.25f, 1.25f, 1f)
        } else {

            onUpdateCallback(mvp)
        }
        Matrix.multiplyMM(mvp, 0, projection, 0, mvp, 0)
    }

    fun draw(shader: Shader) {
        GLES20.glUniformMatrix4fv(
            shader.getUniform("mvp"),
            1,
            false,
            mvp,
            0
        )

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        GLES20.glUniform1i(shader.getUniform("uTexture"), 0)

        GLES20.glVertexAttribPointer(
            shader.getAttribute("aPosition"),
            COORDINATE_FOR_VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX_BYTES,
            vertexBuffer
        )

        GLES20.glVertexAttribPointer(
            shader.getAttribute("aTexCoord"),
            2,
            GLES20.GL_FLOAT,
            false,
            2 * 4,
            textCoordBuffer
        )

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indices.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )
    }

    var selected: Boolean = false

    fun measure(position: FloatArray, projection: FloatArray) {
        val start = floatArrayOf(area[0], area[1], 0f, 1f)
        val end = floatArrayOf(area[2], area[3], 0f, 1f)

//        update(projection)

        Matrix.multiplyMV(start, 0, mvp, 0, start, 0)
        Matrix.multiplyMV(end, 0, mvp, 0, end, 0)
        val endArea = floatArrayOf(
            start[0]/start[3],
            start[1]/start[3],
            end[0]/end[3],
            end[1]/end[3]
        )

        if (contain(min(endArea[0], endArea[2]), max(endArea[0], endArea[2]), position[0]) &&
            contain(min(endArea[1], endArea[3]), max(endArea[1], endArea[3]), position[1])) {
            Log.d("devlog", "collision: $name")
            selected = !selected
        }
    }

    private fun contain(start: Float, end: Float, value: Float): Boolean = value in start..end
}