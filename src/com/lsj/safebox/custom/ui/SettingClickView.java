package com.lsj.safebox.custom.ui;

import com.lsj.safebox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	private TextView tv_zdgx,title;
	private TextView content;

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 第二个参数 把所有xml文件中定义的属性 全部封装了
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingClickView(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = View.inflate(getContext(), R.layout.setting_click_view,this);// 创建了布局 直接告诉他爹是谁
		tv_zdgx = (TextView) view.findViewById(R.id.tv_zdgx);
		title = (TextView) view.findViewById(R.id.tv_title);
		content = (TextView) view.findViewById(R.id.tv_content);
	}

	
	/**
	 * 修改类别
	 */
	
	public void setCategory(String str){
		tv_zdgx.setText(str);
	}
	
	/**
	 * 修改标题
	 */
	public void setTiTle(String str) {
		title.setText(str);
	}

	/**
	 * 修改描述信息
	 */
	public void setContent(String str) {
		content.setText(str);
	}
}
