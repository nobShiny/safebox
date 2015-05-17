package com.lsj.safebox.ui;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.R;
import com.lsj.safebox.db.dao.Applock;
import com.lsj.safebox.domin.AppInfo;
import com.lsj.safebox.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �������
 * @author Administrator
 *
 */
public class AppLockActivity extends Activity implements OnClickListener {

	private TextView tv_unlock;
	private TextView tv_locked;
	private LinearLayout ll_unlock;
	private LinearLayout ll_locked;
	private Button btn_return;

	private ListView unlock_list;
	private ListView locked_list;
	private List<AppInfo> appInfos;
	private List<AppInfo> unlockappInfos;
	private List<AppInfo> lockedappInfos;

	private TextView tv_unlock_info;
	private TextView tv_locked_info;

	private Applock dao;

	private AppLockAdapter unlockadapter;
	private AppLockAdapter lockedadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		Toast.makeText(getApplicationContext(), "�ڼ���ǰ�����������￪���������", Toast.LENGTH_LONG).show();
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_locked = (TextView) findViewById(R.id.tv_locked);
		ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
		unlock_list = (ListView) findViewById(R.id.unlock_list);
		locked_list = (ListView) findViewById(R.id.locked_list);
		tv_unlock_info = (TextView) findViewById(R.id.tv_unlock_info);
		tv_locked_info = (TextView) findViewById(R.id.tv_locked_info);
		tv_unlock.setOnClickListener(this);
		tv_locked.setOnClickListener(this);
		
		// ���ذ�ť
		btn_return = (Button) findViewById(R.id.main_back);
		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.main_back:
					finish();
					break;
				}
			}
		});

		// ���������߳�ȥд�õ����������Ϣ
		appInfos = AppInfoProvider.getAppInfos(this);
		dao = new Applock(this);
		unlockappInfos = new ArrayList<AppInfo>();
		lockedappInfos = new ArrayList<AppInfo>();
		for (AppInfo appInfo : appInfos) {
			if (dao.find(appInfo.getPackageName())) {
				lockedappInfos.add(appInfo);
			} else {
				unlockappInfos.add(appInfo);
			}
		}

		unlockadapter = new AppLockAdapter(true);
		lockedadapter = new AppLockAdapter(false);
		unlock_list.setAdapter(unlockadapter);
		locked_list.setAdapter(lockedadapter);
	}

	private class AppLockAdapter extends BaseAdapter {
		private boolean isFlag = true;

		public AppLockAdapter(boolean isFlag) {
			this.isFlag = isFlag;
		}

		@Override
		public int getCount() {
			if (isFlag) {
				tv_unlock_info.setText("δ�������:" + unlockappInfos.size());
				return unlockappInfos.size();
			} else {
				tv_locked_info.setText("�Ѽ������:" + lockedappInfos.size());
				return lockedappInfos.size();
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(AppLockActivity.this,
						R.layout.list_applock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.iv_status = (ImageView) view
						.findViewById(R.id.iv_status);

				view.setTag(holder);
			}

			// �õ�δ�������-�ֻ����û������ϵͳ���
			final AppInfo appInfo;
			if (isFlag) {
				appInfo = unlockappInfos.get(position);
				holder.iv_status.setImageResource(R.drawable.lock);
			} else {
				appInfo = lockedappInfos.get(position);
				holder.iv_status.setImageResource(R.drawable.unlock);
			}

			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());

			holder.iv_status.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isFlag) {

						// 4.ˢ��ҳ��
						// notifyDataSetChanged();
						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 1.0f,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(500);

						view.startAnimation(ta);

						// /��û��һ���������������߳�ִ�У����ҿ��Եȴ�500�����أ�
						// ������ô��
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// 1.��ӵ��Ѽ������ݿ�����
								dao.add(appInfo.getPackageName());

								// 2.��δ�����б��Ƴ�
								unlockappInfos.remove(appInfo);
								// 3.�Ѽ����б�Ҫ����Ƴ�������
								lockedappInfos.add(appInfo);

								unlockadapter.notifyDataSetChanged();
								lockedadapter.notifyDataSetChanged();

							}
						}, 500);

					} else {
						

						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, -1.0f,
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(500);

						view.startAnimation(ta);

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// 1.��ӵ�δ����
								unlockappInfos.add(appInfo);
								// 2.���Ѽ������ݿ��Ƴ�����
								dao.delete(appInfo.getPackageName());
								// 3.�Ѽ����б��Ƴ�����
								lockedappInfos.remove(appInfo);

								// 4.ˢ��ҳ��
								// notifyDataSetChanged();
								unlockadapter.notifyDataSetChanged();
								lockedadapter.notifyDataSetChanged();

							}
						}, 500);

					}

				}
			});

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		ImageView iv_status;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_unlock:// δ����
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_locked.setBackgroundResource(R.drawable.tab_right_default);
			ll_unlock.setVisibility(View.VISIBLE);

			break;

		case R.id.tv_locked:// �Ѽ���
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);

			ll_unlock.setVisibility(View.GONE);

			break;
		}

	}

}
