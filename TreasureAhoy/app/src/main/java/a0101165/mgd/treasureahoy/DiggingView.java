package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

enum moundState{
    buried,
    semiBuried1,
    semiBuried2,
    unburied,
    treasure
}

public class DiggingView extends SurfaceView implements Runnable{

    Thread mDiggingLoopThread = null;
    SurfaceHolder mHolder; // used for locking thread during drawing
    volatile boolean mPlayingDig; // used for stopping thread during onPause and onResume functions
    Paint mPaint;

    Canvas mCanvas;

    BackgroundEntity mBackground;

    long mLastFrameTime;
    int mFPS;

    int mScreenWidth;
    int mScreenHeight;

    moundState mState;

    ArrayList<BaseEntity> mSprites;

    Context mContext;

    public DiggingView(Context context, int screenWidth, int screenHeight){
        super(context);
        mContext = context;
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mBackground = new BackgroundEntity(1, 0, 0, 0, 0);
        mBackground.mMoveSpeed = 0;

        mState = moundState.buried;

        mSprites = new ArrayList<BaseEntity>();

        if (!mBackground.LoadSprite("digging_background.png", getResources())) {
            Log.d("ERROR", "Could not load background sprite");
        }

        int moundScaleX = 500;
        int moundScaleY = 500;

        // load mound sprites
        MoundEntity tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.buried);

        if (!tempSprite.LoadSprite("mound_buried.png", getResources())) {
            Log.d("ERROR", "Could not load mound buried sprite");
        }

        mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.semiBuried1);

        if (!tempSprite.LoadSprite("mound_semiburied_1.png", getResources())) {
            Log.d("ERROR", "Could not load mound buried sprite");
        }

        mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.semiBuried2);

        if (!tempSprite.LoadSprite("mound_semiburied_2.png", getResources())) {
            Log.d("ERROR", "Could not load mound buried sprite");
        }

        mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.unburied);

        if (!tempSprite.LoadSprite("mound_unburied.png", getResources())) {
            Log.d("ERROR", "Could not load mound buried sprite");
        }

        mSprites.add(tempSprite);

        tempSprite = new MoundEntity(1, 0, mScreenWidth / 7, mScreenHeight - (int)(mScreenHeight / 2.5),
                true, moundScaleX, moundScaleY, moundState.treasure);

        if (!tempSprite.LoadSprite("mound_treasure.png", getResources())) {
            Log.d("ERROR", "Could not load mound buried sprite");
        }

        mSprites.add(tempSprite);
    }

    @Override
    public void run() {
        while(mPlayingDig){
            Update();
            Draw();
            ControlFPS();
        }
    }

    public void Update() {
        // update current mound
        for(int i = 0; i < mSprites.size(); i++) {
            if((MoundEntity)(mSprites.get(i)) != null && ((MoundEntity)(mSprites.get(i))).mState == mState){
                ((MoundEntity)(mSprites.get(i))).mIsHidden = false;
            }
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
            Log.e("Thread error", "Thread could not join");
        }
    }

    public void Resume() {
        mPlayingDig = true;
        mDiggingLoopThread = new Thread(this);
        mDiggingLoopThread.start();
    }
}
