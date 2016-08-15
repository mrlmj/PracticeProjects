package com.monkeyliu.practiceproject.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.monkeyliu.practiceproject.R;

/**
 * Created by monkey on 2016/7/28.
 */

public class WhiteboardView extends View{
    private int mBoardBackground;
    private int mPaintColor;
    private int mPaintWidth;
    private Paint mPaint;
    private Path mPath;

    public WhiteboardView(Context context) {
        this(context, null);
    }

    public WhiteboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WhiteboardView);
        mBoardBackground = ta.getColor(R.styleable.WhiteboardView_boardbackground, Color.WHITE);
        mPaintColor = ta.getColor(R.styleable.WhiteboardView_paintColor, Color.RED);
        mPaintWidth = ta.getDimensionPixelSize(R.styleable.WhiteboardView_paintWidth,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        ta.recycle();
        setBackgroundColor(mBoardBackground);
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(touchX, touchY);
                break;

        }
        invalidate();
        return true;
    }
}
