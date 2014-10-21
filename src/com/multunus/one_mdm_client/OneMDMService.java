package com.multunus.one_mdm_client;

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

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("one-mdm", "Service created");
		
		if(isNetworkAvailable()) {
			getRegistrationTask().execute(getDeviceName());
		}
		else {
			Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("one-mdm", "Service started");
		Toast.makeText(this, "Started service", Toast.LENGTH_LONG).show();
		return super.onStartCommand(intent, flags, startId);
	}
	
	protected String getDeviceName() {
		return Build.MANUFACTURER + " " + Build.MODEL;
	}
	
	protected boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}	
	
	protected RegistrationTask getRegistrationTask() {
		return new RegistrationTask();
	}

}
