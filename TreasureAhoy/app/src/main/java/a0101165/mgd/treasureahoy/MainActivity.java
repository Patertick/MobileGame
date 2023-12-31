package a0101165.mgd.treasureahoy;

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



public class MainActivity extends AppCompatActivity {


    ImageView ourView;


    int screenWidth;
    int screenHeight;

    MainMenuView mainMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String value = intent.getStringExtra("Key");

        // find size of display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        mainMenuView = new MainMenuView(this, screenWidth, screenHeight);
        setContentView(mainMenuView);

    }

    @Override
    protected void onStop() {
        super.onStop();

        while(true) {
            mainMenuView.Pause();
            break;
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainMenuView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainMenuView.Pause();
    }

    // if back is pressed on phone we want to finish the activity (quit the game)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            mainMenuView.Pause();
            finish();
            return true;
        }
        return false;
    }
}