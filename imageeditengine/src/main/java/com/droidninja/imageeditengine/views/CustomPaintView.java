package com.droidninja.imageeditengine.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.droidninja.imageeditengine.model.PaintPath;

import java.util.ArrayList;

/**
 * Created by panyi on 17/2/11.
 */

public class CustomPaintView extends View {
    private Paint mPaint;
    private Bitmap mDrawBit;
    private Paint mEraserPaint;

    private Canvas mPaintCanvas = null;

    private float lastX;
    private float lastY;
    private boolean eraser;

    private int mColor;
    private RectF bounds;

    private ArrayList<PaintPath> paths = new ArrayList<>();

    public CustomPaintView(Context context) {
        super(context);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //System.out.println("width = "+getMeasuredWidth()+"     height = "+getMeasuredHeight());
        if (mDrawBit == null) {
            generatorBit();
        }
    }

    private void generatorBit() {
        mDrawBit = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    }

    private void init(Context context) {
        mColor = Color.RED;
        bounds = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setColor(mColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15f);

        mEraserPaint = new Paint();
        mEraserPaint.setAlpha(0);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        mEraserPaint.setStrokeWidth(40);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mPaint.setColor(mColor);
    }

    public void setWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintCanvas = canvas;
        if (mDrawBit != null) {
            canvas.drawBitmap(mDrawBit, 0, 0, null);
        }

        drawPaths(canvas);
    }

    private void drawPaths(Canvas canvas) {
        for (PaintPath path : paths) {
//            if (bounds.contains(path.startX, path.startY) && bounds.contains(path.stopX, path.stopY)) {
            mPaint.setColor(path.color);
            float mx = -1;
            float my = -1;
            for (int i = 0; i < path.coordinates.size(); i++) {
                if (i > 0) {
                    mPaintCanvas.drawLine(mx, my, path.coordinates.get(i).x, path.coordinates.get(i).y, mPaint);
                }
                mx = path.coordinates.get(i).x;
                my = path.coordinates.get(i).y;
            }
//            mPaintCanvas.drawLine(path.startX, path.startY, path.stopX, path.stopY, eraser ? mEraserPaint : mPaint);
//            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ret = true;
                lastX = x;
                lastY = y;
                paths.add(new PaintPath(x, y, mColor));

                break;
            case MotionEvent.ACTION_MOVE:
                ret = true;
                if (bounds.contains(x, y) && bounds.contains(lastX, lastY)) {
                    mPaintCanvas.drawLine(lastX, lastY, x, y, eraser ? mEraserPaint : mPaint);
                }
                lastX = x;
                lastY = y;

                paths.get(paths.size() - 1).coordinates.add(new PaintPath.Coordinate(x, y));
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ret = false;
                break;
        }
        return ret;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
        mPaint.setColor(eraser ? Color.TRANSPARENT : mColor);
    }

    public Bitmap getPaintBit() {
        return mDrawBit;
    }

    public void reset() {
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }

        generatorBit();
    }

    public void setBounds(RectF bitmapRect) {
        this.bounds = bitmapRect;
    }

    public void undo() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }
}
