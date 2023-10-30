package a0101165.mgd.treasureahoy;

import android.util.Log;

public class BackgroundEntity extends BaseEntity{

    int mScreenHeight;

    public BackgroundEntity(int numFrames, int frameNumber, int xPos, int yPos, int screenHeight){
        super(numFrames, frameNumber, xPos, yPos);
        mScreenHeight = screenHeight;
        Log.d("LOG", Integer.toString(screenHeight));
        mWrapScreen = true;
        mYPos = -mScreenHeight; // y and x for background should always be 0
        mXPos = 0;
    }

    @Override
    public void Update() {
        super.Update();
        // move background
        mYPos += mMoveSpeed;
        // reset once it reaches end of sprite
        if (mYPos >= 0) {
            mYPos = -mScreenHeight;
        }
    }



}
