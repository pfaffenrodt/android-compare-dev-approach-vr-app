package de.pfaffenrodt.cardboard;

import android.content.res.Configuration;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import de.pfaffenrodt.opengl.common.Controller;
import de.pfaffenrodt.opengl.common.MenuScene;

public class MainActivity extends GvrActivity{

    public static final float CAMERA_Z = -.1f;
    public static final float ROOM_SCALE = 3f;

    private Controller mController;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeGvrView();
    }

    private void initializeGvrView() {
        GvrView gvrView = (GvrView) findViewById(R.id.gvr_view);
        // Associate a GvrView.StereoRenderer with gvrView.
        gvrView.setRenderer(stereoRenderer);
        gvrView.setTransitionViewEnabled(true);
        gvrView.setOnCardboardBackButtonListener(
                new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
        // Associate the gvrView with this activity.
        setGvrView(gvrView);
    }

    private final GvrView.StereoRenderer stereoRenderer = new GvrView.StereoRenderer() {

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
            mController.draw(mModelViewProjectionMatrix);
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
            mScene = new MenuScene(MainActivity.this);
            mController = new Controller(mScene);
        }

        @Override
        public void onRendererShutdown() {

        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getGvrView().setSystemUiVisibility(getImmersiveStickyFlags());
//        }
    }

    private int getImmersiveStickyFlags(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mController != null){
           return mController.onTouch(event);
        }
        return false;
    }
}
