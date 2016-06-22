package com.android.mobileguard.activities;

import com.android.mobileguard.R;

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
	
	private String[] names = new String[]{"�ֻ�����","ɧ������","����ܼ�","���̹���","����ͳ��","�ֻ�ɱ��","ϵͳ����","���ù���"};
	private int[] icons = new int[]{R.drawable.sjfd,R.drawable.srlj,R.drawable.rjgj,R.drawable.jcgl,R.drawable.lltj,R.drawable.sjsd,R.drawable.xtjs,R.drawable.cygj};
	private String[] desc = new String[]{"Զ�̶�λ�ֻ�","ȫ������ɧ��","�����������","������������","����һĿ��Ȼ","�����޷�����","ϵͳ������","���ù��ߴ�ȫ"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("account", MODE_PRIVATE);//��ʼ��sharedpreferences
		
		iv_home_log = (ImageView)findViewById(R.id.iv_home_log); 
		gv_home_item = (GridView) findViewById(R.id.gv_home_item);
		
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_home_log, "rotationY",45,90,135,180,225,270,315);
		oa.setDuration(4500);
		oa.setRepeatCount(ObjectAnimator.INFINITE);
		oa.setRepeatMode(ObjectAnimator.RESTART);
		oa.start();
		
		gv_home_item.setAdapter(new HomeAdapter());
		gv_home_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//�ж��Ƿ���������
					if(isSetPwd()){
						showSetDialog();
					}else{
						showInputDialog();
						Log.i(TAG,"�����¼����");
					}
					break;

				case 1:
					
					break;
				}
			}

			
		});
	}
	
	protected void showInputDialog() {
		
	}

	protected void showSetDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setpwd, null);
		builder.setView(view);
		builder.setCancelable(false);
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
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", 0).show();
					return;
				}
				if(!pwd.equals(pwdConfirm)){
					Toast.makeText(HomeActivity.this, "���벻һ��", 0).show();
					return;
				}
				Editor editor = sp.edit();
				editor.putString("password", pwd);
				editor.commit();
				dialog.dismiss();
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
	 * ����������ý���
	 * @param view
	 */
	public void enterSettingActivity(View view){
		Intent intent = new Intent(this,SettingActivity.class);
		startActivity(intent);
	}
	/**
	 * ture ��ʾΪ�� false ��ʾ��Ϊ��
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
