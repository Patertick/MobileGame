package a0101165.mgd.treasureahoy;

public class MoundEntity extends BaseEntity{

    moundState mState;

    public MoundEntity(int numFrames, int frameNumber, int xPos, int yPos, boolean hidden, int scaleX, int scaleY, moundState state){
        super(numFrames, frameNumber, xPos, yPos);
        mDrawScaleX = scaleX;
        mDrawScaleY = scaleY;
        mIsHidden = hidden;
        mState = state;
    }

}
