package a0101165.mgd.treasureahoy;

import android.util.Log;

import java.util.Random;

public class LaunchEntity extends BaseEntity{

    int mScreenHeight;
    int mScreenWidth;




    public LaunchEntity(int numFrames, int frameNumber, int xPos, int yPos, int screenWidth, int screenHeight) {
        super(numFrames, frameNumber, xPos, yPos);

        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;


        Random rand = new Random();
        mXPos = (rand.nextInt(mScreenWidth - 160)) + 80;
    }

    @Override
    public void Update(){
        if(mIsMoving) {
            mYPos += mMoveSpeed;
            if (mYPos >= mScreenHeight) {
                mYPos = 0;
                Random rand = new Random();
                mXPos = (rand.nextInt(mScreenWidth - 160)) + 80;
            }
        }
    }


}
