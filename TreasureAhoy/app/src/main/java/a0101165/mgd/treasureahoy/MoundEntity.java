package a0101165.mgd.treasureahoy;

import android.util.Log;


enum moundState{
    buried,
    semiBuried1,
    semiBuried2,
    unburied,
    treasure,
    island
}

public class MoundEntity extends BaseEntity{

    moundState mState;

    public MoundEntity(int numFrames, int frameNumber, int xPos, int yPos, boolean hidden, int scaleX, int scaleY, moundState state){
        super(numFrames, frameNumber, xPos, yPos);
        mDrawScaleX = scaleX;
        mDrawScaleY = scaleY;
        mIsHidden = hidden;
        mState = state;
    }

    @Override
    public void Update() {
        super.Update();
        if(mState == moundState.island) mYPos += mMoveSpeed;
    }


}
