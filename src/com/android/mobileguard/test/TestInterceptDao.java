package com.android.mobileguard.test;

import com.android.mobileguard.db.dao.InterceptDao;

import android.test.AndroidTestCase;

public class TestInterceptDao extends AndroidTestCase {

	public TestInterceptDao() {
		
	}
	
	
	public void testAdd(){
		InterceptDao dao = new InterceptDao(getContext());
		for(int i = 1 ; i < 10 ; i++){
			dao.add("1388888888"+i, "1");
		}
	}
	public void testdelete(){
		InterceptDao dao = new InterceptDao(getContext());
			dao.delete("13888888888");
	}
	public void testUpdate(){
		InterceptDao dao = new InterceptDao(getContext());
			dao.update("13888888886", "3");
	}
	public void tesFind(){
		InterceptDao dao = new InterceptDao(getContext());
			dao.find("1388888888");
	}
	
	

}
