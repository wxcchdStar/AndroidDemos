package wxc.android.androiddemos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

public class PathEffectDemoView extends View {
	private Paint mPaint;
	private float mPhase;
	
	public PathEffectDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PathEffectDemoView(Context context) {
		super(context);
		init();	
	}
	
	private void init() {
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#ff0099cc"));
		mPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPhase -= 1;
        
		drawArrow(canvas);
		
		drawPathTracing(canvas);
		
		drawQuadPath(canvas);
		
		invalidate();
	}
	
	private void drawArrow(Canvas canvas) {
		mPaint.setStrokeWidth(30);
		mPaint.setStyle(Paint.Style.FILL);
		// Create a straight line
		Path path = new Path();
		path.moveTo(0, 50);
		path.lineTo(getWidth(), 50);
		
		// Stamp a concave arrow along the line
		PathEffect effect = new PathDashPathEffect(
		    makeConvexArrow(48.0f, 24.0f),    // "stamp"
		    72.0f,                            // advance, or distance between two stamps
		    mPhase,                           // phase, or offset before the first stamp
		    PathDashPathEffect.Style.ROTATE); // how to transform each stamp

		// Apply the effect and draw the path
		mPaint.setPathEffect(effect);
		canvas.drawPath(path, mPaint);
	}
	
	private Path makeConvexArrow(float length, float height) {
		Path p = new Path();
	    p.moveTo(0.0f, -height / 2.0f);
	    p.lineTo(length - height / 4.0f, -height / 2.0f);
	    p.lineTo(length, 0.0f);
	    p.lineTo(length - height / 4.0f, height / 2.0f);
	    p.lineTo(0.0f, height / 2.0f);
	    p.lineTo(0.0f + height / 4.0f, 0.0f);
	    p.close();
	    return p;
	}
	
	private void drawPathTracing(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, 100);
		path.lineTo(getWidth(), 100);
		
		PathMeasure pm = new PathMeasure(path, false);
		float length = pm.getLength();

		mPaint.setStrokeWidth(15);
		mPaint.setStyle(Paint.Style.STROKE);
		// This PathEffect only affects drawing with the paint's style is set to STROKE or FILL_AND_STROKE. 
		// It is ignored if the drawing is done with style == FILL.
		PathEffect pathEffect = new DashPathEffect(new float[] {length/30, length/30, length/20, length/10}, mPhase);
		mPaint.setPathEffect(pathEffect);
		
		canvas.drawPath(path, mPaint);
	}
	
	private void drawQuadPath(Canvas canvas) {
		// Create a straight line
		Path path = new Path();
		path.moveTo(0, 200);
		path.quadTo(200, 200, 100, 300);
		
		mPaint.setStrokeWidth(15);
		mPaint.setStyle(Paint.Style.STROKE);
		
		canvas.drawPath(path, mPaint);
	}
	
}
