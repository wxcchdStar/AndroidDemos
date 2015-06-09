package wxc.android.androiddemos.widget;

import wxc.android.androiddemos.R;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlideView extends LinearLayout implements View.OnTouchListener {
	private Scroller mScroller;
	private int mHolderWidth;
	private int mLastX;
	private int mLastY;
	
	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mHolderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, 
				getContext().getResources().getDisplayMetrics());
		setOrientation(LinearLayout.HORIZONTAL);
		mScroller = new Scroller(getContext());
		View.inflate(getContext(), R.layout.item_slide, this);
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int scrollX = getScrollX();
		int action = MotionEventCompat.getActionMasked(event);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			if (Math.abs(deltaX) > Math.abs(deltaY) * 2) {
				int newScrollX = scrollX - deltaX;
				if (deltaX != 0) {
					if (newScrollX < 0) {
						newScrollX = 0;
					} else if (newScrollX > mHolderWidth) {
						newScrollX = mHolderWidth;
					}
					scrollTo(newScrollX, 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			int newScrollX2 = 0;
			if (scrollX - mHolderWidth * 0.75 > 0) {
				newScrollX2 = mHolderWidth;
			}
			smoothScrollTo(newScrollX2, 0);
			break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}
	
	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		}
	}

}
