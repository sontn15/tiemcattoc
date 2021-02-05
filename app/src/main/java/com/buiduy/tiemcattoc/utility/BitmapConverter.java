package com.buiduy.tiemcattoc.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {
    // Hàm chuyển đổi từ ảnh BMP sang chuỗi Base64
    public static String bitmapToString(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    // Hàm chuyển đổi chuỗi Base64 sang BMP
    public static Bitmap stringToBitmap(String input)
    {
        byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }
}