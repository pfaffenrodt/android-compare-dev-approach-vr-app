package de.pfaffenrodt.opengl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by Dimitri on 30.08.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class MatrixTest {

    private float[] mIdentityMatrix;

    @Before
    public void setUp() throws Exception {

        mIdentityMatrix = new float[16];
        android.opengl.Matrix.setIdentityM(mIdentityMatrix, 0);

    }

    @Test
    public void testGetTranslation() throws Exception {
        float[] expectedTranslation = new float[3];
        expectedTranslation[0] = 0;
        expectedTranslation[1] = 0;
        expectedTranslation[2] = 0;

        testGetTranslationWithGivenPosition(expectedTranslation);


        expectedTranslation[0] = 0;
        expectedTranslation[1] = 0;
        expectedTranslation[2] = 1;

        testGetTranslationWithGivenPosition(expectedTranslation);

        expectedTranslation[0] = 0;
        expectedTranslation[1] = 1;
        expectedTranslation[2] = 1;

        testGetTranslationWithGivenPosition(expectedTranslation);

        expectedTranslation[0] = 1;
        expectedTranslation[1] = 1;
        expectedTranslation[2] = 1;

        testGetTranslationWithGivenPosition(expectedTranslation);

        expectedTranslation[0] = -1;
        expectedTranslation[1] = 0;
        expectedTranslation[2] = -1;

        testGetTranslationWithGivenPosition(expectedTranslation);

        expectedTranslation[0] = 1;
        expectedTranslation[1] = 1;
        expectedTranslation[2] = 1;

        float[] scale = {1,1,1};
        testGetTranslationWithGivenPositionAndScale(expectedTranslation,scale);

        scale[0] = -1;
        scale[1] = -1;
        scale[2] = -1;
        testGetTranslationWithGivenPositionAndScale(expectedTranslation,scale);

        scale[0] = -1;
        scale[1] = -1;
        scale[2] = -1;
        testGetTranslationWithGivenPositionAndScale(expectedTranslation,scale);

        float[] axis = {1,1,1};
        testGetTranslationWithGivenPositionAndRotation(expectedTranslation,30f,axis);
        testGetTranslationWithGivenPositionAndRotation(expectedTranslation,45f,axis);
        testGetTranslationWithGivenPositionAndRotation(expectedTranslation,90f,axis);
    }

    private void testGetTranslationWithGivenPosition(float[] expectedTranslation) {
        float[] matrix = new float[16];
        android.opengl.Matrix.translateM(matrix,0,mIdentityMatrix,0,expectedTranslation[0],expectedTranslation[1],expectedTranslation[2]);
        float[] translation = Matrix.getTranslation(matrix);
        assertEqualTranslation(expectedTranslation, translation);
    }

    private void testGetTranslationWithGivenPositionAndScale(float[] expectedTranslation, float[] scale) {
        float[] matrix = new float[16];
        android.opengl.Matrix.translateM(matrix,0,mIdentityMatrix,0,expectedTranslation[0],expectedTranslation[1],expectedTranslation[2]);
        android.opengl.Matrix.scaleM(matrix, 0 , scale[0], scale[1], scale[2]);
        float[] translation = Matrix.getTranslation(matrix);
        assertEqualTranslation(expectedTranslation, translation);
    }

    private void testGetTranslationWithGivenPositionAndRotation(float[] expectedTranslation, float angle, float[] axis) {
        float[] matrix = new float[16];
        float[] rotationMatrix = new float[16];
        android.opengl.Matrix.setIdentityM(rotationMatrix,0);
        android.opengl.Matrix.rotateM(rotationMatrix, 0 ,angle, axis[0], axis[1], axis[2]);

        android.opengl.Matrix.translateM(matrix,0,mIdentityMatrix,0,expectedTranslation[0],expectedTranslation[1],expectedTranslation[2]);
        android.opengl.Matrix.multiplyMV(matrix,0,matrix.clone(),0,rotationMatrix,0);

        float[] translation = Matrix.getTranslation(matrix);
        assertEqualTranslation(expectedTranslation, translation);
    }

    private void assertEqualTranslation(float[] expectedTranslation, float[] translation){
        for (int i = 0; i < expectedTranslation.length; i++) {
            assertEquals(expectedTranslation[i],translation[i],0.00001f);
        }
    }
}