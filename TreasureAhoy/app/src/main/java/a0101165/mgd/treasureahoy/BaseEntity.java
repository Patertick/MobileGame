package a0101165.mgd.treasureahoy;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


enum type{
    bigShark,
    smallShark,
    smallObstacle,
    bigObstacle;
}

// parent class that all entities in game world are derived from
public class BaseEntity {

    Bitmap mBitmap; // bitmap of entity to be drawn

    Rect mRectToBeDrawn; // the portion of the bitmap to be drawn in current frame

    // dimensions of a single frame
    int mFrameHeight;
    int mFrameWidth;
    int mNumFrames;
    int mFrameNumber;
    int mDrawScaleX;
    int mDrawScaleY;

    int mXPos;
    int mYPos;
    int mMoveSpeed;

    boolean mWrapScreen;
    boolean mIsHidden;

    type mType;

    public BaseEntity(int numFrames, int frameNumber, int xPos, int yPos) {
        // set entities values
        mFrameNumber = frameNumber;
        mNumFrames = numFrames;
        mXPos = xPos;
        mYPos = yPos;
        mIsHidden = false;
        mMoveSpeed = 0;
        mDrawScaleX = 0;
        mDrawScaleY = 0;

    }

    public boolean LoadSprite(String fileName, Resources res) {
        switch(fileName){
            case "water_moving_background.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.water_moving_background);
                break;
            case "pirate_ship.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.pirate_ship);
                break;
            case "shark_small.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.shark_small);
                mType = type.smallShark;
                break;
            case "shark_big.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.shark_big);
                mType = type.bigShark;
                break;
            case "obstacle_small.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.obstacle_small);
                mType = type.smallObstacle;
                break;
            case "obstacle_big.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.obstacle_big);
                mType = type.bigObstacle;
                break;
            case "digging_background.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.digging_background);
                break;
            case "mound_buried.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.mound_buried);
                break;
            case "mound_semiburied_1.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.mound_semiburied_1);
                break;
            case "mound_semiburied_2.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.mound_semiburied_2);
                break;
            case "mound_unburied.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.mound_unburied);
                break;
            case "mound_treasure.png":
                mBitmap = BitmapFactory.decodeResource(res, R.drawable.mound_treasure);
                break;
            default:
                mBitmap = null;
                break;

        }

        if(mBitmap == null) return false; // no valid bitmap

        // set frame height and width to dimensions of passed in png
        mFrameWidth = (mBitmap.getWidth() / mNumFrames);
        mFrameHeight = mBitmap.getHeight();

        mRectToBeDrawn = new Rect((mFrameNumber * mFrameWidth) - 1,
                0, ((mFrameNumber * mFrameWidth + mFrameWidth) - 1), mFrameHeight);
        return true; // we have a valid bitmap
    }

    public void Update() {
        // do something

    }

    public void Draw(Canvas canvas, Paint paint, int screenWidth, int screenHeight) {
        // draw this entity
        // find position of rect to be drawn
        if(mBitmap == null || mIsHidden) return;
        Rect destRect;
        if(mWrapScreen) {
            destRect = new Rect(mXPos, mYPos, mXPos + screenWidth,
                    mYPos + screenHeight * 2);
        } else {
            destRect = new Rect(mXPos, mYPos, mXPos + mFrameWidth + mDrawScaleX,
                    mYPos + mFrameHeight + mDrawScaleY);
            //Log.e("Offset", Integer.toString(mXDrawOffset) + " X");
           //Log.e("Offset", Integer.toString(mYDrawOffset) + " Y");
        }
        // draw bitmap using dest rect
        canvas.drawBitmap(mBitmap, mRectToBeDrawn, destRect, paint);
    }


    public void SetMoveSpeed(int newMoveSpeed) {
        mMoveSpeed = newMoveSpeed;
    }


    public boolean CheckForPlayerCollision(PlayerEntity player) {
        // check for collision (shark x, y width = 32)
        if(player.mState == State.Attached || player.mState == State.Dead) return false; // if attached, do not check collisions (would be unfair to die whilst not in control)
        if(mXPos + mFrameWidth >= player.mXPos && mXPos <= player.mXPos + player.mFrameWidth){
            // collision is possible
            if(mYPos + mFrameHeight >= player.mYPos && mYPos <= player.mYPos + player.mFrameHeight){
                // collision has occurred
                if(!((LaunchEntity) this).mHasCollided && (mType == type.bigShark || mType == type.smallShark)) {
                    mMoveSpeed = 0;
                    player.mState = State.Attached;
                    player.SetVelocity(0);
                    if(mType == type.bigShark) player.mRotateIncrement = 25.0f;
                    else if(mType == type.smallShark) player.mRotateIncrement = 10.0f;

                    player.mAttachedLaunchObject = (LaunchEntity) this; // give player access to this object
                    ((LaunchEntity) this).mHasCollided = true;
                    return true;
                }
                else if(mType == type.smallObstacle || mType == type.bigObstacle){
                    if(mType == type.smallObstacle) {
                        // reduce velocity when hitting obstacle (small obstacle slows)
                        player.mVelocityX = (int) (player.mVelocityX * 0.5);
                        player.mVelocityY = (int) (player.mVelocityY * 0.5);
                    }
                    else{
                        player.mVelocityX = 0;
                        player.mVelocityY = 0;
                        player.mState = State.Dead;
                    }
                    mYPos = player.mScreenWidth * 2;
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }
}
