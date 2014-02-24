package com.asloob.zappos;

import java.util.ArrayList;

import com.asloob.zappos.network.RequestZappos;

import exceptions.RequestThrottledException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;


public class MainActivity extends ActionBarActivity {

	final boolean DEBUG = true;
	final String TAG = MainActivity.class.getName();
	public static final String PREFS_NAME = "SAVED_PRODUCTS";

	ListView mProductsListView;
	ArrayList<Product> mProducts;
	ProductListAdapter mProductsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		String query = "boots";
		LOGV("query =" + query);
		ProductSearch search = new ProductSearch(this);
		search.execute(query);

		startProductService();
	}

	void initUI() {
		mProductsListView = (ListView) findViewById(R.id.productListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    // Configure the search info and add any event listeners
//	    ...
//	    searchView.setOnSearchClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				String query = ((SearchView) v).getQuery().toString();
//				LOGV("query =" + query);
//				ProductSearch search = new ProductSearch();
//				search.execute(query);
//			}
//		});
	    //searchView.setSuggestionsAdapter(adapter)
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            //openSearch();
	            return true;
//	        case R.id.action_compose:
//	            composeMessage();
//	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	class ProductSearch extends AsyncTask<String, Void, Integer> {
		private Context mContext;
		private static final int OK = 0;

		public ProductSearch(Context context) {
			super();
			this.mContext = context;
		}

		@Override
		protected Integer doInBackground(String... query) {
			//form url
			String url = "http://api.zappos.com/Search?term=" + query[0] + "&key=" + getString(R.string.api_key);
			LOGV("url = " + url);
			RequestZappos requestZappos = new RequestZappos();
			String response = null;
			try {
				response = requestZappos.sendRequestWithPath(url);
			} catch (RequestThrottledException e) {
				return RequestThrottledException.ID;
			}
			mProducts = Parser.parseProducts(response);
			LOGV("size = " + mProducts.size());
			return OK;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			LOGV("result = " + result);
			if(result == OK) {
				mProductsAdapter = new ProductListAdapter(mContext, R.layout.product_list_item, mProducts);
				mProductsAdapter.notifyDataSetChanged();
				mProductsListView.setAdapter(mProductsAdapter);
			} else if (result == RequestThrottledException.ID) {
				//Display pop up
				Toast.makeText(mContext, "Requests Throttled. Try again later", Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void startProductService() {
		if(!isMyServiceRunning()) {
			Intent intent = new Intent(this, ProductService.class);
			startService(intent);
		}
	}

	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ProductService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	void LOGV(String msg) {
		if(DEBUG) {
			Log.v(TAG, msg);
		}
	}

}
