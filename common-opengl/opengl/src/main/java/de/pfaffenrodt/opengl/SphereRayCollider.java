package de.pfaffenrodt.opengl;

import android.util.Log;

/**
 * Created by Dimitri on 26.08.16.
 */
public class SphereRayCollider implements RayCollider {
    private static final String TAG = "SphereRayCollider";
    private final Transform mTransform;
    private float mRadius;

    public SphereRayCollider(Transform transform, float radius) {
        this.mTransform = transform;
        this.mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    @Override
    public boolean intersects(Ray ray) {
        float[] transformMatrix = mTransform.getTransformMatrix();
        float[] translation = Matrix.getTranslation(transformMatrix);
        Vector center = new Vector(translation);
        return sphereIntersect(ray, center, mRadius);
    }

    public boolean sphereIntersect(Ray ray, Vector sphereOrigin, float sphereRadius){
        Vector rayToCenter = sphereOrigin.sub(ray.getPoint());
        float lengthToCenter = rayToCenter.length();
        float lengthToSphereCenter = rayToCenter.dotProduct(ray.getVector());

        if (lengthToSphereCenter < 0.0f){
            /**
             * is in opposite direction. may be behind camera
             */
            return false;
        }
        float distance = (float) Math.sqrt((lengthToCenter * lengthToCenter) - (lengthToSphereCenter * lengthToSphereCenter));
        if(distance < sphereRadius){
            return true;
        }
        return false;
    }

}
