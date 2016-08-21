package de.pfaffenrodt.gearvrf;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRTexture;

import de.pfaffenrodt.gearvrf.menu.Button;

/**
 * Created by Dimitri Pfaffenrodt on 07.08.2016.
 */
public class BackButton extends Button {

    public static final String BACK_BUTTON = "backButton";

    public BackButton(GVRContext gvrContext, GVRTexture texture) {
        super(gvrContext, texture,0.4f,0.4f);
        getTransform().setPositionZ(-0.4f);
        getTransform().setPositionY(-0.4f);
    }

    @Override
    public String getId() {
        return BACK_BUTTON;
    }

}
