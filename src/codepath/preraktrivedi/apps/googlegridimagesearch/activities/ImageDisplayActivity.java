package codepath.preraktrivedi.apps.googlegridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageResult;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(imageResult.getUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

}
