package com.lsj.safebox.ui;

import com.lsj.safebox.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class DragViewActivty extends Activity {
	private LinearLayout ll_drag_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ò���
		setContentView(R.layout.activity_dragview);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);

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
					System.out.println("�ƶ���");
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
					ll_drag_view.layout(l, t, r, b);

					// 5
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// ��ָ̧����¼�
					System.out.println("̧����");
					break;

				}
				// True if the listener has consumed the event, false otherwise.
				// ����ܹ����¼���������ִ�� ����ֵ �޸ĳ�true
				return true;
			}
		});
	}

}
