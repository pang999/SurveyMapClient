package com.surveymapclient.activity;

import java.util.List;

import com.surveymapclient.activity.adapter.HistoryAdapter;
import com.surveymapclient.db.DBHelper;
import com.surveymapclient.db.greendao.Module;
import com.tencent.a.a.a.a.h;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
		adapter=new HistoryAdapter(this,helper.searchDataModule());
		listview.setAdapter(adapter);
	}

}
