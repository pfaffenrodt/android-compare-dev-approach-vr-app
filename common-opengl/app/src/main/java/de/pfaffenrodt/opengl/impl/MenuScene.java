package de.pfaffenrodt.opengl.impl;

import android.content.Context;

import java.util.List;

import de.pfaffenrodt.opengl.Plane;
import de.pfaffenrodt.opengl.Scene;
import de.pfaffenrodt.opengl.SceneObject;
import de.pfaffenrodt.opengl.TextSceneObject;
import de.pfaffenrodt.opengl.Transform;

/**
 * Created by Dimitri on 19.08.16.
 */
public class MenuScene extends Scene {

    public MenuScene(Context context) {
        super(context);

        addNavigationItem(context, R.raw.photosphere_thumb, "test1");
        addNavigationItem(context, R.raw.videos_s_3_thumb, "test2");
        addNavigationItem(context, R.raw.photosphere_thumb, "test3");
        addNavigationItem(context, R.raw.videos_s_3_thumb, "test4");
        addNavigationItem(context, R.raw.photosphere_thumb, "test5");
        addNavigationItem(context, R.raw.videos_s_3_thumb, "test6");

        positionMenu();
    }

    private void addNavigationItem(Context context, int resourceId, String text) {
        addSceneObject(new NavigationItem(context, resourceId, text));
    }

    private void positionMenu(){
        List<SceneObject> sceneObjects = getSceneObjects();
        int size = sceneObjects.size();
        for (int i = 0; i < size; i++) {
            Transform transform = sceneObjects.get(i).getTransform();
            transform.setPosition(0f,0f, NavigationItem.NAVIGATION_ITEM_POSITION_Z);
            float degree = -360.0f * i / size ;
            transform.rotateByAxisWithWorldPivot(degree, 0.0f, 1.0f, 0.0f);
        }
    }

}
