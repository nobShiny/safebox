package com.lsj.safebox.domin;


/**
 * 黑名单实体类
 * @author Administrator
 *
 */
public class BlankNumInfo {
	String blankNum;
	int mode;

	public String getBlankNum() {
		return blankNum;
	}

	public void setBlankNum(String blankNum) {
		this.blankNum = blankNum;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) { // 0 1 2
		if (mode >= 0 && mode <= 2) {
			this.mode = mode;
		} else {
			this.mode = 2;
		}

	}

	public BlankNumInfo(String blankNum, int mode) {
		super();
		this.blankNum = blankNum;
		if (mode >= 0 && mode <= 2) {
			this.mode = mode;
		} else {
			this.mode = 2;
		}
	}

	public BlankNumInfo() {
		super();
	}

	@Override
	public String toString() {
		return "BlankNumInfo [blankNum=" + blankNum + ", mode=" + mode + "]";
	}

}
