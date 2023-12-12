package a0101165.mgd.treasureahoy;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

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

    Location mCurrentLocation;

    SoundPool mSoundPool;

    int mSoundArgh = -1;
    int mSoundBell = -1;
    int mSoundIce = -1;
    int mSoundWood = -1;
    int mSoundOcean = -1;
    boolean mRestartFlag;

    // functions
    public GameView(Context context, int screenWidth, int screenHeight, int newDistance) {
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

        mRestartFlag = false;

        // Sound

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try{
            AssetManager assetManager = mContext.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("bell.mp3");
            mSoundBell = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("ice_break.mp3");
            mSoundIce = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("ocean_move.mp3");
            mSoundOcean = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("pirate_argh.mp3");
            mSoundArgh = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("wood_break.mp3");
            mSoundWood = mSoundPool.load(descriptor, 0);

        } catch(IOException e) {
            Log.e("ERROR", "Sound could not load");
        }

        // add entities

        // background should be first in array list as it is behind every other entity

        BackgroundEntity tempBackground = new BackgroundEntity(1, 0,
                0, 0, mScreenHeight);
        if (!tempBackground.LoadSprite("water_moving_background.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load background sprite");
        } else mEntities.add(tempBackground);

        LaunchEntity tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark big sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark big sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark big sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark big sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("shark_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load shark big sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_small.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);

        tempLaunch = new LaunchEntity(1, 0, 0, -100, mScreenWidth, mScreenHeight);

        if (!tempLaunch.LoadSprite("obstacle_big.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load obstacle small sprite");
        } else mEntities.add(tempLaunch);


        MoundEntity island = new MoundEntity(1, 0, 0, -mTreasureDistance,
                false, 0, 0, moundState.island, mScreenHeight);

        Random randLoc = new Random();
        island.mYPos -= randLoc.nextInt(2000);
        island.mXPos = randLoc.nextInt(mScreenWidth - island.mFrameWidth - island.mFrameWidth) + island.mFrameWidth; // random x position


        if(!island.LoadSprite("island.png", getResources())){
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load island sprite");
        } else mEntities.add(island);

        // we want player to be last in list to it is drawn on top of every other entity



        mPlayer = new PlayerEntity(1, 0, mScreenWidth / 2, mScreenHeight - (mScreenHeight/6));

        if (!mPlayer.LoadSprite("pirate_ship.png", getResources())) {
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Sprite did not load", duration);
            Log.d("ERROR", "Could not load player sprite");
        }

        mPlayer.SetScreenWidth(mScreenWidth);

        mPlayer.mDistanceTravelled = newDistance;

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
        if(mRestartFlag) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("Key", "Back To Menu");
            mContext.startActivity(intent);
        }
    }

    public void DiggingMiniGameStart() {
        Intent intent = new Intent(mContext, DiggingActivity.class);
        intent.putExtra("Key", mPlayer.mDistanceTravelled);
        mContext.startActivity(intent);
    }



    public void Update() {
        if(mPlayer.mState == State.IslandReached) DiggingMiniGameStart();
        else if(mPlayer.mState == State.Dead) {
            Dead(); // return to menu if player is dead
            return;
        }
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
            if(mEntities.get(i).CheckForPlayerCollision(mPlayer, mSoundPool, mSoundWood, mSoundIce, mSoundBell)){
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
            }
        }

    }

    public void Draw() {
        if(mHolder.getSurface().isValid()){
            mPaint.setAlpha(255);
            mCanvas = mHolder.lockCanvas();
            if(mPlayer.mState == State.Dead){
                mCanvas.drawColor(Color.BLACK);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(50);
                mCanvas.drawText("Game over!", ((int)(mScreenWidth/2.5)), (mScreenHeight/8), mPaint);
                mPaint.setTextSize(50);
                mCanvas.drawText("Distance travelled = " + Float.toString(mPlayer.mDistanceTravelled), (mScreenWidth/4), (mScreenHeight/4), mPaint);
                mPaint.setTextSize(50);
                mCanvas.drawText("Touch anywhere on the screen to reset", (mScreenWidth/10), (mScreenHeight/2), mPaint);
            }
            else {
                mCanvas.drawColor(Color.BLACK); // draw background
                // draw the entities
                for (int i = 0; i < mEntities.size(); i++) {
                    mEntities.get(i).Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);
                }

                mPlayer.Draw(mCanvas, mPaint, mScreenWidth, mScreenHeight);

                mPaint.setColor(ComputeLocation());
                mPaint.setAlpha(100);
                Rect screen = new Rect(0, 0, mScreenWidth, mScreenHeight);

                mCanvas.drawRect(screen, mPaint);



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
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(mContext, "Fatal Error", duration);
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
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(mContext, "Fatal Error", duration);
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
                if(mPlayer.mState == State.Dead){
                    mRestartFlag = true;
                    break;
                }

                if(mPlayer.mAttachedLaunchObject != null) {
                    mSoundPool.play(mSoundOcean, 1, 1, 0, 0, 1.0f);
                    mSoundPool.play(mSoundArgh, 1, 1, 0, 0, 1.0f);
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

    public void UpdateLocation(Location newLocation) {
        if(newLocation == null) return;
        mCurrentLocation = newLocation;
    }

    public int ComputeLocation(){
        double timeDifference = mCurrentLocation.getLongitude() / 15;

        if(timeDifference < -12) timeDifference += 12;
        else if(timeDifference > 12) timeDifference -= 12;

        double localStandardTime = (-0.0069 + (mCurrentLocation.getLatitude() * 0.00007)) % 24;



        Date dt = new Date();

        double currentHour = dt.getHours() + localStandardTime + timeDifference;

        Log.e("Current Hour", Double.toString(currentHour));

        // from fully dark to fully day (0 - 255)
        if(currentHour > 0.0 && currentHour <= 12.0) {
            double currentColor = 0 + (255 * (currentHour / 12));
            if(currentColor >= 0.0 && currentColor <= 255.0) return Color.argb(255, (int)(currentColor), (int)(currentColor), (int)(currentColor));
        }
        // from fully day to fully dark (255 - 0)
        else if(currentHour > 12.0 && currentHour <= 24.0) {
            double currentColor = 255 - (255 * ((currentHour - 12) / 12));
            if(currentColor >= 0.0 && currentColor <= 255.0) return Color.argb(255, (int)(currentColor), (int)(currentColor), (int)(currentColor));
        }

        return Color.argb(0, 255, 255, 255); // return white with 0 alpha by default
    }

    //public int GetFPS() { return mFPS; }
    //public void GetScreenDimensions(int width, int height) {
    //    mScreenWidth = width;
    //    mScreenHeight = height;
    //}
}
