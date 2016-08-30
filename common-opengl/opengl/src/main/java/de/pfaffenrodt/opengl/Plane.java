package de.pfaffenrodt.opengl;


import android.content.Context;

/**
 * Scene object with an plane mesh.
 * mesh is an 6 vertex, 2 triangles plane
 */
public class Plane extends SceneObject {

    private final Vector mNormal = new Vector(0,1,0);

    public Plane(Context context, float width, float height) {
        super(context, getVertex(width, height));
    }

    /**
     * create an plane mesh as vertex float array by width and height.
     * mesh is an 6 vertex, 2 triangles plane
     * pivot is in center of plane.
     *
     * for example mesh size of 1 x 1:
     *  0.5, 0.5, 0.0f,// top right
     *  0.5 -0.5, 0.0f,// bottom right
     * -0.5  0.5, 0.0f,// top left
     *  0.5 -0.5, 0.0f,// bottom right
     * -0.5 -0.5, 0.0f,// bottom left
     * -0.5  0.5  0.0f // top left
     *
     * @param width of plane
     * @param height of plane
     * @return vertex array as float.
     */
    public static float[] getVertex(float width, float height) {
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        return new float[]{
                 halfWidth,  halfHeight, 0.0f, // top right
                 halfWidth, -halfHeight, 0.0f, // bottom right
                -halfWidth,  halfHeight, 0.0f, // top left
                 halfWidth, -halfHeight, 0.0f, // bottom right
                -halfWidth, -halfHeight, 0.0f, // bottom left
                -halfWidth,  halfHeight, 0.0f, // top left
        };
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


    public void updateSize(float width, float height){
        updateVertexBuffer(getVertex(width, height));
    }
}