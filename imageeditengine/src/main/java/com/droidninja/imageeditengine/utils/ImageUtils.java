package com.droidninja.imageeditengine.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtils {

    public static Bitmap rotateImage(Bitmap bitmap, int rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }
}
