
attribute vec4 aPosition;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;

uniform mat4 mvp;

void main() {
    gl_Position = mvp * vec4(aPosition.xy, 0, 1);

    vTexCoord = aTexCoord;
}
