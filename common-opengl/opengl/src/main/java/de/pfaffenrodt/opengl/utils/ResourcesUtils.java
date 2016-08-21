package de.pfaffenrodt.opengl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * utils to load resources.
 */
public class ResourcesUtils {
    private static final String TAG = "ResourcesUtils";

    /**
     * read text from raw file as string
     * remove comment lines
     * @param context
     * @param rawResourceId resource id of an raw file. for example R.raw.test
     * @return string from file
     */
    public static String loadStringFromRawResource(Context context, int rawResourceId) {

        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(rawResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            while (line != null) {
                if (!line.startsWith("\\\\")) {
                    stringBuilder.append(line).append("\n");
                }
                line = reader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.e(TAG, "loadStringFromRawResource: ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * load not scaled bitmap
     * @param context
     * @param resourceId
     * @return bitmap
     */
    public static Bitmap loadBitmap(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No pre-scaling
        return BitmapFactory.decodeResource(context.getResources(), resourceId, options);
    }
}
