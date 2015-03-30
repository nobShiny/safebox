package com.lsj.safebox.custom.ui;


import com.lsj.safebox.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * �Զ����һ����Ͽؼ�
 * ��������view
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
		//��XML�ļ�ת����view����,	ָ��view�ĸ������
		View view = inflate(getContext(), R.layout.activity_setting_view, this);
		tv_zdgx = (TextView) view.findViewById(R.id.tv_zdgx);
		title = (TextView) view.findViewById(R.id.tv_title);
		context = (TextView) view.findViewById(R.id.tv_context);
		cb = (CheckBox) view.findViewById(R.id.cb);
		
		
	}
	
	/**
	 * �޸����
	 */
	
	public void setCategory(String str){
		tv_zdgx.setText(str);
	}
	
	/**
	 * �޸ı���
	 */
	public void setTitle(String str){
		title.setText(str);
	}

	/**
	 * �޸�����
	 */
	public void setContext(String str){
		context.setText(str);
	}
	
	/**
	 * �޸�ѡ��״̬
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
	 * �ж��Ƿ�ѡ��
	 */
	public boolean isChecked(){
	 return cb.isChecked();
	}

}
