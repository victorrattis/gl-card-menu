package com.study.vhra.glcardmenu

import android.content.Context
import android.opengl.GLSurfaceView

class CardMenuSurfaceView(context: Context) : GLSurfaceView(context)  {
    init {
        setEGLContextClientVersion(2)
        setRenderer(GlRenderer(context))
    }
}