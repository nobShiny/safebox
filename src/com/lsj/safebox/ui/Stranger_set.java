package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;


/**
 * ������ɶ�İ������������
 * @author Administrator
 *
 */
public class Stranger_set extends Activity{
	TextView smstall,timestall,smstext,phonetext,timetext,quit;
    LinearLayout smslinear,phonelinear,timelinear;
    String []smstime=new String[10];
    String []phonetime=new String[10];
	int n=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stranger_interface);
		findView(); //�������
		getTextcontent(); //��ȡ�����ļ��������������ʱ���
		getdata(); //���������
		onClick();//�¼���Ӧ
	}
	
	public void findView(){
		quit=(TextView) findViewById(R.id.strange_quit);  //���ؼ�
		smstall=(TextView) findViewById(R.id.strange_smsinstall);  //�ظ���������
		timestall=(TextView) findViewById(R.id.strange_timeinstall);// ʱ������
	    smstext=(TextView) findViewById(R.id.strange_smssinstall);//���ٴη��Ͷ���
	    phonetext=(TextView) findViewById(R.id.strange_phoneinstall);//���ٴν���
	    timetext=(TextView) findViewById(R.id.strange_timespaneinstall);//����ʱ�������
	    smslinear=(LinearLayout) findViewById(R.id.strange_smslinear);//  ���Ͷ����¼�
	    phonelinear=(LinearLayout) findViewById(R.id.strange_phonelinear);// �����¼�
	    timelinear=(LinearLayout) findViewById(R.id.strange_timelinear);//  ����ʱ����¼�
	    
	}
	private void getdata(){   // ****��ʼ������****
		int i;
		for(i=0;i<smstime.length;i++){
			smstime[i]="�ڣ�"+(i+1)+" �κ��Ͷ���";
		}
		for(i=0;i<phonetime.length;i++){
			phonetime[i]="�ڣ�"+(i+1)+"�κ���Խ�ͨ";
		}
	}
	
	private void getTextcontent(){
		int n;
		SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		n=sp.getInt("smscount", 0);
		String str=""+n+"��";
		smstext.setText(str);
		n=sp.getInt("phonecount", 0);
		str=str.replace(str, ""+n+"��");
		phonetext.setText(str);
		n=sp.getInt("phonebreak", 0);
		str=str.replace(str, ""+n+"����");
		timetext.setText(str);
	}
	
	public void onClick(){
		
		quit.setOnClickListener(new OnClickListener(){ //�����¼�

			@Override
			public void onClick(View arg0) {
				Stranger_set.this.finish();
			}
		});
		smstall.setOnClickListener(new OnClickListener(){ //������Ϣ���ã���������

			@Override
			public void onClick(View arg0) {
				new Stranger_smsmanager(Stranger_set.this).Dialog(); //һ�Ի������ʽ����
			}
		});
		timestall.setOnClickListener(new OnClickListener(){ //ʱ�������¼�,��ת��ʱ������ҳ��

			@Override
			public void onClick(View arg0) {
				  Intent intent=new Intent(Stranger_set.this,Time_set.class);
			      startActivity(intent);
			}
		});
		smslinear.setOnClickListener(new OnClickListener(){ //������Ϣ���ã���������

			@Override
			public void onClick(View arg0) {
				int z=-1;
				AlertDialog.Builder builder=new AlertDialog.Builder(Stranger_set.this);
				builder.setTitle("��ѡ��");
				builder.setIcon(R.drawable.p13);
				builder.setSingleChoiceItems(smstime, z,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					   n=which+1;	
					}
				});
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
						SharedPreferences.Editor edit=sp.edit();
						edit.putInt("smscount", n);
						edit.commit();
						getTextcontent();
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.create().show();
			}
		});
		phonelinear.setOnClickListener(new OnClickListener(){ //���ٴν�ͨ

			@Override
			public void onClick(View arg0) {
				int z=-1;
				AlertDialog.Builder builder=new AlertDialog.Builder(Stranger_set.this);
				builder.setTitle("��ѡ��");
				builder.setIcon(R.drawable.p13);
				builder.setSingleChoiceItems(phonetime, z,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					   n=which+1;	
					}
				});
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
						SharedPreferences.Editor edit=sp.edit();
						edit.putInt("phonecount", n);
						edit.commit();
						getTextcontent();
						
					}
				});
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
				
			}
		});
		timelinear.setOnClickListener(new OnClickListener(){  //��ͨ��ʱ���

			@Override
			public void onClick(View arg0) {
				 Dialog dialog=new TimePickerDialog(Stranger_set.this,new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
							n=arg1*60+arg2;
							SharedPreferences sp=getSharedPreferences("rule_record", MODE_PRIVATE);
							SharedPreferences.Editor edit=sp.edit();
							edit.putInt("phonebreak", n);
							edit.commit();
							getTextcontent();
						}
					},0,0,true);
					 dialog.show();
			}
			
		});
	}
      
}
