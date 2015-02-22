package com.kristen.pebblestream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

import java.util.UUID;

public class Landing extends Activity {

    //Constants
    public static final String TAG = Landing.class.getSimpleName();
    private static final int NUM_SAMPLES = 15;
    private static final int GRAPH_HISTORY = 200;
    private static final double FRAME_TIME = 1.0 / 10.0;
    private final double alpha = 0.5;

    //State
    private int sampleCount = 0;
    private long lastAverageTime = 0;
    private int[] latest_data;
    private GraphViewSeries seriesX, seriesY, seriesZ;
    private int sampleCounter = 0;
    private int totalData = 0;

    private float pos_x = 0;
    private float prev_vel_x = 0;

    private float pos_y = 0;
    private float prev_vel_y = 0;

    //Layout members
    private TextView
            xView,
            yView,
            zView,
            rateView;
    private Button startButton;
//    private GraphView gView;
//    private ImageView mCanvasImage;
    private CanvasView mCanvasView;
//    private Canvas canvas;
    private Paint black;
    private SurfaceHolder holder;
    private double grav_x;
    private double grav_y;
    private double grav_z;
    private double multiplier = 1;

    //Other members
    private PebbleDataReceiver receiver;
    private UUID uuid = UUID.fromString("2893b0c4-2bca-4c83-a33a-0ef6ba6c8b17");
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        xView = (TextView)findViewById(R.id.x_view);
        yView = (TextView)findViewById(R.id.y_view);
        zView = (TextView)findViewById(R.id.z_view);
        rateView = (TextView)findViewById(R.id.rate_view);
        startButton = (Button)findViewById(R.id.start_button);

        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PebbleDictionary dict = new PebbleDictionary();
                dict.addInt32(0, 0);
                PebbleKit.sendDataToPebble(getApplicationContext(), uuid, dict);
            }

        });

        //Graph
        seriesX = new GraphViewSeries("X", new GraphViewSeriesStyle(Color.argb(255, 255, 0, 0), 2), new GraphViewData[] {
                new GraphViewData(1, 0)
        });
        seriesY = new GraphViewSeries("Y", new GraphViewSeriesStyle(Color.argb(255, 0, 255, 0), 2), new GraphViewData[] {
                new GraphViewData(1, 0)
        });
        seriesZ = new GraphViewSeries("Z", new GraphViewSeriesStyle(Color.argb(255, 0, 0, 255), 2), new GraphViewData[] {
                new GraphViewData(1, 0)
        });

        black = new Paint();
        black.setARGB(255, 0, 0, 0);

//        mCanvasImage = (ImageView) findViewById(R.id.canvasImage);
//        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bitmap);
//        mCanvasImage.setImageBitmap(bitmap);

        mCanvasView = (CanvasView) findViewById(R.id.canvasView);
//        holder = mCanvasView.getHolder();

        grav_x = 0;
        grav_y = 0;
        grav_z = 0;

    }

    @Override
    protected void onResume() {
        super.onResume();

        receiver = new PebbleDataReceiver(uuid) {

            @Override
            public void receiveData(Context context, int transactionId, PebbleDictionary data) {
                PebbleKit.sendAckToPebble(getApplicationContext(), transactionId);

                //Count total data
                totalData += 3 * NUM_SAMPLES * 4;

                //Get data
                latest_data = new int[3 * NUM_SAMPLES];
//				Log.d(TAG, "NEW DATA PACKET");
                for(int i = 0; i < NUM_SAMPLES; i++) {
                    for(int j = 0; j < 3; j++) {
                        try {
                            latest_data[(3 * i) + j] = data.getInteger((3 * i) + j).intValue();
                        } catch(Exception e) {
                            latest_data[(3 * i) + j] = -1;
                        }
                    }
//					Log.d(TAG, "Sample " + i + " data: X: " + latest_data[(3 * i)] + ", Y: " + latest_data[(3 * i) + 1] + ", Z: " + latest_data[(3 * i) + 2]);
                }


                grav_x = alpha * grav_x + (1 - alpha) * latest_data[0];
                grav_y = alpha * grav_y + (1 - alpha) * latest_data[1];
//                grav_z = alpha * grav_z + (1 - alpha) * latest_data[2];

                latest_data[0] -= grav_x;
                latest_data[1] -= grav_y;
//                latest_data[2] -= grav_z;


                pos_x += prev_vel_x * FRAME_TIME + (Math.pow(FRAME_TIME, 2) * multiplier * latest_data[0])/2;
                prev_vel_x += latest_data[0] * FRAME_TIME;

                pos_y += prev_vel_y * FRAME_TIME + (Math.pow(FRAME_TIME, 2) * multiplier * latest_data[1])/2;
                prev_vel_y += latest_data[1] * FRAME_TIME;

                Log.d(TAG, "X: " + pos_x + ", Y: " + pos_y);

                // positions in canvas axis are switched
                mCanvasView.setX(pos_y);
                mCanvasView.setY(pos_x);

//                mCanvasView.setX(pos_x);
//                mCanvasView.setY(pos_y);

//                if (holder.getSurface().isValid()) {
//                    Canvas canvas = holder.lockCanvas();
//                    canvas.drawCircle(pos_x, pos_y, 5, black);
//                    holder.unlockCanvasAndPost(canvas);
//                }

                //Show
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        xView.setText("X: " + latest_data[0]);
                        yView.setText("Y: " + latest_data[1]);
                        zView.setText("Z: " + latest_data[2]);


                        mCanvasView.postInvalidate();
                    }

                });

                //Show on graph
                for(int i = 0; i < NUM_SAMPLES; i++) {
                    seriesX.appendData(new GraphViewData(sampleCounter, latest_data[(3 * i)]), true, GRAPH_HISTORY);
                    seriesY.appendData(new GraphViewData(sampleCounter, latest_data[(3 * i) + 1]), true, GRAPH_HISTORY);
                    seriesZ.appendData(new GraphViewData(sampleCounter, latest_data[(3 * i) + 2]), true, GRAPH_HISTORY);
                    sampleCounter++;
                }

                if(System.currentTimeMillis() - lastAverageTime > 1000) {
                    lastAverageTime = System.currentTimeMillis();

                    rateView.setText("" + sampleCount + " samples per second."
                            + "\n"
                            + data.size() + " * 4-btye int * " + sampleCount + " samples = " + (4 * data.size() * sampleCount) + " Bps."
                            + "\n"
                            + "Total data received: " + getTotalDataString());
                    sampleCount = 0;
                } else {
                    sampleCount++;
                }
            }

        };

        PebbleKit.registerReceivedDataHandler(this, receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    private String getTotalDataString() {
        if(totalData < 1000) {
            return "" + totalData + " Bytes.";
        } else if(totalData > 1000 && totalData < 1000000) {
            return "" + totalData / 1000 + " KBytes.";
        } else {
            return "" + totalData / 1000000 + " MBytes.";
        }
    }
}