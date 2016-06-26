package com.android.mobileguard.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mobileguard.utils.PackageInfoUtils;
import com.android.mobileguard.utils.StreamTools;
import com.android.mobileguard.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";
	private static final int SHOW_UPDATE_DIGLOG = 1;
	private static final int ERROR = 0;
	public static final int LOG = 2;
	private String version;// 版本号
	private String downPath;
	private ProgressDialog pd;

	private TextView tv_splash_version;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIGLOG:
				showUpdateDialog(msg.obj.toString());
				break;

			case ERROR:
				Toast.makeText(SplashActivity.this, "错误码：" + msg.obj, 0).show();
				loadMainUI();
				break;
			case LOG:
				SystemClock.sleep(2000);
				loadMainUI();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);

		version = PackageInfoUtils.getPackageVersion(this);
		tv_splash_version.setText("版本号：" + version + "\n©2016版权所有");
		SharedPreferences sp = getSharedPreferences("settingconfig",
				MODE_PRIVATE);
		boolean update = sp.getBoolean("status", true);
		if (update) {
			new Thread(new CheckVersionTask()).start();
		} else {
			new Thread() {
				public void run() {
					loadMainUI();
				}
			};
		}
		copyAddressDB();
	}

	protected void showUpdateDialog(String desc) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("升级提醒");

		builder.setMessage(desc);
		builder.setPositiveButton("立刻升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				pd = new ProgressDialog(SplashActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				HttpUtils http = new HttpUtils();
				File sdDir = Environment.getExternalStorageDirectory();
				File file = new File(sdDir, SystemClock.uptimeMillis() + ".apk");
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Log.i(TAG, "file absolut path" + file.getAbsolutePath());
					http.download(downPath, file.getAbsolutePath(),
							new RequestCallBack<File>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Toast.makeText(SplashActivity.this,
											"下载失败!", 0).show();
									Log.i(TAG, "下载路径" + downPath);

									loadMainUI();
									pd.dismiss();

								}

								@Override
								public void onSuccess(
										ResponseInfo<File> fileinfo) {
									pd.dismiss();
									Toast.makeText(SplashActivity.this,
											"下载成功!", 0).show();
									// 覆盖安装apk文件
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(
											Uri.fromFile(fileinfo.result),
											"application/vnd.android.package-archive");
									startActivity(intent);
								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									super.onLoading(total, current, isUploading);
									pd.setMax((int) total);
									pd.setProgress((int) current);
								}

							});
				} else {
					Toast.makeText(SplashActivity.this, "sd卡不可用，无法自动更新！", 0)
							.show();
					loadMainUI();
				}
			}
		});
		builder.setNegativeButton("暂不升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				loadMainUI();
			}
		});
		builder.show();
	}

	private void loadMainUI() {

		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 获取服务器配置最新版本号
	 * 
	 * @author feng
	 * 
	 */
	private class CheckVersionTask implements Runnable {

		@Override
		public void run() {
			String path = getResources().getString(R.string.url);
			Message msg = Message.obtain();
			long startTime = SystemClock.currentThreadTimeMillis();
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();

				if (code == 200) {
					try {
						InputStream is = conn.getInputStream();
						String result = StreamTools.readStream(is);
						JSONObject jobj = new JSONObject(result);
						String serverVersion = jobj.getString("version");
						String description = jobj.getString("description");
						downPath = jobj.getString("path");
						if (version.equals(serverVersion)) {
							Log.i(TAG, "已是最新版本，无需升级");
							msg.what = LOG;
						} else {
							Log.i(TAG, "低版本，提示用户升级");
							msg.obj = description;
							msg.what = SHOW_UPDATE_DIGLOG;
						}
						
						
					} catch (JSONException e) {
						msg.what = ERROR;
						msg.obj = "code:408";

						e.printStackTrace();

					}

				} else {
					msg.what = ERROR;
					msg.obj = "code:410";
				}
			} catch (MalformedURLException e) {
				msg.what = ERROR;
				msg.obj = "code:405";
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = ERROR;
				msg.obj = "code:503";

				e.printStackTrace();
			} finally {
				long endTime = SystemClock.currentThreadTimeMillis();
				if (endTime - startTime < 3000) {
					SystemClock.sleep(3000 - (endTime - startTime));
				}
				handler.sendMessage(msg);
			}
			

		}

	}
	private void copyAddressDB(){
		final File file = new File(getFilesDir(),"address.db");
		if(file.exists() && file.length() > 0){
			Log.i(TAG,"数据库已存在，无需拷贝");
			
		}else{
			//如果不存在，在子线程进行拷贝
			new Thread(){
				public void run(){
					
					try {
						InputStream is = getAssets().open("address.db");
						FileOutputStream fos = new FileOutputStream(file);
						byte [] buffer = new byte[2048];
						int len;
						while((len = is.read(buffer)) != -1){ 
							fos.write(buffer, 0, len);
						}
						fos.close();
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		
	}

}
