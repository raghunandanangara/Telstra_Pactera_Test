package com.example.telstra_test.dataadapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.telstra_test.R;
import com.example.telstra_test.datamodel.RowsData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	
	private ArrayList<RowsData> arrNews ;
	private LayoutInflater layoutInflator;
	private ImageLoader imageLoader;

	public ListAdapter(LayoutInflater layoutInflator, ArrayList<RowsData> arrNews,
			ImageLoader imageLoader) {
		this.layoutInflator = layoutInflator;
		this.arrNews = arrNews;
		this.imageLoader = imageLoader;
	}
	
	public void clearAdapter() {
		arrNews.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return arrNews.size();
	}
	
	@Override
	public Object getItem(int i) {
			return arrNews.get(i);
	}
	
	@Override
	public long getItemId(int i) {
		return 0;
	}
	
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh ;
		if(view == null){
			vh = new ViewHolder();
			view = layoutInflator.inflate(R.layout.row_listview, viewGroup, false);
			vh.tvImage = (NetworkImageView) view.findViewById(R.id.imgImage);
			vh.tvTitle = (TextView) view.findViewById(R.id.txtTitle);
			vh.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
			view.setTag(vh);
		}
		else{
			vh = (ViewHolder) view.getTag();
		}

		RowsData rowsData = (RowsData) getItem(i);
		

		String url = rowsData.getImageHref();

		if(url!=null)
		{
			vh.tvImage.setVisibility(View.VISIBLE);
			vh.tvImage.setImageUrl(url, imageLoader);
			
			//Using no_image as a default image
			vh.tvImage.setDefaultImageResId(R.drawable.no_image);
			
			//Using error_image1 if fetching image have any problems
			vh.tvImage.setErrorImageResId(R.drawable.error_image1);
		}
		else
		{
			//If Image URI is null then hide corresponding Imageview
			vh.tvImage.setVisibility(View.GONE);
		}
		vh.tvTitle.setText(rowsData.getTitle());
		vh.tvDesc.setText(rowsData.getDescription());
		return view;
	}
	
	//To Hold views in a Listview
	class ViewHolder{
		//This ImageView is Volley Library special ImageView
		NetworkImageView tvImage;
		TextView tvTitle;
		TextView tvDesc;
	}
}
