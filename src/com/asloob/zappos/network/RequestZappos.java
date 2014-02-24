package com.asloob.zappos.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import exceptions.RequestThrottledException;

import android.util.Log;

public class RequestZappos {

	final String TAG = RequestZappos.class.getName();

public String sendRequestWithPath(String path) throws RequestThrottledException {

		String result = "";
		try {
			HttpGet request = null;
			request = new HttpGet(path);

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParameters, schReg);

			HttpClient client = new DefaultHttpClient(conMgr, httpParameters);

			// HttpClient client1 = new DefaultHttpClient();
			HttpResponse response = null;
			int retry = 3;
			while (retry > 0) {
				try {
					response = client.execute(request);
					break;

				} catch (Exception e) {
					e.printStackTrace();
					retry--;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			StatusLine line = response.getStatusLine();
			Log.d(TAG, "status " + line.getStatusCode());
			if(line.getStatusCode() == 401) {

				throw new RequestThrottledException();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			StringBuilder strBuilder = new StringBuilder();
			String singleLine = "";

			while ((singleLine = in.readLine()) != null) {
			    strBuilder.append(singleLine);
			}
			result = strBuilder.toString();
			Log.v(TAG, result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
