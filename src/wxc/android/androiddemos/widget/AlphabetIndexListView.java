package wxc.android.androiddemos.widget;

import wxc.android.androiddemos.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class AlphabetIndexListView extends ListView {
	public static final String TAG = "AlphabetIndexListView";
	
	// 索引条宽度
	private int indexerWidth = 24; // default 24 DP
	// 悬浮框的边长
	private int showLetterSide = 0; // default width/5
	// 索引条背景颜色
	private int indexBarBackgroudColor = 0xFF8a8888;
	// 索引条字体颜色
	private int indexBarTextColor = 0xFF666666;
	// 悬浮框背景颜色
	private int showLetterBackgroundColor = 0xFF666666;
	// 悬浮框字体颜色
	private int showLetterColor = 0xFFFFFFFF;
	// 索引
	private String[] sections = new String[26];
	// 是否显示悬浮框
	private boolean showLetter = false;
	
	private Paint paint;
	private Paint letterPaint;
	
	// 索引条坐标
	private RectF indexBarRect;
	// 悬浮框坐标
	private RectF showLetterRect;
	
	// 索引在sections中的位置
	private int indexPosition;

	public AlphabetIndexListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		setFastScrollEnabled(false);
		
		// init the content of the index bar
		for (int i = 0; i < 26; i++) {
			sections[i] = String.valueOf((char) ('A' + i));
		}
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		indexerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indexerWidth, dm);
		
		TypedArray ta = null;
		try {
			ta = context.obtainStyledAttributes(attrs, R.styleable.AlphabetIndexListView);
			indexerWidth = ta.getDimensionPixelSize(R.styleable.AlphabetIndexListView_indexerWidth, indexerWidth);
			indexBarBackgroudColor = ta.getColor(R.styleable.AlphabetIndexListView_indexBarBackgroudColor, indexBarBackgroudColor);
			indexBarTextColor = ta.getColor(R.styleable.AlphabetIndexListView_indexBarTextColor, indexBarTextColor);
			showLetterBackgroundColor = ta.getColor(R.styleable.AlphabetIndexListView_showLetterBackgroundColor, showLetterBackgroundColor);
			showLetterColor = ta.getColor(R.styleable.AlphabetIndexListView_showLetterColor, showLetterColor);
			showLetterSide = ta.getDimensionPixelSize(R.styleable.AlphabetIndexListView_showLetterSide, showLetterSide);
		} finally {
			if (ta != null) {
				ta.recycle();
			}
		}
		
		paint = new Paint();
		paint.setAntiAlias(true);
		
		letterPaint = new Paint();
		letterPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		// 索引条高度
		int sumIndexHeight = height - indexerWidth;
		
		// draw the background of the index bar 
		if (showLetter) {
			paint.setColor(indexBarBackgroudColor);
			paint.setStrokeWidth(indexerWidth);
			paint.setStyle(Style.FILL);
			
			if (indexBarRect == null) {
				indexBarRect = new RectF(width - indexerWidth, 0, width, height);
			}
			// 画索引条，上下是半圆，半径为索引条宽度的1/2
			canvas.drawRoundRect(indexBarRect, indexerWidth / 2f, indexerWidth / 2f, paint);
		}
		
		// draw the text of the index bar
		letterPaint.setColor(indexBarTextColor);
		// 索引条字体为宽度的一半
		letterPaint.setTextSize(indexerWidth / 2);
		// 字体居中显示
		letterPaint.setTextAlign(Align.CENTER);
		// 计算索引字体高度
		float indexLetterHeight = - letterPaint.descent() - letterPaint.ascent();
		// 每个索引的高度
		int perIndexHeight = sumIndexHeight / sections.length;
		int indexLetterPos = 0;
		for (int i = 0; i < sections.length; i++) {
			indexLetterPos = indexerWidth / 2 + (int) (i * perIndexHeight + perIndexHeight / 2 + indexLetterHeight / 2); 
			canvas.drawText(sections[i], width - indexerWidth / 2, indexLetterPos, letterPaint);
		}
		
		// draw the dialog that show letter
		if (showLetter) {
			// draw the background of the dialog
			paint.setColor(showLetterBackgroundColor);
			paint.setStyle(Style.FILL);
			if (showLetterSide == 0) {
				showLetterSide = width / 5;
			}
			float halfSide = showLetterSide / 2f;
			if (showLetterRect == null) {
				showLetterRect = new RectF(width / 2f - halfSide, height / 2f - halfSide, 
						width / 2f + halfSide, height / 2f + halfSide);
			}
			canvas.drawRoundRect(showLetterRect, halfSide / 4, halfSide / 4, paint);
			
			// draw the letter in the dialog
			letterPaint.setColor(showLetterColor);
			letterPaint.setTextAlign(Align.CENTER);
			letterPaint.setTextSize(showLetterSide);
			float letterHeight = - letterPaint.descent() - letterPaint.ascent();
			int xPos = (int) (width / 2);
			int yPos = (int) (height / 2 + letterHeight / 2);
			canvas.drawText(sections[indexPosition], xPos, yPos, letterPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float xPos = ev.getX();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isTouchIndexBar(xPos)) {
				if (!showLetter) {
					computeIndexPosition(ev.getY());
					showLetter = true;
					invalidate();
				}	
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (showLetter) {
				computeIndexPosition(ev.getY());
				if (getAdapter() instanceof SectionIndexer) {
					char section = sections[indexPosition].charAt(0);
					int position = ((SectionIndexer) getAdapter()).getPositionForSection(section);
					if (position >= 0) {
						setSelection(position);
					}
				}
				invalidate();
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (showLetter) {
				showLetter = false;
				invalidate();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private void computeIndexPosition(float y) {
		float perIndexHeight = (getHeight() - indexerWidth) / sections.length;
		indexPosition = (int) Math.floor((y - indexerWidth / 2) / perIndexHeight);
		if (indexPosition >= sections.length) {
			indexPosition = sections.length - 1;
		} else if (indexPosition < 0) {
			indexPosition = 0;
		}
	}
	
	private boolean isTouchIndexBar(float x) {
		if (x > getWidth() - indexerWidth) {
			return true;
		}
		return false;
	}
	
}
