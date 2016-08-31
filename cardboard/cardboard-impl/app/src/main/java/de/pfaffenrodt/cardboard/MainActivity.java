package de.pfaffenrodt.cardboard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class MainActivity extends GvrActivity{

    private CardboardRenderer mCardboardRenderer;

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
        mCardboardRenderer = new CardboardRenderer(this);
        gvrView.setRenderer(mCardboardRenderer);
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
        if(mCardboardRenderer.getController() != null){
           return mCardboardRenderer.getController().onTouch(event);
        }
        return false;
    }
}
