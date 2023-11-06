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

    public void IncrementState() {
        switch(mState){
            case buried:
                mState = moundState.semiBuried1;
                break;
            case semiBuried1:
                mState = moundState.semiBuried2;
                break;
            case semiBuried2:
                mState = moundState.unburied;
                break;
            case unburied:
                mState = moundState.treasure;
                break;
            case treasure:
                mState = moundState.buried;
                break;
            default:
                Log.e("ERROR", "Mound state is island on digging view!");
        }
    }
}
