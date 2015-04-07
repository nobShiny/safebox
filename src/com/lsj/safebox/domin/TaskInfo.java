package com.lsj.safebox.domin;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	
	private boolean isChecked;
	
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	private Drawable icon;
	private String name;
	private String packName;
	
	private boolean user;
	/**
	 * byte
	 */
	private long memsize;
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public boolean isUser() {
		return user;
	}
	public void setUser(boolean user) {
		this.user = user;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	@Override
	public String toString() {
		return "TaskInfo [name=" + name + ", packName=" + packName + ", user="
				+ user + ", memsize=" + memsize + "]";
	}
	
	
	

}
