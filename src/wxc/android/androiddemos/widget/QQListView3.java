package wxc.android.androiddemos.widget;

import wxc.android.androiddemos.R;
import android.content.Context;
import android.drm.DrmStore.Action;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 左滑显示删除按钮，添加动画
 * 
 * @author WXC
 *
 */
public class QQListView3 extends ListView implements View.OnTouchListener  {
	private static final String TAG = "QQListView3";
	// 计算滑动速度
	private VelocityTracker mVelocityTracker;
	// 记录滑动开始的第一个点
	private PointF mFirstPoint = new PointF();
	// 控制滑动是否完成
	private boolean mIsFling = false;
	// 控制删除按钮显示
	private boolean mIsShown = false;
	// 动画是否播放
	private boolean mIsPlay = false;
	// 删除按钮视图
	private View mDeleteView;
	// 删除按钮
	private Button mDeleteBtn;
	// 按钮遮罩
	private ImageView mShadeView;
	// 需要显示按钮的View
	private ViewGroup mViewGroup;

	public QQListView3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnTouchListener(this);
		initDeleteView();
	}
	
	private void initDeleteView () {
		mDeleteView = LayoutInflater.from(getContext()).inflate(R.layout.item_delete_button3, null);
		mDeleteBtn = (Button) mDeleteView.findViewById(R.id.btn_delete);
		mDeleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "delete button clicked!", Toast.LENGTH_SHORT).show();
			}
		});
		mShadeView = (ImageView) mDeleteView.findViewById(R.id.img_shade);
	}

	public QQListView3(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.listViewStyle);
	}

	public QQListView3(Context context) {
		this(context, null);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		if (mIsPlay) { // 动画播放期间，任何Touch事件均无效
//			return true;
//		}
		boolean isHandle = false;
		int action = MotionEventCompat.getActionMasked(event);
		try {
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
//				if (mIsShown && !mIsFling)  {
//					mIsShown = false;
//				}
				// 当mIsFling为true时，表示触发显示button动画，
				// 但手指还没有离开屏幕，这时ListView不能表现任何touch行为，如：滚动等
//				if (mIsFling) {
//					mIsFling = false;
//				}
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			}
			
			Log.d(TAG, "mIsFling:"+mIsFling+",mIsShown:"+mIsShown);
			if (mIsFling || mIsShown) {
				isHandle = true;
			}
			return isHandle;
		} finally {
			// 把下面的代码放在这里是因为return会先执行
			// 这就解决了当在删除按钮消失的时候，过一会抬起手指，onItemClick不被执行的情况
			// 如果把此代码放在上面，那么当抬起手指时，mIsShown就是false，就不会拦截此Touch事件了，此事件通过后，认为触发了ListView的OnItemClick。
			if (action == MotionEvent.ACTION_UP) {
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
			}
		}
	}
	
	private void onSlide(int position) {
		mIsFling = true;
		showDeleteBtn(position);
	}

	private void showDeleteBtn(int position) {
		initDeleteView();
		mIsShown = true;
		int firstVisiblePos = getFirstVisiblePosition();
		mViewGroup = (ViewGroup) getChildAt(position - firstVisiblePos);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mViewGroup.addView(mDeleteView, params);
		TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		ta.setDuration(500);
		ta.setRepeatCount(0);
		ta.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mIsPlay = true;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mShadeView.setVisibility(View.GONE);
				mIsPlay = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
		});
		mShadeView.startAnimation(ta);
		Toast.makeText(getContext(), "show", Toast.LENGTH_SHORT).show();
	}
	
	private void hideDeleteBtn() {
		if (mViewGroup != null ) {
			TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			ta.setDuration(500);
			ta.setRepeatCount(0);
			ta.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					mIsPlay = true;
					mShadeView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mViewGroup.removeView(mDeleteView);
					mIsPlay = false;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
			});
			mShadeView.startAnimation(ta);
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
