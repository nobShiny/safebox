package com.lsj.safebox.ui;

import com.lsj.safebox.R;
import com.lsj.safebox.utils.SmsBackupUtils;
import com.lsj.safebox.utils.SmsBackupUtils.SmsBackupCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SmsBackupActivity extends Activity{
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smsbackup);
	}
	
	public void smsBackup(View view) {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份,请稍后......");
		pd.show();
		new Thread(){
			public void run() {
				try {
					SmsBackupUtils.backupSms(getApplicationContext(), new SmsBackupCallback() {
						@Override
						public void progressSmsbackup(int progress) {
							pd.setProgress(progress);
						}
						@Override
						public void beforeSmsbackup(int total) {
							pd.setMax(total);
						}
					});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(SmsBackupActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(SmsBackupActivity.this, "备份失败,请检查SD卡是否正常工作", Toast.LENGTH_LONG).show();
						}
					});
				}finally{
					pd.dismiss();
				}
			};
		}.start();
		
	}

	/**
	 * 短信的还原
	 * @param view
	 */
	public void smsRestore(View view){
		
		SmsBackupUtils.restoreSms(this,true);
		Toast.makeText(this, "还原成功", 0).show();
	}
}
	

