package de.pfaffenrodt.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import de.pfaffenrodt.opengl.utils.BufferUtils;
import de.pfaffenrodt.opengl.utils.OpenGLUtils;

/**
 * Created by Dimitri on 18.08.16.
 */
public class SceneObject implements Renderable, RayCollider{
    private static final String TAG = "SceneObject";


    static final int COORDS_PER_VERTEX = 3;
    public static final int COORDS_PER_UV = 2;

    private final Transform mTransform = new Transform();
    private int mModelViewProjectionMatrixHandle;
    private int mPositionHandle;

    private FloatBuffer mVertexBuffer;

    private final int mVertexCount;
    private final int mVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private Shader mShader;
    private int mTextureDataHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

    private FloatBuffer mTextureCoordinateBuffer;

    private List<SceneObject> mChildSceneObjects;

    protected RayCollider mRayCollider;

    public SceneObject(){
        mVertexCount = 0;
        /**
         * empty object. used as group
         */
    }

    public SceneObject(Context context, float[] vertex) {
        mVertexCount = vertex.length / COORDS_PER_VERTEX;
        initVertexBuffer(vertex);
        initShader(context);
    }

    public Transform getTransform() {
        return mTransform;
    }

    private void initShader(Context context) {
        mShader = new Shader(context);
    }

    public Shader getShader() {
        return mShader;
    }

    public void loadTexture(int resourceId){
        float[] uvMap = getUvMap();
        if(uvMap == null|| uvMap.length == 0){
            throw new IllegalArgumentException("SceneObject not contain an uvMap. override getUvMap() and provide an uv map");
        }
        mTextureCoordinateBuffer = BufferUtils.createFloatBuffer(uvMap);
        mTextureDataHandle = mShader.loadTexture(resourceId);
    }

    public void loadTexture(Bitmap bitmap){
        if(mTextureDataHandle != 0){
            mShader.updateTexture(mTextureDataHandle, bitmap);
            return;
        }
        float[] uvMap = getUvMap();
        if(uvMap == null|| uvMap.length == 0){
            throw new IllegalArgumentException("SceneObject not contain an uvMap. override getUvMap() and provide an uv map");
        }
        mTextureCoordinateBuffer = BufferUtils.createFloatBuffer(uvMap);
        mTextureDataHandle = mShader.loadTexture(bitmap);
    }

    private void initVertexBuffer(float[] coords) {
        // initialize vertex byte buffer for shape coordinates
        this.mVertexBuffer = BufferUtils.createFloatBuffer(coords);
    }

    protected void updateVertexBuffer(float[] coords){
        this.mVertexBuffer.clear();
        this.mVertexBuffer.put(coords);
        this.mVertexBuffer.position(0);
    }


    public void draw(float[] modelViewProjectionMatrix) {

        float[] scratch = new float[16];
        /**
         * translate rotate and scale by transformMatrix. share with child sceneObjects.
         */
        Matrix.multiplyMM(scratch, 0, modelViewProjectionMatrix, 0, mTransform.getTransformMatrix(), 0);

        if(mTextureDataHandle != 0) {
            /**
             * draw object only with loaded texture
             */
            drawSceneObject(scratch);
        }

        drawChildren(scratch);
    }

    private void drawSceneObject(float[] modelViewProjectionMatrix) {
        int programId = mShader.getProgramId();
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(programId);
        mShader.updateColor();
        // get handle to vertex mShader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(programId, Shader.ATTRIBUTE_POSITION);
        if(mTextureDataHandle != 0){
            mTextureUniformHandle = GLES20.glGetUniformLocation(programId, Shader.UNIFORM_TEXTURE);
            mTextureCoordinateHandle = GLES20.glGetAttribLocation(programId, Shader.ATTRIBUTE_TEXTURE_COORDINATE);
            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

            // Tell the texture uniform sampler to use this texture in the mShader by binding to texture unit 0.
            GLES20.glUniform1i(mTextureUniformHandle, 0);
            GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
                    COORDS_PER_UV,
                    GLES20.GL_FLOAT,
                    false,
                    0, mTextureCoordinateBuffer);
            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        }

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                mVertexStride,
                mVertexBuffer
        );

        // get handle to shape's transformation matrix
        mModelViewProjectionMatrixHandle = GLES20.glGetUniformLocation(programId, Shader.UNIFORM_MODEL_VIEW_PROJECTION_MATRIX);
        OpenGLUtils.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mModelViewProjectionMatrixHandle, 1, false, modelViewProjectionMatrix, 0);
        OpenGLUtils.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawArrays(
                GLES20.GL_TRIANGLES,
                0, //first
                mVertexCount
        );

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        if(mTextureDataHandle != 0) {
            GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
        }
    }

    private void drawChildren(float[] modelViewProjectionMatrix) {
        if(mChildSceneObjects == null){
            return;
        }
        for (int i = 0; i < mChildSceneObjects.size(); i++) {
            SceneObject sceneObject = mChildSceneObjects.get(i);
            if(sceneObject != null){
                sceneObject.draw(modelViewProjectionMatrix);
            }
        }
    }

    public float[] getUvMap(){
        return new float[0];
    }

    public List<SceneObject> getChildSceneObjects() {
        return mChildSceneObjects;
    }

    public void setChildSceneObjects(List<SceneObject> childSceneObjects) {
        mChildSceneObjects = childSceneObjects;
    }

    public void addChild(SceneObject sceneObject){
        if(sceneObject == null){
            return;
        }
        if(mChildSceneObjects == null){
            mChildSceneObjects = new ArrayList<>();
        }
        mChildSceneObjects.add(sceneObject);
    }

    public RayCollider getRayCollider() {
        return mRayCollider;
    }

    public void setRayCollider(RayCollider rayCollider) {
        mRayCollider = rayCollider;
    }

    @Override
    public boolean intersects(Ray ray) {
        if(mRayCollider != null){
            return mRayCollider.intersects(ray);
        }
        return false;
    }
}
