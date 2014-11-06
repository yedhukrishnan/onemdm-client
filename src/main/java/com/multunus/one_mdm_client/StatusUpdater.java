package com.multunus.one_mdm_client;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StatusUpdater extends AsyncTask<ScriptExecutionOutput, Void, String> {

    String url = "";

    @Override
    protected String doInBackground(ScriptExecutionOutput... scriptExecutionOutput) {
        ScriptExecutionOutput output = scriptExecutionOutput[0];
        int scriptID = output.getScriptID();
        url = generateUrl(scriptID);
        Log.d("one-mdm", "Sending status to " + url);
        String status = output.getStatus();
        String executionOutput = output.getResult();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost apiPost = new HttpPost(url);
        JSONObject registrationData = new JSONObject();
        StringEntity registrationDataEntity = null;

        try {
            registrationData.put("status", status);
            registrationData.put("output", executionOutput);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected String generateUrl(int id) {
        return Config.SERVER + "script/" + id;
    }

}
