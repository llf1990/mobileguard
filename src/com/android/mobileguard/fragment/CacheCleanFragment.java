package com.android.mobileguard.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.android.mobileguard.R;

import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CacheCleanFragment extends Fragment {
	protected static final int CACHE_RESULT = 0;
	protected static final int SCANNING = 1;
	protected static final int SCAN_FINISH = 2;
	protected static final String TAG = "CacheCleanFragment";
	private ProgressBar pb_cache_process;
	private TextView tv_cache_scan_status;
	private LinearLayout ll_frame_cache;
	private Button bt_cache_cleanall;
	
	
	private PackageManager pm;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			final CacheInfo cache;
			switch (msg.what) {
			case CACHE_RESULT:
				cache = (CacheInfo) msg.obj;
				Log.i(TAG, cache.toString());
				String cachesize = Formatter.formatFileSize(getActivity(), cache.cacheSize);
				View view = View.inflate(getActivity(), R.layout.item_clean_cache, null);
				view.setBackgroundResource(R.drawable.dg_cancel_selector);
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent("android.setting.APPLICATION_DETAILS_SETTINGS");
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setData(Uri.parse("package:"+cache.packName));
						startActivity(intent);
					}
				});
				ImageView iv_cache_icon = (ImageView) view.findViewById(R.id.iv_cache_appicon);
				TextView tv_cache_appname = (TextView) view.findViewById(R.id.tv_cache_appname);
				TextView tv_cache_appsize = (TextView) view.findViewById(R.id.tv_cache_size);
				iv_cache_icon.setImageDrawable(cache.appicon);
				tv_cache_appname.setText(cache.appname);
				tv_cache_appsize.setText(cachesize);
				ll_frame_cache.addView(view, 0);
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
		bt_cache_cleanall = (Button) view.findViewById(R.id.bt_cache_cleanall);
		pm = getActivity().getPackageManager();
		
		
		bt_cache_cleanall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//模拟一个超级大空间请求
				Method[] methods = PackageManager.class.getDeclaredMethods();
				for (Method method : methods) {
					if("freeStorageAndNotify".equals(method.getName())){
						try {
							method.invoke(pm, Integer.MAX_VALUE,new IPackageDataObserver.Stub(){

								@Override
								public void onRemoveCompleted(String packageName,
										boolean succeeded) throws RemoteException {
									
								}
								
							});
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Toast.makeText(getActivity(), "全部清理完毕", 0).show();
					ll_frame_cache.removeAllViews();
					tv_cache_scan_status.setText("系统已经优化到最佳状态");
				}
			}
		});
		
		return view;
	}
	@Override
	public void onStart() {
		
		ll_frame_cache.removeAllViews();
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
									cache.appicon = pm.getApplicationInfo(packName, 0).loadIcon(pm);
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
		Drawable appicon;
		@Override
		public String toString() {
			return packName+":"+cacheSize;
		}
	}
	class CleanCacheObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			System.out.println(packageName+"....." +succeeded);
		}
		
	}
}

