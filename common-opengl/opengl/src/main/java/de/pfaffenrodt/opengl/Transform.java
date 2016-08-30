package de.pfaffenrodt.opengl;

import android.opengl.Matrix;

/**
 * Created by Dimitri Pfaffenrodt on 19.08.2016.
 */
public class Transform {

    private final float[] mTransformMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mPosition = new float[3];
    private final float[] mRotation = new float[3];
    private final float[] mScale = new float[3];
    private float[] mTempMatrix;

    public Transform() {
        setPosition(0,0,0);
        initScale();
        setRotation(0,0,0);
    }

    private void initScale() {
        mScale[0] = 1;
        mScale[1] = 1;
        mScale[2] = 1;
    }

    public float[] getTransformMatrix() {
        Matrix.setIdentityM(mTransformMatrix, 0);
        Matrix.translateM(mTransformMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
        mTempMatrix = mTransformMatrix.clone();
        Matrix.multiplyMM(mTransformMatrix, 0, mTempMatrix, 0, mRotationMatrix, 0);
        Matrix.scaleM(mTransformMatrix, 0 , mScale[0], mScale[1], mScale[2]);
        return mTransformMatrix;
    }

    public float[] getPosition() {
        return mPosition;
    }

    public void setPosition(float x, float y, float z) {
        mPosition[0] = x;
        mPosition[1] = y;
        mPosition[2] = z;
    }

    public void setPosition(Vector point) {
        mPosition[0] = point.getX();
        mPosition[1] = point.getY();
        mPosition[2] = point.getZ();
    }

    public float[] getRotation() {
        return mRotation;
    }

    public void setRotation(float x, float y, float z) {
        mRotation[0] = x;
        mRotation[1] = y;
        mRotation[2] = z;
        Matrix.setRotateEulerM(mRotationMatrix, 0, mRotation[0], mRotation[1], mRotation[2]);
    }

    public float[] getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        setScale(scale, scale, scale);
    }

    public void setScale(float x, float y, float z) {
        mScale[0] = x;
        mScale[1] = y;
        mScale[2] = z;
    }

    public void rotateByAxisWithWorldPivot(
            float angle,
            float axisX, float axisY, float axisZ) {

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, -mPosition[0], -mPosition[1], -mPosition[2]);
        Matrix.rotateM(mRotationMatrix, 0, angle, axisX, axisY, axisZ);
        Matrix.translateM(mRotationMatrix, 0, mPosition[0], mPosition[1], mPosition[2]);
    }
}
