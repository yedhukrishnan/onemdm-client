package com.multunus.one_mdm_client;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class OneMDMService extends Service {

	static final String ONE_MDM_PREFERENCE_KEY = "oneMdm";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("one-mdm", "Service created");

		if(!isDeviceRegistered()) {
			if(isNetworkAvailable()) {
				getRegistrationTask().execute(getDeviceName());
			}
			else {
				Toast.makeText(this, "Oops! Something went wrong. Please check your internet connectivty and restart the app.", Toast.LENGTH_LONG).show();
				this.stopSelf();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);	
		Log.d("one-mdm", "Service started");
		Notification notification = new Notification.Builder(this)
		.setContentTitle("One MDM Service")
		.setContentText("One MDM Service is running")
		.setSmallIcon(R.drawable.ic_launcher)
		.build();
		startForeground(400, notification);
		return START_STICKY;
	}

	protected String getDeviceName() {
		return Build.MANUFACTURER + " " + Build.MODEL;
	}

	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

		Log.d("one-mdm", "network available? " + isConnected);
		return isConnected;
	}	

	protected RegistrationTask getRegistrationTask() {
		return new RegistrationTask(this);
	}

	protected void setRegistrationInfo(String deviceName) {
		this.getSharedPreferences(ONE_MDM_PREFERENCE_KEY, MODE_PRIVATE).edit().putString("name", deviceName).commit();
	}

	public boolean isDeviceRegistered() {
		return !this.getSharedPreferences(ONE_MDM_PREFERENCE_KEY, MODE_PRIVATE).getString("name", "").isEmpty();
	}

}
