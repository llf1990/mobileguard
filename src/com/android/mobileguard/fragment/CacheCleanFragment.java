package com.android.mobileguard.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.android.mobileguard.R;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CacheCleanFragment extends Fragment {
	protected static final int CACHE_RESULT = 0;
	protected static final int SCANNING = 1;
	protected static final int SCAN_FINISH = 2;
	protected static final String TAG = "CacheCleanFragment";
	private ProgressBar pb_cache_process;
	private TextView tv_cache_scan_status;
	private LinearLayout ll_frame_cache;
	private PackageManager pm;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			CacheInfo cache;
			switch (msg.what) {
			case CACHE_RESULT:
				cache = (CacheInfo) msg.obj;
				Log.i(TAG, cache.toString());
				String cachesize = Formatter.formatFileSize(getActivity(), cache.cacheSize);
				TextView tv_cache_info = new TextView(getActivity());
				tv_cache_info.setText(cache.appname+"缓存大小:"+cachesize);
				ll_frame_cache.addView(tv_cache_info, 0);
				break;

			case SCANNING:
				tv_cache_scan_status.setText("正在扫描："+msg.obj.toString());
				break;
			case SCAN_FINISH:
				tv_cache_scan_status.setText("扫描完成");
				break;
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  View.inflate(getActivity(), R.layout.fragment_cleancache, null);
		pb_cache_process = (ProgressBar) view.findViewById(R.id.pb_cache_process);
		tv_cache_scan_status = (TextView) view.findViewById(R.id.tv_cache_scan_status);
		ll_frame_cache = (LinearLayout) view.findViewById(R.id.ll_frame_cache);
		pm = getActivity().getPackageManager();
		
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		
		new Thread(){
			@Override
			public void run() {
				int progress = 0;
				List<PackageInfo> list =  pm.getInstalledPackages(0);
				pb_cache_process.setMax(list.size());
				for(PackageInfo pinfo : list){
					getCacheSize(pinfo.packageName);
					Message msg = Message.obtain();
					try {
						msg.obj = pm.getApplicationInfo(pinfo.packageName, 0).loadLabel(pm).toString();
						Log.i(TAG, msg.obj.toString());
						msg.what = SCANNING;
						handler.sendMessage(msg);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progress++;
					pb_cache_process.setProgress(progress);
					SystemClock.sleep(20);
				}
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	
	/**
	 * 获取缓存数据
	 */
	private void getCacheSize(final String packName){
		Method[] methods = PackageManager.class.getDeclaredMethods();
		for(Method method : methods){
			if("getPackageSizeInfo".equals(method.getName())){
				try {
					method.invoke(pm , packName, new IPackageStatsObserver.Stub() {
						
						@Override
						public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
								throws RemoteException {
							// TODO Auto-generated method stub
							long cacheSize = pStats.cacheSize;
							if(cacheSize > 0){
								CacheInfo cache = new CacheInfo();
								cache.cacheSize = cacheSize;
								cache.packName = packName;
								try {
									cache.appname = pm.getApplicationInfo(packName, 0).loadLabel(pm).toString();
								} catch (NameNotFoundException e) {
									e.printStackTrace();
								}
								Message msg = Message.obtain();
								msg.what = CACHE_RESULT;
								msg.obj = cache;
								handler.sendMessage(msg);
		
								}
						}
					});
				}catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}finally{
				
				}
			}
					
		}
	}				
	private class CacheInfo{
		String packName;
		String appname;
		long cacheSize;
		@Override
		public String toString() {
			return packName+":"+cacheSize;
		}
	}
}

