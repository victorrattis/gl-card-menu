
attribute vec4 aPosition;

uniform mat4 mvp;

void main() {
    gl_Position = mvp * aPosition;
}
