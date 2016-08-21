package de.pfaffenrodt.gearvrf;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.gearvrf.GVRActivity;

/**
 * Created by Dimitri Pfaffenrodt on 07.08.2016.
 *
 * GVRActivity implementation add {@link VRMain} as GVRMain
 */
public class VRActivity extends GVRActivity {

    private VRMain gvrMain;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this,gestureListener);
        gvrMain = new VRMain();
        setMain(gvrMain,"gvr.xml");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gestureDetector = null;
        gvrMain = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector != null){
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private final GestureDetector.OnGestureListener gestureListener
            = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if(gvrMain!= null){
                gvrMain.onSingleTapUp(event);
            }
            return false;
        }
    };
}
