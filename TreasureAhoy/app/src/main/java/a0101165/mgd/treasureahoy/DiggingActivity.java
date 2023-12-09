package a0101165.mgd.treasureahoy;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.ImageView;

public class DiggingActivity extends AppCompatActivity {


    ImageView ourView;


    int screenWidth;
    int screenHeight;

    DiggingView diggingView;

    SensorManager mSensorManager;
    Sensor mAccelerometerSensor;
    Sensor mGeomagneticSensor;
    SensorEventListener mAccelerometerEventListener;
    SensorEventListener mGeomagneticEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int value = intent.getIntExtra("Key", 0);

        // find size of display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        diggingView = new DiggingView(this, screenWidth, screenHeight, value);
        setContentView(diggingView);


        mSensorManager = (SensorManager) (getSystemService(SENSOR_SERVICE));
        mGeomagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometerSensor == null || mGeomagneticSensor == null){
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, "No accelerometer or magnetic field sensor", duration);
            Log.d("ERROR", "Device has no accelerometer sensor");
            return;
        }

        //ourView = (ImageView) findViewById(R.id.imageView);





        mAccelerometerEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                diggingView.SetAccelerometerReadings(sensorEvent.values);
                // update orientation values on change
                diggingView.FindOrientation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i){

            }
        };
        mGeomagneticEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                diggingView.SetMagnetometerReadings(sensorEvent.values);
                // update orientation values on change
                diggingView.FindOrientation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        mSensorManager.registerListener(mGeomagneticEventListener, mGeomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mAccelerometerEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // get bitmaps

    }

    @Override
    protected void onStop() {
        super.onStop();

        while(true) {
            diggingView.Pause();
            break;
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccelerometerEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mGeomagneticEventListener, mGeomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        diggingView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerEventListener);
        mSensorManager.unregisterListener(mGeomagneticEventListener);
        diggingView.Pause();
    }

    // if back is pressed on phone we want to finish the activity (quit the game)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            diggingView.Pause();
            finish();
            return true;
        }
        return false;
    }
}