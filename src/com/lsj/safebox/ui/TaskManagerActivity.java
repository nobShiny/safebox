package com.lsj.safebox.ui;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.R;
import com.lsj.safebox.domin.TaskInfo;
import com.lsj.safebox.engine.TaskInfoProvider;
import com.lsj.safebox.utils.SystemInfoUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskManagerActivity extends Activity {

	private TextView tv_run_process;
	private TextView tv_avail_ram;
	private ActivityManager am;
	private Button main_back;

	private ListView list_task_item;
	private LinearLayout ll_loading;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> usertaskInfos;// �û����̼���
	private List<TaskInfo> systemtaskInfos;// ϵͳ���̼���
	private TaskManagerAdpter adpter;
	private TextView tv_status;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			ll_loading.setVisibility(View.INVISIBLE);
			adpter = new TaskManagerAdpter();
			list_task_item.setAdapter(adpter);
		};
	};
	// ��ǰ���н�������
	private int runProcessCount;
	// �����ڴ�
	private long availRam;
	// ���ڴ�
	private long totalAvailRam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		setContentView(R.layout.activity_task_manager);
		tv_run_process = (TextView) findViewById(R.id.tv_run_process);
		tv_avail_ram = (TextView) findViewById(R.id.tv_avail_ram);
		tv_status = (TextView) findViewById(R.id.tv_status);
		main_back = (Button) findViewById(R.id.main_back);
		runProcessCount = SystemInfoUtils.getRunngProcessCount(this);
		availRam = SystemInfoUtils.getAvailRam(this);
		totalAvailRam = SystemInfoUtils.getTotalRam(this);
		tv_run_process.setText("�����н���:" + runProcessCount + "��");
		tv_avail_ram.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(this, availRam) + "/"
				+ Formatter.formatFileSize(this, totalAvailRam));
		list_task_item = (ListView) findViewById(R.id.list_task_item);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		fillData();
		// ���ĳһ�����¼�
		list_task_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object obj = list_task_item.getItemAtPosition(position);
				if (obj != null) {
					TaskInfo info = (TaskInfo) obj;
					CheckBox cb_status = (CheckBox) view
							.findViewById(R.id.cb_status);

					if (info.getPackName().equals(getPackageName())) {
						return;
					}
					if (info.isChecked()) {
						// ��ѡ��
						info.setChecked(false);
						cb_status.setChecked(false);
					} else {
						// ѡ��
						info.setChecked(true);
						cb_status.setChecked(true);
					}
				}

			}
		});

		list_task_item.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (systemtaskInfos != null && usertaskInfos != null) {
					if (firstVisibleItem > usertaskInfos.size()) {
						// ��ʾϵͳ����
						tv_status.setText("ϵͳ����(" + systemtaskInfos.size()
								+ ")");
					} else {
						// �û�����
						tv_status.setText("�û�����(" + usertaskInfos.size() + ")");
					}
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

	// ��������
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				taskInfos = TaskInfoProvider
						.getTaskInfos(TaskManagerActivity.this);
				usertaskInfos = new ArrayList<TaskInfo>();
				systemtaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUser()) {
						usertaskInfos.add(taskInfo);
					} else {
						systemtaskInfos.add(taskInfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	private class TaskManagerAdpter extends BaseAdapter {

		@Override
		public int getCount() {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			boolean showsystem = sp.getBoolean("showsystem", true);
			if (showsystem) {
				return usertaskInfos.size() + 1 + systemtaskInfos.size() + 1;
			} else {
				return usertaskInfos.size() + 1;
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û�����(" + usertaskInfos.size() + ")");
				return tv;
			} else if (position == (usertaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ����(" + systemtaskInfos.size() + ")");
				return tv;
			} else if (position <= usertaskInfos.size()) {
				taskInfo = usertaskInfos.get(position - 1);
			} else {
				taskInfo = systemtaskInfos.get(position - usertaskInfos.size()
						- 2);
			}
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(TaskManagerActivity.this,
						R.layout.list_task_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_memsize = (TextView) view
						.findViewById(R.id.tv_memsize);
				holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);

			}

			// �õ�ĳ��������Ϣ����ʾ
			holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_name.setText(taskInfo.getName());
			holder.tv_memsize.setText(Formatter.formatFileSize(
					TaskManagerActivity.this, taskInfo.getMemsize()));
			// ״̬��У��
			if (taskInfo.isChecked()) {
				holder.cb_status.setChecked(true);
			} else {
				holder.cb_status.setChecked(false);
			}

			if (taskInfo.getPackName().equals(getPackageName())) {
				holder.cb_status.setVisibility(View.INVISIBLE);
			} else {
				holder.cb_status.setVisibility(View.VISIBLE);
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			TaskInfo taskInfo;
			if (position == 0) {
				return null;
			} else if (position == (usertaskInfos.size() + 1)) {
				return null;
			} else if (position <= usertaskInfos.size()) {
				taskInfo = usertaskInfos.get(position - 1);
			} else {
				taskInfo = systemtaskInfos.get(position - usertaskInfos.size()
						- 2);
			}

			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb_status;
	}

	// ���-ȫѡ
	public void selectAll(View view) {
		for (TaskInfo taskInfo : usertaskInfos) {
			taskInfo.setChecked(true);
		}

		for (TaskInfo taskInfo : systemtaskInfos) {
			taskInfo.setChecked(true);
		}

		adpter.notifyDataSetChanged();

	}

	// ���-��ѡ
	public void unSelect(View view) {
		for (TaskInfo taskInfo : usertaskInfos) {
			if (taskInfo.getPackName().equals(getPackageName())) {
				continue;
			}
			if (taskInfo.isChecked()) {
				taskInfo.setChecked(false);
			} else {
				taskInfo.setChecked(true);
			}

		}

		for (TaskInfo taskInfo : systemtaskInfos) {
			if (taskInfo.isChecked()) {
				taskInfo.setChecked(false);
			} else {
				taskInfo.setChecked(true);
			}
		}

		adpter.notifyDataSetChanged();

	}

	// ����¼�-һ������
	public void killAll(View view) {

		List<TaskInfo> taskInfosKilled = new ArrayList<TaskInfo>();
		int killedProcess = 0;
		long freeMemSize = 0;

		for (TaskInfo taskInfo : usertaskInfos) {

			if (taskInfo.getPackName().equals(getPackageName())) {
				continue;
			}
			if (taskInfo.isChecked()) {
				// ��ɱ
				// android.os.Process.killProcess(android.os.Process.myPid());
				am.killBackgroundProcesses(taskInfo.getPackName());
				// usertaskInfos.remove(taskInfo);
				taskInfosKilled.add(taskInfo);
				killedProcess++;
				freeMemSize += taskInfo.getMemsize();
			}

		}

		for (TaskInfo taskInfo : systemtaskInfos) {
			if (taskInfo.isChecked()) {
				am.killBackgroundProcesses(taskInfo.getPackName());
				// systemtaskInfos.remove(taskInfo);
				taskInfosKilled.add(taskInfo);
				killedProcess++;
				freeMemSize += taskInfo.getMemsize();
			}
		}

		for (TaskInfo taskInfo : taskInfosKilled) {

			if (taskInfo.isUser()) {
				usertaskInfos.remove(taskInfo);
			} else {
				systemtaskInfos.remove(taskInfo);
			}
		}
		// ��ǰ���н�����
		runProcessCount -= killedProcess;
		// ��ʣ���ڴ�
		availRam += freeMemSize;
		tv_run_process.setText("�����н���:" + runProcessCount + "��");
		tv_avail_ram.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(this, availRam) + "/"
				+ Formatter.formatFileSize(this, totalAvailRam));
		Toast.makeText(
				getApplicationContext(),
				"ɱ���ˣ�"
						+ killedProcess
						+ "�����̣��ͷ��ڴ棺"
						+ Formatter.formatFileSize(getApplicationContext(),
								freeMemSize), 1).show();

		adpter.notifyDataSetChanged();

		// fillData();
	}

	// ����¼�-�����������ҳ��
	public void reEnterSetting(View view) {
		Intent intent = new Intent(this, TaskManagerSettingActivity.class);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		adpter.notifyDataSetChanged();// getCount();---��getView();
	}

}