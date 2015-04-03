package com.lsj.safebox.domin;

import android.graphics.drawable.Drawable;

/**
 * 应用程序的实体对象
 * @author Administrator
 *
 */
public class AppInfo {
	private String name;
	private String versionName;
	private String packageName;
	private boolean isUser; // 是否是用户程序
	private boolean isRom; // 是否在手机内存中
	private Drawable icon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	public boolean isRom() {
		return isRom;
	}

	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", versionName=" + versionName
				+ ", packageName=" + packageName + ", isUser=" + isUser
				+ ", isRom=" + isRom + ", icon=" + icon + "]";
	}

}
