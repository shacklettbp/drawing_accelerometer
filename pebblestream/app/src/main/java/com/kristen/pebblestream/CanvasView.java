package com.kristen.pebblestream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class CanvasView extends View {

    private static final String TAG = CanvasView.class.getSimpleName();

<<<<<<< HEAD
    private float x = 0;
    private float y = 0;
    private float prev_x = 0;   //to store values and see if you need to draw a line in between
    private float prev_y = 0;
=======
    private Vector<Float> mVectorX;
    private Vector<Float> mVectorY;
    private float x = 10;
    private float y = 10;
    private int radius = 5;
    
<<<<<<< HEAD
>>>>>>> FETCH_HEAD
=======
>>>>>>> FETCH_HEAD
    private Bitmap bitmap;
    private int size_of_square = 5;
    private int max_space_allowed = 4;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //gets rid of the original square
        if (x == 0 && y == 0) return;

        Paint black = new Paint();
        int color = Color.argb(255, 0, 0, 0);

        super.onDraw(canvas);

<<<<<<< HEAD
        for (int i = -size_of_square; i < size_of_square; i++) {
            for (int j = -size_of_square; j < size_of_square; j++) {
                if (x + i >= 0 && y + j >= 0) {
                    prev_x = x;
                    prev_y = y;
                    bitmap.setPixel((int) x + i, (int) y + j, color);
=======
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


            for (int i = -1*radius; i < radius; i++) {
                for (int j = -1*radius; j < radius; j++) {
                    if (x + i >= 0 && y + j >= 0) {
                        bitmap.setPixel((int) x + i, (int) y + j, color);
                    }
>>>>>>> FETCH_HEAD
                }
            }
        }

        /* Need to put drawLines on actual bitmap, not the canvas. Canvas is reset and not saved. */

        //if distance between two points is too large, draw line to connect them
//        double distBetweenPts = Math.sqrt(Math.pow((x - prev_x), 2) + Math.pow((y - prev_y),2));
//        if (distBetweenPts >= max_space_allowed) {
//            int position = 0;
//            float pts[] = new float[size_of_square * 2 * 4];
//            for (int i = -size_of_square; i < size_of_square; i++) {
//                if (x + i < 0 && y + i < 0) return;
//                Log.d(TAG, "DistBetweenPts: " + distBetweenPts  + ", Position: " + position);
//                pts[position * 4] = prev_x + i;
//                pts[position * 4 + 1] = prev_y + i;
//                pts[position * 4 + 2] = x+i;
//                pts[position * 4 + 3] = y+i;
//                position++;
//            }
//        //canvas.drawLines(pts, 0, pts.length, black);
//        }
        canvas.drawBitmap(bitmap, 0, 0, black);
    }

    public void setX(float x_pos) {
        x = x_pos;
    }

    public void setY(float y_pos) { y = y_pos; }
}
