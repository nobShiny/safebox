package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragViewActivty extends Activity {
	private LinearLayout ll_drag_view;
	private SharedPreferences sp;
	private WindowManager wm;
	private int with;
	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		// 获取屏幕宽和高
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);// 测量屏幕 把测量后的参数
														// 赋值给DisplayMetrics 对象
		with = outMetrics.widthPixels;
		height = outMetrics.heightPixels;
		// 设置布局
		setContentView(R.layout.activity_dragview);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);
		// 回显位置
		int l = sp.getInt("lastX", 0);
		int t = sp.getInt("lastY", 0);
		System.out.println(l);
		System.out.println(t);
		// 没有获取宽和高 还没有执行完onMessure 宽和高没有测量出来
		// int width = ll_drag_view.getWidth();
		// int height=ll_drag_view.getHeight();
		// System.out.println(width);
		// System.out.println(height);
		// 这个方法也没有生效 onLayout
		// ll_drag_view.layout(l, t, l+width, t+height);
		// 使用的参数类型 取决于 控件所在的父容器的类型 如果不知道 ViewGroup.LayoutParams
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_drag_view
				.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;

		// 把参数赋值给控件
		ll_drag_view.setLayoutParams(params);

		setTouch();

		setDoubleClick();
	}

	long firstTime;

	private void setDoubleClick() {

		ll_drag_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("aaaa");
				if (firstTime != 0) {
					long secondTime = System.currentTimeMillis();
					if (secondTime - firstTime < 500) {
						// 产生了双击事件
						System.out.println("双击事件");
						firstTime = 0;

						int halfHeiht = height / 2;
						int halfWidth = with / 2;

						int t = halfHeiht - ll_drag_view.getHeight() / 2;
						int l = halfWidth - ll_drag_view.getWidth() / 2;

						ll_drag_view.layout(l, t, l + ll_drag_view.getWidth(),
								t + ll_drag_view.getHeight());
					}
				} else {
					firstTime = System.currentTimeMillis();
					new Thread() {
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							firstTime = 0;
						};
					}.start();
				}
			}
		});
	}

	public void setTouch() {
		ll_drag_view.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: // 处理按下的事件
					// 1 记录开始的位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					System.out.println("按下了");
					break;
				case MotionEvent.ACTION_MOVE: // 移动的事件
					// 2 记录新的位置
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 3 计算移动坐标的变化
					int dX = newX - startX;
					int dY = newY - startY;
					// 4 控件移动坐标的变化
					// 重新分配控件的位置 l left top right bottom
					int l = ll_drag_view.getLeft() + dX;
					int t = ll_drag_view.getTop() + dY;
					int r = l + ll_drag_view.getWidth();
					int b = t + ll_drag_view.getHeight();
					// 不允许移动边缘位置
					if (l < 0 || t < 0 || r > with || b > height - 15) {
						break;
					}
					ll_drag_view.layout(l, t, r, b);

					// 5
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// 手指抬起的事件
					// 记录坐标
					int lastX = ll_drag_view.getLeft();
					int lastY = ll_drag_view.getTop();
					Editor edit = sp.edit();
					edit.putInt("lastX", lastX);
					edit.putInt("lastY", lastY);
					edit.commit();

					break;

				}
				// True if the listener has consumed the event, false otherwise.
				// 如果能够让事件正常往下执行 返回值 修改成true
				return false;
			}
		});
	}

}
