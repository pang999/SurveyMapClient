package com.surveymapclient.activity;

import java.io.File;
import java.util.List;
import com.surveymapclient.activity.adapter.HistoryAdapter;
import com.surveymapclient.common.IToast;
import com.surveymapclient.db.DBHelper;
import com.surveymapclient.db.OperateData;
import com.surveymapclient.db.greendao.Module;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class HistoryActivity extends Activity {
	HistoryAdapter adapter;
	ListView listview;
	private DBHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		helper=DBHelper.getInstance(this);
		listview=(ListView) findViewById(R.id.historylist);

		final List<Module> mList=OperateData.searchModule(helper);
		adapter=new HistoryAdapter(this,mList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				int type=mList.get(position).getType();				
				if (type==0) {		
					helper.deleteDataModule(mList.get(position));
					Intent intent=new Intent();
					Bundle bundle=new Bundle();
					bundle.putInt("TYPE", 0);
					bundle.putLong("KEY", mList.get(position).getKey());
					bundle.putString("Title", mList.get(position).getName());
					intent.putExtras(bundle);	
					intent.setClass(HistoryActivity.this, DefineActivity.class);
					startActivity(intent);
				}
				if (type==1) {
					File file=new File(mList.get(position).getImgUrl());
					if (file.exists()) {
						helper.deleteDataModule(mList.get(position));
						Intent intent=new Intent();
						Bundle bundle=new Bundle();
						bundle.putInt("TYPE", 0);
						bundle.putLong("KEY", mList.get(position).getKey());
						bundle.putString("Title", mList.get(position).getName());
						bundle.putString("imgUrl", mList.get(position).getImgUrl());
						intent.putExtras(bundle);	
						intent.setClass(HistoryActivity.this, CameraActivity.class);
						startActivity(intent);
					}else {
						helper.deleteDataModule(mList.get(position));
						IToast.show(HistoryActivity.this, "±³¾°Í¼Æ¬ÒÑ±»É¾³ý");
					}					
				}
			}
		});

		adapter=new HistoryAdapter(this,helper.searchDataModule());
		listview.setAdapter(adapter);

	}

}
