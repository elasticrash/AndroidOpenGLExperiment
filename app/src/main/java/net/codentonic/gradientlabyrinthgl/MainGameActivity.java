package net.codentonic.gradientlabyrinthgl;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MainGameActivity extends AppCompatActivity implements SensorEventListener {

    private OpenGLView openGLView;
    private TextView outPut1;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private final static int OVERFLOW_LIMIT = 20;
    private final static int SENSITIVITY = 4;
    private float[][] movingAverage = new float[3][OVERFLOW_LIMIT];
    private int overflow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        openGLView = findViewById(R.id.openGLView);
        outPut1 = findViewById(R.id.accText);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        initListeners();
    }

    public void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onResume(){
        super.onResume();
        openGLView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        openGLView.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (openGLView.GLReady) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float v1 = Math.round(event.values[0] * 100.0) / 100f;
                float v2 = Math.round(event.values[1] * 100.0) / 100f;
                float v3 = Math.round(event.values[2] * 100.0) / 100f;

                movingAverage[0][overflow] = v1;
                movingAverage[1][overflow] = v2;

                float s1 = calculateAverage(movingAverage[0]);
                float s2 = calculateAverage(movingAverage[1]);

                String eventValueString = String.format("a:%f b:%f c:%f", v1, v2, v3);
                outPut1.setText("accelerometer " + eventValueString);
                if (openGLView.renderer.objectsReady) {
                    openGLView.renderer.getCircle().CalculatePoints(s1 / SENSITIVITY, s2 / SENSITIVITY, 0.1f, 55);
                    openGLView.requestRender();
                }
            }
            openGLView.requestRender();
        }
        overflow += 1;
        if (overflow >= OVERFLOW_LIMIT) {
            overflow = 0;
        }
    }

    private float calculateAverage(float[] input) {
        DoubleStream io = IntStream.range(0, input.length)
                .mapToDouble(i -> input[i]);
        float sum = (float)io.sum();
        return sum/OVERFLOW_LIMIT;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
