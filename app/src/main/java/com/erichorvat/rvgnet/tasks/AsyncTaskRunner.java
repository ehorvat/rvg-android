package com.erichorvat.rvgnet.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.erichorvat.rvgnet.interfaces.OnChange;
import com.erichorvat.rvgnet.interfaces.OnStart;
import com.erichorvat.rvgnet.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class AsyncTaskRunner extends AsyncTask<String, String, String>{
	// connection timeout, in milliseconds (waiting to connect)
	private static final int CONN_TIMEOUT = 10000;

	// socket timeout, in milliseconds (waiting for data_right)
	private static final int SOCKET_TIMEOUT = 10000;
	private static final String CONN_TIMEOUT_MSG = "timeout";

	private int taskType = GET_TASK;
	private Context mContext;
	private Activity a;
	public static final int POST_TASK = 1;
	public static final int GET_TASK = 2;
	private final String method;
	Object listener;
	private String data;

	String result = "";


	public AsyncTaskRunner(int taskType, Context mContext, Activity a, String method, Object listener) {

		this.taskType = taskType;
		this.mContext = mContext;
		this.a = a;
		this.method = method;
		this.listener = listener;
	}

	public void setStore(String data) {

		this.data = data;
	}
	
	protected String doInBackground(String... urls) {
		String url = urls[0];
		int l = url.length();

		HttpResponse response;
		try {
			response = doResponse(url);

			if (response == null) {

				return result;
			} else {
				if (response.getEntity() != null) {
					
					result = inputStreamToString(response.getEntity()
							.getContent());
					

					}

				}

		} catch (UnknownHostException e) {
			result = "connError";
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
            result = "connError";
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            result = "connError";
            e.printStackTrace();
        } catch (IOException e) {
            result = "connError";
            e.printStackTrace();
        }
        return result;
	}

	protected void onPostExecute(String result) {
		
		if(method.equals(Constants.ON_START)){
			((OnStart) listener).onStart(result);
		}
        if(method.equals(Constants.ON_CHANGE)){
            ((OnChange) listener).onChange(result);
        }
	}

	@Override
	protected void onPreExecute() {
	
	}

	// Establish connection and socket (data_right retrieval) timeouts
	private HttpParams getHttpParams() {

		HttpParams htpp = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

		return htpp;
	}

	private HttpResponse doResponse(String url) throws ClientProtocolException,
			IOException, ConnectTimeoutException {
		// Use our connection and data_right timeouts as parameters for our
		// DefaultHttpClient
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());
		HttpPost httppost = null;
		StringEntity entity;
		HttpResponse response = null;

		switch (taskType) {

		case POST_TASK:
			httppost = new HttpPost(url);
			// Add parameters
			entity = new StringEntity(data);
			//httppost.setHeader("Content-type", "application/json");
			//httppost.setEntity(entity);

            response = httpclient.execute(httppost);

			break;

		case GET_TASK:
			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);
			break;
		}

		return response;
	}

	private String inputStreamToString(InputStream is) {

		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
		}

		// Return full string
		return total.toString();
	}

	
}
