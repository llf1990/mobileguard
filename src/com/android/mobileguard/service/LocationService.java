package com.android.mobileguard.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {
	private LocationManager lm;
	private MyLocationListener listener;
	
	public LocationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		
		
		return null;
	}
	@Override
	public void onCreate() {
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> list = lm.getAllProviders();
		
		if(list.size() > 0 && list.contains("gps")){
			listener = new MyLocationListener();
			lm.requestLocationUpdates("gps", 0, 0, listener);
		}
		
		super.onCreate();
	}
	private class MyLocationListener implements LocationListener{
		@Override
		public void onLocationChanged(Location location) {
			String la = "la:"+location.getLatitude()+"\n";
			String lo = "lo:"+location.getLongitude();
			SharedPreferences sp = getSharedPreferences("account", MODE_PRIVATE);
			String safeNumber = sp.getString("safePhoneNum", "");
			SmsManager.getDefault().sendTextMessage(safeNumber, null, la+lo, null, null);
			lm.removeUpdates(listener);
			listener = null;
			stopSelf();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}
}
