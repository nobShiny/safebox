package com.lsj.safebox.custom.ui;


import com.lsj.safebox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 自定义的一个组合控件
 * 设置中心view
 * @author Administrator
 *
 */
public class SettingView extends RelativeLayout {

	private TextView tv_zdgx;
	private TextView title;
	private TextView context;
	private CheckBox cb;
	private String title2;
	private String des_on;
	private String des_off;

	public SettingView(Context context) {
		super(context);
		init();
	}

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		String category = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.lsj.safebox", "category");
		title2 = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.lsj.safebox", "title1");
		des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.lsj.safebox", "des_on");
		des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.lsj.safebox", "des_off");
		setCategory(category);
		setTitle(title2);
		if(cb.isChecked()){
			setContext(des_on);
		}else{
			setContext(des_off);
		}
		
	}

	
	private void init() {
		//把XML文件转化成view对象,	指定view的父类对象
		View view = inflate(getContext(), R.layout.activity_setting_view, this);
		tv_zdgx = (TextView) view.findViewById(R.id.tv_zdgx);
		title = (TextView) view.findViewById(R.id.tv_title);
		context = (TextView) view.findViewById(R.id.tv_context);
		cb = (CheckBox) view.findViewById(R.id.cb);
		
		
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
	public void setTitle(String str){
		title.setText(str);
	}

	/**
	 * 修改内容
	 */
	public void setContext(String str){
		context.setText(str);
	}
	
	/**
	 * 修改选中状态
	 */
	
	public void setChecked(boolean b){
		cb.setChecked(b);
		if(b){
			setContext(des_on);
		}else{
			setContext(des_off);
		}
	}
	
	/**
	 * 判断是否被选中
	 */
	public boolean isChecked(){
	 return cb.isChecked();
	}

}
