package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// view to use for main menu activity
public class MainMenuView extends SurfaceView implements Runnable{
    // variables
    Thread mGameLoopThread = null;
    SurfaceHolder mHolder; // used for locking thread during drawing
    volatile boolean mPlayingMenu; // used for stopping thread during onPause and onResume functions
    Paint mPaint;

    Canvas mCanvas;

    long mLastFrameTime;
    int mFPS;

    int mScreenWidth;
    int mScreenHeight;

    ArrayList<BaseEntity> mEntities;

    // functions
    public MainMenuView(Context context, int screenWidth, int screenHeight){
        // initialize variables
        super(context);
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();
        mEntities = new ArrayList<BaseEntity>();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        // add entities

        // background should be first in array list as it is behind every other entity

        BackgroundEntity tempBackground = new BackgroundEntity(1, 0,
                0, 0, mScreenHeight);
        if(!tempBackground.LoadSprite("water_moving_background.png", getResources()))
        {
            Log.d("ERROR", "Could not load background sprite");
        }

        mEntities.add(tempBackground);
        // we want player to be last in list to it is drawn on top of every other entity

        PlayerEntity tempPlayer = new PlayerEntity(1, 0, mScreenWidth/2, mScreenHeight/2);

        if(!tempPlayer.LoadSprite("pirate_ship.png", getResources())){
            Log.d("ERROR", "Could not load player sprite");
        }

        mEntities.add(tempPlayer);
    }

    @Override
    public void run() {
        while(mPlayingMenu){
            Update();
            Draw();
            ControlFPS();
        }
    }

    public void Update() {
        // simulation
        // run update functions of all saved entities
        for(int i = 0; i < mEntities.size(); i++) {
            mEntities.get(i).Update();
        }
    }

    public void Draw() {
        if(mHolder.getSurface().isValid()){
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK); // draw background
            // draw the entities
            for(int i = 0; i < mEntities.size(); i++) {
                mEntities.get(i).Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);
            }

            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void ControlFPS() {
        long timeThisFrame = (System.currentTimeMillis() - mLastFrameTime);
        long timeToSleep = 100 - timeThisFrame; // time to pause between each frame
        if(timeThisFrame > 0) {
            mFPS = (int) ( 1000/ timeThisFrame);
        }
        if(timeToSleep > 0) {
            try {
                mGameLoopThread.sleep(timeToSleep);
            } catch(InterruptedException e){
                Log.e("Thread error", "Thread could not sleep");
            }
        }

        mLastFrameTime = System.currentTimeMillis();
    }

    public void Pause() {
        mPlayingMenu = false;
        try {
            mGameLoopThread.join();
        } catch(InterruptedException e){
            Log.e("Thread error", "Thread could not join");
        }
    }

    public void Resume() {
        mPlayingMenu = true;
        mGameLoopThread = new Thread(this);
        mGameLoopThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (int i = 0; i < mEntities.size(); i++) {
            mEntities.get(i).ToggleMovement(100);
        }

        return true;
    }

    //public int GetFPS() { return mFPS; }
    //public void GetScreenDimensions(int width, int height) {
    //    mScreenWidth = width;
    //    mScreenHeight = height;
    //}
}
