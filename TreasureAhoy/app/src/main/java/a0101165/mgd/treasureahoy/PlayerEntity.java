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
        destRect = new Rect(mXPos, mYPos, mXPos + mFrameWidth,
                mYPos + mFrameHeight);
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

            rotMat.postRotate(mRotateAngle, xPivot, yPivot);

            double angleInRads = mRotateAngle * (Math.PI / 180.0f);

            // trace circle using sin and cos for offset values
            float xOffset = (float) (Math.sin(angleInRads)) * radius;

            float yOffset = (float) (Math.cos(angleInRads)) * radius;

            double intAngle = mRotateAngle % 90.0f;



            intAngle *= 2.0f; // divide by 90 then multiply by 180 (so multiply by two)
            intAngle = intAngle * (Math.PI / 180.0f);
            intAngle = Math.sin(intAngle);

            Log.d("Angle", Double.toString(intAngle));
            if((mRotateAngle >= 45.0f && mRotateAngle <= 90.0f) || (mRotateAngle >= 135.0f && mRotateAngle <= 180.0f) ||
                    (mRotateAngle >= 225.0f && mRotateAngle <= 270.0f) || (mRotateAngle >= 315.0f && mRotateAngle <= 360.0f)){
                mXPos = mXPos + (int) (16.0f * (float)(intAngle));
                mYPos = mYPos + (int) (16.0f * (float)(intAngle));
            } else{
                mXPos = mXPos - (int) (16.0f * (float)(intAngle));
                mYPos = mYPos - (int) (16.0f * (float)(intAngle));
            }
            //mXPos = mOriginX - (int) xOffset;
            //mYPos = mOriginY - (int) yOffset;
            //rotMat.postTranslate(mOriginX, mOriginY + mAttachedLaunchObject.mFrameHeight * 2);

            mRotMap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), rotMat, true);


            mRotateAngle += mRotateIncrement;

            if(mRotateAngle >= 360.0f)
            {
                mRotateAngle = 0.0f;
            }

        }
    }
}

