//VERTEX SHADER
// This matrix member variable provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
uniform mat4 u_model_view_projection_matrix;

attribute vec4 a_position;//position of vertex
attribute vec2 a_texture_coordinate;//uv map
attribute vec4 a_color;//color multiply to texture

varying vec2 v_texture_coordinate;//uv map forward to fragment shader
void main() {
// The matrix must be included as a modifier of gl_Position.
// Note that the uMVPMatrix factor *must be first* in order
// for the matrix multiplication product to be correct.
  gl_Position = u_model_view_projection_matrix * a_position;
  v_texture_coordinate = a_texture_coordinate;
}