package com.lsj.safebox.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;


/**
 * ��SD����ȡtxt�ĵ��ϵĵ�ַ
 * @author Administrator
 *
 */
public class Region_djust {
    MultiAutoCompleteTextView mact;
    LinearLayout ll;
    ListView lv;
    
    
    static List<String>list=new ArrayList<String>();
    static List<ArrayList<String>>lists=new ArrayList<ArrayList<String>>();
    EditText tv;
   
    public Region_djust(){    }
    
    public static List<String>getProvice(){
    	return list;
    }
    public static List<ArrayList<String>>getCity(){
    	return lists;
    }
    
    static{
    	String data=convertCodeAndGetText(FileUtil.PATH+"/region.txt");
    	int j=0,i=0;
    	j=data.indexOf("/n");
    	while(j<=data.length()){
    		String str="";
    		if(i>=0&&j>0){
    			str=data.substring(i, j);
    			
    			if(i!=0)str=str.substring(1);}
    		else break;
    		int n=0,m=0;
    		n=data.indexOf('��');
    		if(n>0){
    			String nu=str.substring(0, n);
    			if(nu!=null){list.add(nu);}
    		
    		}
    		n=str.indexOf(' ');
    		m=str.indexOf(' ', n+1);
    		ArrayList<String>item=new ArrayList<String>();
    		while(m<str.length()){
    			
    			item.add(str.substring(n+1, m));
    			n=m;
    			m=str.indexOf(' ', n+1);
    			if(n==str.lastIndexOf(' ')){item.add(str.substring(n));break;}
    		}
    		lists.add(item);
    		i=j+1;
    		if(data.lastIndexOf("\n")==i)break;
    		j=data.indexOf("/n",i+1);
    	}
      list.add("����");
      list.add("�Ϻ�");
      list.add("���");
      list.add("����");
    }
    
    
    /*  android��Windowϵͳ������ʽ��ͬ��ת��*/
    private static String convertCodeAndGetText(String str_filepath) {// ת��

        File file = new File(str_filepath);
        BufferedReader reader;
        String text = "";
        try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] first3bytes = new byte[3];
                in.read(first3bytes);//�ҵ��ĵ���ǰ�����ֽڲ��Զ��ж��ĵ����͡�
                in.reset();
                if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                                && first3bytes[2] == (byte) 0xBF) {// utf-8

                        reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

                } else if (first3bytes[0] == (byte) 0xFF
                                && first3bytes[1] == (byte) 0xFE) {

                        reader = new BufferedReader(
                                        new InputStreamReader(in, "unicode"));
                } else if (first3bytes[0] == (byte) 0xFE
                                && first3bytes[1] == (byte) 0xFF) {

                        reader = new BufferedReader(new InputStreamReader(in,
                                        "utf-16be"));
                } else if (first3bytes[0] == (byte) 0xFF
                                && first3bytes[1] == (byte) 0xFF) {

                        reader = new BufferedReader(new InputStreamReader(in,
                                        "utf-16le"));
                } else {

                        reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                }
                String str = reader.readLine();

                while (str != null) {
                        text = text + str + "/n";
                        str = reader.readLine();

                }
                reader.close();

        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return text;
}
    
}
