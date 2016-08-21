package de.pfaffenrodt.opengl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitri Pfaffenrodt on 19.08.2016.
 */
public class Scene implements Renderable{

    private List<SceneObject> mSceneObjects;

    public Scene(Context context) {
    }

    public List<SceneObject> getSceneObjects() {
        return mSceneObjects;
    }

    public void setSceneObjects(List<SceneObject> sceneObjects) {
        mSceneObjects = sceneObjects;
    }

    public void addSceneObject(SceneObject sceneObject){
        if(sceneObject == null){
            return;
        }
        if(mSceneObjects == null){
            mSceneObjects = new ArrayList<>();
        }
        mSceneObjects.add(sceneObject);
    }

    @Override
    public void draw(float[] modelViewProjectionMatrix) {
        if(mSceneObjects != null){
            for (int i = 0; i < mSceneObjects.size(); i++) {
                SceneObject sceneObject = mSceneObjects.get(i);
                if(sceneObject != null){
                    sceneObject.draw(modelViewProjectionMatrix);
                }
            }
        }
    }
}
