package de.pfaffenrodt.gearvrf.menu;

import android.support.annotation.NonNull;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import de.pfaffenrodt.gearvrf.R;

/**
 * Created by Dimitri Pfaffenrodt on 08.08.2016.
 */
public class MainMenu extends GVRSceneObject {
    public MainMenu(GVRContext gvrContext) {
        super(gvrContext);
        List<GVRSceneObject> menuItems = createItems(gvrContext);

        positionAndAddItems(menuItems);
    }

    private void positionAndAddItems(List<GVRSceneObject> menuItems) {
        for (int i = 0, size = menuItems.size(); i < size; ++i) {
            GVRSceneObject gvrSceneObject = menuItems.get(i);
            gvrSceneObject.getTransform().setPosition(0.0f, 0.0f, -0.75f);
            float degree = 360.0f * i / (size + 1);
            gvrSceneObject.getTransform().rotateByAxisWithPivot(degree, 0.0f, 1.0f,
                    0.0f, 0.0f, 0.0f, 0.0f);
            addChildObject(gvrSceneObject);
        }
    }

    @NonNull
    private List<GVRSceneObject> createItems(GVRContext gvrContext) {
        List<GVRSceneObject> menuItems = new ArrayList<GVRSceneObject>();
        {
            Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.photosphere_thumb));
            Thumbnail photo = new Thumbnail(gvrContext, texture, "photo");
            menuItems.add(photo);
        }
        {
            Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.videos_s_3_thumb));
            Thumbnail video = new Thumbnail(gvrContext, texture, "video");
            menuItems.add(video);
        }
        {
            Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.photosphere_thumb));
            Thumbnail photo = new Thumbnail(gvrContext, texture, "photo");
            menuItems.add(photo);
        }
        {
            Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.videos_s_3_thumb));
            Thumbnail video = new Thumbnail(gvrContext, texture, "video");
            menuItems.add(video);
        }
        return menuItems;
    }
}
