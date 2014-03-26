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
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.adapters.ImageResultArrayAdapter;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageResult;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageSearchAppData;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.SearchFilters;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.EndlessScrollListener;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.ImageSearchUtils;
import codepath.preraktrivedi.apps.googlegridimagesearch.utils.LayoutUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GoogleGridImageSearchMainActivity extends Activity {

	private static final String TAG = GoogleGridImageSearchMainActivity.class.getSimpleName();
	private Context mContext;
	private static boolean sIsSearchAlreadyDone = false;
	private TextView tvSearch, tvNotFoundText;
	private GridView gvImages;
	private ImageView ivNotFound;
	private EditText etSearch;
	private ImageButton ibSearchBtn;
	private RelativeLayout rlResultContainer, rlErrorContainer;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;
	private SearchFilters searchFilters;
	private ImageSearchAppData appData;
	private static final String AMPERSAND = "&", EQUALS = "=", QUESTIONMARK = "?", QUERY = "q",
			TYPE = "imgtype", SITE = "as_sitesearch", COLOR = "imgcolor",
			SIZE = "imgsz", RSSIZE = "rsz", START = "start", VERSION = "v",
			RESPONSE_DATA = "responseData", RESULTS = "results";
	public static final String SEARCH_RESULT_POSITION = "resultposition";
	private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_grid_image_search_main);
		mContext = this;
		appData = ImageSearchAppData.getInstance();
		searchFilters = getSearchFilters();
		initializeUI();
		handleIntent(getIntent());
	}

	private void initializeUI() {
		//		ActionBar bar = getActionBar();
		//		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5500FF")));
		rlResultContainer = (RelativeLayout) findViewById(R.id.rl_result_container);
		rlErrorContainer = (RelativeLayout) findViewById(R.id.rl_query_container);
		tvSearch = (TextView) findViewById(R.id.tv_search_query);
		gvImages = (GridView) findViewById(R.id.gvImages);
		etSearch = (EditText) findViewById(R.id.et_search);
		ivNotFound = (ImageView) findViewById(R.id.iv_not_found);
		tvNotFoundText = (TextView) findViewById(R.id.tv_not_found);
		ibSearchBtn = (ImageButton) findViewById(R.id.ib_action_done);
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);
		setupListeners();
		showErrorViews(true);
	}

	private void setupListeners() {
		gvImages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				intent.putExtra(SEARCH_RESULT_POSITION, imageResult);
				startActivity(intent);
			}
		});

		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreResults(page);
			}
		});

		ibSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (validateInput()) {
					performSearch(etSearch.getText().toString().trim());
				}else {
					showErrorViews(true);
				}
			}
		});

		etSearch.setOnEditorActionListener(new OnEditorActionListener() {        
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_DONE){
					if (validateInput()) {
						performSearch(etSearch.getText().toString().trim());
					} else {
						showErrorViews(true);
					}
				}
				return false;
			}
		});
	}

	private boolean validateInput() {
		Editable editable = etSearch.getText();
		boolean isValidInput = false;
		String query = "";
		if (editable != null) {
			query = editable.toString();
			if (!TextUtils.isEmpty(query.trim())) {
				isValidInput = true;
			}
		} 
		return isValidInput;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.google_grid_image_search_main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.action_search) + "</font>"));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_refresh:
			String query = searchFilters.getSearchQuery();
			if (!TextUtils.isEmpty(query)) {
				performSearch(query);
			} else {
				LayoutUtils.showToast(mContext, "Please enter the query to search");
			}
			return true;
		case R.id.action_filters:
			loadFilterActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void loadFilterActivity() {
		Intent i = new Intent(mContext, SearchFiltersActivity.class);
		startActivity(i);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			performSearch(query);
		}
	}

	public void performSearch(String query) {
		Log.d(TAG, "Perform search for query - "+ query);
		sIsSearchAlreadyDone = true;
		if (TextUtils.isEmpty(query)) {
			LayoutUtils.showToast(mContext, "Please enter the query to search");
			return;
		}
		imageResults.clear();
		imageAdapter.clear();
		imageAdapter.notifyDataSetInvalidated();
		searchFilters.setSearchQuery(query);
		tvSearch.setText("Current Search : " + query);
		searchForImages(0);
	}

	public void searchForImages(int offset){

		if (ImageSearchUtils.isNetworkActive(mContext)) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(buildQueryUrlFromFilters(offset), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					try {
						Log.d(TAG, "JSON Success result - " + response.toString()); 
						JSONArray joArray = response.getJSONObject(RESPONSE_DATA).getJSONArray(RESULTS);
						if (joArray.length() > 0) {
							imageAdapter.addAll(ImageResult.fromJSONArray(joArray));
							showErrorViews(false);
						} else {
							showErrorViews(true);
						}
					} catch (JSONException e) {
						Log.e(TAG, "JSON Exception - " + e.toString()); 
						showErrorViews(true);
					}
				}

				@Override
				public void onFailure(Throwable e, JSONObject error) {
					showErrorViews(true);
					LayoutUtils.showToast(mContext, "Something went wrong, please try again.");
				}
			});
		} else {
			showErrorViews(true);
			LayoutUtils.showToast(mContext, "There seems to be a problem with your internet connection.");
		}
	}

	private void showErrorViews(boolean show) {
		if (show) {
			rlErrorContainer.setVisibility(View.VISIBLE);
			rlResultContainer.setVisibility(View.GONE);
		} else {
			rlResultContainer.setVisibility(View.VISIBLE);
			rlErrorContainer.setVisibility(View.GONE);
		}

		if (!sIsSearchAlreadyDone) {
			ivNotFound.setVisibility(View.INVISIBLE);
			tvNotFoundText.setVisibility(View.GONE);
		} else {
			ivNotFound.setVisibility(View.VISIBLE);
			tvNotFoundText.setVisibility(View.VISIBLE);
		}
	}

	private String buildQueryUrlFromFilters(int page) {
		StringBuffer sb = new StringBuffer("");
		searchFilters = getSearchFilters(); //validate latest search filters
		if (searchFilters == null || TextUtils.isEmpty(searchFilters.getSearchQuery())) {
			LayoutUtils.showToast(mContext, "Search Query is empty");
			return sb.toString();
		}
		sb.append(BASE_URL + QUESTIONMARK + RSSIZE + EQUALS + 8);
		sb.append(AMPERSAND + START + EQUALS + page);
		sb.append(AMPERSAND + VERSION + EQUALS + 1.0);
		sb.append(AMPERSAND + QUERY + EQUALS + Uri.encode(searchFilters.getSearchQuery()));
		if (!TextUtils.isEmpty(searchFilters.getColorFilter())) {
			sb.append(AMPERSAND + COLOR + EQUALS + searchFilters.getColorFilter());
		}
		if (!TextUtils.isEmpty(searchFilters.getWebsiteFilter())) {
			sb.append(AMPERSAND + SITE + EQUALS + Uri.encode(searchFilters.getWebsiteFilter()));
		}
		if (!TextUtils.isEmpty(searchFilters.getSizeFilter())) {
			sb.append(AMPERSAND + SIZE + EQUALS + searchFilters.getSizeFilter());
		}
		if (!TextUtils.isEmpty(searchFilters.getTypeFilter())) {
			sb.append(AMPERSAND + TYPE + EQUALS + searchFilters.getTypeFilter());
		}
		Log.d(TAG, "Current image query url - \n" + sb.toString());
		return sb.toString();

	}

	private SearchFilters getSearchFilters() {
		SearchFilters filters = null;
		if (appData.getCurrentSearchFilters() != null) {
			filters = appData.getCurrentSearchFilters();
		} else {
			filters = new SearchFilters("", "", "", "", "");
		}

		appData.setCurrentSearchFilters(filters);
		return filters;
	}

	private void loadMoreResults(int page) {
		if (page < 9) {
			searchForImages(page);
		}
	}
}
