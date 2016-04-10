package com.surveymapclient.activity;

import java.util.ArrayList;
import java.util.Arrays;

import com.surveymapclient.activity.fragment.HistoryFragment;
import com.surveymapclient.activity.fragment.HomeFragment;
import com.surveymapclient.activity.fragment.SettingFragment;
import com.surveymapclient.db.DBHelper;
import com.tencent.a.a.a.a.h;

import android.app.Activity;

import android.content.res.Configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;


public class HomeActivity extends FragmentActivity {
	
	private static final String FRAGMENT_TAGS = "fragmentTags";
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;
	private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;
    private DBHelper helper;
    RadioGroup group;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		helper=DBHelper.getInstance(this);
		fragmentManager = getSupportFragmentManager();
		if (savedInstanceState == null) {
            initData();
            initView();
        } else {
            initFromSavedInstantsState(savedInstanceState);
        }
	}
	 private void initFromSavedInstantsState(Bundle savedInstanceState) {
	        currIndex = savedInstanceState.getInt(CURR_INDEX);
	        fragmentTags = savedInstanceState.getStringArrayList(FRAGMENT_TAGS);
	        showFragment();
	    }
	private void initData() {
        currIndex = 0;
        fragmentTags = new ArrayList<String>(Arrays.asList("Fragment1", "Fragment2", "Fragment3"));
    }

    private void initView() {
       group=(RadioGroup) findViewById(R.id.bottom_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.foot_bar_home: currIndex = 0; break;
                    case R.id.foot_bar_health: currIndex = 1; break;
                    case R.id.foot_bar_market: currIndex = 2; break;
                    default: break;
                }
                showFragment();
            }
        });
        showFragment();
    }
    private void showFragment() {
    	  Fragment fragment=null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      try{
        if( fragmentManager!=null)fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
      }catch(Exception e){
    	  return;
      }
        if (fragment == null) {
            fragment = instantFragment(currIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }
    private Fragment instantFragment(int currIndex) {
        Fragment fragment = null;
        switch (currIndex) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new HistoryFragment(this,helper.searchDataModule());
                break;
            case 2:
                fragment = new SettingFragment();
                break;
        }
        return fragment;
    }
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
