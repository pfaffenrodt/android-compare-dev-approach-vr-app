package de.pfaffenrodt.opengl.common;

import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

import de.pfaffenrodt.opengl.Ray;
import de.pfaffenrodt.opengl.Scene;
import de.pfaffenrodt.opengl.SceneObject;

/**
 * Created by Dimitri on 31.08.16.
 */
public class Controller {
    private static final String TAG = "Controller";

    private boolean mSingleTab;
    private boolean mTouch;
    private int mWidth;
    private int mHeight;
    private float mTouchX;
    private float mTouchY;
    private Scene mScene;
    private boolean mUpdateOnlyOnTouch;

    public Controller(Scene scene) {
        mScene = scene;
        clearSelectedState();
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void draw(float[] modelViewProjectionMatrix){
        if(mTouch || !mUpdateOnlyOnTouch) {
            selectItemByCurrentCameraDirection(modelViewProjectionMatrix);
        }
    }

    public boolean isUpdateOnlyOnTouch() {
        return mUpdateOnlyOnTouch;
    }

    public void setUpdateOnlyOnTouch(boolean updateOnlyOnTouch) {
        mUpdateOnlyOnTouch = updateOnlyOnTouch;
    }

    public boolean onTouch(MotionEvent e){
        mTouchX = e.getX();
        mTouchY = e.getY();
        switch (e.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mTouch = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouch = false;
                break;
        }
        return mTouch;
    }

    public boolean onSingleTapConfirmed(MotionEvent e){
        Log.d(TAG, "onSingleTapConfirmed() called with: " + "e = [" + e + "]");
        mSingleTab = true;
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
