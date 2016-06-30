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
		//��ȡ�ϴ���������
		long ttx = TrafficStats.getTotalTxBytes();
		//��ȡ������������
		long trx = TrafficStats.getTotalRxBytes();
		long total = ttx + trx;
		tv_traffic_total.setText(Formatter.formatFileSize(this, total));
		//��ȡ�ƶ���������
		long mrx = TrafficStats.getMobileRxBytes();
		//��ȡ�ƶ���������
		long mtx = TrafficStats.getMobileTxBytes();
		long mobile = mrx + mtx;
		tv_traffic_mobile.setText(Formatter.formatFileSize(this, mobile));
		/**
		 *TrafficStats.getUidTxBytes(uid)����uid��ȡӦ�õ�������Ϣ
		 *TrafficStats.getUidRxBytes(uid)
		 */
		
		
		
	}
}
