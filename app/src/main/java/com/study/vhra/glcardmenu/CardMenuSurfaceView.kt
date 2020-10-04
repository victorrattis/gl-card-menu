package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class CardMenuSurfaceView(context: Context) : GLSurfaceView(context)  {
    private val renderer: GlRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GlRenderer(context)
        setRenderer(renderer)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            renderer.onTouchEvent(it.x, it.y)
        }
        return super.onTouchEvent(event)
    }
}