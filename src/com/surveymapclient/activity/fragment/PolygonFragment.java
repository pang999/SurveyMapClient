package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.activity.adapter.PolygonAdapter;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.PolygonBean;

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

public class PolygonFragment extends Fragment {
	
	ListView listView;
	ImageView imageView;
	static Context mContext;
	static List<PolygonBean> mList;
	PolygonAdapter adapter;
	
	public static PolygonFragment newInstance(Context context,List<PolygonBean> list){
		PolygonFragment polygonFragment=new PolygonFragment();
		mContext=context;
		mList=list;
		return polygonFragment;
	}
	public PolygonFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_polygon, container, false);
		listView=(ListView) parentView.findViewById(R.id.listpolygons);
		adapter=new PolygonAdapter(mContext,mList);
		if (mList.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.nopolygonshow);
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
