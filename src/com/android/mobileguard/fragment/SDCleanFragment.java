package com.android.mobileguard.fragment;

import java.io.File;

import com.android.mobileguard.R;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SDCleanFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			return View.inflate(getActivity(), R.layout.fragment_cleansd, null);
		}
		
		
		private void oncrete() {
			// TODO Auto-generated method stub
			File file = Environment.getExternalStorageDirectory();
			File[] files = file.listFiles();
			for (File tfile : files) {
				if(tfile.isFile()){
					//���ļ�����չ����.tmp .temp��������
				}else{
					//���ļ��У��ݹ����
				}
			}
			
		}
}
