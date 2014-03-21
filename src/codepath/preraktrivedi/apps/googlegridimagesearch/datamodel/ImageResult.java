package codepath.preraktrivedi.apps.googlegridimagesearch.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{
	private static final long serialVersionUID = 551693840093501875L;
	private String fullUrl;
	private String thumbUrl;

	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;
		}
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public String toString() {
		return this.thumbUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray imageJsonResults) {
		ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
		for (int x=0; x<imageJsonResults.length(); x++) {
			try {
				imageResults.add(new ImageResult(imageJsonResults.getJSONObject(x)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imageResults;
	}

}