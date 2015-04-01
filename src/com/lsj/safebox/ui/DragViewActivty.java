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
		// ��ȡ��Ļ��͸�
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);// ������Ļ �Ѳ�����Ĳ���
														// ��ֵ��DisplayMetrics ����
		with = outMetrics.widthPixels;
		height = outMetrics.heightPixels;
		// ���ò���
		setContentView(R.layout.activity_dragview);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);
		// ����λ��
		int l = sp.getInt("lastX", 0);
		int t = sp.getInt("lastY", 0);
		System.out.println(l);
		System.out.println(t);
		// û�л�ȡ��͸� ��û��ִ����onMessure ��͸�û�в�������
		// int width = ll_drag_view.getWidth();
		// int height=ll_drag_view.getHeight();
		// System.out.println(width);
		// System.out.println(height);
		// �������Ҳû����Ч onLayout
		// ll_drag_view.layout(l, t, l+width, t+height);
		// ʹ�õĲ������� ȡ���� �ؼ����ڵĸ����������� �����֪�� ViewGroup.LayoutParams
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_drag_view
				.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;

		// �Ѳ�����ֵ���ؼ�
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
						// ������˫���¼�
						System.out.println("˫���¼�");
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
				case MotionEvent.ACTION_DOWN: // �����µ��¼�
					// 1 ��¼��ʼ��λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					System.out.println("������");
					break;
				case MotionEvent.ACTION_MOVE: // �ƶ����¼�
					// 2 ��¼�µ�λ��
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 3 �����ƶ�����ı仯
					int dX = newX - startX;
					int dY = newY - startY;
					// 4 �ؼ��ƶ�����ı仯
					// ���·���ؼ���λ�� l left top right bottom
					int l = ll_drag_view.getLeft() + dX;
					int t = ll_drag_view.getTop() + dY;
					int r = l + ll_drag_view.getWidth();
					int b = t + ll_drag_view.getHeight();
					// �������ƶ���Եλ��
					if (l < 0 || t < 0 || r > with || b > height - 15) {
						break;
					}
					ll_drag_view.layout(l, t, r, b);

					// 5
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// ��ָ̧����¼�
					// ��¼����
					int lastX = ll_drag_view.getLeft();
					int lastY = ll_drag_view.getTop();
					Editor edit = sp.edit();
					edit.putInt("lastX", lastX);
					edit.putInt("lastY", lastY);
					edit.commit();

					break;

				}
				// True if the listener has consumed the event, false otherwise.
				// ����ܹ����¼���������ִ�� ����ֵ �޸ĳ�true
				return false;
			}
		});
	}

}
