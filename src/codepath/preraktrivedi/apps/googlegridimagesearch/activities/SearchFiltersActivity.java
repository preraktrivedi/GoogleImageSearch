package codepath.preraktrivedi.apps.googlegridimagesearch.activities;

import static codepath.preraktrivedi.apps.googlegridimagesearch.utils.ImageSearchUtils.*;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageSearchAppData;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.SearchFilters;

public class SearchFiltersActivity extends Activity {

	private Spinner spImageSize, spImageColor, spImageType;
	private EditText etSiteFilter;
	private Button btnSave;
	private ImageSearchAppData appData;
	private SearchFilters searchFilters;
	public static final String SEARCH_FILTERS = "SearchFilters";
	public static final String HTTP_PROTOCOL = "http://";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appData = ImageSearchAppData.getInstance();
		searchFilters = appData.getCurrentSearchFilters();
		setContentView(R.layout.activity_search_filters);
		initializeUi();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_filters, menu);
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

	private void initializeUi() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spImageColor = (Spinner) findViewById(R.id.spImageColor);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		btnSave = (Button) findViewById(R.id.btnSave);
		populateFilters();
		initializeListeners();
	}

	private void populateFilters() {

		if (searchFilters.getSizeFilter().length() > 0) {
			spImageSize.setSelection(getItemPositionInSpinner(spImageSize, searchFilters.getSizeFilter()));
		}

		if (searchFilters.getColorFilter().length() > 0) {
			spImageColor.setSelection(getItemPositionInSpinner(spImageColor, searchFilters.getColorFilter()));
		}

		if (searchFilters.getTypeFilter().length() > 0) {
			spImageType.setSelection(getItemPositionInSpinner(spImageType, searchFilters.getTypeFilter()));
		}

		if (searchFilters.getWebsiteFilter().length() > 0) {
			etSiteFilter.setText(searchFilters.getWebsiteFilter());
		}

	}

	private void initializeListeners() {
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateSearchFilters();
			}
		});
	}

	private void updateSearchFilters() {
		String siteFilter = "", colorFilter = spImageColor.getSelectedItem().toString(), sizeFilter = spImageSize.getSelectedItem().toString(), typeFilter = spImageType.getSelectedItem().toString();
		Editable etString = etSiteFilter.getText();

		if (etString != null) {
			siteFilter = Uri.encode(etString.toString().trim());
			searchFilters.setWebsiteFilter(validateFilterInput(siteFilter) && URLUtil.isValidUrl(HTTP_PROTOCOL + siteFilter) ? siteFilter : "");
		}

		searchFilters.setColorFilter(validateFilterInput(colorFilter) ? colorFilter : "");
		searchFilters.setSizeFilter(validateFilterInput(sizeFilter) ? sizeFilter : "");
		searchFilters.setTypeFilter(validateFilterInput(typeFilter) ? typeFilter : "");

		Log.d("SEARCHFILTER", "\nColor filter - " + colorFilter + "\nsite  filter - " + URLUtil.isValidUrl(HTTP_PROTOCOL + siteFilter) + " - " + siteFilter + "\n size  filter - " + sizeFilter + "\n type  filter - " + typeFilter);
		this.finish();
	}

}
