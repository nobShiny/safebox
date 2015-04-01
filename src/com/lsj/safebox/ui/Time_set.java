package com.lsj.safebox.ui;

import java.util.Calendar;

import com.lsj.safebox.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * �����������ʱ�����ð���ȫ�����أ�ָ��ʱ�������
 * @author Administrator
 *
 */
public class Time_set extends Activity{
     RadioGroup rg;
     LinearLayout startlinear,stoplinear;  //��ʼʱ��ٷ�������ʱ��ٷ��¼�
     TextView starttime,starttitle,stoptime,stoptitle,quit;
     SharedPreferences sp;
     boolean bool=true; //�ж�ʱ��ģʽ
     @SuppressLint("HandlerLeak")
	Handler handler=new Handler(){  //����ҳ���ʱ����и���

		@Override
		public void handleMessage(Message msg) {
           if(msg.what==1){
        		String startime=""+sp.getInt("starthour", 00)+":"+sp.getInt("startminue", 00);
        		String endtime=""+sp.getInt("endhour", 00)+":"+sp.getInt("endminue", 00);
        		
        		if(sp.getInt("starthour", 00)<25){
        		starttime.setText(startime);
        		stoptime.setText(endtime);
        		}else if(sp.getInt("starthour", 00)==25){
        			starttime.setText("00:00");
        			stoptime.setText("00:00");
        		}
           }
			super.handleMessage(msg);
		}
    	 
     };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_set);
		
		setView();//�������
		handler.sendEmptyMessage(1);//����Ѿ�����ʱ��
		onClick();//�¼�����
	}
	
	public void setView(){
		rg=(RadioGroup) findViewById(R.id.timeRgroup);
		startlinear=(LinearLayout) findViewById(R.id.startLinear);
		stoplinear=(LinearLayout) findViewById(R.id.stopLinear);
		starttime=(TextView) findViewById(R.id.time_starttime);
		starttitle=(TextView) findViewById(R.id.time_starttimee);
		stoptime=(TextView) findViewById(R.id.time_stoptime);
		stoptitle=(TextView) findViewById(R.id.time_stoptimee);
		quit=(TextView) findViewById(R.id.time_quit);
		sp=getSharedPreferences("rule_record", MODE_PRIVATE);
		
	}
	
	public void onClick(){  //�ٷ��¼�
		 rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){ //ѡ��ʱ������ģʽ

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				   switch(arg1){
				   
				   case R.id.time_day:{  //ȫ������
					   starttitle.setTextColor(getResources().getColor(R.color.gray));
					   stoptitle.setTextColor(getResources().getColor(R.color.gray));
					   bool=false;
					
					   SharedPreferences.Editor edit=sp.edit();
					   //edit.clear();
					   edit.putInt("starthour", 25);
					   edit.commit();
					   handler.sendEmptyMessage(1);
				   }
				   break;
				   case R.id.time_break: {  //ָ������ʱ��
					   starttitle.setTextColor(getResources().getColor(R.color.blak));
					   stoptitle.setTextColor(getResources().getColor(R.color.blak));
					   bool=true;
					   startlinear.setFocusable(true);
					   stoplinear.setFocusable(true);
				   }
					   break;
				   }
				   
				  
			}
		 });
		 
		 quit.setOnClickListener(new OnClickListener(){  //�˳��¼�

			@Override
			public void onClick(View arg0) {
				   Time_set.this.finish();
			}
			 
		 });
	   startlinear.setOnClickListener(new OnClickListener(){  //��ʼʱ��

		@Override
		public void onClick(View arg0) {
			Calendar c=Calendar.getInstance();
			if(bool){
				new TimePickerDialog(Time_set.this, new TimePickerDialog.OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
						  SharedPreferences.Editor edit=sp.edit();
						   edit.putInt("starthour", arg1);
						   edit.putInt("startminue", arg2);
						   edit.commit();
						   handler.sendEmptyMessage(1);
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
			}
		}
		   
	   });
	   stoplinear.setOnClickListener(new OnClickListener(){  //����ʱ��

			@Override
			public void onClick(View arg0) {
				Calendar c=Calendar.getInstance();
				if(bool){
					new TimePickerDialog(Time_set.this, new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
							 SharedPreferences.Editor edit=sp.edit();
							   edit.putInt("endhour", arg1);
							   edit.putInt("endminue", arg2);
							   edit.commit();
							   handler.sendEmptyMessage(1);
						}
					}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
				}
			}
			   
		   });
	}
       
}
