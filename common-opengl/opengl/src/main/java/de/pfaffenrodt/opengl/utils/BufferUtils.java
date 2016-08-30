package de.pfaffenrodt.opengl.utils;

import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Dimitri on 26.08.16.
 */
public class BufferUtils {

    public static final int BYTE_SIZE_FLOAT = 4;
    public static final int BYTE_SIZE_SHORT = 2;

    @NonNull
    public static FloatBuffer createFloatBuffer(float[] values) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                values.length * BYTE_SIZE_FLOAT);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(values);
        floatBuffer.position(0);
        return floatBuffer;
    }
}
