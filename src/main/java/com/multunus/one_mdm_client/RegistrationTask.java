package com.multunus.one_mdm_client;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class RegistrationTask extends AsyncTask<String, Void, Integer> {
	
	private static final String REGISTRATION_URL = Config.SERVER + "device/create";
	private OneMDMService service;

	public RegistrationTask(OneMDMService service) {
		this.service = service;
	}

	@Override
	protected Integer doInBackground(String... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost apiPost = new HttpPost(REGISTRATION_URL);
		JSONObject registrationData = new JSONObject();
		StringEntity registrationDataEntity = null;
		int deviceID = 0;
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
        apiPost.setHeader("Authorization", "Bearer " + Config.ACCESS_TOKEN);
		
		try {
			HttpResponse httpResponse = httpClient.execute(apiPost);
			String responseBody = "";
			
			String line;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			while ((line = bufferedReader.readLine()) != null) {
				responseBody += line;
			}
			
			deviceID = new JSONObject(responseBody).getInt("id");
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return deviceID;
	}

	@Override
	protected void onPostExecute(Integer deviceID) {
		service.setRegistrationInfo(deviceID);
		super.onPostExecute(deviceID);
	}

} 
