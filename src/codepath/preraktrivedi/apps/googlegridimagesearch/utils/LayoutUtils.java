package codepath.preraktrivedi.apps.googlegridimagesearch.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class LayoutUtils {

	public static void showToast(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 300);
		toast.show();
	}
	
}
