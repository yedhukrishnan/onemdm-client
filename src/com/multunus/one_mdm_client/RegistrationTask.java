package com.multunus.one_mdm_client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class RegistrationTask extends AsyncTask<String, Void, String> {
	
	private static final String REGISTRATION_URL = "http://onemdm.herokuapp.com/device/create";

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost apiPost = new HttpPost(REGISTRATION_URL);
		JSONObject registrationData = new JSONObject();
		StringEntity registrationDataEntity = null;
		String deviceName = params[0];
		
		try {
			registrationData.put("name", deviceName);
			registrationDataEntity = new StringEntity(registrationData.toString());
			registrationDataEntity.setContentType("application/json;charset=UTF-8");
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		apiPost.setEntity(registrationDataEntity);
		try {
			HttpResponse httpResponse = httpClient.execute(apiPost);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return deviceName;
	}

} 
