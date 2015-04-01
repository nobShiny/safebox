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
		// 注册滑动的监听
		lv_call_sms.setOnScrollListener(new OnScrollListener() {
			// listview 滑动状态 发生变化调用
			// 静止状态
			// 滑动状态
			// 惯性滑动 飞速滑动
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE: // 静止状态
					int lastVisiblePosition = lv_call_sms
							.getLastVisiblePosition();// 获取到最后一个可见的位置
					if (lastVisiblePosition == blanknums.size() - 1) {
						// 到达最后一个位置
						// 加载新的数据
						startIndex += MAX_NUM;
						fillData();// 重新加载数据
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: // 滑动状态
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 惯性滑动状态
					break;
				}
			}

			// 当listVIew 滑动时候调用
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
					// 在原有集合上 添加新的集合
					blanknums.addAll(dao.queryPart(MAX_NUM, startIndex));
				}
			}
		}.excute();
	}

	// 添加按钮的点击事件
	public void addBlacknum(View v) {
		AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
		dialog = builder.create();
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_add_blanknum, null);

		// 初始化控件
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
					Toast.makeText(getApplicationContext(), "黑名单不能为空", 0)
							.show();
				} else if (dao.queryBlankNum(blanknum) == -1) {
					switch (rg.getCheckedRadioButtonId()) {
					case R.id.rb_sms:// 短信拦截
						dao.addBlankNum(blanknum, BlankNum.SMS);
						blanknums.add(0, new BlankNumInfo(blanknum,
								BlankNum.SMS));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						break;
					case R.id.rb_tel:// 电话拦截
						dao.addBlankNum(blanknum, BlankNum.TEL);
						blanknums.add(0, new BlankNumInfo(blanknum,
								BlankNum.TEL));
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						break;
					case R.id.rb_all:// 全部拦截
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

		// convertView 代表刚出屏幕 view对象
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			//
			if (convertView != null && convertView instanceof RelativeLayout) {
				System.out.println("复用了拖" + position);
				// 复用了拖
				view = convertView;
				// 口袋里拿出笔记本
				holder = (ViewHolder) view.getTag();

			} else {
				System.out.println("请了新的拖" + position); // 8次
				// 顾了新的拖
				view = View.inflate(getApplicationContext(),
						R.layout.item_call_sms_safe, null);
				// 买了个笔记本
				holder = new ViewHolder();
				// 记录映射关系
				holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				// 把笔记本放到口袋里
				view.setTag(holder);
			}
			final BlankNumInfo blankNumInfo = blanknums.get(position);

			// TextView tv_num=(TextView) view.findViewById(R.id.tv_num);
			// TextView tv_mode=(TextView) view.findViewById(R.id.tv_mode);

			holder.tv_num.setText(blankNumInfo.getBlankNum());
			switch (blankNumInfo.getMode()) {
			case BlankNum.SMS:
				holder.tv_mode.setText("短信拦截");
				break;
			case BlankNum.TEL:
				holder.tv_mode.setText("电话拦截");
				break;
			case BlankNum.ALL:
				holder.tv_mode.setText("全部拦截");
				break;

			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							CallSmsSafeActivity.this);
					builder.setTitle("是否删除" + blankNumInfo.getBlankNum());
					builder.setIcon(R.drawable.ic_launcher);
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									dao.deleteBlankNum(blankNumInfo
											.getBlankNum());
									blanknums.remove(blankNumInfo);
									adapter.notifyDataSetChanged();// 刷新界面
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("取消", null); // null 默认点击隐藏dialog
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
	 * 创建了笔记本
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
