package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.custom.ui.SettingClickView;
import com.lsj.safebox.custom.ui.SettingView;
import com.lsj.safebox.service.AddressService;
import com.lsj.safebox.service.BlankNumService;
import com.lsj.safebox.service.MonitorSerivce;
import com.lsj.safebox.utils.ServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity implements OnClickListener {

	private SettingView sv_setting_update, sv_setting_address,sv_setting_blanknum,sv_setting_applock;
	private SettingClickView sv_setting_changebg,sv_setting_change_postion;
	private SharedPreferences sp;
	private Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
		sv_setting_changebg = (SettingClickView) findViewById(R.id.sv_setting_changebg);
		sv_setting_change_postion = (SettingClickView) findViewById(R.id.sv_setting_change_postion);
		sv_setting_blanknum = (SettingView) findViewById(R.id.sv_setting_blanknum);
		sv_setting_applock = (SettingView) findViewById(R.id.sv_setting_applock);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);

		update();//�Զ�����ѡ��
		changebg();//�޸��������ʽ
		
		changePostion();//�޸������λ��
		applock();//�������

		// ���ذ�ť
		btn_return = (Button) findViewById(R.id.main_back);
		btn_return.setOnClickListener(this);

	}

	/**
	 * �����������
	 */
	@Override
	protected void onStart() {
		super.onStart();
		
		address();//��������ʾ
		blanknum();//����������
	}
	
	/**
	 * �����������
	 */
	private void applock(){
		if(ServiceUtils.isRunning(this, "com.lsj.safebox.service.MonitorSerivce")){
			sv_setting_applock.setChecked(true);
		}else{
			sv_setting_applock.setChecked(false);
		}
		
		sv_setting_applock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(), MonitorSerivce.class);
				if(sv_setting_applock.isChecked()){
					//  ֹͣ���� 
					
					stopService(intent);
					sv_setting_applock.setChecked(false);
				}else{
					// �������� 
					startService(intent);
					sv_setting_applock.setChecked(true);
				}
			}
		});
	}
	
	/**
	 * ���������ع���
	 */
	private void blanknum() {
		// ���ݻ��� 
				if(ServiceUtils.isRunning(this, "com.lsj.safebox.service.BlankNumService")){
					sv_setting_blanknum.setChecked(true);
				}else{
					sv_setting_blanknum.setChecked(false);
				}
				
				sv_setting_blanknum.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getApplicationContext(), BlankNumService.class);
						if(sv_setting_blanknum.isChecked()){
							//  ֹͣ���� 
							
							stopService(intent);
							sv_setting_blanknum.setChecked(false);
						}else{
							// �������� 
							startService(intent);
							sv_setting_blanknum.setChecked(true);
						}
					}
				});
			}
	

	/**
	 * �������������
	 */
	private void address() {
		// ���ݻ���
		if (ServiceUtils.isRunning(this,
				"com.lsj.safebox.service.AddressService")) {
			sv_setting_address.setChecked(true);
		} else {
			sv_setting_address.setChecked(false);
		}

		sv_setting_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						AddressService.class);
				if (sv_setting_address.isChecked()) {
					// ֹͣ����

					stopService(intent);
					sv_setting_address.setChecked(false);
				} else {
					// ��������
					startService(intent);
					sv_setting_address.setChecked(true);
				}
			}
		});
	}

	/**
	 * �޸���ʾ���λ��
	 */
	private void changePostion() {
		sv_setting_change_postion.setCategory("��ʾ��λ��");
		sv_setting_change_postion.setTiTle("��������ʾ��λ��");
		sv_setting_change_postion.setContent("���ù�������ʾ�����ʾλ��");
		sv_setting_change_postion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),DragViewActivty.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * �޸���������λ����
	 */
	private void changebg() {
		final String[] items = { "��͸��", "���ͳ�", "�����", "������", "��ԭ��" };
		
		sv_setting_changebg.setCategory("��ʾ����");
		sv_setting_changebg.setTiTle("��������ʾ����");
		sv_setting_changebg.setContent(items[sp.getInt("which", 0)]);
		sv_setting_changebg.setOnClickListener(new OnClickListener() {     

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ����");
				builder.setIcon(R.drawable.ic_launcher);
				// ����1 ��ѡ���Ӧ���ַ������� 
				// ����2 Ĭ��ѡ�е��ĸ�λ�� 
				// ����3 ѡ���¼�
				builder.setSingleChoiceItems(items, sp.getInt("which", 0),
						new DialogInterface.OnClickListener() {
							// which �������һ����Ŀ
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ��Ŀ����¼�
								dialog.dismiss();
								sv_setting_changebg.setContent(items[which]);
								Editor edit = sp.edit();
								edit.putInt("which", which);
								edit.commit();
							}
						});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
			}
		});
	}

	/**
	 * �Զ���������
	 */
	public void update() {
		// sv_setting_update.setTiTle("�Զ�����");
		// keyֵ �ڶ������� Ĭ��ֵ(����Ҳ�����Ӧkeyֵ )
		boolean flag = sp.getBoolean("update", true);
		if (flag) {
			sv_setting_update.setChecked(true);
			// sv_setting_update.setContent("�Զ����¿���");
		} else {
			sv_setting_update.setChecked(false);
			// sv_setting_update.setContent("�Զ����¹ر�");
		}

		sv_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Editor edit = sp.edit();
				if (sv_setting_update.isChecked()) {
					sv_setting_update.setChecked(false);
					// sv_setting_update.setContent("�Զ����¹ر�");
					edit.putBoolean("update", false);
				} else {
					sv_setting_update.setChecked(true);
					// sv_setting_update.setContent("�Զ����¿���");
					edit.putBoolean("update", true);
				}
				// edit.apply();
				edit.commit();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_back:
			// Toast.makeText(getApplicationContext(), "���ķ��ؾ�", 0).show();
			finish();
			break;

		}
	}
}
