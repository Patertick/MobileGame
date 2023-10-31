package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// view to use for main menu activity
public class MainMenuView extends SurfaceView implements Runnable{
    Thread mMenuLoopThread = null;
    SurfaceHolder mHolder; // used for locking thread during drawing
    volatile boolean mPlayingMenu; // used for stopping thread during onPause and onResume functions
    Paint mPaint;

    Canvas mCanvas;

    BackgroundEntity mBackground;

    long mLastFrameTime;
    int mFPS;

    int mScreenWidth;
    int mScreenHeight;

    ArrayList<Widget> mWidgets;

    Context mContext;

    public MainMenuView(Context context, int screenWidth, int screenHeight)
    {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mBackground = new BackgroundEntity(1, 0, 0, 0, screenHeight);
        mBackground.mMoveSpeed = 10;

        if (!mBackground.LoadSprite("water_moving_background.png", getResources())) {
            Log.d("ERROR", "Could not load background sprite");
        }

        mWidgets = new ArrayList<Widget>();

        Widget tempWidget = new Widget(1, 0, mScreenWidth/4 - 50, mScreenHeight/16, "Treasure Ahoy!", false,
                100);

        mWidgets.add(tempWidget);

        tempWidget = new Widget(1, 0, mScreenWidth/2 - 100, mScreenHeight - mScreenHeight / 4, "Start Game", true,
                50);

        mWidgets.add(tempWidget);


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
        mBackground.Update();
    }

    public void Draw() {
        if (mHolder.getSurface().isValid()) {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK); // draw background
            mBackground.Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);

            // draw the widgets
            for(int i = 0; i < mWidgets.size(); i++) {
                mWidgets.get(i).Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);
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
                mMenuLoopThread.sleep(timeToSleep);
            } catch(InterruptedException e){
                Log.e("Thread error", "Thread could not sleep");
            }
        }

        mLastFrameTime = System.currentTimeMillis();
    }

    public void Pause() {
        mPlayingMenu = false;
        try {
            mMenuLoopThread.join();
        } catch(InterruptedException e){
            Log.e("Thread error", "Thread could not join");
        }
    }

    public void Resume() {
        mPlayingMenu = true;
        mMenuLoopThread = new Thread(this);
        mMenuLoopThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                // go through widgets that are interactable, if the touch event was on one of these widgets, then we want to do its function
                for(int i = 0; i < mWidgets.size(); i++) {

                    if(mWidgets.get(i).mInteractable){
                        Log.e("Touch X", Float.toString(motionEvent.getX()));
                        Log.e("Touch Y", Float.toString(motionEvent.getY()));
                        Log.e("Touch Widget X", Float.toString(mWidgets.get(i).mXPos));
                        Log.e("Touch Widget Y", Float.toString(mWidgets.get(i).mYPos));
                        if(motionEvent.getX() > mWidgets.get(i).mXPos && motionEvent.getX() < mWidgets.get(i).mXPos + (mWidgets.get(i).mTextScale * 2))
                        {
                            if(motionEvent.getY() > mWidgets.get(i).mYPos - mWidgets.get(i).mTextScale && motionEvent.getY() < mWidgets.get(i).mYPos)
                            {
                                Log.e("Touch", "Touched widget");
                                Intent intent = new Intent(mContext, GameActivity.class);
                                intent.putExtra("Key", "Game start");
                                mContext.startActivity(intent);
                            }
                        }
                    }
                }
                // start game
        }
        return true;
    }

}
