package com.monkeyliu.loadinganimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by monkey on 2016/8/15.
 */

public class WaveLoadingView extends View{

    private static final int LOADING_PERIOD = 500;
    private int mLineCount;
    private int mLineColor;
    private int mLineBase;
    private int mLineWidth;
    private int mLineMaxHeight;
    private int mLineMinHeight;
    private int mLineMargin;
    private int mPeriod;

    private Line[] mLines;
    private Paint mPaint;
    private AnimatorSet mAnimatorSet;

    public WaveLoadingView(Context context) {
        this(context, null);
    }

    public WaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveLoadingView);
        mLineCount = ta.getInt(R.styleable.WaveLoadingView_lineCount, 4);
        mLineColor = ta.getColor(R.styleable.WaveLoadingView_lineColor, Color.BLUE);
        //基于哪个line开始动画
        mLineBase  = ta.getInt(R.styleable.WaveLoadingView_lineBase, 0);
        mPeriod = ta.getInt(R.styleable.WaveLoadingView_period, LOADING_PERIOD);
        mLineWidth = ta.getDimensionPixelSize(R.styleable.WaveLoadingView_lineWidth,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()));
        mLineMinHeight = ta.getDimensionPixelSize(R.styleable.WaveLoadingView_lineMinHeight,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics()));
        mLineMaxHeight = ta.getDimensionPixelSize(R.styleable.WaveLoadingView_lineMaxHeight,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics()));
        mLineMargin = ta.getDimensionPixelSize(R.styleable.WaveLoadingView_lineMargin,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        ta.recycle();
        initComponent();
    }

    private void initComponent() {
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);

        mLines = new Line[mLineCount];
        mAnimatorSet = new AnimatorSet();
        int base = 0;
        if (mLineBase == 0) {
            base = 0;
        } else if (mLineBase == 1) {
            base = mLineCount / 2;
        } else if (mLineBase == 2){
            base = mLineCount - 1;
        }
        for(int i = 0; i < mLineCount; i++){
            mLines[i] = new Line(mLineWidth, mLineMinHeight);
            ObjectAnimator lineAnimator = createLineAnimator(mLines[i], Math.abs(i - base) * mPeriod / mLineCount);
            if(i == base){
                lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        invalidate();
                    }
                });
            }
            mAnimatorSet.playTogether(lineAnimator);
        }

    }

    public void start(){
        mAnimatorSet.start();
    }

    public void stop(){
        mAnimatorSet.cancel();
    }

    private ObjectAnimator createLineAnimator(Line line, long delayTime){
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(line, "height", mLineMinHeight, mLineMaxHeight);
        objectAnimator.setDuration(mPeriod);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setStartDelay(delayTime);
        return objectAnimator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            width = Math.min((mLineMargin + mLineWidth) * mLineCount + mLineMargin, widthSize);
            height = Math.min(mLineMaxHeight, heightSize);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min((mLineMargin + mLineWidth) * mLineCount + mLineMargin, widthSize);
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST){
            width = widthSize;
            height = Math.min(mLineMaxHeight, heightSize);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0,getHeight()/2);
        for(int i = 0; i < mLineCount; i++) {
            float left = mLineMargin + i * (mLineMargin+mLineWidth);
            float right = left + mLineWidth;
            float top = -mLines[i].getHeight()/2;
            float bottom = -top;
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
        canvas.restore();
    }

    /**
     * Loading动画中的每条线
     */
    private class Line{
        private int mWidth;
        private int mHeight;

        public Line(int width, int height){
            this.mWidth = width;
            this.mHeight = height;
        }

        public void setWidth(int width){
            this.mWidth = width;
        }

        public int getWidth(){
            return mWidth;
        }

        public void setHeight(int height){
            this.mHeight = height;
        }

        public int getHeight(){
            return mHeight;
        }
    }
}
