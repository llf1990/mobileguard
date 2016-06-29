package com.android.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
	private FrameLayout rl_process_loadinglayout;
	private List<ProcessInfo> list; // 所有进程信息集合
	private List<ProcessInfo> syslist; // 所有系统进程
	private List<ProcessInfo> userlist; // 所有用户进程
	private Myadpater adapter;
	private ProcessInfo pinfo;
	private CheckBox cb_process_select;
	private Button bt_kill_process; // 清理所有进程

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);
		tv_process_number = (TextView) findViewById(R.id.tv_process_number);
		tv_process_memory = (TextView) findViewById(R.id.tv_process_memory);
		lv_pro_infolist = (ListView) findViewById(R.id.lv_pro_infolist);
		rl_process_loadinglayout = (FrameLayout) findViewById(R.id.rl_process_loadinglayout);
		bt_kill_process = (Button) findViewById(R.id.bt_kill_process);

		updateData();

		lv_pro_infolist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Object obj = lv_pro_infolist.getItemAtPosition(position);
				if (obj != null) {
					ProcessInfo proinfo = (ProcessInfo) obj;
					cb_process_select = (CheckBox) view
							.findViewById(R.id.cb_process_select);
					if (proinfo.isIschecked()) {
						// 当前为勾选，点击应当取消
						cb_process_select.setChecked(false);
						proinfo.setIschecked(false);
					} else {
						// 当前未选中，点击应选中
						cb_process_select.setChecked(true);
						proinfo.setIschecked(true);
					}
				}
			}

		});
		bt_kill_process.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				for (ProcessInfo pinfo : userlist) {
					if (pinfo.isIschecked()) {
						am.killBackgroundProcesses(pinfo.getPackName());
					}
				}
				for (ProcessInfo pinfo : syslist) {
					if (pinfo.isIschecked()) {
						am.killBackgroundProcesses(pinfo.getPackName());
					}
				}
				updateData();
			}

		});

	}

	private void updateData() {
		
		tv_process_number.setText("运行进程：" + getRunningProcess() + "个");

		String avaiMem = Formatter.formatFileSize(this, getMemoryAvaliable());

		tv_process_memory.setText("内存可用:" + avaiMem);
		adapter = new Myadpater();
		new Thread() {
			public void run() {
				rl_process_loadinglayout.setVisibility(View.VISIBLE);
				list = ProcessInfoProvider.getRunningProcessInfo(ProcessManagerActivity.this);
				syslist = new ArrayList<ProcessInfo>();
				userlist = new ArrayList<ProcessInfo>();
				for (ProcessInfo pinfo : list) {
					if (pinfo.isSys()) {
						syslist.add(pinfo);
					} else {
						userlist.add(pinfo);
					}
				}
				Log.i(TAG, String.valueOf(list.size()));
				runOnUiThread(new Runnable() {
					public void run() {
						lv_pro_infolist.setAdapter(adapter);
						rl_process_loadinglayout.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}

	public int getRunningProcess() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	public long getMemoryAvaliable() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// 获取当前系统内存信息，将信息存放在outinfo里面
		MemoryInfo outinfo = new MemoryInfo();
		am.getMemoryInfo(outinfo);
		return outinfo.availMem;
	}

	private class Myadpater extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == userlist.size() + 1) {

				return null;
			} else if (position <= userlist.size()) {
				int newPosition = position - 1;
				pinfo = userlist.get(newPosition);
			} else {
				int newPosition = position - userlist.size() - 2;
				pinfo = syslist.get(newPosition);
			}
			return pinfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户进程:" + userlist.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setPadding(10, 5, 5, 5);
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position == userlist.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统进程:" + syslist.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setPadding(10, 5, 5, 5);
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position <= userlist.size()) {
				int newPosition = position - 1;
				pinfo = userlist.get(newPosition);

			} else {
				int newPosition = position - userlist.size() - 2;
				pinfo = syslist.get(newPosition);
			}

			View view = null;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				holder = new ViewHolder();
				view = View.inflate(ProcessManagerActivity.this,
						R.layout.item_processmanager, null);
				holder.iv_appicon = (ImageView) view
						.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView) view
						.findViewById(R.id.tv_appname);
				holder.tv_appsize = (TextView) view
						.findViewById(R.id.tv_appsize);
				holder.ck_process_select = (CheckBox) view
						.findViewById(R.id.cb_process_select);
				view.setTag(holder);
			}
			holder.iv_appicon.setImageDrawable(pinfo.getAppIcon());
			holder.tv_appname.setText(pinfo.getAppName());
			holder.tv_appsize.setText("占用内存："
					+ Formatter.formatFileSize(ProcessManagerActivity.this,
							pinfo.getMemSize()));
			if (pinfo.isIschecked()) {
				holder.ck_process_select.setChecked(true);
			} else {
				holder.ck_process_select.setChecked(false);
			}

			return view;
		}

		private class ViewHolder {
			ImageView iv_appicon;
			TextView tv_appname;
			TextView tv_appsize;
			CheckBox ck_process_select;
		}

	}
	/**
	 * 一键全选
	 * @param view
	 */
	public void selectall(View view){
		for(ProcessInfo pinfo: userlist){
			pinfo.setIschecked(true);
		}
		for(ProcessInfo pinfo: syslist){
			pinfo.setIschecked(true);
		}
	}
	
	
	public void clearall(View view){
		for(ProcessInfo pinfo: userlist){
			pinfo.setIschecked(!pinfo.isIschecked());
		}
		for(ProcessInfo pinfo: syslist){
			pinfo.setIschecked(!pinfo.isIschecked());
		}
	}
}
