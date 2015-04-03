package com.lsj.safebox.domin;

import android.graphics.drawable.Drawable;

/**
 * Ӧ�ó����ʵ�����
 * @author Administrator
 *
 */
public class AppInfo {
	private String name;
	private String versionName;
	private String packageName;
	private boolean isUser; // �Ƿ����û�����
	private boolean isRom; // �Ƿ����ֻ��ڴ���
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
