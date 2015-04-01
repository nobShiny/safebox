package com.lsj.safebox.ui;

import java.util.List;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.BlankNum;
import com.lsj.safebox.domin.BlankNumInfo;
import com.lsj.safebox.utils.MyAsynTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CallSmsSafeActivity extends Activity {
	private ListView lv_call_sms;
	private BlankNumAdapter adapter;
	private BlankNum dao;
	private List<BlankNumInfo> blanknums;
	private AlertDialog dialog;
	private ProgressBar pb_process;
	public static final int MAX_NUM = 20;
	int startIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		pb_process = (ProgressBar) findViewById(R.id.pb_process);
		lv_call_sms = (ListView) findViewById(R.id.lv_call_sms);
		dao = new BlankNum(getApplicationContext());
		fillData();
		// ע�Ử���ļ���
		lv_call_sms.setOnScrollListener(new OnScrollListener() {
			// listview ����״̬ �����仯����
			// ��ֹ״̬
			// ����״̬
			// ���Ի��� ���ٻ���
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE: // ��ֹ״̬
					int lastVisiblePosition = lv_call_sms
							.getLastVisiblePosition();// ��ȡ�����һ���ɼ���λ��
					if (lastVisiblePosition == blanknums.size() - 1) {
						// �������һ��λ��
						// �����µ�����
						startIndex += MAX_NUM;
						fillData();// ���¼�������
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // ����״̬
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// ���Ի���״̬
					break;
				}
			}

			// ��listVIew ����ʱ�����
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	public void fillData() {
		new MyAsynTask() {
			@Override
			public void preTask() {
				pb_process.setVisibility(View.VISIBLE);
			}

			@Override
			public void postTask() {
				if (adapter == null) {
					adapter = new BlankNumAdapter();
					lv_call_sms.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				pb_process.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBack() {
				if (blanknums == null) {
					blanknums = dao.queryPart(MAX_NUM, startIndex);
				} else {
					// ��ԭ�м����� ����µļ���
					blanknums.addAll(dao.queryPart(MAX_NUM, startIndex));
				}
			}
		}.excute();
	}

	// ��Ӱ�ť�ĵ���¼�
	public void addBlacknum(View v) {
		AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
		dialog = builder.create();
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_add_blanknum, null);

		// ��ʼ���ؼ�
		final EditText et_blanknum = (EditText) view
				.findViewById(R.id.et_blanknum);
		Button ok = (Button) view.findViewById(R.id.btn_ok);
		Button cancel = (Button) view.findViewById(R.id.btn_cancel);
		final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String blanknum = et_blanknum.getText().toString().trim();
				if (TextUtils.isEmpty(blanknum)) {
					Toast.makeText(getApplicationContext(), "����������Ϊ��", 0)
							.show();
				} else if (dao.queryBlankNum(blanknum) == -1) {
					switch (rg.getCheckedRadioButtonId()) {
					case R.id.rb_sms:// ��������
						dao.addBlankNum(blanknum, BlankNum.SMS);
						blanknums.add(0, new BlankNumInfo(blanknum,
								BlankNum.SMS));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						break;
					case R.id.rb_tel:// �绰����
						dao.addBlankNum(blanknum, BlankNum.TEL);
						blanknums.add(0, new BlankNumInfo(blanknum,
								BlankNum.TEL));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						break;
					case R.id.rb_all:// ȫ������
						dao.addBlankNum(blanknum, BlankNum.ALL);
						blanknums.add(0, new BlankNumInfo(blanknum,
								BlankNum.ALL));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						break;
					}
				}
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private class BlankNumAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return blanknums.size();
		}

		// convertView ����ճ���Ļ view����
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			//
			if (convertView != null && convertView instanceof RelativeLayout) {
				System.out.println("��������" + position);
				// ��������
				view = convertView;
				// �ڴ����ó��ʼǱ�
				holder = (ViewHolder) view.getTag();

			} else {
				System.out.println("�����µ���" + position); // 8��
				// �����µ���
				view = View.inflate(getApplicationContext(),
						R.layout.item_call_sms_safe, null);
				// ���˸��ʼǱ�
				holder = new ViewHolder();
				// ��¼ӳ���ϵ
				holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				// �ѱʼǱ��ŵ��ڴ���
				view.setTag(holder);
			}
			final BlankNumInfo blankNumInfo = blanknums.get(position);

			// TextView tv_num=(TextView) view.findViewById(R.id.tv_num);
			// TextView tv_mode=(TextView) view.findViewById(R.id.tv_mode);

			holder.tv_num.setText(blankNumInfo.getBlankNum());
			switch (blankNumInfo.getMode()) {
			case BlankNum.SMS:
				holder.tv_mode.setText("��������");
				break;
			case BlankNum.TEL:
				holder.tv_mode.setText("�绰����");
				break;
			case BlankNum.ALL:
				holder.tv_mode.setText("ȫ������");
				break;

			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							CallSmsSafeActivity.this);
					builder.setTitle("�Ƿ�ɾ��" + blankNumInfo.getBlankNum());
					builder.setIcon(R.drawable.ic_launcher);
					builder.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dao.deleteBlankNum(blankNumInfo
											.getBlankNum());
									blanknums.remove(blankNumInfo);
									adapter.notifyDataSetChanged();// ˢ�½���
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("ȡ��", null); // null Ĭ�ϵ������dialog
					builder.show();

				}
			});

			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	/**
	 * �����˱ʼǱ�
	 * 
	 * @author yu
	 * 
	 */
	static class ViewHolder {
		TextView tv_num;
		TextView tv_mode;
		ImageView iv_delete;
	}
}
