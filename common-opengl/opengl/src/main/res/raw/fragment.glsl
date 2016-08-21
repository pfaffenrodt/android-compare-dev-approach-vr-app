//FRAGMENT SHADER
precision mediump float;
uniform sampler2D u_texture;
varying vec2 v_texture_coordinate;
void main() {
  // Multiply the color by the diffuse illumination level and texture value to get final output color.
  gl_FragColor =  texture2D(u_texture, v_texture_coordinate);
}