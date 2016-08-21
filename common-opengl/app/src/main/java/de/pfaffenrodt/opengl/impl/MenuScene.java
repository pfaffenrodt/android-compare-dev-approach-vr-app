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

    public static final float NAVIGATION_ITEM_POSITION_Z = 1.75f;
    public static final float NAVIGATION_ITEM_WIDTH = 1.5f;
    public static final float NAVIGATION_ITEM_HEIGHT = 0.75f;

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
        SceneObject groupSceneObject = new SceneObject();

        Plane plane = new Plane(context, NAVIGATION_ITEM_WIDTH, NAVIGATION_ITEM_HEIGHT);
        plane.loadTexture(resourceId);

        TextSceneObject textObject = new TextSceneObject(context, 512, 256);
        textObject.setText(text);
        textObject.getTransform().setPosition(0,-NAVIGATION_ITEM_HEIGHT,0);

        groupSceneObject.addChild(plane);
        groupSceneObject.addChild(textObject);

        addSceneObject(groupSceneObject);
    }

    private void positionMenu(){
        List<SceneObject> sceneObjects = getSceneObjects();
        int size = sceneObjects.size();
        for (int i = 0; i < size; i++) {
            Transform transform = sceneObjects.get(i).getTransform();
            transform.setPosition(0f,0f, NAVIGATION_ITEM_POSITION_Z);
            float degree = -360.0f * i / size ;
            transform.rotateByAxisWithWorldPivot(degree, 0.0f, 1.0f, 0.0f);
        }
    }

}
