package com.gitonway.countzero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.gitonway.countzero.animation.TimelyEvaluator;
import com.gitonway.countzero.model.NumberUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.util.Property;

public class TimelyView extends View {

	private static final float RATIO = 1f;

	private static final float STROKE_WIDTH = 5.0f;

	private static final int TEXT_COLOR = 0xFFff5252;
	
	public volatile int DURATION = 1000;

	private volatile int from = 9;

	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;
	

	private FinishListener mFinishListener;
	
	private volatile ObjectAnimator objectAnimator = null;
	

	
	public interface FinishListener {
        public void onCountDownFinish();
    }
	
	
	private static final Property<TimelyView, float[][]> CONTROL_POINTS_PROPERTY = new Property<TimelyView, float[][]>(
			float[][].class, "controlPoints") {
		@Override
		public float[][] get(TimelyView object) {
			return object.getControlPoints();
		}

		@Override
		public void set(TimelyView object, float[][] value) {
			object.setControlPoints(value);
		}
	};
	private Paint mPaint = null;
	private Path mPath = null;
	private float[][] controlPoints = null;

	public TimelyView(Context context) {
		super(context);
		init();
	}

	public TimelyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimelyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public float[][] getControlPoints() {
		return controlPoints;
	}

	public void setControlPoints(float[][] controlPoints) {
		this.controlPoints = controlPoints;
		invalidate();
	}

	public ObjectAnimator animate(int start, int end) {
		float[][] startPoints = NumberUtils.getControlPointsFor(start);
		float[][] endPoints = NumberUtils.getControlPointsFor(end);

		return ObjectAnimator.ofObject(this, CONTROL_POINTS_PROPERTY,
				new TimelyEvaluator(), startPoints, endPoints);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (controlPoints == null)
			return;

		int length = controlPoints.length;

		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		float minDimen = height > width ? width : height;

		mPath.reset();
		mPath.moveTo(minDimen * controlPoints[0][0], minDimen
				* controlPoints[0][1]);
		for (int i = 1; i < length; i += 3) {
			mPath.cubicTo(minDimen * controlPoints[i][0], minDimen
					* controlPoints[i][1], minDimen * controlPoints[i + 1][0],
					minDimen * controlPoints[i + 1][1], minDimen
							* controlPoints[i + 2][0], minDimen
							* controlPoints[i + 2][1]);
		}
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
		int heigthWithoutPadding = height - getPaddingTop()
				- getPaddingBottom();

		int maxWidth = (int) (heigthWithoutPadding * RATIO);
		int maxHeight = (int) (widthWithoutPadding / RATIO);

		if (widthWithoutPadding > maxWidth) {
			width = maxWidth + getPaddingLeft() + getPaddingRight();
		} else {
			height = maxHeight + getPaddingTop() + getPaddingBottom();
		}

		setMeasuredDimension(width+50, height);
	}

	private void init() {
		// A new paint with the style as stroke.
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(TEXT_COLOR);
		mPaint.setStrokeWidth(STROKE_WIDTH);
		mPaint.setStyle(Paint.Style.STROKE);
		mPath = new Path();
	}

	/**
	 * start count down
	 */
	public void start() {
		
		if (!isRunning()) {
			mPlayingState = RUNNING;
			countDown();
		}
		
		
	}

	public void setFromNumber(int from) {
		this.from = from;
	}
	
	public int getFromNumber() {
		return from;
	}


	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}
	
	public void setOnFinish(FinishListener callback){
		mFinishListener=callback;
	}

	private void countDown() {
		if (from <= 0) {
			mPlayingState = STOPPED;
			mFinishListener.onCountDownFinish();
		} else {  

			objectAnimator = this.animate(from, from - 1);
			objectAnimator.setDuration(DURATION);

			ValueAnimator valueAnimator = ValueAnimator.ofInt(0, DURATION);
			valueAnimator.setDuration(DURATION);
			valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {

					if (objectAnimator != null)
						objectAnimator.setCurrentPlayTime(valueAnimator
								.getCurrentPlayTime());
					Integer currentAnimatedValue = (Integer) valueAnimator
							.getAnimatedValue();
					if (currentAnimatedValue >= DURATION) {
						from--;
						countDown();
					}
				}
			});

			valueAnimator.start();

		}
	}
}
