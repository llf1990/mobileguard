package com.android.mobileguard.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mobileguard.utils.PackageInfoUtils;
import com.android.mobileguard.utils.StreamTools;
import com.androidpro.mobilesafety.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SplashActivity extends Activity {
	
	
	
	private TextView tv_splash_version;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        
        String version = PackageInfoUtils.getPackageVersion(this);
        tv_splash_version.setText("版本号："+version+"\n©2016版权所有");
        new Thread(new CheckVersionTask()).start();
        
    }

    private class CheckVersionTask implements Runnable{

		private static final String TAG = "SplashActivity";

		@Override
		public void run() {
			String path = getResources().getString(R.string.url);
			
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();
				
				if(code == 200){
					try {
						InputStream is = conn.getInputStream() ;
						String result = StreamTools.readStream(is);
						JSONObject jobj	= new JSONObject(result);
						String serverVersion = jobj.getString("version");
						Log.i(TAG,serverVersion);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
    	
    }
    
}

