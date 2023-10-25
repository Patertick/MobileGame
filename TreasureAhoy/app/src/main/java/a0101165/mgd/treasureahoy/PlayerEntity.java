package a0101165.mgd.treasureahoy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.RotateDrawable;
import android.graphics.Matrix;
import android.util.Log;

import java.math.*;

enum State {
    Start,
    Attached,
    Moving,
    Dead;
}

public class PlayerEntity extends BaseEntity{

    LaunchEntity mAttachedLaunchObject;

    boolean mNotMoving;

    State mState;

    float mRotateAngle;
    float mRotateIncrement;

    int mOriginX;
    int mOriginY;

    Bitmap mRotMap;

    public PlayerEntity(int numFrames, int frameNumber, int xPos, int yPos) {
        super(numFrames, frameNumber, xPos, yPos);

        mNotMoving = false;
        mAttachedLaunchObject = null;
        mState = State.Start;
        mOriginX = xPos;
        mOriginY = yPos;

        mRotateAngle = 0.0f;
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

        if(mState == State.Attached && mAttachedLaunchObject != null) { // if attached to a valid object
            // Rotate around attached object, turn off collision, stop moving
            mNotMoving = true;


            // Set origin to centre of attached object
            mOriginX = mAttachedLaunchObject.mXPos - (mAttachedLaunchObject.mFrameWidth / 2);
            mOriginY = mAttachedLaunchObject.mYPos - (mAttachedLaunchObject.mFrameHeight / 2);

            // trace circle with defined radius
            float radius = 200.0f;

            // Create matrix and rotate around origin by angle amount
            Matrix rotMat = new Matrix();

            float xPivot = (float) (mFrameWidth) / 2;

            float yPivot = (float) (mFrameHeight) / 2;

            rotMat.postRotate(mRotateAngle, mXPos + xPivot, mYPos + yPivot);

            double angleInRads = mRotateAngle * (Math.PI / 180.0f);

            // trace circle using sin and cos for offset values
            float xOffset = (float) (Math.sin(-angleInRads + 90.0f)) * radius;

            float yOffset = (float) (Math.cos(-angleInRads + 90.0f)) * radius;

            double rotOffset = 32.0f * Math.abs(Math.sin(angleInRads*2));

            mXDrawOffset = (int) -rotOffset;
            mYDrawOffset = (int) -rotOffset;
            mXPos = mOriginX - (int) xOffset;
            mYPos = mOriginY - (int) yOffset;
            //rotMat.postTranslate(mOriginX, mOriginY + mAttachedLaunchObject.mFrameHeight * 2);

            mRotMap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), rotMat, true);


            mRotateAngle += mRotateIncrement;

            if(mRotateAngle > 360.0f)
            {
                mRotateAngle = mRotateAngle - 360.0f;
            }

        }
    }
}

