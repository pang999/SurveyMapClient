package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.entity.RectangleBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class RectangleAdapter extends BaseAdapter implements ListAdapter{

	LayoutInflater layoutInflater;
	List<RectangleBean> list;
	public RectangleAdapter(Context context,List<RectangleBean> list) {
		// TODO Auto-generated constructor stub
		this.list=list;
		layoutInflater=LayoutInflater.from(context);
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
			viewHolder=new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.listview_rectangle, null);
			viewHolder.rectName=(TextView) convertView.findViewById(R.id.rectangleNameshow);
			viewHolder.rectArea=(TextView) convertView.findViewById(R.id.rectangleAreashow);
			viewHolder.rectWidth=(TextView) convertView.findViewById(R.id.rectanglewidthshow);
			viewHolder.rectLenght=(TextView) convertView.findViewById(R.id.rectanglelenghshow);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.rectName.setText(list.get(position).getRectName());
		viewHolder.rectArea.setText(list.get(position).getRectArea()+"m²");
		viewHolder.rectWidth.setText(list.get(position).getPaintWidth()+"m");
		viewHolder.rectLenght.setText(list.get(position).getRectLenght()+"m");
		return convertView;
	}
	
	private ViewHolder viewHolder;
	
	private static class ViewHolder{
		TextView rectName;
		TextView rectArea;
		TextView rectWidth;
		TextView rectLenght;
	}

}
