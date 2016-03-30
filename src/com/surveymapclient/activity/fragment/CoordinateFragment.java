package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.CoordinateAdapter;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.CoordinateBean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CoordinateFragment extends Fragment {
	
	ListView listView;
	ImageView imageView;
	CoordinateAdapter adapter;
	static List<CoordinateBean> mlist;
	static Context mContext;
	public static CoordinateFragment newInstance(Context context,List<CoordinateBean> list){
		CoordinateFragment coordinateFragment=new CoordinateFragment();
		mContext=context;
		mlist=list;
		return coordinateFragment;
	}

	public CoordinateFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_coordinate, container, false);
		adapter=new CoordinateAdapter(mContext,mlist);
		listView=(ListView) parentView.findViewById(R.id.listcoordinate);
		if (mlist.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.nocoordinateshow);
			imageView.setVisibility(View.VISIBLE);
		}
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				IToast.show(mContext, "position="+position);
			}
		});
		return parentView;
	}
}
