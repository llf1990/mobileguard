package com.android.mobileguard.test;

import java.util.Random;

import com.android.mobileguard.db.dao.InterceptDao;

import android.test.AndroidTestCase;

public class TestInterceptDao extends AndroidTestCase {

	public TestInterceptDao() {
		
	}
	
	
	public void testAdd(){
		InterceptDao dao = new InterceptDao(getContext());
		int origin = 1388888888;
		Random r = new Random();
		
		for(int i = 1 ; i < 5000 ; i++){
			Integer flag = r.nextInt(3)+1;
			dao.add(Integer.toString(origin+i), flag.toString());
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
