package codepath.preraktrivedi.apps.googlegridimagesearch.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5007324874218013174L;
	private String url;
	private String tbUrl;
	private String titleNoFormatting;
	private String contentNoFormatting;

	private static final String URL = "url", TB_URL = "tbUrl",
			TITLE = "titleNoFormatting", CONTENT = "contentNoFormatting";

	public ImageResult(JSONObject jo) {
		try {
			this.url = jo.getString(URL);
			this.tbUrl = jo.getString(TB_URL);
			this.titleNoFormatting = jo.getString(TITLE);
			this.contentNoFormatting = jo.getString(CONTENT);
		} catch (JSONException e) {
			this.url = null;
			this.tbUrl = null;
			this.titleNoFormatting = null;
			this.contentNoFormatting = null;
		}
	}

	public String getUrl() {
		return url;
	}

	public String getTbUrl() {
		return tbUrl;
	}

	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}

	public String getContentNoFormatting() {
		return contentNoFormatting;
	}

	public String toString() {
		return this.tbUrl;

	}

	public static List<ImageResult> fromJSONArray(JSONArray joArray) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < joArray.length(); i++) {
			try {
				results.add(new ImageResult(joArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
}