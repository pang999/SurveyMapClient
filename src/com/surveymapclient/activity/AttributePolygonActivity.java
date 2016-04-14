package com.surveymapclient.activity;

import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.entity.PolygonBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AttributePolygonActivity extends Activity {
	EditText polygonName,polygonArea,polygonDesc;
	TextView polygonColor,polygonlines;
	ListView listview;
	LineAdapter adapter;
	PolygonBean polygon;
	int polygoncolor=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_polygon);
		polygonName=(EditText) findViewById(R.id.editpolygonName);
		polygonArea=(EditText) findViewById(R.id.editpolygonArea);
		polygonDesc=(EditText) findViewById(R.id.editpolygonDescripte);
		polygonColor=(TextView) findViewById(R.id.editpolygoneColor);
		polygonlines=(TextView) findViewById(R.id.polygonlineshow);
		listview=(ListView) findViewById(R.id.polygonlinelist);
		polygon=new PolygonBean();
		Bundle bundle=this.getIntent().getExtras();
		polygon=(PolygonBean) bundle.getSerializable("Polygon");
		polygonName.setText(polygon.getPolyName());
		polygonArea.setText(polygon.getPolyArea()+"");
		polygonDesc.setText(polygon.getDescripe());
		polygonlines.setText(""+polygon.getPolyLine().size());
		int c=polygon.getPolyColor();
		if (c==Color.RED) {
			polygonColor.setText("红色");
			polygoncolor=Color.RED;
		}else if (c==Color.BLUE) {
			polygonColor.setText("蓝色");
			polygoncolor=Color.BLUE;
		}else if (c==Color.GREEN) {
			polygonColor.setText("绿色");
			polygoncolor=Color.GREEN;
		}
		adapter=new LineAdapter(this, polygon.getPolyLine());
		listview.setAdapter(adapter);
	}
	public void onSetColor(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributePolygonActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    指定下拉列表的显示数据
		final String[] scolor = {"红色", "蓝色", "绿色"};
		builder.setTitle("选择一种颜色");
        //    设置一个下拉的列表选择项
		builder.setItems(scolor, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub				
				if (scolor[i]=="红色") {
					polygoncolor=Color.RED;
				}else if (scolor[i]=="蓝色") {
					polygoncolor=Color.BLUE;
				}else if (scolor[i]=="绿色") {
					polygoncolor=Color.GREEN;
				}
				polygonColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
	public void AttributeBack(View view){
		finish();
	}
}
