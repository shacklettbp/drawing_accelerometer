package com.kristen.pebblestream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

public class CanvasView extends View {

    private static final String TAG = CanvasView.class.getSimpleName();

    private Vector<Float> mVectorX;
    private Vector<Float> mVectorY;
    private float x = 10;
    private float y = 10;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mVectorX = new Vector<Float>();
        mVectorY = new Vector<Float>();

//        Canvas canvas = getHolder().lockCanvas();
//        canvas.drawColor(Color.WHITE);
//        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint black = new Paint();
        black.setARGB(255, 0, 0, 0);

        for (int i = 0; i < mVectorX.size(); i++) {
            canvas.drawCircle(mVectorX.get(i), mVectorY.get(i), 5, black);
        }

    }


    public void addX(float x) {
        mVectorX.add(x);
    }

    public void addY(float y) {
        mVectorY.add(y);
    }
}
