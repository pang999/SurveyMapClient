package com.surveymapclient.activity.adapter;


import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.entity.LineBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LineAdapter extends BaseAdapter implements ListAdapter{

	private  LayoutInflater layoutInflater;
	
	private List<LineBean> list;
	
	public LineAdapter(Context context,List<LineBean> list) {
		// TODO Auto-generated constructor stub
		this.list=list;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.listview_line, null);
			viewHolder.lineName=(TextView) convertView.findViewById(R.id.lineNameshow);
			viewHolder.lineLenght=(TextView) convertView.findViewById(R.id.lineLenghtshow);
			viewHolder.lineAngle=(TextView) convertView.findViewById(R.id.lineAngleshow);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.lineName.setText(list.get(position).getName());
		viewHolder.lineLenght.setText(list.get(position).getLength()+"m");
		viewHolder.lineAngle.setText(list.get(position).getAngle()+"бу");
//		viewHolder.lineName.setText("lineName");
//		viewHolder.lineLenght.setText("100 m");
//		viewHolder.lineAngle.setText("79бу");
		return convertView;
	}
	
	private ViewHolder viewHolder;

	private static class ViewHolder{
		TextView lineName;
		TextView lineLenght;
		TextView lineAngle;
	}
	

}
