package wxc.android.androiddemos.widget;

import wxc.android.androiddemos.R;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 左滑显示删除按钮，显示时任何touch事件均视为消除删除按钮
 * 实现方式：设置OnTouchListener，手动检测DOWN、UP、MOVE三个Touch事件。
 * 
 * @author WXC
 *
 */
public class QQListView2 extends ListView implements View.OnTouchListener  {
	private static final String TAG = "QQListView2";
	// 计算滑动速度
	private VelocityTracker mVelocityTracker;
	// 记录滑动开始的第一个点
	private PointF mFirstPoint = new PointF();
	// 控制滑动是否完成
	private boolean mIsFling = false;
	// 控制删除按钮显示
	private boolean mIsShown = false;
	// 删除按钮
	private Button mDeleteBtn;
	// 需要显示按钮的View
	private ViewGroup mViewGroup;

	public QQListView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnTouchListener(this);
		initDeleteView();
	}
	
	private void initDeleteView () {
		mDeleteBtn = (Button) LayoutInflater.from(getContext()).inflate(R.layout.item_delete_button, null);
		mDeleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "delete button clicked!", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public QQListView2(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.listViewStyle);
	}

	public QQListView2(Context context) {
		this(context, null);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean isHandle = false;
		int action = MotionEventCompat.getActionMasked(event);
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mVelocityTracker.clear();
			if (mIsShown) {
				hideDeleteBtn();
			} else {
				mFirstPoint.x = event.getX();
				mFirstPoint.y = event.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.computeCurrentVelocity(1000);
			Log.d(TAG, "XVelocity:" + mVelocityTracker.getXVelocity()
					+ ", YVelocity:" + mVelocityTracker.getYVelocity());
			if (!mIsFling && !mIsShown 
					// Y轴不能滑动太快
					&& Math.abs(mVelocityTracker.getYVelocity()) < 500) {
				int firstPos = pointToPosition((int) mFirstPoint.x, (int) mFirstPoint.y);
				float curX = event.getX();
				float curY = event.getY();
				int curPos = pointToPosition((int) curX, (int) curY);
				int dp5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
						5, getContext().getResources().getDisplayMetrics());
				int dp3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
						5, getContext().getResources().getDisplayMetrics());
				// 显示条件：同一个列表项，x轴必须滑动了一定的距离，y轴滑动距离不能太大
				// 加上之前的y轴的滑动速度不能太大，一共四个条件
				if (firstPos == curPos && mFirstPoint.x - curX > dp5 && Math.abs(mFirstPoint.y - curY) < dp3) {
					onSlide(firstPos);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			// 当手指抬起后，才认为显示button或消除button结束
			// 防止当动画结束后，手指未离开屏幕从而触发ListView的其他touch行为，如：滚动等
			if (mIsShown && !mIsFling)  {
				mIsShown = false;
			}
			// 当mIsFling为true时，表示触发显示button动画，
			// 但手指还没有离开屏幕，这时ListView不能表现任何touch行为，如：滚动等
			if (mIsFling) {
				mIsFling = false;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		
		if (mIsFling || mIsShown) {
			isHandle = true;
		}
		return isHandle;
	}
	
	private void onSlide(int position) {
		mIsFling = true;
		showDeleteBtn(position);
	}

	private void showDeleteBtn(int position) {
		mIsShown = true;
		int firstVisiblePos = getFirstVisiblePosition();
		mViewGroup = (ViewGroup) getChildAt(position - firstVisiblePos);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mViewGroup.addView(mDeleteBtn, params);
		Toast.makeText(getContext(), "show", Toast.LENGTH_SHORT).show();
	}
	
	private void hideDeleteBtn() {
		if (mViewGroup !=null ) {
			mViewGroup.removeView(mDeleteBtn);
		}
		Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mVelocityTracker != null)  {
			mVelocityTracker.recycle();
		}
	}
}
