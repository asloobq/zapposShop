package com.asloob.zappos;

import java.util.ArrayList;

import com.asloob.zappos.network.RequestZappos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;


public class MainActivity extends ActionBarActivity {

	final boolean DEBUG = true;
	final String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String query = "boots";
		LOGV("query =" + query);
		ProductSearch search = new ProductSearch();
		search.execute(query);
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

	class ProductSearch extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... query) {
			//form url
			String url = "http://api.zappos.com/Search?term=" + query[0] + "&key=" + getString(R.string.api_key);
			LOGV("url = " + url);
			RequestZappos requestZappos = new RequestZappos();
			String response = requestZappos.sendRequestWithPath(url);
			ArrayList<Product> products = Parser.parseProducts(response);
			LOGV("size = " + products.size());
			return null;
		}

	}

	void LOGV(String msg) {
		if(DEBUG) {
			Log.v(TAG, msg);
		}
	}

}
