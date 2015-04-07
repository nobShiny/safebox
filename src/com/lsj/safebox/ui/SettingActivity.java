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

		update();//自动更新选项
		changebg();//修改来电框样式
		
		changePostion();//修改来电框位置
		applock();//程序加密

		// 返回按钮
		btn_return = (Button) findViewById(R.id.main_back);
		btn_return.setOnClickListener(this);

	}

	/**
	 * 来电地区提醒
	 */
	@Override
	protected void onStart() {
		super.onStart();
		
		address();//归属地显示
		blanknum();//黑名单功能
	}
	
	/**
	 * 程序加密设置
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
					//  停止服务 
					
					stopService(intent);
					sv_setting_applock.setChecked(false);
				}else{
					// 开启服务 
					startService(intent);
					sv_setting_applock.setChecked(true);
				}
			}
		});
	}
	
	/**
	 * 黑名单拦截功能
	 */
	private void blanknum() {
		// 数据回显 
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
							//  停止服务 
							
							stopService(intent);
							sv_setting_blanknum.setChecked(false);
						}else{
							// 开启服务 
							startService(intent);
							sv_setting_blanknum.setChecked(true);
						}
					}
				});
			}
	

	/**
	 * 来电归属地设置
	 */
	private void address() {
		// 数据回显
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
					// 停止服务

					stopService(intent);
					sv_setting_address.setChecked(false);
				} else {
					// 开启服务
					startService(intent);
					sv_setting_address.setChecked(true);
				}
			}
		});
	}

	/**
	 * 修改提示框的位置
	 */
	private void changePostion() {
		sv_setting_change_postion.setCategory("提示框位置");
		sv_setting_change_postion.setTiTle("归属地提示框位置");
		sv_setting_change_postion.setContent("设置归属地提示框的显示位置");
		sv_setting_change_postion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),DragViewActivty.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 修改来电提醒位置栏
	 */
	private void changebg() {
		final String[] items = { "半透明", "果缤橙", "天空蓝", "雾霾灰", "草原绿" };
		
		sv_setting_changebg.setCategory("提示框风格");
		sv_setting_changebg.setTiTle("归属地提示框风格");
		sv_setting_changebg.setContent(items[sp.getInt("which", 0)]);
		sv_setting_changebg.setOnClickListener(new OnClickListener() {     

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				builder.setIcon(R.drawable.ic_launcher);
				// 参数1 单选框对应的字符串数组 
				// 参数2 默认选中的哪个位置 
				// 参数3 选中事件
				builder.setSingleChoiceItems(items, sp.getInt("which", 0),
						new DialogInterface.OnClickListener() {
							// which 点击的哪一个条目
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 条目点击事件
								dialog.dismiss();
								sv_setting_changebg.setContent(items[which]);
								Editor edit = sp.edit();
								edit.putInt("which", which);
								edit.commit();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
	}

	/**
	 * 自动更新设置
	 */
	public void update() {
		// sv_setting_update.setTiTle("自动更新");
		// key值 第二个参数 默认值(如果找不到对应key值 )
		boolean flag = sp.getBoolean("update", true);
		if (flag) {
			sv_setting_update.setChecked(true);
			// sv_setting_update.setContent("自动更新开启");
		} else {
			sv_setting_update.setChecked(false);
			// sv_setting_update.setContent("自动更新关闭");
		}

		sv_setting_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Editor edit = sp.edit();
				if (sv_setting_update.isChecked()) {
					sv_setting_update.setChecked(false);
					// sv_setting_update.setContent("自动更新关闭");
					edit.putBoolean("update", false);
				} else {
					sv_setting_update.setChecked(true);
					// sv_setting_update.setContent("自动更新开启");
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
			// Toast.makeText(getApplicationContext(), "我四返回君", 0).show();
			finish();
			break;

		}
	}
}
