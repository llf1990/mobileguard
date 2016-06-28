package com.android.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.AppInfo;
import com.android.mobileguard.engine.AppInfoProvider;
import com.android.mobileguard.utils.SystemInfoUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManagerActivity extends Activity {
	
	private static final String TAG = "AppManagerActivity";
	private TextView tv_internal_freesize;
	private TextView tv_external_freesize;
	private ListView lv_appmanger_applist;
	private List<AppInfo> list;
	private List<AppInfo> userlist;
	private List<AppInfo> syslist;
	private MyAdapter adapter;
	private ProgressBar pb;
	private TextView tv_appman_loading;
	private TextView tv_appman_desctotal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		
		tv_internal_freesize = (TextView) findViewById(R.id.tv_internal_freesize);
		tv_external_freesize = (TextView) findViewById(R.id.tv_external_freesize);
		lv_appmanger_applist = (ListView) findViewById(R.id.lv_appmanger_applist);
		pb = (ProgressBar) findViewById(R.id.pb_appman_loading);
		tv_appman_loading = (TextView) findViewById(R.id.tv_appman_loading);
		tv_appman_desctotal = (TextView) findViewById(R.id.tv_appman_desctotal);
		
		//给listview注册滚动监听器，实现悬浮textview动态更新
		lv_appmanger_applist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(userlist != null && syslist != null){
					if (firstVisibleItem > userlist.size()) {

						tv_appman_desctotal.setText("系统程序 " + syslist.size()
								+ "个");
					} else{
					tv_appman_desctotal.setText("用户程序 "+userlist.size()+"个");
				}
			  }
			}
		});
		
		String internalsize = Formatter.formatFileSize(this, SystemInfoUtils.getInternalStorageAvailableSize());
		String externalsize = Formatter.formatFileSize(this, SystemInfoUtils.getExternalStorageAvailableSize());
		
		tv_internal_freesize.setText("内存可用："+internalsize);
		tv_external_freesize.setText("sd卡可用："+externalsize);
		adapter = new MyAdapter();
		new Thread(){
			public void run(){
				pb.setVisibility(View.VISIBLE);
				tv_appman_loading.setVisibility(View.VISIBLE);
				list = AppInfoProvider.getAllAppInfo(AppManagerActivity.this);
				userlist = new ArrayList<AppInfo>();
				syslist = new ArrayList<AppInfo>();
				for (AppInfo appinfo : list) {
					if(appinfo.isSystem()){
						syslist.add(appinfo);
					}else{
						userlist.add(appinfo);
					}
				}
				runOnUiThread( new Runnable() {
					public void run() {
					lv_appmanger_applist.setAdapter(adapter);
					pb.setVisibility(View.INVISIBLE);
					tv_appman_loading.setVisibility(View.INVISIBLE);
					}
				
				});
			}
		}.start();
		
		
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size()+2;
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
			AppInfo appinfo;
			if(position == 0){//显示用户应用数量条目
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户程序 "+userlist.size()+" 个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setPadding(10, 5, 5, 5);
				tv.setTextColor(Color.WHITE);
				return tv;
			}else if(position == userlist.size()+1){//显示系统应用数量条目
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统程序 "+syslist.size()+" 个");
				tv.setPadding(10, 5, 5, 5);
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				return tv;
			}else if(position <= userlist.size() ){
				int newPosition = position - 1;
				appinfo = userlist.get(newPosition);
			}else{
				int newPosition = position - 2 - userlist.size();
				appinfo = syslist.get(newPosition);
			}
			if(convertView != null && convertView instanceof RelativeLayout ){
				view = convertView;
				holder = (ViewHolder) view.getTag();
				Log.i(TAG,holder.iv_icon.toString());
			}else{
				view = View.inflate(AppManagerActivity.this, R.layout.item_appmanager, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
				holder.tv_appsize= (TextView) view.findViewById(R.id.tv_appsize);
				holder.iv_install_location = (ImageView) view.findViewById(R.id.iv_appman_installloc);
				view.setTag(holder);
			}
			
			
			holder.iv_icon.setImageDrawable(appinfo.getAppIcon());
			holder.tv_appname.setText(appinfo.getAppName());
			long tempsize =  appinfo.getAppSize();
			Log.i(TAG, String.valueOf(tempsize));
			String size = Formatter.formatFileSize(AppManagerActivity.this,tempsize);
				holder.tv_appsize.setText("程序大小："+size);
			if(appinfo.isInRom()){
				holder.iv_install_location.setImageResource(R.drawable.memory);
			}else{
				holder.iv_install_location.setImageResource(R.drawable.sd);
			}
			
			return view;
		}
		private class ViewHolder{
			ImageView iv_icon;
			TextView tv_appname;
			TextView tv_appsize;
			ImageView iv_install_location;
		}
	}
}
