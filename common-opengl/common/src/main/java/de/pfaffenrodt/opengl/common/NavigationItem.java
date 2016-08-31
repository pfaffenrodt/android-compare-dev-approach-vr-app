package de.pfaffenrodt.opengl.common;

import android.content.Context;

import de.pfaffenrodt.opengl.Plane;
import de.pfaffenrodt.opengl.SceneObject;
import de.pfaffenrodt.opengl.SphereRayCollider;
import de.pfaffenrodt.opengl.TextSceneObject;

/**
 * Created by Dimitri on 26.08.16.
 */
public class NavigationItem extends SceneObject implements Selectable{

    public static final float NAVIGATION_ITEM_POSITION_Z = 1.75f;
    public static final float NAVIGATION_ITEM_WIDTH = 1.5f;
    public static final float NAVIGATION_ITEM_HEIGHT = 0.75f;
    public static final float NAVIGATION_ITEM_RADIUS = 0.75f;
    private final int mResourceId;

    boolean mSelected;
    private final Plane mThumbnail;
    private final TextSceneObject mText;
    private final String mTextValue;

    public NavigationItem(Context context, int resourceId, String text) {
        mThumbnail = new Plane(context, NAVIGATION_ITEM_WIDTH, NAVIGATION_ITEM_HEIGHT);
        mResourceId = resourceId;
        mThumbnail.loadTexture(mResourceId);

        mText = new TextSceneObject(context, 512, 256);
        mTextValue = text;
        mText.setText(mTextValue);
        mText.getTransform().setPosition(0,-NAVIGATION_ITEM_HEIGHT,0);

        addChild(mThumbnail);
        addChild(mText);

        mRayCollider = new SphereRayCollider(getTransform(), NAVIGATION_ITEM_RADIUS);
    }

    @Override
    public void setSelected(boolean isSelected) {
        mSelected = isSelected;
        invalidSelection();
    }

    private void invalidSelection() {
        if(mSelected){
            getTransform().setScale(1.2f);
            mThumbnail.getShader().setColor(1f,1f,1f,1f);
            mText.getShader().setColor(1f,1f,1f,1f);
        }else{
            getTransform().setScale(1f);
            mThumbnail.getShader().setColor(1f,1f,1f,.5f);
            mText.getShader().setColor(1f,1f,1f,.5f);
        }
    }

    @Override
    public String toString() {
        return "NavigationItem{" +
                "mTextValue='" + mTextValue + '\'' +
                ", mResourceId=" + mResourceId +
                '}';
    }
}
