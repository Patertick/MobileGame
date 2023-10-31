package a0101165.mgd.treasureahoy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Widget extends BaseEntity{

    String mText;
    boolean mInteractable;
    int mTextScale;

    public Widget(int numFrames, int frameNumber, int xPos, int yPos, String widgetText, boolean interactable, int textScale) {
        super(numFrames, frameNumber, xPos, yPos);
        mText = widgetText;
        mInteractable = interactable;
        mTextScale = textScale;
    }

    @Override
    public void Draw(Canvas canvas, Paint paint, int screenWidth, int screenHeight){
        paint.setColor(Color.argb(255, 0, 0, 0)); // black text
        paint.setTextSize(mTextScale);
        canvas.drawText(mText, mXPos, mYPos, paint);
    }
}
