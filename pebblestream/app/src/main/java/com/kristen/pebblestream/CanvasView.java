package com.kristen.pebblestream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Vector;

public class CanvasView extends View {

    private static final String TAG = CanvasView.class.getSimpleName();

    private Vector<Float> mVectorX;
    private Vector<Float> mVectorY;
    private float x = 10;
    private float y = 10;
    private int radius = 5;
    
    private Bitmap bitmap;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint black = new Paint();

        int color = Color.argb(255, 0, 0, 0);

        super.onDraw(canvas);

        //canvas.drawCircle(10, 10, 50, black);

//        for (int i = 0; i < 100; i ++) {
//            for (int j = 0; j < 100; j++) {
//                Log.d(TAG, "Before setting pixel:" + bitmap.getPixel(i, j));
//
//                bitmap.setPixel(i, j, color);
//
//                Log.d(TAG, "After setting pixel:" + bitmap.getPixel(i, j));
//            }
//        }


            for (int i = -radius; i < radius; i++) {
                for (int j = -radius; j < radius; j++) {
                    if (x + i >= 0 && y + j >= 0) {
                        bitmap.setPixel((int) x + i, (int) y + j, color);
                    }
                }
            }

        Log.d(TAG, "Height: " + canvas.getHeight() + ", Width: " + canvas.getWidth());
        canvas.drawBitmap(bitmap, 0, 0, black);
    }


    public void setX(float x_pos) {
        x = x_pos;
    }

    public void setY(float y_pos) {
        y = y_pos;

    }
}
