package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;



public class DiggingView extends SurfaceView implements Runnable{

    Thread mDiggingLoopThread = null;
    SurfaceHolder mHolder; // used for locking thread during drawing
    volatile boolean mPlayingDig; // used for stopping thread during onPause and onResume functions
    Paint mPaint;

    Canvas mCanvas;

    BackgroundEntity mBackground;

    int mSavedDistance;

    long mLastFrameTime;
    int mFPS;

    int mScreenWidth;
    int mScreenHeight;

    moundState mState;

    ArrayList<BaseEntity> mSprites;

    Context mContext;

    String mCurrentMoveKey;

    public DiggingView(Context context, int screenWidth, int screenHeight, int savedDistance){
        super(context);
        mContext = context;
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mBackground = new BackgroundEntity(1, 0, 0, 0, 0);
        mBackground.mMoveSpeed = 0;

        mSavedDistance = savedDistance;

        mState = moundState.buried;

        mSprites = new ArrayList<BaseEntity>();

        if (!mBackground.LoadSprite("digging_background.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load background sprite");
        }

        int moundScaleX = 500;
        int moundScaleY = 500;

        // load mound sprites
        MoundEntity tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.buried, mScreenHeight);

        if (!tempSprite.LoadSprite("mound_buried.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load mound buried sprite");
        } else mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.semiBuried1, mScreenHeight);

        if (!tempSprite.LoadSprite("mound_semiburied_1.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load mound buried sprite");
        } else mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.semiBuried2, mScreenHeight);

        if (!tempSprite.LoadSprite("mound_semiburied_2.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load mound buried sprite");
        } else mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.unburied, mScreenHeight);

        if (!tempSprite.LoadSprite("mound_unburied.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load mound buried sprite");
        } else mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.treasure, mScreenHeight);

        if (!tempSprite.LoadSprite("mound_treasure.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load mound buried sprite");
        }else mSprites.add(tempSprite);

    }

    @Override
    public void run() {
        while(mPlayingDig){
            Update();
            Draw();
            ControlFPS();
        }
    }

    public void BackToMainMenu(){
        Intent intent = new Intent(mContext, GameActivity.class);
        intent.putExtra("Key", mSavedDistance);
        mContext.startActivity(intent);
    }

    public void Update() {
        // update current mound

        for(int i = 0; i < mSprites.size(); i++) {
            if((MoundEntity)(mSprites.get(i)) != null && ((MoundEntity)(mSprites.get(i))).mState == mState){
                ((MoundEntity)(mSprites.get(i))).mIsHidden = false;
            } else ((MoundEntity)(mSprites.get(i))).mIsHidden = true;
        }



        if(mState == moundState.unburied) {
            int secondsToSleep = 1500;
            Draw();
            try {
                mDiggingLoopThread.sleep(secondsToSleep); // sleep for defined seconds
            } catch(InterruptedException e){
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(mContext, "Fatal Error", duration);
                Log.e("Thread error", "Thread could not sleep");
            }

            mState = moundState.treasure;
        }
        else if(mState == moundState.treasure)
        {
            int secondsToSleep = 2000;
            Draw();
            try {
                mDiggingLoopThread.sleep(secondsToSleep); // sleep for defined seconds
            } catch(InterruptedException e){
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(mContext, "Fatal Error", duration);
                Log.e("Thread error", "Thread could not sleep");
            }

            // may show message to screen or not
            BackToMainMenu();
        }
    }

    public void Draw() {
        if (mHolder.getSurface().isValid()) {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK); // draw background
            mBackground.Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight / 2);

            // draw the widgets
            for(int i = 0; i < mSprites.size(); i++) {
                mSprites.get(i).Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);
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
                mDiggingLoopThread.sleep(timeToSleep);
            } catch(InterruptedException e){
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(mContext, "Fatal Error", duration);
                Log.e("Thread error", "Thread could not sleep");
            }
        }

        mLastFrameTime = System.currentTimeMillis();
    }

    public void Pause() {
        mPlayingDig = false;
        try {
            mDiggingLoopThread.join();
        } catch(InterruptedException e){
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Fatal Error", duration);
            Log.e("Thread error", "Thread could not join");
        }
    }

    public void Resume() {
        mPlayingDig = true;
        mDiggingLoopThread = new Thread(this);
        mDiggingLoopThread.start();
    }

    public void SetMotion(String motionKey){
        switch(motionKey){
            case "UP":
                // digging action complete if going from downwards acceleration to upwards acceleration
                if(mCurrentMoveKey == "DOWN") IncrementState();
                mCurrentMoveKey = "UP";
                break;
            case "DOWN":
                mCurrentMoveKey = "DOWN";
                break;
            default:
                // do nothing
                mCurrentMoveKey = "";
                break;
        }
    }

    public void IncrementState() {
        switch(mState){
            case buried:
                Log.d("IncrementState", "buried to semiburied1");
                mState = moundState.semiBuried1;
                break;
            case semiBuried1:
                Log.d("IncrementState", "semiburied1 to semiburied2");
                mState = moundState.semiBuried2;
                break;
            case semiBuried2:
                Log.d("IncrementState", "semiburied2 to unburied");
                mState = moundState.unburied;
                break;
            default:
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(mContext, "Fatal Error", duration);
                Log.e("ERROR", "Mound state is island on digging view!");
        }
    }


}
