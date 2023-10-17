package a0101165.mgd.treasureahoy;

public class PlayerEntity extends BaseEntity{

    LaunchEntity mAttachedLaunchObject;

    boolean mNotMoving;

    public PlayerEntity(int numFrames, int frameNumber, int xPos, int yPos) {
        super(numFrames, frameNumber, xPos, yPos);

        mNotMoving = false;
        mAttachedLaunchObject = null;
    }
}
