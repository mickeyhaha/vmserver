package com.vm.handler;

public enum Command {
	
	OPENDOOR (1, "opendoor"),
	STARTINVENTORY (2, "startinv"),
	STOPINVENTORY (3, "stopinv");
	
	int id;
	String cmd;
	
	Command(int id, String cmd) {
		this.id = id;
		this.cmd = cmd;
	}


	
	
	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getCmd() {
		return cmd;
	}




	public void setCmd(String cmd) {
		this.cmd = cmd;
	}




	@Override
	public String toString() {
		return cmd;
	}
	
}
