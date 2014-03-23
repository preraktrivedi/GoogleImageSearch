package codepath.preraktrivedi.apps.googlegridimagesearch.datamodel;

import java.io.Serializable;

public class SearchFilters implements Serializable {

	private static final long serialVersionUID = 763172601182287928L;
	private String searchQuery;
	private String websiteFilter;
	private String colorFilter; 
	private String sizeFilter; 
	private String typeFilter;
	private boolean isFilterActive;

	public SearchFilters(String searchQuery, String websiteFilter, String colorFilter, String sizeFilter, String typeFilter) {
		super();
		this.searchQuery = searchQuery;
		this.websiteFilter = websiteFilter;
		this.colorFilter = colorFilter;
		this.sizeFilter = sizeFilter;
		this.typeFilter = typeFilter;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getWebsiteFilter() {
		return websiteFilter;
	}

	public void setWebsiteFilter(String websiteFilter) {
		this.websiteFilter = websiteFilter;
	}

	public String getColorFilter() {
		return colorFilter;
	}

	public void setColorFilter(String colorFilter) {
		this.colorFilter = colorFilter;
	}

	public String getSizeFilter() {
		return sizeFilter;
	}

	public void setSizeFilter(String sizeFilter) {
		this.sizeFilter = sizeFilter;
	}

	public String getTypeFilter() {
		return typeFilter;
	}

	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter;
	}

	public boolean isFilterActive() {
		return isFilterActive;
	}

	public void setFilterActive(boolean isFilterActive) {
		this.isFilterActive = isFilterActive;
	}

}
