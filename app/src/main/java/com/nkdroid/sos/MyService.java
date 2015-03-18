package com.nkdroid.sos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	private static final String TAG = "MyService";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {

			@Override
			public void OnShake() {
				Toast.makeText(getApplicationContext(), "Device shaken!", Toast.LENGTH_SHORT).show();
			}
		});
		ShakeDetector.updateConfiguration(10,10);
		ShakeDetector.start();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		ShakeDetector.stop();
		ShakeDetector.destroy();
	}
}
