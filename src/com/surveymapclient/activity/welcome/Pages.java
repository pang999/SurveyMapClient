package com.surveymapclient.activity.welcome;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.ViewPagerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Pages extends Activity implements OnPageChangeListener {
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pages);

		// 初始化页�?
		initViews();

		// 初始化底部小�?
		initDots();
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列�?
		views.add(inflater.inflate(R.layout.page_one, null));
		views.add(inflater.inflate(R.layout.page_two, null));
		views.add(inflater.inflate(R.layout.page_three, null));
		views.add(inflater.inflate(R.layout.page_four, null));

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);
		vp = (ViewPager) findViewById(R.id.pager_viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private LinearLayout ll;

	private void initDots() {
		ll = (LinearLayout) findViewById(R.id.points);
		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(false);
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(true);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(true);
		dots[currentIndex].setEnabled(false);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	// 当当前页面被滑动时调�?
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 当新的页面被选中时调�?
	@Override
	public void onPageSelected(int arg0) {

		if (arg0 == views.size() - 1) {
			ll.setVisibility(View.INVISIBLE);
		} else {
			ll.setVisibility(View.VISIBLE);
		}
		// 设置底部小点选中状�?
		setCurrentDot(arg0);
	}

}