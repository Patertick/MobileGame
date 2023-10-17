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

    int mXPos;
    int mYPos;
    int mMoveSpeed;

    boolean mWrapScreen;
    boolean mIsMoving;
    boolean mIsHidden;

    type mType;

    public BaseEntity(int numFrames, int frameNumber, int xPos, int yPos) {
        // set entities values
        mFrameNumber = frameNumber;
        mNumFrames = numFrames;
        mXPos = xPos;
        mYPos = yPos;
        mIsMoving = true;
        mIsHidden = true;
        mMoveSpeed = 100;

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
            default:
                mBitmap = null;
                break;

        }

        if(mBitmap == null) return false; // no valid bitmap

        // set frame height and width to dimensions of passed in png
        mFrameWidth = mBitmap.getWidth() / mNumFrames;
        mFrameHeight = mBitmap.getHeight();
        mRectToBeDrawn = new Rect((mFrameNumber * mFrameWidth) - 1,
                0, (mFrameNumber * mFrameWidth + mFrameWidth) - 1, mFrameHeight);
        return true; // we have a valid bitmap
    }

    public void Update() {
        // do something
    }

    public void Draw(Canvas canvas, Paint paint, int screenWidth, int screenHeight) {
        // draw this entity
        // find position of rect to be drawn
        if(mBitmap == null) return;
        Rect destRect;
        if(mWrapScreen) {
            destRect = new Rect(mXPos, mYPos, mXPos + screenWidth,
                    mYPos + screenHeight * 2);
        } else {
            destRect = new Rect(mXPos, mYPos, mXPos + mFrameWidth,
                    mYPos + mFrameHeight);
        }
        // draw bitmap using dest rect
        canvas.drawBitmap(mBitmap, mRectToBeDrawn, destRect, paint);
    }


    public void ToggleMovement(int newMoveSpeed) {
        if(mIsMoving) {
            mIsMoving = false;
        } else {
            mIsMoving = true;
            mMoveSpeed = newMoveSpeed;
        }
    }

    public boolean CheckForPlayerCollision(PlayerEntity player) {
        // check for collision (shark x, y width = 32)
        if(mXPos + mFrameWidth >= player.mXPos && mXPos <= player.mXPos + mFrameWidth){
            // collision is possible
            if(mYPos + mFrameHeight >= player.mYPos && mYPos <= player.mYPos + mFrameHeight){
                // collision has occurred
                Log.d("Launch val", "Collision");
                player.mNotMoving = true;
                player.mAttachedLaunchObject = (LaunchEntity) this; // give player access to this object
                return true;
            }
        }
        Log.d("Launch val", "No Collision");
        return false;
    }
}
