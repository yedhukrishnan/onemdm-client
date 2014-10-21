package com.multunus.one_mdm_client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class OneMDMService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("one-mdm", "Service created");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("one-mdm", "Service started");
		Toast.makeText(this, "Started service", Toast.LENGTH_LONG).show();
	
		return super.onStartCommand(intent, flags, startId);
	}

}
