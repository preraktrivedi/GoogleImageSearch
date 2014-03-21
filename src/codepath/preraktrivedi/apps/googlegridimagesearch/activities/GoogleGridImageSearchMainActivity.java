package codepath.preraktrivedi.apps.googlegridimagesearch.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.adapters.ImageResultArrayAdapter;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageResult;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.EndlessScrollListener;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.LayoutUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GoogleGridImageSearchMainActivity extends Activity {

	private Context mContext;
	private TextView tvSearch;
	private GridView gvImages;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;
	private static int REQUEST_CODE = 1, RESULT_CODE = 1;
	private String searchQuery,  imgsz, imgcolor, imgtype, as_sitesearch;
	private int imgcolorPos, imgszPos, imgtypePos, start = 0;
	private static String BASE_ADDRESS = "https://ajax.googleapis.com/ajax/services/search/images?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_grid_image_search_main);
		mContext = this;
		initializeUI();
		handleIntent(getIntent());
	}

	private void initializeUI() {
		tvSearch = (TextView) findViewById(R.id.tv_search_query);
		gvImages = (GridView) findViewById(R.id.gvImages);
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);
		setupListeners();
	}

	private void setupListeners() {
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				intent.putExtra("result", imageResult);
				startActivity(intent);
			}

		});

		gvImages.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMore(totalItemsCount);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_grid_image_search_main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(mContext, "Search", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	private void doMySearch(String query) {
		
		if (!TextUtils.isEmpty(query)) {
			searchQuery = query;
		} else {
			return;
		}

		if (searchQuery != null) {
			tvSearch.setText(searchQuery);
			LayoutUtils.showToast(mContext, "Searching for " + searchQuery);

			String fullUrl;
			if (imgsz != null && imgcolor!= null && imgtype!= null  && as_sitesearch!= null) {

			 fullUrl = BASE_ADDRESS + "rsz=8" + "&start=0" + "&v=1.0"
					+ "&imgsz=" + imgsz + "&imgcolor=" + imgcolor + "&imgtype="
					+ imgtype + "&as_sitesearch=" + as_sitesearch + "&q="
					+ Uri.encode(searchQuery);
			} else if ( imgsz != null && imgcolor!= null && imgtype!= null && as_sitesearch== null) {
				 fullUrl = BASE_ADDRESS + "rsz=8" + "&start=0" + "&v=1.0"
						+ "&imgsz=" + imgsz + "&imgcolor=" + imgcolor + "&imgtype="
						+ imgtype + "&q=" + Uri.encode(searchQuery);
			} else {
				 fullUrl = BASE_ADDRESS + "rsz=8" + "&start=0" + "&v=1.0" + "&q=" + Uri.encode(searchQuery);
			}

			AsyncHttpClient client = new AsyncHttpClient();
			loadImages(client, fullUrl, true);
		} else {
			LayoutUtils.showToast(mContext, "Please enter a search query!");
		}
	}

	public void loadMore(int totalItemsCount) {
		start = totalItemsCount;
		String fullUrl;
		if (imgsz != null && imgcolor!= null && imgtype!= null  && as_sitesearch!= null) {

			fullUrl = BASE_ADDRESS + "rsz=8" + "&start=" +start + "&v=1.0"
					+ "&imgsz=" + imgsz + "&imgcolor=" + imgcolor + "&imgtype="
					+ imgtype + "&as_sitesearch=" + as_sitesearch + "&q="
					+ Uri.encode(searchQuery);
		} else if ( imgsz != null && imgcolor!= null && imgtype!= null && as_sitesearch== null) {
			fullUrl = BASE_ADDRESS + "rsz=8" + "&start=" + start + "&v=1.0"
					+ "&imgsz=" + imgsz + "&imgcolor=" + imgcolor + "&imgtype="
					+ imgtype + "&q=" + Uri.encode(searchQuery);
		} else {
			fullUrl = BASE_ADDRESS + "rsz=8" + "&start=" + start + "&v=1.0" + "&q=" + Uri.encode(searchQuery);
		}

		AsyncHttpClient client = new AsyncHttpClient();
		loadImages(client, fullUrl, false);
	}
	
	private void loadImages(AsyncHttpClient client, String fullUrl, final boolean clear) {
		client.get(fullUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData")
							.getJSONArray("results");
					if (clear) {
						imageResults.clear();
					}
					imageAdapter.addAll(ImageResult
							.fromJSONArray(imageJsonResults));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
