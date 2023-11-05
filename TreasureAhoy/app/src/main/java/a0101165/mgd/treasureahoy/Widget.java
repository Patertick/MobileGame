package a0101165.mgd.treasureahoy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Widget extends BaseEntity{

    String mText;
    boolean mInteractable;
    int mTextScale;
    int mWidgetWidth;
    int mWidgetHeight;

    public Widget(int numFrames, int frameNumber, int xPos, int yPos, String widgetText, boolean interactable, int textScale) {
        super(numFrames, frameNumber, xPos, yPos);
        mWidgetWidth = 0;
        mWidgetHeight = 0;
        mText = widgetText;
        mInteractable = interactable;
        mTextScale = textScale;
    }

    @Override
    public void Draw(Canvas canvas, Paint paint, int screenWidth, int screenHeight){

        paint.setTextSize(mTextScale);
        Rect bounds = new Rect();
        paint.getTextBounds(mText, 0, mText.length(), bounds);
        if(mInteractable){
            // if interactable, draw a background and set a widget height and width
            paint.setColor(Color.argb(255, 255, 255, 255)); // white background
            canvas.drawRect(mXPos, mYPos - (int)(mTextScale / 1.5), mXPos + bounds.right, mYPos, paint);
            mWidgetWidth = bounds.right;
            mWidgetHeight = (int)(mTextScale / 1.5);
        }
        paint.setColor(Color.argb(255, 0, 0, 0)); // black text
        canvas.drawText(mText, mXPos, mYPos, paint);

    }
}
