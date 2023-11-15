package a0101165.mgd.treasureahoy;

import android.util.Log;
import java.util.Random;



public class MoundEntity extends BaseEntity{

    moundState mState;
    int mScreenHeight;
    int mStartY;

    public MoundEntity(int numFrames, int frameNumber, int xPos, int yPos, boolean hidden, int scaleX, int scaleY, moundState state, int screenHeight){
        super(numFrames, frameNumber, xPos, yPos);
        mDrawScaleX = scaleX;
        mDrawScaleY = scaleY;
        mIsHidden = hidden;
        mState = state;
        mScreenHeight = screenHeight;
        mStartY = yPos;
    }

    @Override
    public void Update() {
        super.Update();
        if(mState == moundState.island) {
            mYPos += mMoveSpeed;
            if(mYPos > mScreenHeight) {
                Random rand = new Random();
                mYPos = mStartY - rand.nextInt(2000);
            }
        }
    }


}
