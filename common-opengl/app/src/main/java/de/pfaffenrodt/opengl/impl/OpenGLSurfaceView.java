package de.pfaffenrodt.opengl.impl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by Dimitri on 18.08.16.
 */
public class OpenGLSurfaceView extends GLSurfaceView {
    private static final String TAG = "OpenGLSurfaceView";
    private final OpenGLRenderer mRenderer;
    private final GestureDetectorCompat mGestureDetectorCompat;

    public OpenGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new OpenGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        setWillNotDraw(true);
        mGestureDetectorCompat = new GestureDetectorCompat(context, gestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         mGestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    private final GestureDetector.OnGestureListener gestureListener
            = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mRenderer.scroll(distanceX);
            return true;
        }
    };
}
