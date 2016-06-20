package com.androidpro.mobilesafety;

import com.androidbasic.mobilesafety.utils.PackageInfoUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
        tv_splash_version.setText("°æ±¾ºÅ£º"+version);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
}
