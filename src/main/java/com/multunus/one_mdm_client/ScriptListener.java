package com.multunus.one_mdm_client;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScriptListener {

    OneMDMService service;

    public void start(OneMDMService service) {
        this.service = service;
        Log.d("one-mdm", "Starting script polling.");
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                JSONObject script = null;
                int scriptID = 0;
                String command = null;
                boolean rootPermissionRequired = false;
                ScriptExecutionOutput output;

                try {
                    script = (JSONObject) getExecutableScript().get("script");
                } catch (JSONException e) {}

                if(script != null) {
                    try {
                        scriptID = script.getInt("id");
                        command = script.getString("content");
                        rootPermissionRequired = script.getBoolean("rootPermission");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    output = new ScriptExecutor().execute(command, rootPermissionRequired);
                    output.setScriptID(scriptID);
                    try {
                        printMessage(output, script.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new StatusUpdater().execute(output);
                }

            }
        }, 5, Config.SCRIPT_POLLING_INTERVAL, TimeUnit.SECONDS);
    }

    protected JSONObject getExecutableScript() throws JSONException {
        BufferedReader in = null;
        String script = null;

        HttpClient client = new DefaultHttpClient();

        URI website = null;
        try {
            int deviceID = service.getApplicationContext().getSharedPreferences(service.ONE_MDM_PREFERENCE_KEY, service.getApplicationContext().MODE_PRIVATE).getInt("deviceID", 0);
            website = new URI(Config.SERVER + "script/executable?device=" + deviceID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpGet request = new HttpGet();
        request.setURI(website);
        request.setHeader("Authorization", "Bearer " + Config.ACCESS_TOKEN);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.getStatusLine().getStatusCode();

        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer("");
        String l = "";
        String nl = System.getProperty("line.separator");
        try {
            while ((l = in.readLine()) != null) {
                sb.append(l + nl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        script = sb.toString();

        Log.d("one-mdm", script);

        return new JSONObject(script);
    }

    private void printMessage(ScriptExecutionOutput output, String scriptName) {
        Log.d("one-mdm", scriptName);
        Log.d("one-mdm", "Notifying user");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(service.getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("One MDM");
        mBuilder.setContentText(scriptName + " Script Execution " + output.getStatus());
        Intent resultIntent = new Intent(service.getApplicationContext(), OneMDMActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.getApplicationContext());
        stackBuilder.addParentStack(OneMDMActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) service.getSystemService(service.getApplicationContext().NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
