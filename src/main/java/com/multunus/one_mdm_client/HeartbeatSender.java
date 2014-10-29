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

public class HeartbeatSender {
	private static final String HEARTBEAT_URL = Config.SERVER + "heartbeat/create";

	public String send(int deviceID) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost apiPost = new HttpPost(HEARTBEAT_URL);
		JSONObject heartbeatData = new JSONObject();
		StringEntity heartbeatDataEntity = null;
		
		try {
			heartbeatData.put("device", deviceID);
			heartbeatDataEntity = new StringEntity(heartbeatData.toString());
			heartbeatDataEntity.setContentType("application/json;charset=UTF-8");
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		apiPost.setEntity(heartbeatDataEntity);
		
		try {
			HttpResponse httpResponse = httpClient.execute(apiPost);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
