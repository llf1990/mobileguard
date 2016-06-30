package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.utils.MD5Utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	public static final String TAG = "HomeActivity";
	private ImageView iv_home_log;
	private GridView gv_home_item;
	private Dialog dialog;
	private SharedPreferences sp;
	private Editor editor;
	
	
	private String[] names = new String[]{"手机防盗","骚扰拦截","软件管家","进程管理","流量统计","手机杀毒","系统加速","常用功能"};
	private int[] icons = new int[]{R.drawable.sjfd,R.drawable.srlj,R.drawable.rjgj,R.drawable.jcgl,R.drawable.lltj,R.drawable.sjsd,R.drawable.xtjs,R.drawable.cygj};
	private String[] desc = new String[]{"远程定位手机","全面拦截骚扰","管理您的软件","管理正在运行","流量一目了然","病毒无法藏身","系统快如火箭","常用工具大全"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("account", MODE_PRIVATE);//初始化sharedpreferences
		editor = sp.edit();
		iv_home_log = (ImageView)findViewById(R.id.iv_home_log); 
		gv_home_item = (GridView) findViewById(R.id.gv_home_item);
		
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_log, "rotationY",45,90,135,180,225,270,315);
		oa.setDuration(3000);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.RESTART);
		oa.start();
		
		gv_home_item.setAdapter(new HomeAdapter());
		gv_home_item.setOnItemClickListener(new OnItemClickListener() {
		Intent intent;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//判断是否设置密码
					if(isSetPwd()){
						showSetDialog();
					}else{
						showInputDialog();
						Log.i(TAG,"输入登录密码");
					}
					break;

				case 1:
					intent = new Intent(HomeActivity.this,InterceptActivity.class);
					startActivity(intent);
				case 2:
					intent = new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(HomeActivity.this,ProcessManagerActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(HomeActivity.this,TrafficManagerActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent = new Intent(HomeActivity.this,AntvirusActivity.class);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(HomeActivity.this,SysAccelerateActivity.class);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(HomeActivity.this,CommonToolsActivity.class);
					startActivity(intent);
					
					break;
				}
			}

			
		});
	}
	
	protected void showInputDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enterpwd, null);
		builder.setView(view);
		dialog = builder.show();
		final EditText et_enter_pwd = (EditText) view.findViewById(R.id.et_enterdialog_pwd);
		Button bt_enterdialog_confirm = (Button) view.findViewById(R.id.bt_enterdialog_confirm);
		Button bt_enterdialog_cancle = (Button) view.findViewById(R.id.bt_enterdialog_cancle);
		
		bt_enterdialog_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd = et_enter_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(pwd) ){
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
					return;
				}
				String password = sp.getString("password", null);
				if(!MD5Utils.encode(pwd).equals(password)){
					Toast.makeText(HomeActivity.this, "密码错误", 0).show();
					return;
				}else{
					dialog.dismiss();
					Log.i(TAG, "进入找回主界面");
					boolean config = sp.getBoolean("configed", false);
					if(config){
						Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
						startActivity(intent);
					}else{
						Intent intent = new Intent(HomeActivity.this, SetFindStepOne.class);
						startActivity(intent);
					}
					
					
				}
			}
		});
		bt_enterdialog_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
	}

	protected void showSetDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setpwd, null);
		builder.setView(view);
		dialog = builder.show();
		final EditText et_setdialog_pwd = (EditText) view.findViewById(R.id.et_setdialog_pwd);
		final EditText et_setdialog_confirm = (EditText) view.findViewById(R.id.et_setdialog_confirm);
		Button bt_setdialog_confirm = (Button) view.findViewById(R.id.bt_setdialog_confirm);
		Button bt_setdialog_cancle = (Button) view.findViewById(R.id.bt_setdialog_cancle);
		
		bt_setdialog_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd = et_setdialog_pwd.getText().toString().trim();
				String pwdConfirm = et_setdialog_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm)){
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
					return;
				}
				if(!pwd.equals(pwdConfirm)){
					Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
					return;
				}
				
				editor.putString("password", MD5Utils.encode(pwd));
				editor.commit();
				dialog.dismiss();
				showInputDialog();
			}
		});
		
		bt_setdialog_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private class HomeAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return names.length;
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
				View view = View.inflate(HomeActivity.this, R.layout.item_home, null);
				ImageView iv_homeitem_bgpic = (ImageView) view.findViewById(R.id.tv_homeitem_bgpic);
				TextView tv_homeitem_title = (TextView) view.findViewById(R.id.tv_homeitem_title);
				TextView tv_homeitem_desc = (TextView) view.findViewById(R.id.tv_homeitem_desc);
				iv_homeitem_bgpic.setImageResource(icons[position]);
				tv_homeitem_title.setText(names[position]);
				tv_homeitem_desc.setText(desc[position]);
				
				return view;
		}
		
	}
	/**
	 * 点击进入设置界面
	 * @param view
	 */
	public void enterSettingActivity(View view){
		Intent intent = new Intent(this,SettingActivity.class);
		startActivity(intent);
	}
	/**
	 * ture 表示为空 false 表示不为空
	 * @return
	 */
	public boolean isSetPwd(){
		String pwd = sp.getString("password", "");
		if(TextUtils.isEmpty(pwd)){
			return true;
		}else{
			return false;
		}
		
		
	}
}
