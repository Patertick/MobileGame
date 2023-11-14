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
    SensorEventListener mAccelerometerEventListener;

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
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometerSensor == null){
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this, "No accelerometer", duration);
            Log.d("ERROR", "Device has no accelerometer sensor");
            return;
        }



        //ourView = (ImageView) findViewById(R.id.imageView);





        mAccelerometerEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                if(sensorEvent.values[1] > 5.0f) {
                    diggingView.SetMotion("UP");
                }
                else if(sensorEvent.values[1] < -5.0f) {
                    diggingView.SetMotion("DOWN");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i){

            }
        };
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
        mSensorManager.registerListener(mAccelerometerEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        diggingView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelerometerEventListener);
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