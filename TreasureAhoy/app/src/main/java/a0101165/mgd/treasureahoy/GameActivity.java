package a0101165.mgd.treasureahoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {
    ImageView ourView;

    int screenWidth;
    int screenHeight;

    GameView gameView;

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


        gameView = new GameView(this, screenWidth, screenHeight, value);
        setContentView(gameView);




        //ourView = (ImageView) findViewById(R.id.imageView);



        // get bitmaps

    }

    @Override
    protected void onStop() {
        super.onStop();

        while(true) {
            gameView.Pause();
            break;
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.Pause();
    }

    // if back is pressed on phone we want to finish the activity (quit the game)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            gameView.Pause();
            finish();
            return true;
        }
        return false;
    }
}