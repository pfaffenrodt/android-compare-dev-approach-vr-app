package de.pfaffenrodt.gearvrf;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRCamera;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject.GVRVideoType;

import java.io.IOException;
import java.util.concurrent.Future;

import de.pfaffenrodt.gearvrf.menu.Button;
import de.pfaffenrodt.gearvrf.menu.Control;
import de.pfaffenrodt.gearvrf.menu.MainMenu;
import de.pfaffenrodt.gearvrf.menu.PickHandler;

/**
 * Created by Dimitri Pfaffenrodt on 07.08.2016.
 * <p/>
 * GVRMain implementation.
 * handle framework callbacks.
 * manage to switch between menu, photo and video scene
 * <p/>
 * photo and video scenes are an copy from GearVRf-Demos
 */
public class VRMain extends GVRMain {
    private static final String TAG = "VRMain";
    private final int STATE_MENU = 0;
    private final int STATE_PHOTO = 1;
    private final int STATE_VIDEO = 2;
    private int currentState = STATE_MENU;
    private int newState = currentState;
    private GVRContext gvrContext;

    private boolean singleTab;
    private PickHandler pickHandler;
    private GVRScene scene;
    private MediaPlayer mediaPlayer;

    /**
     * initialize default scene
     *
     * @param gvrContext
     * @throws Throwable
     */
    @Override
    public void onInit(GVRContext gvrContext) throws Throwable {
        this.gvrContext = gvrContext;
        scene = this.gvrContext.getNextMainScene();
        currentState = STATE_MENU;
        GVRCamera leftCamera = scene.getMainCameraRig().getLeftCamera();
//        leftCamera.getTransform().setScale(10.0f, 10.0f, 10.0f);
        leftCamera.setBackgroundColor(1.0f,1.0f,1.0f,1.0f);
        GVRCamera rightCamera = scene.getMainCameraRig().getRightCamera();
//        rightCamera.getTransform().setScale(10.0f, 10.0f, 10.0f);
        rightCamera.setBackgroundColor(1.0f,1.0f,1.0f,1.0f);
        scene.getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gvrContext,scene));
        pickHandler = new PickHandler();
        scene.getEventReceiver().addListener(pickHandler);
        switchToMenuScene();
    }

    public void onSingleTapUp(MotionEvent event) {
        singleTab = true;
    }

    private void switchToMenuScene() {
        scene.removeAllSceneObjects();//clear scene
        scene.addSceneObject(new MainMenu(gvrContext));
    }

    private void switchToPhotoScene() {
        scene.removeAllSceneObjects();//clear scene
        GVRSphereSceneObject sphereObject = null;

        // load texture
        Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.photosphere));

        // create a sphere scene object with the specified texture and triangles facing inward (the 'false' argument)
        sphereObject = new GVRSphereSceneObject(gvrContext, false, texture);

        // add the scene object to the scene graph
        scene.addSceneObject(sphereObject);
        addBackButton(scene);
    }

    private void switchToVideoScene() {

        scene.removeAllSceneObjects();//clear scene

        // set up camerarig position (default)
        scene.getMainCameraRig().getTransform().setPosition(0.0f, 0.0f, 0.0f);

        // create sphere / mesh
        GVRSphereSceneObject sphere = new GVRSphereSceneObject(gvrContext, false);
        GVRMesh mesh = sphere.getRenderData().getMesh();

        // create mediaplayer instance
        mediaPlayer = new MediaPlayer();
        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = gvrContext.getContext().getAssets().openFd("videos_s_3.mp4");
            android.util.Log.d(TAG, "Assets was found.");
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            android.util.Log.d(TAG, "DataSource was set.");
            assetFileDescriptor.close();
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            gvrContext.getActivity().finish();
            android.util.Log.e(TAG, "Assets were not loaded. Stopping application!");
        }

        mediaPlayer.setLooping(true);
        android.util.Log.d(TAG, "starting player.");
        mediaPlayer.start();

        // create video scene
        GVRVideoSceneObject video = new GVRVideoSceneObject(gvrContext, mesh, mediaPlayer, GVRVideoType.MONO);
        video.setName("video");

        // apply video to scene
        scene.addSceneObject(video);
        addBackButton(scene);
    }

    private void addBackButton(GVRScene scene) {
        // load texture
        GVRTexture texture = gvrContext.loadTexture(new GVRAndroidResource(gvrContext, R.mipmap.ic_backbutton));
        Button button = new BackButton(gvrContext, texture);
        //add button to scene
        scene.addSceneObject(button);
    }

    @Override
    public void onStep() {
        handleSingleTab();
        handleNewState();
    }

    private void handleSingleTab() {
        if (singleTab) {
            singleTab = false;
            if(pickHandler != null && pickHandler.pickedObject != null){
                GVRSceneObject pickedObject = pickHandler.pickedObject;
                if(pickedObject instanceof Control){
                    String id = ((Control) pickedObject).getId();
                    Log.d(TAG, "handleSingleTab() called with: " + id);
                    handleButton(id);
                }
            }
        }
    }

    private void handleNewState() {
        if (newState != currentState) {
            switch (newState) {
                case STATE_MENU:
                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer = null;
                    }
                    currentState = STATE_MENU;
                    switchToMenuScene();
                    break;
                case STATE_PHOTO:
                    currentState = STATE_PHOTO;
                    switchToPhotoScene();
                    break;
                case STATE_VIDEO:
                    currentState = STATE_VIDEO;
                    switchToVideoScene();
                    break;
            }
        }
    }

    private void handleButton(String id){
        switch(id){
            case BackButton.BACK_BUTTON:
                newState = STATE_MENU;
                break;
            case "photo":
                newState = STATE_PHOTO;
                break;
            case "video":
                newState = STATE_VIDEO;
                break;
        }
    }
}
