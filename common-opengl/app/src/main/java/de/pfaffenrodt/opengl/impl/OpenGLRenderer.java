package de.pfaffenrodt.opengl.impl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.pfaffenrodt.opengl.Scene;

/**
 * Created by Dimitri on 18.08.16.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {

    public static final float CAMERA_Z = -3f;
    private final float[] mModelViewProjectionMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private Context mContext;

    private float mAngle = 0f;

    private Scene mScene;

    public OpenGLRenderer(Context context) {
        this.mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mScene = new MenuScene(mContext);
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        float[] scratch = new float[16];
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
                0, 0, CAMERA_Z,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // Create a rotation for the triangle
        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
//         long time = SystemClock.uptimeMillis() % 8000L;
//         float angle = 0.045f * ((int) time);
//        float scale = (time + 0.001f) / 8000L;
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 1f, 0.0f);
        // Combine the rotation matrix with the projection and camera view
        // Note that the mModelViewProjectionMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mModelViewProjectionMatrix, 0, mRotationMatrix, 0);

        if(mScene != null) {
            mScene.draw(scratch);
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(
                mProjectionMatrix,
                0, //offset
                -ratio,//left
                ratio, //right
                -1, //top
                1, //bottom
                3f,//near,
                10//far
                );
    }

    public void scroll(float distanceX){
        mAngle = (mAngle+distanceX) % 360;
    }
}
