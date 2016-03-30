package com.surveymapclient.activity.fragment;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.HistoryAdapter;
import com.surveymapclient.common.Contants;

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
	public HistoryFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_and_popu_history, container, false);
		listview=(ListView) view.findViewById(R.id.list_history_item);
		adapter=new HistoryAdapter(mContext);
		listview.setAdapter(adapter);
		return view;
	}
}
