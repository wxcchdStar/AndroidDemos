package wxc.android.androiddemos.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 左滑显示删除按钮，做任何touch事件均为消除删除按钮
 * 实现方式：使用GestureDetector检测手势
 * 
 * GestureDetector会造成一种现象：onDown --> onScroll --> onFling,
 * 不能明确区分onScroll和onFling两种手势，在QQList中几乎水平左滑才会显示删除按钮
 * 
 * 太过麻烦，而且不一定可以具体实现想要的效果，因此不采用
 * 
 * @author WXC
 *
 */
@Deprecated
public class QQListView extends ListView implements 
		GestureDetector.OnGestureListener, View.OnTouchListener {
	private static final String TAG = "QQListView";
	private GestureDetector mGestureDetector;
	
	private boolean mIsShown; // 刪除按钮是否显示

	public QQListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(context, this);
		setOnTouchListener(this);
	}

	public QQListView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.listViewStyle);
	}

	public QQListView(Context context) {
		this(context, null);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mIsShown) {
			
			return true;
		}
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d(TAG, "onDown");
		// 判断删除按钮是否已显示，若显示则使其消失
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d(TAG, "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG, "onSingleTapUp");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d(TAG, "onScroll");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d(TAG, "onLongPress");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG, "onFling:" + velocityX + "," + velocityY);
		return false;
	}

}
