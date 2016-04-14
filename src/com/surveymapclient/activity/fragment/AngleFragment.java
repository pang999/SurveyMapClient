package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.AngleAdapter;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.AngleBean;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class AngleFragment extends Fragment {
	
	AngleAdapter adapter;
	ListView listView;
	ImageView imageView;
	static Context mContext;
	
	static List<AngleBean> mList;
	
	public static AngleFragment newInstance(Context context,List<AngleBean> list){
		AngleFragment angleFragment=new AngleFragment();
		mContext=context;
		mList=list;
		return angleFragment;
	}

	public AngleFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_angle, container, false);
		adapter=new AngleAdapter(mContext,mList);
		listView=(ListView) parentView.findViewById(R.id.listangle);
		if (mList.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.noangleshow);
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
