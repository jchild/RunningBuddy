package com.example.runningbuddy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageAdapter extends BaseAdapter{
	
	private Context mContext;

	public ImageAdapter(Context c){
		mContext = c;
	}

	public int getCount(){
		return mThumbIds.length;
	}
	public Object getItem(int position){
		return null;
	}
	public long getItemId(int position){
		return 0;
	}
	public View getView(int position, View convertView, ViewGroup parent){
		ImageView imageView;
		if(convertView== null){
			imageView = new ImageView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(new GridView.LayoutParams(params));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(16,16,16,16);
		} else{
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(mThumbIds[position]);
		return imageView;
	}
	
	private Integer[] mThumbIds = {
            R.drawable.ic_user, R.drawable.ic_map, R.drawable.ic_place
           
    };
	
}
