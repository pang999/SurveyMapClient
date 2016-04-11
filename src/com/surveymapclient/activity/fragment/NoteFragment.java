package com.surveymapclient.activity.fragment;

import java.util.List;

import com.surveymapclient.activity.AttributeLineActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.activity.adapter.NoteAdapter;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.NoteBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NoteFragment extends Fragment {

	ListView listView;
	NoteAdapter adapter;
	ImageView imageView;
	static List<NoteBean> mList;
	
	static Context mContext;	
	public static NoteFragment newInstance(Context context,List<NoteBean> list){
		NoteFragment noteFragment=new NoteFragment();
		mContext=context;
		mList=list;
		return noteFragment;
	}
	
	public  NoteFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parentView=inflater.inflate(R.layout.fragment_notes, container, false);
		listView=(ListView) parentView.findViewById(R.id.listnotes);
		if (mList.size()==0) {
			imageView=(ImageView) parentView.findViewById(R.id.listnodatashow);
			imageView.setVisibility(View.VISIBLE);
		}
		adapter=new NoteAdapter(mContext, mList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
		
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
