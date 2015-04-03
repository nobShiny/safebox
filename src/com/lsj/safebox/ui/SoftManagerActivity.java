package com.lsj.safebox.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import com.lsj.safebox.R;
import com.lsj.safebox.domin.AppInfo;
import com.lsj.safebox.engine.AppInfoProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SoftManagerActivity extends Activity implements OnClickListener {
	private ListView lv_soft_manager;
	private SoftAdapter adapter;
	private List<AppInfo> appInfos;
	private List<AppInfo> userInfos;// 用户程序的集合
	private List<AppInfo> systemInfos;// 系统程序的集合
	private TextView tv_count;
	private PopupWindow popupWindow;
	private AppInfo appInfo;
	Button main_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soft_manager);
		lv_soft_manager = (ListView) findViewById(R.id.lv_soft_manager);
		tv_count = (TextView) findViewById(R.id.tv_count);
		fillData();
		lv_soft_manager.setOnScrollListener(new OnScrollListener() {
			// 当listView 滑动状态发生变化的时候调用
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			// 当listview 滑动时候调用
			// firstVisibleItem listVIew 第一个可见条目的位置
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}

				if (firstVisibleItem >= userInfos.size() + 1) {
					tv_count.setText("系统程序(" + systemInfos.size() + ")个");
				} else {
					tv_count.setText("用户程序(" + userInfos.size() + ")个");
				}
			}
		});
		// 5
		lv_soft_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == userInfos.size() + 1) {
					return;
				}
				if (position <= userInfos.size()) {
					appInfo = userInfos.get(position - 1);
				} else {
					appInfo = systemInfos.get(position - userInfos.size() - 2);
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}

				// 获取条目的位置
				int[] location = new int[2];
				view.getLocationInWindow(location); // 获取到了x 和y的坐标 存放到了 2个元素的数组中
				int x = location[0];
				int y = location[1];
				// 弹出popupWindow
				View popup_view = View.inflate(getApplicationContext(),
						R.layout.popup_soft_manager, null);
				// 初始化控件
				LinearLayout ll_uninstall = (LinearLayout) popup_view
						.findViewById(R.id.ll_uninstall);
				LinearLayout ll_start = (LinearLayout) popup_view
						.findViewById(R.id.ll_start);
				LinearLayout ll_shared = (LinearLayout) popup_view
						.findViewById(R.id.ll_shared);
				// 设置点击事件
				ll_uninstall.setOnClickListener(SoftManagerActivity.this);
				ll_start.setOnClickListener(SoftManagerActivity.this);
				ll_shared.setOnClickListener(SoftManagerActivity.this);

				// 参数1 弹出框显示的布局 参数2 popupWIndow 宽度 参数高度
				popupWindow = new PopupWindow(popup_view, -2, -2);
				// 给popupWindow设置背景
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				// 展示popupWindow 参数 1 挂载的父窗体
				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						x + 50, y);

				AlphaAnimation animation = new AlphaAnimation(0.4f, 1.0f);
				animation.setDuration(1000);

				// ScaleAnimation scaleAnimation=new ScaleAnimation(0f, 1.0f,
				// 0f, 1.0f, 0.5f, 0.5f);
				ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1.0f,
						0f, 1.0f, Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(1000);

				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(scaleAnimation);
				animationSet.addAnimation(animation);
				popup_view.startAnimation(animationSet);// 运行动画
			}
		});
		
		main_back = (Button) findViewById(R.id.main_back);
		main_back.setOnClickListener(this);
	}

	public void fillData() {
		appInfos = AppInfoProvider.getAppInfos(this);
		userInfos = new ArrayList<AppInfo>();
		systemInfos = new ArrayList<AppInfo>();
		for (AppInfo appInfo : appInfos) {
			if (appInfo.isUser()) {
				userInfos.add(appInfo);
			} else {
				systemInfos.add(appInfo);
			}
		}
		if (adapter == null) {
			adapter = new SoftAdapter();
			lv_soft_manager.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	// 3
	private class SoftAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userInfos.size() + systemInfos.size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// AppInfo appInfo = appInfos.get(position);
			if (position == 0) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("用户程序(" + userInfos.size() + ")个");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			} else if (position == userInfos.size() + 1) {
				TextView textView = new TextView(getApplicationContext());
				textView.setText("系统程序(" + systemInfos.size() + ")个");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			}
			AppInfo appInfo;
			if (position <= userInfos.size()) {
				appInfo = userInfos.get(position - 1);
			} else {
				appInfo = systemInfos.get(position - userInfos.size() - 2);
			}

			View view = null;
			ViewHolder holder = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_soft_manager, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_rom = (TextView) view.findViewById(R.id.tv_rom);

				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());

			if (appInfo.isRom()) {
				holder.tv_rom.setText("手机内存");
			} else {
				holder.tv_rom.setText("SD卡中");
			}

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

	// 5.1
	@Override
	protected void onDestroy() {
		// 避免窗体泄露
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
		super.onDestroy();
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_rom;
	}

	// 6
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_uninstall:// 卸载
			uninstall();
			break;
		case R.id.ll_start:// 启动
			start();
			break;
		case R.id.ll_shared:// 分享
			shared();
			break;
		case R.id.main_back:
			finish();
			break;

		}
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	// 6.1
	private void shared() {
		/*
		 * START {act=android.intent.action.SEND typ=text/plain flg=0x3000001
		 * 
		 * cmp=com.android.mms/.ui.ComposeMessageActivity (has clip) (has
		 * extras) u=0} from pid 629
		 */
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐您一款应用程序" + appInfo.getName()
				+ "下载地址google市场");
		startActivity(intent);

		// 详细信息
		// Intent intent=new Intent();
		// intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		// intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
		// startActivity(intent);
	}

	// 6.2
	private void start() {
		PackageManager manager = getPackageManager();
		Intent launchIntentForPackage = manager
				.getLaunchIntentForPackage(appInfo.getPackageName());
		startActivity(launchIntentForPackage);
	}

	// 6.3
	/*
	 * <intent-filter> <action android:name="android.intent.action.VIEW" />
	 * <action android:name="android.intent.action.DELETE" /> <category
	 * android:name="android.intent.category.DEFAULT" /> <data
	 * android:scheme="package" /> </intent-filter>
	 */
	private void uninstall() {
		if (appInfo.isUser()) {
			if (appInfo.getPackageName().equals(getPackageName())) {
				Toast.makeText(getApplicationContext(), "想卸载我没门!!!!!!!!", 0)
						.show();
				return;
			}
			Intent intent = new Intent();
			intent.setAction("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
			startActivityForResult(intent, 0);
		} else {
			Toast.makeText(getApplicationContext(), "系统程序需要root权限才能删除", 0)
					.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}
	
}
