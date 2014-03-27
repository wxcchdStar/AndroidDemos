package wxc.android.androiddemos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_enter_qqlist).setOnClickListener(this);
		findViewById(R.id.btn_scroller).setOnClickListener(this);
		findViewById(R.id.btn_quick_index).setOnClickListener(this);
		findViewById(R.id.btn_path_effect).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_enter_qqlist:
			startActivity(QQListActivity.class);
			break;
		case R.id.btn_scroller:
			startActivity(ScrollerActivity.class);
			break;
		case R.id.btn_quick_index:
			startActivity(AlphabetIndexActivity.class);
			break;
		case R.id.btn_path_effect:
			startActivity(PathEffectActivity.class);
			break;
		default:
			break;
		}
	}

	private void startActivity(Class<? extends Activity> clazz) {
		startActivity(new Intent(this, clazz));
	}
}
