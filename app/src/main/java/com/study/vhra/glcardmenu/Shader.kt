package com.study.vhra.glcardmenu

data class Shader(
    var vertex: Int,
    var fragment: Int,
    var id: Int = 0
) {
    fun isSuccess() = id > 0
}