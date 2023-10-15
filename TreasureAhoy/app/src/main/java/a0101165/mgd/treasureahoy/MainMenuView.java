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

    ArrayList<BaseEntity> mEntities;

    // functions
    public MainMenuView(Context context){
        // initialize variables
        super(context);
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();
        mEntities = new ArrayList<BaseEntity>();

        // add a base entity
        //Bitmap bitmap;
        //BaseEntity temp = new BaseEntity(32, 32, 1, 0,
        //        null, 64, 64);
        //mEntities.add(temp);
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
                mEntities.get(i).Draw();
            }

            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void ControlFPS() {
        long timeThisFrame = (System.currentTimeMillis() - mLastFrameTime);
        long timeToSleep = 500 - timeThisFrame; // time to pause between each frame
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
        return true;
    }

    public int GetFPS() { return mFPS; }
    //public void SetCanvas(Canvas canvas) { mCanvas = canvas; }
}
