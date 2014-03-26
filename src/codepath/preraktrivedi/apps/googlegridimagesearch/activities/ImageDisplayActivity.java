package codepath.preraktrivedi.apps.googlegridimagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageResult;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageSearchAppData;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.LayoutUtils;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	private static final String TAG = ImageDisplayActivity.class.getSimpleName();
	private ShareActionProvider mShareActionProvider;
	private SmartImageView ivImage;
	private TextView tvTitle;
	private MenuItem shareItem;
	private Context mContext;
	private ImageResult imageResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initializeUi();
	}

	private void initializeUi() {
		setContentView(R.layout.activity_image_display);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(ImageSearchAppData.getInstance().getCurrentSearchFilters().getSearchQuery());
		imageResult = (ImageResult) getIntent().getSerializableExtra(GoogleGridImageSearchMainActivity.SEARCH_RESULT_POSITION);
		ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(imageResult.getUrl());
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(imageResult.getTitleNoFormatting());
		postDelayShareAction();
	}

	private void postDelayShareAction() {
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				setupShareAction();
			}
		};
		handler.postDelayed(runnable, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		shareItem = menu.findItem(R.id.menu_item_share); 
		mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();  
		//setShareIntent(createShareIntent());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Intent getShareIntent(Uri bmpUri) {  
		Intent shareIntent = null;
		if (mShareActionProvider != null) {  
			shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.setType("image/*"); 
			Log.d(TAG, "Created share intent!");
		}  
		return shareIntent;
	}  

	public void setupShareAction() {
		Uri bmpUri = getLocalBitmapUri(ivImage.getDrawable()); 
		if(bmpUri != null){
			Intent shareIntent = getShareIntent(bmpUri);
			if (shareIntent != null) {
				mShareActionProvider.setShareIntent(shareIntent);
			}
		} else {
			LayoutUtils.showToast(mContext,  "Something went wrong, could not share this event");
		}	   
	}


	public Uri getLocalBitmapUri(Drawable drawable) {
		Uri bmpUri = null;
		Bitmap bitmap;
		if(drawable != null && drawable instanceof BitmapDrawable){
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		} else {
			return null;
		}
		try {
			String fileName = "share_"+ UUID.randomUUID().toString() +".png";
			File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);  
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			Log.e(TAG, "IO Exception \n " + e.toString());
			e.printStackTrace();
		}
		return bmpUri;
	}

}
