package wxc.android.androiddemos.activity;

import wxc.android.androiddemos.widget.PathEffectDemoView;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PathEffectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.setLayoutParams(params);
		ll.addView(new PathEffectDemoView(this), params);
		setContentView(ll);
	}
}
