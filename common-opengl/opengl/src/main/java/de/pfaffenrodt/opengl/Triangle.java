package de.pfaffenrodt.opengl;


import android.content.Context;

public class Triangle extends SceneObject{

    public Triangle(Context context,float width, float height) {
        super(context, getVertex(width, height));
    }

    public static float[] getVertex(float width, float height){
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        return new float[]{
                0f, halfHeight, 0.0f,// top center
                -halfWidth, -halfHeight, 0.0f,// bottom left
                halfWidth, -halfHeight, 0.0f// bottom right
        };
    }
}