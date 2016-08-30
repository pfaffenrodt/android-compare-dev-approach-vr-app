package de.pfaffenrodt.opengl;

import android.util.Log;

/**
 * Created by Dimitri on 26.08.16.
 */
public class Ray{
    private static final String TAG = "Ray";
    private Vector point;
    private Vector mVector;
    private final static float[] mInvertedModelViewProjectionMatrix = new float[16];

    public Ray(float[] fromPoint, float[] toPoint) {
        this.point = new Vector(fromPoint);
        mVector = Vector.between(point, new Vector(toPoint));
        mVector.normalise();
    }

    public Ray(Vector fromPoint, Vector toPoint) {
        this.point = fromPoint;
        mVector = Vector.between(fromPoint, toPoint);
        mVector.normalise();
    }

    public Vector getPoint() {
        return point;
    }

    public void setPoint(Vector point) {
        this.point = point;
    }

    public Vector getVector() {
        return mVector;
    }

    public void setVector(Vector vector) {
        mVector = vector;
    }



    /**
     * http://antongerdelan.net/
     * opengl/raycasting.html
     * @param viewportX
     * @param viewportY
     */
    public static Ray create(float viewportX, float viewportY, float viewportWidth, float viewportHeight, float[] projectionMatrix){
        /**
         * 1. calculate normalized Device Coordinates form viewport position
         */
        float x = (2f * viewportX) / viewportWidth -1f;
        float y = 1.0f - (2.0f * viewportY) / viewportHeight;

        /**
         * 2. Homogeneous Clip Coordinates
         *
         * z = -1 We want our ray's z to point forwards - this is usually the negative z direction in OpenGL style
         */
        float w = 1.0f;

        final float[] nearPointNormalized = {x,y,-1 ,w};
        final float[] farPointNormalized = {x,y,1,w};

        android.opengl.Matrix.invertM(mInvertedModelViewProjectionMatrix, 0, projectionMatrix, 0);

        final float[] nearPoint = new float[4];
        final float[] farPoint = new float[4];
        android.opengl.Matrix.multiplyMV(nearPoint, 0, mInvertedModelViewProjectionMatrix, 0, nearPointNormalized, 0);
        android.opengl.Matrix.multiplyMV(farPoint, 0, mInvertedModelViewProjectionMatrix, 0, farPointNormalized, 0);
        Vector.divideProjection(nearPoint);
        Vector.divideProjection(farPoint);
        Ray ray = new Ray(nearPoint, farPoint);
        Log.d(TAG, "ray create: " + ray.toString());
        return ray;
    }

    @Override
    public String toString() {
        return "Ray{" +
                "point=" + point.toString() +
                ", mVector=" + mVector.toString() +
                '}';
    }
}
