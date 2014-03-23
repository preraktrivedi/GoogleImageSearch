package codepath.preraktrivedi.apps.googlegridimagesearch.datamodel;

public class ImageSearchAppData {

	private static ImageSearchAppData sInstance;
	
	public static synchronized ImageSearchAppData getInstance() {
		if (null == sInstance) {
			sInstance = new ImageSearchAppData();
		}
		return sInstance;
	}

	private SearchFilters currentSearchFilters;

	public SearchFilters getCurrentSearchFilters() {
		return currentSearchFilters;
	}

	public void setCurrentSearchFilters(SearchFilters currentSearchFilters) {
		this.currentSearchFilters = currentSearchFilters;
	}
}
