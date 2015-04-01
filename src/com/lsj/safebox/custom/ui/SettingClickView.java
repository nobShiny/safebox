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

	// �ڶ������� ������xml�ļ��ж�������� ȫ����װ��
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingClickView(Context context) {
		super(context);
		init();
	}

	private void init() {
		View view = View.inflate(getContext(), R.layout.setting_click_view,this);// �����˲��� ֱ�Ӹ���������˭
		tv_zdgx = (TextView) view.findViewById(R.id.tv_zdgx);
		title = (TextView) view.findViewById(R.id.tv_title);
		content = (TextView) view.findViewById(R.id.tv_content);
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
	public void setTiTle(String str) {
		title.setText(str);
	}

	/**
	 * �޸�������Ϣ
	 */
	public void setContent(String str) {
		content.setText(str);
	}
}
