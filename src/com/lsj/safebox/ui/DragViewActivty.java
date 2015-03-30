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
		// 设置布局
		setContentView(R.layout.activity_dragview);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);

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
					System.out.println("移动了");
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
					ll_drag_view.layout(l, t, r, b);

					// 5
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// 手指抬起的事件
					System.out.println("抬起了");
					break;

				}
				// True if the listener has consumed the event, false otherwise.
				// 如果能够让事件正常往下执行 返回值 修改成true
				return true;
			}
		});
	}

}
