package com.study.vhra.glcardmenu

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import com.study.vhra.glcardmenu.utils.BufferUtils
import java.nio.FloatBuffer
import java.nio.ShortBuffer


class CardModel {
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
        0.5f,  0.5f
    )


    private lateinit var indexBuffer: ShortBuffer
    private val indices = shortArrayOf(
        0, 1, 2,
        0, 2, 3
    )

    private var mvp: FloatArray = FloatArray(16)

    private val numberOfVertices: Int = vertices.size / COORDINATE_FOR_VERTEX

    private fun getVertexCapacity(): Int = vertices.size * COORDINATE_BYTES

    var position: FloatArray = FloatArray(3)

    fun load(context: Context) {
        vertexBuffer = BufferUtils.createFloatBuffer(vertices, getVertexCapacity())
        indexBuffer = BufferUtils.createShortBuffer(indices, indices.size * 2)

        Matrix.setIdentityM(mvp, 0);
        Matrix.translateM(mvp, 0, position[0], position[1], position[2])

        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        val options = BitmapFactory.Options()
        options.inScaled = false // No pre-scaling

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.texture, options)
    }

    fun draw(shader: Shader) {
        GLES20.glUniformMatrix4fv(
            shader.getUniform("mvp"),
            1,
            false,
            mvp,
            0
        )

        GLES20.glVertexAttribPointer(
            shader.getAttribute("aPosition"),
            COORDINATE_FOR_VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX_BYTES,
            vertexBuffer
        )

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indices.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )
    }
}