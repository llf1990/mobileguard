package com.android.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.db.dao.AppLockDBDao;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppLockActivity extends Activity implements OnClickListener {
	
	private TextView tv_applock_unlock;
	private TextView tv_applock_locked;
	private LinearLayout ll_applock_unlock;
	private LinearLayout ll_applock_locked;
	private ListView lv_applock_unlock;
	private ListView lv_applock_locked;
	private TextView tv_unlock_number;
	private TextView tv_locked_number;
	
	private PackageManager pm;//包管理器
	private List<PackageInfo> list;//所有安装包信息
	private List<PackageInfo> lockedlist;//已锁定清单
	
	private List<PackageInfo> unlocklist;//未锁定清单
	
	
	private MyUnlockApapter adapter;
	private MyLockedApapter lockedadapter;
	private AppLockDBDao dao ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		tv_applock_unlock = (TextView) findViewById(R.id.tv_applock_unlock);
		tv_applock_locked = (TextView) findViewById(R.id.tv_applock_locked);
		
		ll_applock_unlock = (LinearLayout) findViewById(R.id.ll_applock_unlock);
		ll_applock_locked = (LinearLayout) findViewById(R.id.ll_applock_locked);
		
		tv_applock_unlock.setOnClickListener(this);
		tv_applock_locked.setOnClickListener(this);
		
		lv_applock_unlock = (ListView) findViewById(R.id.lv_applock_unlock);
		lv_applock_locked = (ListView) findViewById(R.id.lv_applock_locked);
		
		tv_unlock_number = (TextView) findViewById(R.id.tv_unlock_number);
		tv_locked_number = (TextView) findViewById(R.id.tv_locked_number);
		
		pm = getPackageManager();
		dao = new AppLockDBDao(this);
		lockedlist = new ArrayList<PackageInfo>();
		unlocklist = new ArrayList<PackageInfo>();
		list = pm.getInstalledPackages(0);
		for(PackageInfo pinfo : list){
			if(dao.find(pinfo.packageName)){
				lockedlist.add(pinfo);
			}else{
				unlocklist.add(pinfo);
			}
		}
		adapter = new MyUnlockApapter();
		lockedadapter = new MyLockedApapter();
		new Thread(){
			public void run() {
				lv_applock_unlock.setAdapter(adapter);
				lv_applock_locked.setAdapter(lockedadapter);
			};
		}.start();
		/**
		 * 未加锁item注册点击事件
		 */
		lv_applock_unlock.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
					//点击将报名写入数据库
				final PackageInfo pinfo = unlocklist.get(position);
				long result = dao.add(pinfo.packageName);
				if(result != -1){
					Toast.makeText(AppLockActivity.this, "锁定成功", 0).show();
					
					TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
							Animation.RELATIVE_TO_SELF, 0,  Animation.RELATIVE_TO_SELF, 0);
					//动画非阻塞式，新开启线程执行动画，因此需要设置动画结束后移除元素
					ta.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							unlocklist.remove(position);
							lockedlist.add(pinfo);
							adapter.notifyDataSetChanged();
							lockedadapter.notifyDataSetChanged();
						}
					});
					ta.setDuration(300);
					view.startAnimation(ta);
				}else{
					Toast.makeText(AppLockActivity.this, "锁定失败", 0).show();
				}
			}
			
		});
		lv_applock_locked.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
					//点击将报名写入数据库
				final PackageInfo pinfo = lockedlist.get(position);
				int result = dao.delete(pinfo.packageName);
				if(result != 0){
					Toast.makeText(AppLockActivity.this, "解锁成功", 0).show();
					
					TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1f,
							Animation.RELATIVE_TO_SELF, 0,  Animation.RELATIVE_TO_SELF, 0);
					ta.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							lockedlist.remove(position);
							unlocklist.add(pinfo);
							lockedadapter.notifyDataSetChanged();
							adapter.notifyDataSetChanged();							
						}
					});
						ta.setDuration(300);
						view.startAnimation(ta);
				}else{
					Toast.makeText(AppLockActivity.this, "解锁失败", 0).show();
				}
			}
			
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_applock_unlock:
			ll_applock_unlock.setVisibility(View.VISIBLE);
			ll_applock_locked.setVisibility(View.INVISIBLE);
			tv_applock_unlock.setBackgroundResource(R.drawable.titlebar_button_pressed);
			tv_applock_locked.setBackgroundResource(R.drawable.titlebar_button_normal);
			break;

		case R.id.tv_applock_locked:
			ll_applock_locked.setVisibility(View.VISIBLE);
			ll_applock_unlock.setVisibility(View.INVISIBLE);
			tv_applock_locked.setBackgroundResource(R.drawable.titlebar_button_pressed);
			tv_applock_unlock.setBackgroundResource(R.drawable.titlebar_button_normal);
			break;
		}
		
	}
	/**
	 * 已加锁适配器
	 * @author feng
	 *
	 */
	class MyLockedApapter extends BaseAdapter{

		@Override
		public int getCount() {
			tv_locked_number.setText("加锁应用："+lockedlist.size()+"个");
			return lockedlist.size();
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
				view = View.inflate(AppLockActivity.this, R.layout.item_applock_unlock, null);
				holder = new ViewHolder();
				holder.tv_appName = (TextView) view.findViewById(R.id.tv_applock_appname);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_applock_appicon);
				
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.iv_icon.setImageDrawable(lockedlist.get(position).applicationInfo.loadIcon(pm)) ;
			holder.tv_appName.setText(lockedlist.get(position).applicationInfo.loadLabel(pm));
			return view;
		}
		
	}
	/**
	 * 未加锁数据适配器
	 * @author feng
	 *
	 */
	class MyUnlockApapter extends BaseAdapter{

		@Override
		public int getCount() {
			tv_unlock_number.setText("未加锁应用："+unlocklist.size()+"个");
			return unlocklist.size();
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
				view = View.inflate(AppLockActivity.this, R.layout.item_applock_locked, null);
				holder = new ViewHolder();
				holder.tv_appName = (TextView) view.findViewById(R.id.tv_applock_appname);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_applock_appicon);
				
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.iv_icon.setImageDrawable(unlocklist.get(position).applicationInfo.loadIcon(pm)) ;
			holder.tv_appName.setText(unlocklist.get(position).applicationInfo.loadLabel(pm));
			
			return view;
		}
		
	}
	class ViewHolder{
		ImageView iv_icon;
		TextView tv_appName;
		ImageView iv_unlcok;
	}
}
