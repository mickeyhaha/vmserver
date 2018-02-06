package com.vm.handler;

import java.util.HashMap;
import java.util.Map;

public class ContextHolder {
	private static ContextHolder holder = null;
	//key: requestId, value: result (1 - paid, 0 - not paid)
	private Map<String, String> payResult = null;
	public static String PAID = "1";
	public static String NOT_PAID = "0";
	
	public static boolean paid = false;
	
	private ContextHolder() {
		payResult = new HashMap<String, String>();
	}
	
	public static ContextHolder getInstantce() {
		if(holder == null) {
			holder = new ContextHolder();
		}
		return holder;
	}
	
	public void addPayRequest(String requestId) {
		payResult.put(requestId, NOT_PAID);
	}
	
	public void setPaid(String requestId) {
		payResult.put(requestId, PAID);
	}
	

	public String getPaidResult(String requestId) {
		return payResult.get(requestId);
	}

}
