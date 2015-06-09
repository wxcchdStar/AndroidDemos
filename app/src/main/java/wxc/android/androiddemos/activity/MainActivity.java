package wxc.android.androiddemos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import wxc.android.androiddemos.R;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_enter_qqlist).setOnClickListener(this);
        findViewById(R.id.btn_scroller).setOnClickListener(this);
        findViewById(R.id.btn_quick_index).setOnClickListener(this);
        findViewById(R.id.btn_path_effect).setOnClickListener(this);
        findViewById(R.id.btn_wx_shake).setOnClickListener(this);
        findViewById(R.id.btn_scrollview_and_listview).setOnClickListener(this);
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
            case R.id.btn_wx_shake:
                startActivity(ShakeActivity.class);
            case R.id.btn_scrollview_and_listview:
                startActivity(ListScrollViewActivity.class);
                break;
            default:
                break;
        }
    }

    private void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
