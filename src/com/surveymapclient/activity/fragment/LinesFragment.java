package com.surveymapclient.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.AttributeLineActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.LineBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LinesFragment extends Fragment{
	
	ListView listView;
	LineAdapter adapter;
	ImageView imageView;
	static List<LineBean> mList;
	
	static Context mContext;	
	public static LinesFragment newInstance(Context context,List<LineBean> list){
		LinesFragment linesFragment=new LinesFragment();
		mContext=context;
		mList=list;
		return linesFragment;
	}
	
	public  LinesFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_lines, container, false);
		listView=(ListView) parentView.findViewById(R.id.listlines);
		if (mList.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.listnodatashow);
			imageView.setVisibility(View.VISIBLE);
		}
		adapter=new LineAdapter(mContext, mList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("TYPE", 0);
				bundle.putInt("Index", position);
				bundle.putSerializable("Line", mList.get(position));
				intent.putExtras(bundle);
				intent.setClass(getActivity(), AttributeLineActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		return parentView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
