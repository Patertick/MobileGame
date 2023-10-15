package a0101165.mgd.treasureahoy;


import android.graphics.Bitmap;
import android.graphics.Rect;

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

    public BaseEntity(int frameHeight, int frameWidth, int numFrames, int frameNumber, Bitmap bitmap,
                      int xPos, int yPos) {
        // set entities values
        mBitmap = bitmap;
        mFrameNumber = frameNumber;
        mNumFrames = numFrames;
        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mXPos = xPos;
        mYPos = yPos;
    }

    public void Update() {
        // do something
    }

    public void Draw() {
        // draw this entity
    }

}
