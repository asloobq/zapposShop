package com.asloob.zappos;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.asloob.zappos.network.RequestZappos;
import com.asloob.zappos.sugar.SavedProduct;

import exceptions.RequestThrottledException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class ProductService extends IntentService {

	private static final boolean DEBUG = false;
	private static final String TAG = ProductService.class.getName();

	public ProductService() {
		super(ProductService.class.getName());
		// TODO Auto-generated constructor stub
	}


	boolean checkDiscount(String productId) {
		String url = "http://api.zappos.com/Product/" + productId + "?key=" + getString(R.string.api_key);
		LOGV("url = " + url);
		RequestZappos requestZappos = new RequestZappos();
		String response;
		try {
			response = requestZappos.sendRequestWithPath(url);
		} catch (RequestThrottledException e1) {
			e1.printStackTrace();
			return false;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			Product product = Parser.parseProduct(jsonObject);
			return product.isOnDiscount();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	void createNotification(SavedProduct prod) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Saved Item on SALE !!!")
		.setContentText(prod.getProductName() +  " is now on SALE. Buy it now!");


		Intent resultIntent = new Intent(this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		builder.setContentIntent(resultPendingIntent);


		NotificationManager manager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(Integer.parseInt(prod.getProductId()), builder.build());
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		List<SavedProduct> products = SavedProduct.listAll(SavedProduct.class);
		while(products.size() > 0) {

			for(SavedProduct prod : products) {
				if(checkDiscount(prod.getProductId())) {
					//show notification
					//mark as read
					createNotification(prod);
				} else {
					//move on
				}
			}

		 long endTime = System.currentTimeMillis() + 5*1000;//wait for 1 min
	      while (System.currentTimeMillis() < endTime) {
	          synchronized (this) {
	              try {
	                  wait(endTime - System.currentTimeMillis());

	              } catch (Exception e) {
	              }
	          }
	      }
		}
	}


	void LOGV(String msg) {
		if(DEBUG) {
			Log.v(TAG, msg);
		}
	}

}