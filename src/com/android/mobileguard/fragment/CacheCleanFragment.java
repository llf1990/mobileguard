package com.android.mobileguard.fragment;

import com.android.mobileguard.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CacheCleanFragment extends Fragment {
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return View.inflate(getActivity(), R.layout.fragment_cleancache, null);
	}
}
