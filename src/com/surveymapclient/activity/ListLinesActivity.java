package com.surveymapclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.surveymapclient.activity.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListLinesActivity extends Activity {

	SimpleAdapter adapter;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_listlines);
		listview=(ListView) findViewById(R.id.listlines);
		List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();  
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("lenght", i*100);
			map.put("angle", "0.0");
			map.put("name", "lineName"+i);
			data.add(map);
		}
		adapter = new SimpleAdapter(this, data, R.layout.listlines_item, new String[]{"lenght","angle","name"}, new int[]{R.id.list_lineslenght,R.id.list_linesangle,R.id.list_linesname}); 				
		listview.setAdapter(adapter);
	}
}
