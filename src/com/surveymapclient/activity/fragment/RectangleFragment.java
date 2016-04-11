package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.RectangleAdapter;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.RectangleBean;

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

public class RectangleFragment extends Fragment {
	
	ListView listView;
	RectangleAdapter adapter;
	ImageView imageView;
	static Context mContext;
	static List<RectangleBean> mList;
	public static RectangleFragment newInstance(Context context,List<RectangleBean> list){
		RectangleFragment rectangleFragment=new RectangleFragment();
		mContext=context;
		mList=list;
		return rectangleFragment;
	}
	public RectangleFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_rectangle, container, false);
		adapter=new RectangleAdapter(mContext,mList);
		listView=(ListView) parentView.findViewById(R.id.listrectangle);
		if (mList.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.rectnodatashow);
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
