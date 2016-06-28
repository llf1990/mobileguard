package com.android.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.AppInfo;
import com.android.mobileguard.engine.AppInfoProvider;
import com.android.mobileguard.utils.SystemInfoUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity implements OnClickListener {
	
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
	private PopupWindow popup;
	private LinearLayout ll_appman_uninstall;
	private LinearLayout ll_appman_start;
	private LinearLayout ll_appman_share;
	private LinearLayout ll_appman_information;
	private AppInfo appinfo;
	private View itemview;
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
		
		
		
		//��listviewע�������������ʵ������textview��̬����
		lv_appmanger_applist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(popup != null){
					popup.dismiss();
					popup = null;
				}
				if(userlist != null && syslist != null){
					if (firstVisibleItem > userlist.size()) {

						tv_appman_desctotal.setText("ϵͳ���� " + syslist.size()
								+ "��");
					} else{
					tv_appman_desctotal.setText("�û����� "+userlist.size()+"��");
				}
			  }
			}
		});
		
		setOnListviewitemClick();
		
		String internalsize = Formatter.formatFileSize(this, SystemInfoUtils.getInternalStorageAvailableSize());
		String externalsize = Formatter.formatFileSize(this, SystemInfoUtils.getExternalStorageAvailableSize());
		
		tv_internal_freesize.setText("�ڴ���ã�"+internalsize);
		tv_external_freesize.setText("sd�����ã�"+externalsize);
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
	//listviewע�����¼���������������
	private void setOnListviewitemClick() {
		lv_appmanger_applist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){//��ʾ�û�Ӧ��������Ŀ
					return;
				}else if(position == userlist.size()+1){//��ʾϵͳӦ��������Ŀ
					return;
				}else if(position <= userlist.size() ){
					int newPosition = position - 1;
					appinfo = userlist.get(newPosition);
				}else{
					int newPosition = position - 2 - userlist.size();
					appinfo = syslist.get(newPosition);
				}
					
					itemview = View.inflate(AppManagerActivity.this,R.layout.item_appman_popwindow, null);
				    if(popup != null){//����Ƿ������������壬����ر�
				    	popup.dismiss();
				    	popup= null;
				    }
				    //��ʼ��������ť
				    ll_appman_uninstall = (LinearLayout) itemview.findViewById(R.id.ll_appman_uninstall);
					ll_appman_start = (LinearLayout) itemview.findViewById(R.id.ll_appman_start);
					ll_appman_share = (LinearLayout) itemview.findViewById(R.id.ll_appman_share);
					ll_appman_information = (LinearLayout) itemview.findViewById(R.id.ll_appman_information);
					//ע�����¼�
					ll_appman_uninstall.setOnClickListener(AppManagerActivity.this);
					ll_appman_start.setOnClickListener(AppManagerActivity.this);
					ll_appman_share.setOnClickListener(AppManagerActivity.this);
					ll_appman_information.setOnClickListener(AppManagerActivity.this);
					
				    //�½�һ��popupʵ������
				    //ע�⣬Ҫpopupwindow���Ŷ�����һ��Ҫ�����ñ�����ɫ����͸��
				    popup= new PopupWindow(itemview,-2,-2);
				    popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location = new int[2];
					view.getLocationInWindow(location);
					popup.showAtLocation(parent, Gravity.LEFT+Gravity.TOP, 200, location[1]);
					//���Ŷ���
					ScaleAnimation sa = new ScaleAnimation(0.3f,1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
					sa.setDuration(500);
					itemview.startAnimation(sa);
			}
			
		}); 
		
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
			
			if(position == 0){//��ʾ�û�Ӧ��������Ŀ
				TextView tv = new TextView(getApplicationContext());
				tv.setText("�û����� "+userlist.size()+" ��");
				tv.setBackgroundColor(Color.GRAY);
				tv.setPadding(10, 5, 5, 5);
				tv.setTextColor(Color.WHITE);
				return tv;
			}else if(position == userlist.size()+1){//��ʾϵͳӦ��������Ŀ
				TextView tv = new TextView(getApplicationContext());
				tv.setText("ϵͳ���� "+syslist.size()+" ��");
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
				holder.tv_appsize.setText("�����С��"+size);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_appman_uninstall:
			uninstallApplication();
			break;

		case R.id.ll_appman_start:
			startApplication();
			break;
		case R.id.ll_appman_share:
			shareApplication();
			break;
		case R.id.ll_appman_information:
			showApplicationInfo();
			break;
		}
	}
	private void showApplicationInfo() {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:"+appinfo.getPackageName()));
		startActivity(intent);
	}
	/**
	 * <action android:name="android.intent.action.SEND" />
	 *  <category android:name="android.intent.category.DEFAULT" />
	 *  <data android:mimeType="text/plain" />
	 */
	private void shareApplication() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ�һ��ܺ��õ����,"+appinfo.getAppName());
		startActivity(intent);
	}
	private void startApplication() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appinfo.getPackageName());
		if(intent != null){
			startActivity(intent);
		}else{
			Toast.makeText(this, "����������", 0).show();
		}
	}
	@Override
	protected void onDestroy() {
		if(popup != null){
			popup.dismiss();
			popup = null;
		}
		super.onDestroy();
	}
	
	public void uninstallApplication(){
		Log.i(TAG,appinfo.toString());
		//ע�����ж�ع㲥������
		AppUninstallReceiver receiver = new AppUninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appinfo.getPackageName()));
		startActivity(intent);
		//����Ӧ�ó����Ƿ�ж�ص����½���UI
		
	}
	private class AppUninstallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = intent.getData().toString();
			String appname = data.replace("package:", "");
			unregisterReceiver(this);
			//����UI����
			AppInfo  appinfotemp = null;
			for (AppInfo appinfo : userlist) {
				if(appname != null && appname.equals(appinfo.getPackageName())){
					appinfotemp = appinfo;
				}
			}
			if(appinfotemp != null){
				userlist.remove(appinfotemp);
			}
			adapter.notifyDataSetChanged();
		}
		
	}
}
