package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    // variables
    Thread mGameLoopThread = null;
    SurfaceHolder mHolder; // used for locking thread during drawing
    volatile boolean mPlayingMenu; // used for stopping thread during onPause and onResume functions
    Paint mPaint;

    Canvas mCanvas;

    Context mContext;

    long mLastFrameTime;
    int mFPS;

    int mScreenWidth;
    int mScreenHeight;

    int mLaunchObjectNumber;
    int mObstacleObjectNumber;

    int mTreasureDistance;

    ArrayList<BaseEntity> mEntities;

    PlayerEntity mPlayer;

    // functions
    public GameView(Context context, int screenWidth, int screenHeight) {
        // initialize variables
        super(context);
        mContext = context;
        mHolder = getHolder();
        mPaint = new Paint();
        mLastFrameTime = System.currentTimeMillis();
        mEntities = new ArrayList<BaseEntity>();

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mLaunchObjectNumber = 4;
        mObstacleObjectNumber = 2;

        mTreasureDistance = 6000;

        // add entities

        // background should be first in array list as it is behind every other entity

        BackgroundEntity tempBackground = new BackgroundEntity(1, 0,
                0, 0, mScreenHeight);
        if (!tempBackground.LoadSprite("water_moving_background.png", getResources())) {
            Log.d("ERROR", "Could not load background sprite");
        }

        mEntities.add(tempBackground);

        LaunchEntity tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            Log.d("ERROR", "Could not load shark small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            Log.d("ERROR", "Could not load shark big sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            Log.d("ERROR", "Could not load shark big sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            Log.d("ERROR", "Could not load shark big sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            Log.d("ERROR", "Could not load shark big sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            Log.d("ERROR", "Could not load shark big sprite");
        }

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_big.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_big.png", getResources())) {
            Log.d("ERROR", "Could not load obstacle small sprite");
        }

        mEntities.add(tempLaunch);


        MoundEntity island = new MoundEntity(1, 0, 0, -mTreasureDistance,
                false, 0, 0, moundState.island);

        if(!island.LoadSprite("island.png", getResources())){
            Log.d("ERROR", "Could not load island sprite");
        }

        Random randXLoc = new Random();
        island.mXPos = randXLoc.nextInt(mScreenWidth - island.mFrameWidth - island.mFrameWidth) + island.mFrameWidth; // random x position

        mEntities.add(island);

        // we want player to be last in list to it is drawn on top of every other entity



        mPlayer = new PlayerEntity(1, 0, mScreenWidth, mScreenHeight / 2);

        if (!mPlayer.LoadSprite("pirate_ship.png", getResources())) {
            Log.d("ERROR", "Could not load player sprite");
        }

        mPlayer.SetScreenWidth(mScreenWidth);

        //mEntities.add(mPlayer);

        for (int i = 0; i < mEntities.size(); i++) {
            // enforce that only a certain amount of launch objects are visible at once
            // set all launch & obstacle entities to hidden
            if (mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark
                    || mEntities.get(i).mType == type.bigObstacle || mEntities.get(i).mType == type.smallObstacle) {
                mEntities.get(i).mIsHidden = true;
            }

        }
        // set objects to be visible
        int numObjectsVisible = 0;
        while (numObjectsVisible < mLaunchObjectNumber) {
            Random randInt = new Random();
            int i = randInt.nextInt(mEntities.size()); // get random entity
            if ((mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark) &&
                    mEntities.get(i).mIsHidden) {
                // if entity is valid set to visible
                mEntities.get(i).mIsHidden = false; // set to be visible
                numObjectsVisible++; // increment value
                if (mPlayer.mAttachedLaunchObject == null) {
                    mPlayer.mAttachedLaunchObject = (LaunchEntity) (mEntities.get(i));
                    if (mEntities.get(i).mType == type.bigShark) mPlayer.mRotateIncrement = 25.0f;
                    else if (mEntities.get(i).mType == type.smallShark)
                        mPlayer.mRotateIncrement = 10.0f;
                    ((LaunchEntity) (mEntities.get(i))).mHasCollided = true;
                    mEntities.get(i).mXPos = mPlayer.mXPos;
                    mEntities.get(i).mYPos = mPlayer.mYPos;
                    mPlayer.mState = State.Attached;
                    mPlayer.SetVelocity(0);
                }

            }
        }
        numObjectsVisible = 0;
        while (numObjectsVisible < mObstacleObjectNumber) {
            Random randInt = new Random();
            int i = randInt.nextInt(mEntities.size()); // get random entity
            if ((mEntities.get(i).mType == type.bigObstacle || mEntities.get(i).mType == type.smallObstacle) &&
                    mEntities.get(i).mIsHidden) {
                // if entity is valid set to visible
                mEntities.get(i).mIsHidden = false; // set to be visible
                numObjectsVisible++; // increment value
                Log.e("Objects visible", Integer.toString(numObjectsVisible));
            }
        }
        for (int i = 0; i < mEntities.size(); i++) {
            if ((mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark) &&
                    mEntities.get(i).mIsHidden) {
                // set all hidden objects to top of screen
                mEntities.get(i).mYPos = 0;
            } else if ((mEntities.get(i).mType == type.smallObstacle || mEntities.get(i).mType == type.bigObstacle) &&
                    mEntities.get(i).mIsHidden) {
                mEntities.get(i).mYPos = 0;
            }

        }
    }

    @Override
    public void run() {
        while(mPlayingMenu){
            Update();
            Draw();
            ControlFPS();
        }
    }

    public void Dead() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("Key", "Back to menu");
        mContext.startActivity(intent);
    }

    public void DiggingMiniGameStart() {
        Intent intent = new Intent(mContext, DiggingActivity.class);
        intent.putExtra("Key", "Digging mini game start");
        mContext.startActivity(intent);
    }



    public void Update() {
        if(mPlayer.mState == State.IslandReached) DiggingMiniGameStart();
        if(mPlayer.mState == State.Dead) Dead(); // return to menu if player is dead
        // simulation
        // run update functions of all saved entities

        // do player update first
        int numLaunchObjectsVisible = 0;
        int numObstacleObjectsVisible = 0;
        mPlayer.Update();

        // check for dead

        // entity loop
        for(int i = 0; i < mEntities.size(); i++) {
            if(mEntities.get(i) == mPlayer || mEntities.get(i).mIsHidden) continue;

            if(mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark) numLaunchObjectsVisible++;
            if(mEntities.get(i).mType == type.bigObstacle || mEntities.get(i).mType == type.smallObstacle) numObstacleObjectsVisible++;

            // if player moveSpeed is minus or 0, set all other move speeds to 0
            if(mPlayer.GetVelocityY() <= 0) mEntities.get(i).SetMoveSpeed(0);
            else mEntities.get(i).SetMoveSpeed((int)(mPlayer.GetVelocityY()));

            mEntities.get(i).Update();
            // check for player collision
            if(mEntities.get(i).CheckForPlayerCollision(mPlayer)){
                // we hit an object
            }
        }


        // if our current visible objects falls below set amount, find new random entity to put on screen
        while(numLaunchObjectsVisible < mLaunchObjectNumber) {
            Random randInt = new Random();
            int i = randInt.nextInt(mEntities.size()); // get random entity
            if ((mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark) &&
                    mEntities.get(i).mIsHidden) {
                // if entity is valid set to visible
                mEntities.get(i).mIsHidden = false; // set to be visible
                numLaunchObjectsVisible++; // increment value
            }
        }

        while(numObstacleObjectsVisible < mObstacleObjectNumber) {
            Random randInt = new Random();
            int i = randInt.nextInt(mEntities.size()); // get random entity
            if ((mEntities.get(i).mType == type.bigObstacle || mEntities.get(i).mType == type.smallObstacle) &&
                    mEntities.get(i).mIsHidden) {
                // if entity is valid set to visible
                mEntities.get(i).mIsHidden = false; // set to be visible
                numObstacleObjectsVisible++; // increment value
                Log.e("Objects visible", Integer.toString(numObstacleObjectsVisible));
            }
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

            mPlayer.Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);

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
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                if(mPlayer.mAttachedLaunchObject != null) {
                    if(mPlayer.mAttachedLaunchObject.mType == type.bigShark) mPlayer.SetVelocity(450);
                    else if(mPlayer.mAttachedLaunchObject.mType == type.smallShark) mPlayer.SetVelocity(250);

                    mPlayer.SetState(State.Moving);
                    for (int i = 0; i < mEntities.size(); i++) {;
                        mEntities.get(i).SetMoveSpeed(mPlayer.GetVelocityY());

                        if((mEntities.get(i).mType == type.bigShark || mEntities.get(i).mType == type.smallShark) &&
                                mEntities.get(i) != mPlayer.mAttachedLaunchObject) ((LaunchEntity) mEntities.get(i)).mHasCollided = false;
                    }
                    mPlayer.mAttachedLaunchObject = null; // when launching reset attached object
                }
                break;
        }
        return true;
    }

    //public int GetFPS() { return mFPS; }
    //public void GetScreenDimensions(int width, int height) {
    //    mScreenWidth = width;
    //    mScreenHeight = height;
    //}
}
