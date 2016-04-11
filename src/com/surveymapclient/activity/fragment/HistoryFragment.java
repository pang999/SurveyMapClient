package com.surveymapclient.activity.fragment;

import java.util.List;

<<<<<<< HEAD
import com.surveymapclient.activity.AttributeLineActivity;
import com.surveymapclient.activity.DefineActivity;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.HistoryAdapter;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
<<<<<<< HEAD
import com.surveymapclient.db.DBHelper;
import com.surveymapclient.db.OperateData;
import com.surveymapclient.db.greendao.Module;

import android.content.Context;
import android.content.Intent;
=======
import com.surveymapclient.db.greendao.Module;

import android.content.Context;
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import android.widget.ListView;

public class HistoryFragment extends Fragment {

	ListView listview;
	HistoryAdapter adapter;
	Context mContext;
	List<Module> mList;
<<<<<<< HEAD
	DBHelper helper;
	public HistoryFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
//		Logger.i("数据库数据", "Module="+list.size());	
		helper=DBHelper.getInstance(context);
		mList=OperateData.searchModule(helper);
=======
	public HistoryFragment(Context context,List<Module> list) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		Logger.i("数据库数据", "Module="+list.size());				
		mList=list;
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.activity_history, container, false);
		listview=(ListView) view.findViewById(R.id.historylist);
		adapter=new HistoryAdapter(mContext,mList);
		listview.setAdapter(adapter);
<<<<<<< HEAD
		initView();
		return view;
	}
	void initView(){
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				helper.deleteDataModule(mList.get(position));
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("TYPE", 0);
				bundle.putLong("KEY", mList.get(position).getKey());
				bundle.putString("Title", mList.get(position).getName());
				intent.putExtras(bundle);				
				intent.setClass(getActivity(), DefineActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}
=======
		return view;
	}
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
}
