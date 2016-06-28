package com.android.mobileguard.activities;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.ProcessInfo;
import com.android.mobileguard.engine.ProcessInfoProvider;



public class ProcessManagerActivity extends Activity {
	protected static final String TAG = "ProcessManagerActivity";
	private TextView tv_process_number;
	private TextView tv_process_memory;
	private ListView lv_pro_infolist;
	private RelativeLayout rl_process_loadinglayout;
	private List<ProcessInfo> list; //所有进程信息集合
	private Myadpater adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);
		tv_process_number = (TextView) findViewById(R.id.tv_process_number);
		tv_process_memory = (TextView) findViewById(R.id.tv_process_memory);
		lv_pro_infolist = (ListView) findViewById(R.id.lv_pro_infolist);
		rl_process_loadinglayout = (RelativeLayout) findViewById(R.id.rl_process_loadinglayout);
		
		tv_process_number.setText("运行进程："+getRunningProcess()+"个");
		
		String avaiMem = Formatter.formatFileSize(this, getMemoryAvaliable());
		
		tv_process_memory.setText("内存可用:"+avaiMem);
		adapter = new Myadpater();
		new Thread(){
			public void run(){
				rl_process_loadinglayout.setVisibility(View.VISIBLE);
				list = ProcessInfoProvider.getRunningProcessInfo(ProcessManagerActivity.this);
				Log.i(TAG, String.valueOf(list.size()));
				runOnUiThread( new Runnable() {
					public void run() {
						lv_pro_infolist.setAdapter(adapter);
						rl_process_loadinglayout.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}
	
	public int getRunningProcess(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	
	public long getMemoryAvaliable(){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//获取当前系统内存信息，将信息存放在outinfo里面
		MemoryInfo outinfo = new MemoryInfo();
		am.getMemoryInfo(outinfo);
		return outinfo.availMem;
	}
	private class Myadpater extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				view = View.inflate(ProcessManagerActivity.this, R.layout.item_processmanager, null);
				holder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
				holder.tv_appsize = (TextView) view.findViewById(R.id.tv_appsize);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
				holder.iv_appicon.setImageDrawable(list.get(position).getAppIcon());
				holder.tv_appname.setText(list.get(position).getAppName());
				holder.tv_appsize.setText("占用内存："+Formatter.formatFileSize(ProcessManagerActivity.this, list.get(position).getMemSize()));
			
			return view;
		}
		
		private class ViewHolder{
			ImageView iv_appicon;
			TextView tv_appname;
			TextView tv_appsize;
		}
		
	}
}


