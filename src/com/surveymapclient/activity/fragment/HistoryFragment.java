package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.HistoryAdapter;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.db.greendao.Module;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryFragment extends Fragment {

	ListView listview;
	HistoryAdapter adapter;
	Context mContext;
	List<Module> mList;
	public HistoryFragment(Context context,List<Module> list) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		Logger.i("数据库数据", "Module="+list.size());				
		mList=list;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.activity_history, container, false);
		listview=(ListView) view.findViewById(R.id.historylist);
		adapter=new HistoryAdapter(mContext,mList);
		listview.setAdapter(adapter);
		return view;
	}
}
