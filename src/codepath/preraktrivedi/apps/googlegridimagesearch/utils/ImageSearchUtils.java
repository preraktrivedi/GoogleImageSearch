package codepath.preraktrivedi.apps.googlegridimagesearch.utils;

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
}
