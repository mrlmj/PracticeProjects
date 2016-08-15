package com.monkeyliu.zoomimageview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by monkey on 2016/8/3.
 */

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener{

    private float mInitScale;
    private float mMidScale;
    private float mMaxScale;

    private boolean mOnce;

    private Matrix mScaleMatrix;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaleMatrix = new Matrix();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
db adb


        adbdsasda



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {

        if(!mOnce) {
            int width = getWidth();
            int height = getHeight();

            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }

            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            //计算scale
            float scale = 1.0f;
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
                Log.v("seewo", "scale:" + scale + ",width:" + width + ",dw:" + dw);
            }

            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }

            if ((dh > height && dw > width) || (dh < height && dw < width)) {
                scale = Math.min(height * 1.0f / dh, width * 1.0f / dw);
            }

            int offsetX = (width - dw) / 2;
            int offsetY = (height - dh) / 2;

            mInitScale = scale;
            mMidScale = 2 * scale;
            mMidScale = 4 * scale;

            mScaleMatrix.reset();
            mScaleMatrix.postTranslate(offsetX, offsetY);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);
            mOnce = true;
        }

    }
}
