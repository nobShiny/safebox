package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.widget.EditText;
/**
 * �����ԶԻ������ʽ������ɷ��Ͷ��ŵĲ鿴������
 * @author lipeng
 *
 */
public class Stranger_smsmanager {
	private Context context;
	private String query;
	private String answer;
       public Stranger_smsmanager(Context context){
    	   this.context=context;
       }
       
       public void Dialog(){ //һ���Ի���
    	   getData();//�������
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("���Ͷ��Ź���")
    	          .setMessage(query+"\n"+answer)
    	          .setIcon(context.getResources().getDrawable(R.drawable.p13));
    	   builder.setPositiveButton("������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				answer_set();
			}
		  });
    	   builder.setNegativeButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				query_set();
			}
		});
    	   builder.create().show();
       }
       
       private void getData(){  //�������
    	   SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
    	   query=sp.getString("query", "�Բ���������⻹û���ã�");
    	   answer=sp.getString("answer", "�Բ�����Ĵ𰸻�û���ã�");
       }
       
       private void query_set(){  //�������öԻ���
    	   final EditText tv=new EditText(context);
    	   tv.setTextColor(context.getResources().getColor(R.color.blak));
    	   tv.setTextSize(20);
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("��������")
    	          .setIcon(R.drawable.p13)
    	          .setView(tv);
    	   builder.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
		    	   SharedPreferences.Editor edit=sp.edit();
		    	   edit.putString("query", tv.getText().toString());
		    	   edit.commit();
			}
		  });
    	   builder.setNegativeButton("ȡ��", new OnClickListener() {
   			
   			@Override
   			public void onClick(DialogInterface arg0, int arg1) {
   				
   			}
   		});
    	   builder.create().show();
       }
       
       private void answer_set(){   //�����öԻ���
    	   final EditText tv=new EditText(context);
    	   tv.setTextColor(context.getResources().getColor(R.color.blak));
    	   tv.setTextSize(20);
    	   AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	   builder.setTitle("������")
    	          .setIcon(R.drawable.p13)
    	          .setView(tv);
    	   builder.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 SharedPreferences sp=context.getSharedPreferences("rule_record", Context.MODE_PRIVATE);
		    	   SharedPreferences.Editor edit=sp.edit();
		    	   edit.putString("answer", tv.getText().toString());
		    	   edit.commit();
			}
		  });
    	   builder.setNegativeButton("ȡ��", new OnClickListener() {
   			
   			@Override
   			public void onClick(DialogInterface arg0, int arg1) {
   				
   			}
   		});
    	   builder.create().show();
       }
}
