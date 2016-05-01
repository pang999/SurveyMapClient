package com.surveymapclient.activity;

import java.util.ArrayList;
import java.util.List;
import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class DefineDataListActivity extends FragmentActivity implements OnClickListener {

	ImageView btnback; 
    ListView listView;
	Button complete; 
    List<LineBean> linelist;
    List<LineBean> list;
    List<PolygonBean> polygonlist;
    LineAdapter adapter;
   
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_datalist);
		btnback=(ImageView) findViewById(R.id.btnback);
		listView=(ListView) findViewById(R.id.linelists);
		complete=(Button) findViewById(R.id.complete);
		complete.setOnClickListener(this);
		btnback.setOnClickListener(this);
		Intent intent=getIntent(); 
		linelist=(List<LineBean>)intent.getSerializableExtra("LineList");
		polygonlist=(List<PolygonBean>) intent.getSerializableExtra("PolyList");
		list=new ArrayList<LineBean>();
		list=linelist;
		for (int i = 0; i < polygonlist.size(); i++) {
			List<LineBean> lineBeans=polygonlist.get(i).getPolyLine();
			for (int j = 0; j <lineBeans.size() ; j++) {
				list.add(lineBeans.get(j));
			}
		}
		adapter=new LineAdapter(this,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("TYPE", 0);
				intent.putExtra("Index", position);
				intent.putExtra("Line", list.get(position));
				intent.setClass(DefineDataListActivity.this, AttributeLineActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnback:
			     finish();
			break;

		default:
			break;
		}
	}

}
