package com.surveymapclient.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.powerble.BLEHomePageActivity;
import com.surveymapclient.activity.R;

public class SettingFragment extends Fragment implements OnClickListener{
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_setting, container, false);	
		LinearLayout ll_ble=(LinearLayout)view.findViewById(R.id.onSetBLE);
		ll_ble.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.onSetBLE:
			startActivity(new Intent(getActivity(),BLEHomePageActivity.class));
			break;

		default:
			break;
		}
	}


}
