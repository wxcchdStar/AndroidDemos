package wxc.android.androiddemos;

import wxc.android.androiddemos.widget.QQListView3;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class QQListActivity extends Activity {
	private QQListView3 mList;
	private QQListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qqlist);
		
		mList = (QQListView3) findViewById(R.id.qqlv);
		mAdapter = new QQListAdapter(this);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(QQListActivity.this, "item clicked!", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
	public static class QQListAdapter extends BaseAdapter {
		private String[] mData = new String[26];
		private Context mContext;
		
		public QQListAdapter(Context ctx) {
			mContext = ctx;
			initData();
		}
		
		private void initData() {
			for (int i = 0; i < 26; i++) {
				mData[i] = String.valueOf((char) ('A' + i));
			}
		}

		@Override
		public int getCount() {
			return mData.length;
		}

		@Override
		public String getItem(int position) {
			return mData[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView contentTv = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qqlist, null);
				contentTv = (TextView) convertView.findViewById(R.id.tv_content);
				convertView.setTag(contentTv);
			} else {
				contentTv = (TextView) convertView.getTag();
			}
			contentTv.setText(getItem(position));
			return convertView;
		}
		
	}
}
