package de.pfaffenrodt.opengl;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Dimitri on 26.08.16.
 */
public class RaySceneObject extends SceneObject {
    static final float RAY_WIDTH = 0.1f;
    static final float RAY_HEIGHT = 0.1f;
    static final float RAY_LENGTH = 2.0f;

    public RaySceneObject(Context context) {
        super(context, getVertex(null));
        loadTexture(R.raw.ic_ray);
    }

    /**
     * create an plane mesh as vertex float array.
     * mesh is an 6 vertex, 2 triangles plane
     * pivot is in point of ray.
     *
     * for example mesh size of 1 x 1:
     *  0.5, 0.5, 0.0f,// top right
     *  0.5 -0.5, 0.0f,// bottom right
     * -0.5  0.5, 0.0f,// top left
     *  0.5 -0.5, 0.0f,// bottom right
     * -0.5 -0.5, 0.0f,// bottom left
     * -0.5  0.5  0.0f // top left
     *
     * @return vertex array as float.
     */
    public static float[] getVertex(@Nullable Ray ray) {

        float perspectiveRotation = 0.2f;
        float halfWidth = RAY_WIDTH / 2f;
        float halfHeight = RAY_HEIGHT / 2f;
        Vector rayEndPoint =
                ray == null
                ? new Vector(0,0,1)
                : ray.getVector().scale(RAY_LENGTH);
        float[] mesh = {
                rayEndPoint.getX() + halfWidth, rayEndPoint.getY() + halfHeight + perspectiveRotation, rayEndPoint.getZ(), // top right
                halfWidth, -halfHeight, 0.0f, // bottom right
                rayEndPoint.getX() - halfWidth, rayEndPoint.getY() + halfHeight + perspectiveRotation, rayEndPoint.getZ(), // top left
                halfWidth, -halfHeight, 0.0f, // bottom right
                -halfWidth, -halfHeight, 0.0f, // bottom left
                rayEndPoint.getX() - halfWidth, rayEndPoint.getY() + halfHeight + perspectiveRotation, rayEndPoint.getZ(), // top left
        };
        return mesh;
    }

    public void update(Ray ray){
        getTransform().setPosition(ray.getPoint());
        updateVertexBuffer(getVertex(ray));
    }

    private final float[] mTextureUVData = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    @Override
    public float[] getUvMap() {
        return mTextureUVData;
    }

}
