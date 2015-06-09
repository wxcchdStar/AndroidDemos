package wxc.android.androiddemos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import wxc.android.androiddemos.R;

/**
 * Created by Chenhd on 2015/6/9.
 */
public class ListScrollViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_scroll_view);
        String[] data = new String[500];
        for (int i = 0; i < data.length; i++) {
            data[i] = String.valueOf(i);
        }
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
    }
}
