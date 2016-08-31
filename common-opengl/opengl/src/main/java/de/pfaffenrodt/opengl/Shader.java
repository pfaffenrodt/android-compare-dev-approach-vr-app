package de.pfaffenrodt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.IntDef;

import de.pfaffenrodt.opengl.utils.OpenGLUtils;
import de.pfaffenrodt.opengl.utils.ResourcesUtils;

/**
 * Created by Dimitri Pfaffenrodt on 18.08.2016.
 */
public class Shader {
    public static final String ATTRIBUTE_POSITION = "a_position";
    public static final String UNIFORM_COLOR = "u_color";
    public static final String UNIFORM_TEXTURE = "u_texture";
    public static final String ATTRIBUTE_TEXTURE_COORDINATE = "a_texture_coordinate";
    public static final String UNIFORM_MODEL_VIEW_PROJECTION_MATRIX = "u_model_view_projection_matrix";

    private final String vertexShaderCode;
    private final String fragmentShaderCode;

    private int vertexShaderId;
    private int fragmentShaderId;
    private int programId;
    private Context mContext;
    private int mColorHandle;
    private float[] mColor = {
            1f, 1f, 1f, .5f
    };

    @IntDef(value = {GLES20.GL_VERTEX_SHADER, GLES20.GL_FRAGMENT_SHADER})
    public @interface ShaderType {
    }

    public Shader(Context context) {
        this.mContext = context;
        vertexShaderCode = ResourcesUtils.loadStringFromRawResource(context, R.raw.vertex);
        fragmentShaderCode = ResourcesUtils.loadStringFromRawResource(context, R.raw.fragment);
        createOpenGLESProgram();
    }

    private void createOpenGLESProgram() {
        // Enable blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        vertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        // create empty OpenGL ES Program
        programId = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(programId, vertexShaderId);

        // add the fragment shader to program
        GLES20.glAttachShader(programId, fragmentShaderId);

        GLES20.glBindAttribLocation(programId, 0, ATTRIBUTE_POSITION);
        GLES20.glBindAttribLocation(programId, 1, ATTRIBUTE_TEXTURE_COORDINATE);
        // creates OpenGL ES program executables
        GLES20.glLinkProgram(programId);
    }

    public int loadShader(@ShaderType int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public String getVertexShaderCode() {
        return vertexShaderCode;
    }

    public String getFragmentShaderCode() {
        return fragmentShaderCode;
    }

    public int getVertexShaderId() {
        return vertexShaderId;
    }

    public int getFragmentShaderId() {
        return fragmentShaderId;
    }

    public int getProgramId() {
        return programId;
    }

    public int loadTexture(int textureResourceId) {
        Bitmap bitmap = ResourcesUtils.loadBitmap(mContext, textureResourceId);
        return loadTexture(bitmap);
    }

    /**
     * @param bitmap will be recycled at end after bitmap was forwarded to gpu ram
     * @return
     */
    public int loadTexture(Bitmap bitmap) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        }
        bitmap.recycle();//release bitmap from vm
        return textureHandle[0];
    }

    public void updateTexture(int textureHandle, Bitmap bitmap) {
        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();//release bitmap from vm
    }

    public void setColor(float red, float green, float blue, float alpha) {
        mColor = new float[]{red, green, blue, alpha};
    }

    public void updateColor() {
        mColorHandle = GLES20.glGetUniformLocation(programId, UNIFORM_COLOR);
        OpenGLUtils.checkGlError("glGetUniformLocation");
        GLES20.glUniform4fv(mColorHandle,1, mColor,0);
        OpenGLUtils.checkGlError("glUniformMatrix4fv");
    }
}
