package com.surveymapclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.surveymapclient.activity.R.layout;
import com.surveymapclient.activity.fragment.AngleFragment;
import com.surveymapclient.activity.fragment.CoordinateFragment;
import com.surveymapclient.activity.fragment.LinesFragment;
import com.surveymapclient.activity.fragment.NoteFragment;
import com.surveymapclient.activity.fragment.PolygonFragment;
import com.surveymapclient.activity.fragment.RectangleFragment;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.TimeUtils;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.NoteBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.utils.PagerSlidingTabStrip;

import android.R.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DataListActivity extends FragmentActivity {

    PagerSlidingTabStrip pagerSlidingTabStrip;
    ViewPager pager;   
    private DisplayMetrics dm;
    private ArrayList<String> list = new ArrayList<String>();
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    List<LineBean> linelist;
    List<PolygonBean> polygonlist;
    List<RectangleBean> rectlist;
    List<CoordinateBean> coorlist;
    List<AngleBean> anglelist;
    List<TextBean> textlist;
    List<AudioBean> audiolist;
    List<NoteBean> notelist;
    public Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_daralist);
		pagerSlidingTabStrip=(PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager=(ViewPager) findViewById(R.id.pager);
		notelist=new ArrayList<NoteBean>();
		textlist=new ArrayList<TextBean>();
		audiolist=new ArrayList<AudioBean>();
		Intent intent=getIntent(); 
		linelist=(List<LineBean>)intent.getSerializableExtra("LineList");
		polygonlist=(List<PolygonBean>) intent.getSerializableExtra("PolyList");
		rectlist=(List<RectangleBean>) intent.getSerializableExtra("RectList");	
		coorlist=(List<CoordinateBean>) intent.getSerializableExtra("CoorList");
		anglelist=(List<AngleBean>) intent.getSerializableExtra("AngleList");
		textlist=(List<TextBean>) intent.getSerializableExtra("TextList");
		audiolist=(List<AudioBean>) intent.getSerializableExtra("AudioList");
		for (int i = 0; i < textlist.size(); i++) {
			NoteBean note=new NoteBean();
			note.setText(textlist.get(i).getText());
			notelist.add(note);
		}
		for (int i = 0; i < audiolist.size(); i++) {
			NoteBean note=new NoteBean();
			note.setAudioUrl(audiolist.get(i).getUrl());
			note.setAudioTime(TimeUtils.convertMilliSecondToMinute2(audiolist.get(i).getLenght()));
			notelist.add(note);
		}
		Logger.i("传递linelist","传递linelist="+ linelist.size());
		HandleData();
		mContext=this;
	}
	  public void HandleData() {
	        dm = getResources().getDisplayMetrics();
	        pagerSlidingTabStrip.setFillViewport(true);
	        pagerSlidingTabStrip.setShouldExpand(true);
	        pagerSlidingTabStrip.setTabBackground(0);
	        pagerSlidingTabStrip.setDividerColor(0);
	        pagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(2,
	                14.0F, dm));
	        setPagerSlidingTabStrip();
	    }
	    private void setPagerSlidingTabStrip() {
	        list.add("直线");
	        list.add("多变形");
	        list.add("矩形");
	        list.add("坐标");
	        list.add("角度");
	        list.add("注释");
	        myFragmentPagerAdapter = new MyFragmentPagerAdapter(
	                getSupportFragmentManager(), list);
	        pager.setAdapter(myFragmentPagerAdapter);
	        pagerSlidingTabStrip.setViewPager(pager);
	    }
	    public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
	        private ArrayList<String> list = new ArrayList<String>();
	        public MyFragmentPagerAdapter(FragmentManager fm) {
	            super(fm);
	        }
	        public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<String> list){
	            super(fm);
	            this.list=list;
	        }

	        @Override
	        public CharSequence getPageTitle(int position) {
	            return list.get(position);
	        }

	        @Override
	        public Fragment getItem(int position) {
	            Fragment fragment=null;
	            switch (position){
	                case 0:
	                    fragment= LinesFragment.newInstance(mContext,linelist);
	                    break;
	                case 1:
	                    fragment= PolygonFragment.newInstance(mContext,polygonlist);
	                    break;
	                case 2:
	                    fragment= RectangleFragment.newInstance(mContext,rectlist);
	                    break;
	                case 3:
	                    fragment= CoordinateFragment.newInstance(mContext,coorlist);
	                    break;
	                case 4:
	                    fragment= AngleFragment.newInstance(mContext,anglelist);
	                    break;
	                case 5:
	                	fragment=NoteFragment.newInstance(mContext, notelist);
	            }
	            return fragment;
	        }

	        @Override
	        public int getCount() {
	            return list.size();
	        }
	    }
	    
	    
	    
	    
	    
	    
	    
	    public void onClickBack(View v){
	    	finish();
	    }
}
