package wxc.android.androiddemos.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import wxc.android.androiddemos.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.list)
    ListView mListView;

    private ArrayList<Class<?>> mActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mActivities = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfos != null) {
            for (ResolveInfo resolveInfo : resolveInfos) {
                if (resolveInfo.activityInfo != null && getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                    try {
                        Class<?> clazz = Class.forName(resolveInfo.activityInfo.name);
                        mActivities.add(clazz);

                        String simpleName = clazz.getSimpleName();
                        int end = simpleName.indexOf("Activity");
                        data.add(simpleName.substring(0, end));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
    }

    @OnItemClick(R.id.list)
    public void onItemClick(int position) {
        startActivity(new Intent(this, mActivities.get(position)));
    }

}
