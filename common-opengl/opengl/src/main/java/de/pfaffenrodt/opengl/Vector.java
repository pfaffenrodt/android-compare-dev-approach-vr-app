package de.pfaffenrodt.opengl;

/**
 * Created by Dimitri on 26.08.16.
 */
public class Vector {
    private float mX;
    private float mY;
    private float mZ;

    public Vector(float[] vector) {
        mX = vector[0];
        mY = vector[1];
        mZ = vector[2];
    }

    public Vector(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getZ() {
        return mZ;
    }

    public void setZ(float z) {
        mZ = z;
    }

    public static Vector between(Vector from, Vector to){
        return to.sub(from);
    }

    public Vector crossProduct(Vector other){
        return new Vector(
                (mY * other.mZ) - (mZ * other.mY),
                (mZ * other.mX) - (mX * other.mZ),
                (mX * other.mY) - (mY * other.mX)
        );
    }

    public float dotProduct(Vector other){
        return mX * other.mX
                + mY * other.mY
                + mZ * other.mZ;
    }

    public Vector scale(float scaleFactor){
        return new Vector(
                mX* scaleFactor,
                mY * scaleFactor,
                mZ * scaleFactor
        );
    }

    public float length(){
        return (float) Math.sqrt(
                  mX * mX
                + mY * mY
                + mZ * mZ
        );
    }

    public static float length(float[] vector){
        return (float) Math.sqrt(
                  vector[0] * vector[0]
                + vector[1] * vector[1]
                + vector[2] * vector[2]
        );
    }

    public Vector translate(Vector vector){
        return new Vector(
                mX + vector.mX,
                mY + vector.mY,
                mZ + vector.mZ
        );
    }


    public static void divideProjection(float[] vector){
        if(vector[3] != 0){
            vector[0]/=vector[3];
            vector[1]/=vector[3];
            vector[2]/=vector[3];
            vector[3] = 0;
        }
    }

    public void normalise(){
        float length = length();
        mX /= length;
        mY /= length;
        mZ /= length;
    }

    public static void normalise(float[] vector){
        float length = length(vector);
        vector[0] /= length;
        vector[1] /= length;
        vector[2] /= length;
    }

    public Vector sub(Vector other){
        return new Vector(
                mX - other.getX(),
                mY - other.getY(),
                mZ - other.getZ()
        );
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + mX +
                ", y=" + mY +
                ", z=" + mZ +
                '}';
    }
}
