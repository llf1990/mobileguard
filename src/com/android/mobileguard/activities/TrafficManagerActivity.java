package com.android.mobileguard.activities;

import java.text.Normalizer.Form;

import com.android.mobileguard.R;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	
	
	private TextView tv_traffic_total;
	private TextView tv_traffic_mobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activtiy_trafficmanager);
		
		tv_traffic_total = (TextView) findViewById(R.id.tv_traffic_total);
		tv_traffic_mobile = (TextView) findViewById(R.id.tv_traffic_mobile);
		//获取上传流量总数
		long ttx = TrafficStats.getTotalTxBytes();
		//获取接收流量总数
		long trx = TrafficStats.getTotalRxBytes();
		long total = ttx + trx;
		tv_traffic_total.setText(Formatter.formatFileSize(this, total));
		//获取移动接收流量
		long mrx = TrafficStats.getMobileRxBytes();
		//获取移动发送流量
		long mtx = TrafficStats.getMobileTxBytes();
		long mobile = mrx + mtx;
		tv_traffic_mobile.setText(Formatter.formatFileSize(this, mobile));
		/**
		 *TrafficStats.getUidTxBytes(uid)根据uid获取应用的流量信息
		 *TrafficStats.getUidRxBytes(uid)
		 */
		
		
		
	}
}
