package de.pfaffenrodt.opengl.impl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.pfaffenrodt.opengl.Ray;
import de.pfaffenrodt.opengl.Scene;
import de.pfaffenrodt.opengl.SceneObject;
import de.pfaffenrodt.opengl.common.MenuScene;
import de.pfaffenrodt.opengl.common.NavigationItem;
import de.pfaffenrodt.opengl.common.Selectable;

/**
 * Created by Dimitri on 18.08.16.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "OpenGLRenderer";
    public static final float CAMERA_Z = -3f;
    private final float[] mModelViewProjectionMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private Context mContext;

    private float mAngle = 0f;

    private Scene mScene;

    private boolean mSingleTab;
    private boolean mTouch;
    private int mWidth;
    private int mHeight;
    private float mTouchX;
    private float mTouchY;

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
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 1f, 0.0f);
        // Combine the rotation matrix with the projection and camera view
        // Note that the mModelViewProjectionMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mModelViewProjectionMatrix, 0, mRotationMatrix, 0);

        if(mTouch) {
            selectItemByCurrentCameraDirection(scratch);
        }
        if(mScene != null) {
            mScene.draw(scratch);
        }
    }


    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        mWidth = width;
        mHeight = height;

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
                5//far
                );
    }

    public void scroll(float distanceX){
        mAngle = (mAngle + (distanceX * .25f)) % 360;
    }

    public boolean onSingleTapConfirmed(MotionEvent e){
        Log.d(TAG, "onSingleTapConfirmed() called with: " + "e = [" + e + "]");
        mSingleTab = true;
        return true;
    }

    public boolean onTouch(MotionEvent e){
        mTouchX = e.getX();
        mTouchY = e.getY();
        switch (e.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mTouch = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouch = false;
                break;
        }
        return true;
    }

    private void selectItemByCurrentCameraDirection(float[] modelViewProjectionMatrix) {
        /**
         * center ray origin
         */
        mTouchX = mWidth / 2f;
        mTouchY = mHeight / 2f;

        Ray ray = Ray.create(mTouchX, mTouchY, mWidth, mHeight, modelViewProjectionMatrix);

        clearSelectedState();
        selectItem(ray);
    }

    private void clearSelectedState() {
        List<SceneObject> sceneObjects = mScene.getSceneObjects();
        for (int i = 0; i < sceneObjects.size(); i++) {
            SceneObject sceneObject = sceneObjects.get(i);
            if(sceneObject instanceof Selectable) {
                ((Selectable) sceneObject).setSelected(false);
            }
        }
    }

    private void selectItem(Ray ray) {
        List<SceneObject> intersectionSceneObjects = mScene.getIntersectionSceneObjects(ray);
        for (int i = 0; i < intersectionSceneObjects.size(); i++) {
            SceneObject sceneObject = intersectionSceneObjects.get(i);
            if(sceneObject instanceof Selectable){
                ((Selectable) sceneObject).setSelected(true);
                if(mSingleTab){
                    mSingleTab = false;
                    onSelectItem(sceneObject);
                }
            }
        }
    }

    private void onSelectItem(final SceneObject sceneObject) {
        if(sceneObject instanceof NavigationItem){
            Log.i(TAG, "onSelectItem: navigationItem:"+sceneObject.toString());
        }
    }
}
