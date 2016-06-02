package wxc.android.androiddemos.activity;

import wxc.android.androiddemos.R;
import wxc.android.androiddemos.model.ShakeDetector;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WxShakeActivity extends AppCompatActivity implements ShakeDetector.Listener {
	private ShakeDetector mShakeDetector;
	private SensorManager mSensorManager;
	
	private RelativeLayout mShakeUp;
	private RelativeLayout mShakeDown;
	private ImageView mShakeLineUp;
	private ImageView mShakeLineDown;
	
	private boolean mIsShaking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mShakeDetector = new ShakeDetector(this);
		
		mShakeUp = (RelativeLayout) findViewById(R.id.rl_shake_up);
		mShakeDown = (RelativeLayout) findViewById(R.id.rl_shake_down);
		mShakeLineUp = (ImageView) findViewById(R.id.img_shake_line_up);
		mShakeLineDown = (ImageView) findViewById(R.id.img_shake_line_down);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mShakeDetector.start(mSensorManager);
	}

	@Override
	public void hearShake() {
		if (!mIsShaking) {
			mIsShaking = true;
			Toast.makeText(this, "Shake!!!", Toast.LENGTH_SHORT).show();
			startShakeUpAnimation();
			startShakeDownAnimation();
		}
	}
	
	private void startShakeUpAnimation() {
		TranslateAnimation logoUpTransAni = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, -0.4f);
		logoUpTransAni.setDuration(800);
		logoUpTransAni.setRepeatCount(1);
		logoUpTransAni.setRepeatMode(Animation.REVERSE);
		logoUpTransAni.setAnimationListener(new  Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				mShakeLineDown.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mIsShaking = false;
				mShakeLineDown.setVisibility(View.GONE);
			}
		});
		mShakeUp.startAnimation(logoUpTransAni);
	}
	
	private void startShakeDownAnimation() {
		TranslateAnimation logoUpTransAni = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 
				TranslateAnimation.RELATIVE_TO_SELF, 0.4f);
		logoUpTransAni.setDuration(800);
		logoUpTransAni.setRepeatCount(1);
		logoUpTransAni.setRepeatMode(Animation.REVERSE);
		logoUpTransAni.setAnimationListener(new  Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				mShakeLineUp.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mShakeLineUp.setVisibility(View.GONE);
			}
		});
		mShakeDown.startAnimation(logoUpTransAni);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// 注释掉此行代码，打开此页面，按home退出，再进入微信摇一摇页面，可比较两者摇一摇的差别。
		mShakeDetector.stop();
	}
}
