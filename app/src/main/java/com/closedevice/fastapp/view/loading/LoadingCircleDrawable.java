package com.closedevice.fastapp.view.loading;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * A drawable to draw loading
 */
public class LoadingCircleDrawable extends LoadingDrawable {
    private static final int ANGLE_ADD = 5;
    private static final int MIN_ANGLE_SWEEP = 3;
    private static final int MAX_ANGLE_SWEEP = 255;

    private RectF mBackgroundOval = new RectF();
    private RectF mForegroundOval = new RectF();

    private float mStartAngle;
    private float mSweepAngle;
    private int mAngleIncrement = 3;

    public LoadingCircleDrawable() {
        super();
    }

    public LoadingCircleDrawable(int minSize) {
        super(minSize);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int centerX = bounds.centerX();
        int centerY = bounds.centerY();

        int center = Math.min(centerX, centerY);
        int maxStrokeWidth = (int) Math.max(mForegroundPaint.getStrokeWidth(), mBackgroundPaint.getStrokeWidth());

        int areRadius = center - ((maxStrokeWidth) >> 1) - 1;
        mBackgroundOval.set(centerX - areRadius, centerY - areRadius, centerX + areRadius, centerY + areRadius);

        areRadius = center - ((maxStrokeWidth) >> 1) - 1;
        mForegroundOval.set(centerX - areRadius, centerY - areRadius, centerX + areRadius, centerY + areRadius);
    }

    @Override
    protected void onProgressChange(float progress) {
        mStartAngle = 0;
        mSweepAngle = 360 * progress;
    }

    @Override
    protected void refresh(long startTime, long curTime, long timeLong) {
        final float angle = ANGLE_ADD;
        mStartAngle += angle;

        if (mStartAngle > 360) {
            mStartAngle -= 360;
        }

        if (mSweepAngle > MAX_ANGLE_SWEEP) {
            mAngleIncrement = -mAngleIncrement;
        } else if (mSweepAngle < MIN_ANGLE_SWEEP) {
            mSweepAngle = MIN_ANGLE_SWEEP;
            return;
        } else if (mSweepAngle == MIN_ANGLE_SWEEP) {
            mAngleIncrement = -mAngleIncrement;
            getNextForegroundColor();
        }
        mSweepAngle += mAngleIncrement;
    }

    @Override
    protected void drawBackground(Canvas canvas, Paint backgroundPaint) {
        canvas.drawArc(mBackgroundOval, 0, 360, false, backgroundPaint);
    }

    @Override
    protected void drawForeground(Canvas canvas, Paint foregroundPaint) {
        canvas.drawArc(mForegroundOval, mStartAngle, -mSweepAngle, false, foregroundPaint);
    }
}