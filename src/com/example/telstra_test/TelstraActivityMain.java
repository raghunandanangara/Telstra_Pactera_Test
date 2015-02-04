package com.example.telstra_test;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.telstra_test.dataadapter.ListAdapter;
import com.example.telstra_test.datamodel.JsonData;
import com.example.telstra_test.datamodel.RowsData;
import com.example.telstra_test.volleyhelpers.BitmapLruCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;


public class TelstraActivityMain extends ActionBarActivity {

	private static String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";

	/** Context of this Activity*/
	private Context mContext;

	/** Elements in activity_main.xml*/
	private ListView list_view;

	/** Layout inflater instance*/
	private LayoutInflater layoutInflater;

	/** Array list to capture JSON data*/
	private ArrayList<RowsData> arrayJSONData ;

	/** Adapter to populate array list in Listview*/
	private ListAdapter listAdapter;

	/** Pull to Refresh swipeContainer*/
	private SwipeRefreshLayout swipeContainer;

	/** Volley Library Variables*/
	private RequestQueue mRequestQueue_volley;
	private ImageLoader.ImageCache imageCache_volley;
	private ImageLoader imageLoader_volley;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		mContext = this;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telstra_main);

		layoutInflater = LayoutInflater.from(mContext);

		/** Using Memory Cache(RAM) of Volley Library only
		 * If images are in high number you need to implement Volley's Disk Cache also
		 **/
		imageCache_volley = new BitmapLruCache();
		mRequestQueue_volley = Volley.newRequestQueue(this);
		imageLoader_volley = new ImageLoader(mRequestQueue_volley, imageCache_volley);

		/** Initialize Arraylist and Adapter*/
		arrayJSONData = new ArrayList<RowsData>();
		listAdapter = new ListAdapter(layoutInflater, arrayJSONData, imageLoader_volley);

		/** Bind Adapter to the ListView */
		list_view = (ListView) findViewById(R.id.listView);
		list_view.setAdapter(listAdapter);


		/** Initialize SwipeContainer*/
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);

		//Start pull to refresh animation from the launch
		swipeContainer.setRefreshing(true);

		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				/** Initialize and Add JSON Request to Volley Request Queue*/
				JsonObjectRequest jr_swipe = new JsonObjectRequest(Request.Method.GET,url,null,new eqJSONResponseListener(),new eqJSONResponseError());
				mRequestQueue_volley.add(jr_swipe);

			} 
		});

		/** Initialize and Add JSON Request to Volley Request Queue*/
		JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET,url,null,new eqJSONResponseListener(),new eqJSONResponseError());
		mRequestQueue_volley.add(jr);

	}

	class eqJSONResponseError implements Response.ErrorListener
	{
		@Override
		public void onErrorResponse(VolleyError error) {
			String errorMessage = error.getMessage();
			if(errorMessage!=null)
			{
				/** Stop Pull to Refresh animation if there is any error downloading from Server */
				swipeContainer.setRefreshing(false);
			}
		}
	}

	class eqJSONResponseListener implements Response.Listener<JSONObject>
	{
		@Override
		public void onResponse(JSONObject response) {
			try {
				final GsonBuilder builder = new GsonBuilder();
				final Gson gson = builder.serializeNulls().create();
				/** Convert JSONResponse to POJO Classes using GSON */
				JsonData DataTemp= gson.fromJson(response.toString(), JsonData.class);

				/** Set the title of the Activity from JSON */
				setTitle(DataTemp.title);

				listAdapter.clearAdapter();

				/** Copy all POJO arrays into ArrayList */
				for(RowsData temp : DataTemp.rows)
				{
					//Avoid adding entries which do not have Titles
					if(temp.getTitle()!=null)
						arrayJSONData.add(temp);
				}
			} catch (Exception e) {
				//Catch GSON exceptions
				e.printStackTrace();
			}

			/** Notify Adapter of changes in ArrayList */
			listAdapter.notifyDataSetChanged();

			/** Stop Pull to refresh animation once JSON parsing is successfull*/
			swipeContainer.setRefreshing(false);
		}
	}

	
}
