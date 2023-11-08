package a0101165.mgd.treasureahoy;

import android.util.Log;

import java.util.Random;

public class LaunchEntity extends BaseEntity{

    int mScreenHeight;
    int mScreenWidth;

    boolean mHasCollided;




    public LaunchEntity(int numFrames, int frameNumber, int xPos, int yPos, int screenWidth, int screenHeight) {
        super(numFrames, frameNumber, xPos, yPos);

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mHasCollided = false;
        SpawnRandomLocation();
    }

    @Override
    public void Update(){
        super.Update();
        if(mXPos < 0) mXPos = mFrameWidth;
        if(mXPos + mFrameWidth > mScreenWidth) mXPos = mScreenWidth - mFrameWidth;
        if(mIsHidden) return;
        mYPos += mMoveSpeed;
        if (mYPos >= mScreenHeight) {
            mIsHidden = true;
            Random rand = new Random();
            mYPos = 0;
            mXPos = (rand.nextInt(mScreenWidth - mFrameWidth - mFrameWidth)) + mFrameWidth;
            mHasCollided = false;
        }

    }

    public void SpawnRandomLocation(){
        Random rand = new Random();
        mYPos = (rand.nextInt(mScreenHeight - mFrameHeight)) + mFrameHeight;
        mXPos = (rand.nextInt(mScreenWidth - mFrameWidth - mFrameWidth)) + mFrameWidth;

    }


}
