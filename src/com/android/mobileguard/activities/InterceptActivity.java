package com.android.mobileguard.activities;

import java.util.ArrayList;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.BlackNumber;
import com.android.mobileguard.db.dao.InterceptDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InterceptActivity extends Activity {
	private ListView lv_intercept_blacklist;
	private RelativeLayout rl_intercept_loadingpro;
	private ImageView iv_intercept_empty;
	private ArrayList<BlackNumber> list;
	private InterceptDao dao;
	private BlackNumberAdapter adapter;
	public InterceptActivity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intercept);
		lv_intercept_blacklist = (ListView) findViewById(R.id.lv_intercept_blacklist);
		rl_intercept_loadingpro = (RelativeLayout) findViewById(R.id.rl_intercept_loadingpro);
		iv_intercept_empty = (ImageView) findViewById(R.id.iv_intercept_empty);
		rl_intercept_loadingpro.setVisibility(View.VISIBLE);
		dao = new InterceptDao(InterceptActivity.this);
		new Thread(){
			public void run() {
				list = dao.findAll();
				adapter = new BlackNumberAdapter();
				runOnUiThread(new  Runnable() {
					public void run() {
						lv_intercept_blacklist.setAdapter(adapter);
						rl_intercept_loadingpro.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();
		
		
	}
	private class BlackNumberAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			int count = list.size();
			if(count> 0 ){
				iv_intercept_empty.setVisibility(View.INVISIBLE);
			}else{//黑名单没有数据提醒用户添加
				iv_intercept_empty.setVisibility(View.VISIBLE);
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder ; 
			View view;
			//复用历史view，减少创建个数
			if(convertView == null){
				view = View.inflate(InterceptActivity.this, R.layout.item_blacknumber, null);
				//相当于记录表，记录view对象的引用
				holder = new ViewHolder();
				holder.tv_holder_phoneNumber = (TextView) view.findViewById(R.id.tv_listitem_number);
				holder.tv_holder_mode = (TextView) view.findViewById(R.id.tv_listitem_mode);
				holder.iv_holder_del = (ImageView) view.findViewById(R.id.iv_listitem_del);
				view.setTag(holder);
				
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			final BlackNumber bn = list.get(position);
			holder.tv_holder_phoneNumber.setText(bn.getBlackNumber());
			String mode = bn.getMode();
			if("1".equals(mode)){
				holder.tv_holder_mode.setText("全部拦截");
			}
			if("2".equals(mode)){
				holder.tv_holder_mode.setText("短信拦截");
			}
			if("3".equals(mode)){
				holder.tv_holder_mode.setText("电话拦截");
			}
			//点击删除图标删除数据库记录
			holder.iv_holder_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int flag = dao.delete(bn.getBlackNumber());
					if(flag != 0){
						Toast.makeText(InterceptActivity.this, "删除成功", 0).show();
						//将记录从list列表中删除，并通知adapter更新UI,不适用再次遍历数据库方法
						list.remove(position);
						adapter.notifyDataSetChanged();
					}else{
						Toast.makeText(InterceptActivity.this, "删除失败", 0).show();
					}
					
				}
			});
			
			return view;
		}
		
		private class ViewHolder {
			TextView tv_holder_phoneNumber;
			TextView tv_holder_mode;
			ImageView iv_holder_del;
		}
		
	}
	
	public void addBlackNumber(View view){
		Intent intent = new Intent(InterceptActivity.this,addBlackNumberActivity.class);
		startActivityForResult(intent, 0);
	}
	/**
	 * 返回添加界面blacknumber,mode,isSuccess
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null){
			String addBlackNumber = data.getStringExtra("blacknumber");
			String addMode = data.getStringExtra("mode");
			boolean isSuccess = data.getBooleanExtra("isSuccess", false);
			
			BlackNumber bn = new BlackNumber();
			bn.setBlackNumber(addBlackNumber);
			bn.setMode(addMode);
			//将新的记录添加到list数据，并通知adapter更新UI数据
			list.add(bn);
			adapter.notifyDataSetChanged();
			if(isSuccess){
				Toast.makeText(InterceptActivity.this, "添加成功", 0).show();
			}else{
				Toast.makeText(InterceptActivity.this, "添加失败", 0).show();
			}
		}
//		super.onActivityResult(requestCode, resultCode, data);
	}
}

