package de.pfaffenrodt.opengl;

/**
 * Created by Dimitri on 26.08.16.
 */
public class Matrix {

    public static float[] getTranslation(float[] matrix){
        if(matrix.length < 16){
            throw new IllegalArgumentException("matrix is not an 4x4 matrix");
        }
        float[] translation = {matrix[12], matrix[13], matrix[14], matrix[15]};
        return translation;
    }
}
