package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.service.KillProcessService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 进程设置
 * @author Administrator
 *
 */
public class TaskManagerSettingActivity extends Activity {
	private CheckBox ch_show_system_process;
	private CheckBox ch_kill_process;
	private CheckBox ch_shake_kill_process;
	private SharedPreferences sp;
	private Intent killProcessIntent;
	private Intent shakekillProcessIntent;
	private Button main_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.activity_task_manager_setting);
		ch_show_system_process = (CheckBox) findViewById(R.id.ch_show_system_process);
		ch_kill_process = (CheckBox) findViewById(R.id.ch_kill_process);
		ch_shake_kill_process = (CheckBox) findViewById(R.id.ch_shake_kill_process);
		main_back = (Button) findViewById(R.id.main_back);
		boolean showsystem = sp.getBoolean("showsystem", true);
		if (showsystem) {
			ch_show_system_process.setText("当前状态：显示系统进程");

		} else {
			ch_show_system_process.setText("当前状态：不显示系统进程");
		}
		ch_show_system_process.setChecked(showsystem);

		ch_show_system_process
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						editor.putBoolean("showsystem", isChecked);
						editor.commit();

						if (isChecked) {
							ch_show_system_process.setText("当前状态：显示系统进程");
						} else {
							ch_show_system_process.setText("当前状态：不显示系统进程");
						}

					}
				});

		/**
		 * 锁屏清理进程
		 */
		killProcessIntent = new Intent(this, KillProcessService.class);
		boolean killprocess = sp.getBoolean("killprocess", false);
		if (killprocess) {
			ch_kill_process.setText("当前状态：锁屏杀死进程");
			startService(killProcessIntent);
		} else {
			ch_kill_process.setText("当前状态：锁屏不杀死进程");
			stopService(killProcessIntent);
		}
		ch_kill_process.setChecked(killprocess);

		ch_kill_process.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						editor.putBoolean("killprocess", isChecked);
						editor.commit();

						if (isChecked) {
							ch_kill_process.setText("当前状态：锁屏杀死进程");
							startService(killProcessIntent);
						} else {
							ch_kill_process.setText("当前状态：锁屏不杀死进程");
							stopService(killProcessIntent);
						}

					}
				});
		
		/**
		 * 摇一摇清理进程
		 */
		shakekillProcessIntent = new Intent(this, KillProcessService.class);
		boolean shakekillProcessIntent = sp.getBoolean("killprocess", false);
		if (killprocess) {
			ch_kill_process.setText("当前状态：摇一摇清理进程已开启");
			startService(killProcessIntent);
		} else {
			ch_kill_process.setText("当前状态：摇一摇清理功能已关闭");
			stopService(killProcessIntent);
		}
		ch_shake_kill_process.setChecked(killprocess);
		
		ch_shake_kill_process.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Editor editor = sp.edit();
				editor.putBoolean("killprocess", isChecked);
				editor.commit();
				
				if (isChecked) {
					ch_kill_process.setText("当前状态：摇一摇清理进程已开启");
					startService(killProcessIntent);
				} else {
					ch_kill_process.setText("当前状态：摇一摇清理功能已关闭");
					stopService(killProcessIntent);
				}
				
			}
		});
		
		main_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.main_back:
					finish();
					break;

				default:
					break;
				}
			}
		});
	}
	
}
