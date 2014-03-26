package codepath.preraktrivedi.apps.googlegridimagesearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ImageSearchUtils {

	public static boolean validateFilterInput(String input) {
		return !(input == null || input.isEmpty() || input.contains("no filter"));
	}

	public static int getItemPositionInSpinner(Spinner sp, String value) {
		int position = -1;

		try {
			ArrayAdapter spinnerAdapter = (ArrayAdapter) sp.getAdapter(); 
			position = spinnerAdapter.getPosition(value);
		} catch(Exception e) {
			position = -1;
		}
		return position;
	}

	public static boolean isNetworkActive(Context context) {
		boolean isNetworkPresent = false;

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected()) {
					isNetworkPresent = true;
				}
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected()) {
					isNetworkPresent = true;
				}
			}
		}

		return isNetworkPresent;
	}
}
