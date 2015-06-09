package wxc.android.androiddemos;

import java.lang.ref.WeakReference;

import wxc.android.androiddemos.utils.ImageUtil;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageListActivity extends Activity {
	private Bitmap mPlaceHolderBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}

	public void loadBitmap(int resId, ImageView imageView) {
		if (cancelPotentialWork(resId, imageView)) {
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(resId);
	    }
	}
	
	public static boolean cancelPotentialWork(int data, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final int bitmapData = bitmapWorkerTask.data;
	        // If bitmapData is not yet set or it differs from the new data
	        if (bitmapData == 0 || bitmapData != data) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
		    if (drawable instanceof AsyncDrawable) {
		    	final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		    	return asyncDrawable.getBitmapWorkerTask();
		    }
	    }
	    return null;
	}
	
	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}
	
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		int data = 0;
		
		public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

		// Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	    	if (params.length > 0 && params[0] != null) {
	            data = params[0];
	    	}
	        return ImageUtil.decodeSampledBitmapFromResource(getResources(), data, 100, 100);
	    }
	    
	    
	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	    	if (isCancelled()) {
	    		bitmap.recycle();
	            bitmap = null;
	        }

	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            final BitmapWorkerTask bitmapWorkerTask =
	                    getBitmapWorkerTask(imageView);
	            // Task保存的ImageView引用的里面保存的Task引用是否是当前这个
	            if (this == bitmapWorkerTask && imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }

	}
}
