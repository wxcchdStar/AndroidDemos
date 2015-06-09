package wxc.android.androiddemos;

import wxc.android.androiddemos.widget.AlphabetIndexListView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class AlphabetIndexActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alphabet_index);
		
		AlphabetIndexListView listView = (AlphabetIndexListView) findViewById(R.id.lv_words);
		listView.setAdapter(new WordsAdapter(this));
	}
	
	//必须实现SectionIndexer
	public static class WordsAdapter extends BaseAdapter implements SectionIndexer {
		private Context mContext;
		private String [] mWords = new String[260];
		
		public WordsAdapter(Context context) {
			mContext = context;
			for (int i = 0; i < 26; i++) {
				for (int j = 0; j < 10; j++) {
					if (j == 0) {
						mWords[i * 10] = String.valueOf((char)('A' + i));
					} else {
						mWords[i * 10  + j] = mWords[i*10] + mWords[i*10] + mWords[i*10];
					}
				}
			}
		}

		@Override
		public int getCount() {
			return mWords.length;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public int getItemViewType(int position) {
			if (position % 10 == 0) {
				return 0;
			}
			return 1;
		}

		@Override
		public String getItem(int position) {
			return mWords[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView wordTv = null;
			if (convertView == null) {
				wordTv = new TextView(mContext);
				wordTv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 
						AbsListView.LayoutParams.WRAP_CONTENT));
				int dp16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());
				wordTv.setPadding(dp16, dp16, dp16, dp16);
				convertView = wordTv;
			} else {
				wordTv = (TextView) convertView;
			}
			wordTv.setText(getItem(position));
			if (getItemViewType(position) == 0) {
				wordTv.setTextColor(Color.WHITE);
				wordTv.setBackgroundColor(Color.parseColor("#e00099cc"));
			} else {
				wordTv.setTextColor(Color.BLACK);
				wordTv.setBackgroundColor(Color.TRANSPARENT);
			}
			return wordTv;
		}

		@Override
		public Object[] getSections() {
			return null;
		}

		// 必须提供的方法，section为字符A-Z的INT型数值
		@Override
		public int getPositionForSection(int section) {
			return (section - 'A' )* 10;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}
		
	}
}
