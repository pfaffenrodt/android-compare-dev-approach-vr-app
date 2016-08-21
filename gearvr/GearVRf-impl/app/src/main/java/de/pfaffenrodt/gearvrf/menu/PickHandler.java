package de.pfaffenrodt.gearvrf.menu;

import android.util.Log;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;

/**
 * Created by Dimitri Pfaffenrodt on 07.08.2016.
 */
public class PickHandler implements IPickEvents {
    private static final String TAG = "PickHandler";
    public GVRSceneObject pickedObject = null;

    @Override
    public void onPick(GVRPicker picker) {
        pickedObject = picker.getPicked()[0].hitObject;
        if(pickedObject instanceof Control){
            ((Control) pickedObject).onPick();
        }
    }

    @Override
    public void onNoPick(GVRPicker picker) {
        if(pickedObject instanceof Control){
            ((Control) pickedObject).onNoPick();
        }
        pickedObject = null;
    }

    @Override
    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject collision) {
    }

    @Override
    public void onExit(GVRSceneObject sceneObj) {
    }

    @Override
    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject collision) {
    }
}
