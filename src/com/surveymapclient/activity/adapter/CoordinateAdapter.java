package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.entity.CoordinateBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CoordinateAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	List<CoordinateBean> mList;
	public CoordinateAdapter(Context context,List<CoordinateBean> list) {
		// TODO Auto-generated constructor stub
		layoutInflater=LayoutInflater.from(context);
		mList=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
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
			convertView=layoutInflater.inflate(R.layout.listview_coordinate, null);
			viewHolder.coordName=(TextView) convertView.findViewById(R.id.coordinateNameshow);
			viewHolder.coordVolum=(TextView) convertView.findViewById(R.id.coordinateVolumeshow);
			viewHolder.coordHeight=(TextView) convertView.findViewById(R.id.coordinateHeightshow);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.coordName.setText(mList.get(position).getName());
		viewHolder.coordVolum.setText(mList.get(position).getVolum()+"m³");
		viewHolder.coordHeight.setText(mList.get(position).getHeight()+"m");
		return convertView;
	}

	ViewHolder viewHolder;
	private static class ViewHolder{
		TextView coordName;
		TextView coordVolum;
		TextView coordHeight;
	}
}
