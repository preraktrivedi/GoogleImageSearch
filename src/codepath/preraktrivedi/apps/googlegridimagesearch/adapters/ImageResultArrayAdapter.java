package codepath.preraktrivedi.apps.googlegridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import codepath.preraktrivedi.apps.googlegridimagesearch.R;
import codepath.preraktrivedi.apps.googlegridimagesearch.datamodel.ImageResult;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.simple_image_view, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		SmartImageView ivImage;
		if(convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.simple_image_view, parent, false);
		} else {
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		ivImage.setImageUrl(imageInfo.getThumbUrl());
		return ivImage;
	}

}