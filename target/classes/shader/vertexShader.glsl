#version 330 core

in vec2 v_vertex;

uniform mat4 v_orthographicMatrix;
uniform mat4 v_viewMatrix;
uniform mat4 v_modelMatrix;

void main() {
    gl_Position = v_orthographicMatrix * v_viewMatrix * v_modelMatrix * vec4(v_vertex, 0, 1);
}
