package de.pfaffenrodt.cardboard;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import de.pfaffenrodt.opengl.common.Controller;
import de.pfaffenrodt.opengl.common.MenuScene;

/**
 * Created by Dimitri on 01.09.16.
 */
public class CardboardRenderer implements GvrView.StereoRenderer {

    public static final float CAMERA_Z = -.1f;

    private Controller mController;
    private Context mContext;

    public CardboardRenderer(Context context) {
        mContext = context;
    }

    public Controller getController() {
        return mController;
    }

    public void setController(Controller controller) {
        mController = controller;
    }


    private MenuScene mScene;


    private final float[] mModelViewProjectionMatrix = new float[16];
    private final float[] mHeadView = new float[16];
    private final float[] mView = new float[16];
    private final float[] mCamera = new float[16];
    /**
     * Prepares OpenGL ES before we draw a frame.
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {

        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(
                mCamera, 0,
                0.0f, 0.0f, CAMERA_Z,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f);
        headTransform.getHeadView(mHeadView, 0);
        mController.draw(mHeadView);
    }

    @Override
    public void onDrawEye(Eye eye) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);
        float[] projectionMatrix = eye.getPerspective(.1f,10);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, projectionMatrix, 0, mView, 0);

        if(mScene != null){
            mScene.draw(mModelViewProjectionMatrix);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    /**
     * @param width New width of the surface for a single eye in pixels.
     * @param height New height of the surface for a single eye in pixels.
     */
    @Override
    public void onSurfaceChanged( int width, int height) {
        /**
         * update controller.
         * controller will calculate center and use it to create an ray for picking
         */
        mController.setWidth(width);
        mController.setHeight(height);
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mScene = new MenuScene(mContext);
        mController = new Controller(mScene);
    }

    @Override
    public void onRendererShutdown() {

    }
}
