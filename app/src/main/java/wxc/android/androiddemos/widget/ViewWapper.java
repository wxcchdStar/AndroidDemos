package wxc.android.androiddemos.widget;

import android.view.View;

public class ViewWapper {
	private View view;
	
	public ViewWapper(View view) {
		this.view = view;
	}
	
	public void setWidth(int width) {
		view.getLayoutParams().width = width;
		view.requestLayout();
	}
}
