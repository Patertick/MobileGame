package a0101165.mgd.treasureahoy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.util.Log;

import java.util.Random;

enum State {
    Start,
    Attached,
    Moving,
    Dead
}

public class PlayerEntity extends BaseEntity{

    LaunchEntity mAttachedLaunchObject;

    int mScreenWidth;

    boolean mNotMoving;

    State mState;

    float mRotateAngle;
    float mRotateIncrement;

    int mOriginX;
    int mOriginY;
    int mXDrawOffset;
    int mYDrawOffset;
    int mStartY;

    int mVelocityX;
    int mVelocityY;

    Bitmap mRotMap;

    public PlayerEntity(int numFrames, int frameNumber, int xPos, int yPos) {
        super(numFrames, frameNumber, xPos, yPos);

        mNotMoving = false;
        mAttachedLaunchObject = null;
        mState = State.Start;
        mOriginX = xPos;
        mOriginY = yPos;
        mXDrawOffset = 0;
        mYDrawOffset = 0;
        mStartY = yPos;

        mVelocityX = 0;
        mVelocityY = 0;

        mRotateAngle = 0.0f;

        SetVelocity(50);

        mRotateIncrement = 10.0f;
    }

    @Override
    public void Draw(Canvas canvas, Paint paint, int screenWidth, int screenHeight){
        if(mRotMap == null) return;
        Rect destRect;
        destRect = new Rect(mXPos + mXDrawOffset, mYPos + mYDrawOffset, mXPos + mFrameWidth + mXDrawOffset,
                mYPos + mFrameHeight + mYDrawOffset);
        canvas.drawBitmap(mRotMap, mRectToBeDrawn, destRect, paint);
    }

    @Override
    public void Update(){
        mOriginX = mXPos;
        mOriginY = mYPos;

        mRotMap = mBitmap;


        // Create matrix and rotate around origin by angle amount
        Matrix rotMat = new Matrix();

        float xPivot = (float) (mFrameWidth) / 2;

        float yPivot = (float) (mFrameHeight) / 2;

        rotMat.postRotate(mRotateAngle, xPivot, yPivot);

        double angleInRads = mRotateAngle * (Math.PI / 180.0f);

        double rotOffset = 32.0f * Math.abs(Math.sin(angleInRads*2));

        mXDrawOffset = (int) -rotOffset;
        mYDrawOffset = (int) -rotOffset;

        mRotMap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), rotMat, true);

        if(mState == State.Attached && mAttachedLaunchObject != null) { // if attached to a valid object
            mAttachedLaunchObject.mYPos = mStartY;

            // Rotate around attached object, turn off collision, stop moving
            mNotMoving = true;


            // Set origin to centre of attached object
            mOriginX = mAttachedLaunchObject.mXPos;
            mOriginY = mAttachedLaunchObject.mYPos;

            // trace circle with defined radius
            float radius = 200.0f;

            // trace circle using sin and cos for offset values
            float xOffset = (float) (Math.sin(-angleInRads + 90.0f)) * radius;

            float yOffset = (float) (Math.cos(-angleInRads + 90.0f)) * radius;

            mXPos = mOriginX - (int) xOffset;
            mYPos = mOriginY - (int) yOffset;

            mRotateAngle += mRotateIncrement;

            if(mRotateAngle > 360.0f)
            {
                mRotateAngle = mRotateAngle - 360.0f;
            }

        }
        else if (mState == State.Moving){

            if(mVelocityY < 0) {
                mMoveSpeed = 0; // stop movement for other object
                mYPos -= mVelocityY; // if moving down, do not move objects to follow player

                if(mYPos < 0) mState = State.Dead;
            }

            mXPos += mVelocityX;
            if(mXPos + mFrameWidth > mScreenWidth) {
                mXPos = mScreenWidth - mFrameWidth;
                mVelocityX = -mVelocityX; // reverse velocity
                mRotateAngle = FindNewAngle(mVelocityX, mVelocityY);
            } else if(mXPos < 0) {
                mXPos = 0;
                mVelocityX = -mVelocityX;
                mRotateAngle = FindNewAngle(mVelocityX, mVelocityY);
            }

            mVelocityX = (int)(mVelocityX * 0.95); // not really deceleration but simulates it decently
            mVelocityY = (int)(mVelocityY * 0.95);

            if(mVelocityX == 0 && mVelocityY == 0) mState = State.Dead;// if velocity is 0 and ship is in state moving set state to dead

        }
        else if (mState == State.Start){
            Random randInt = new Random();
            SetVelocity(500);
            mState = State.Moving;
        }
    }

    public void SetVelocity(int newSpeed) {
        double angleInRads = (mRotateAngle) * (Math.PI / 180.0f);
        mVelocityX = (int) (newSpeed * Math.sin(angleInRads));
        mVelocityY = (int) (newSpeed * Math.cos(angleInRads));
    }

    private int FindNewAngle(int velX, int velY){
        double velocityMagnitude = Math.sqrt((velX * velX) + (velY * velY));
        double newAngle = Math.asin(mVelocityX / velocityMagnitude); // x or y should be viable but since x changes velocities more, use x
        newAngle = newAngle * (180.0f / Math.PI); // convert to degrees
        if(mVelocityY < 0) newAngle = -newAngle + 180.0f;
        return (int) newAngle;
    }

    public int GetVelocityY() { return mVelocityY; }

    public void SetScreenWidth(int screenWidth) { mScreenWidth = screenWidth; }

    public void SetState(State newState) { mState = newState; }
}

