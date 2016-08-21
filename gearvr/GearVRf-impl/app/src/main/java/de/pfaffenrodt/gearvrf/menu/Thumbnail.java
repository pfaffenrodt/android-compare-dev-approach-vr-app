package de.pfaffenrodt.gearvrf.menu;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRTexture;

import java.util.concurrent.Future;

/**
 * Created by Dimitri Pfaffenrodt on 08.08.2016.
 */
public class Thumbnail extends Button {
    private String id;
    protected static final float width = 0.8f;
    protected static final float height = 0.4f;

    public Thumbnail(GVRContext gvrContext, GVRTexture texture, String id) {
        super(gvrContext, texture,width,height);
        this.id = id;
    }

    public Thumbnail(GVRContext gvrContext, Future<GVRTexture> texture, String id) {
        super(gvrContext, texture,width,height);
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
