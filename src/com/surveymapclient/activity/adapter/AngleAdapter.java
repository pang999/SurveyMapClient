package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.activity.R.layout;
import com.surveymapclient.entity.AngleBean;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AngleAdapter extends BaseAdapter{

	LayoutInflater layoutInflater;
	List<AngleBean> mList;
	
	public AngleAdapter(Context context,List<AngleBean> list) {
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
			convertView=layoutInflater.inflate(R.layout.listview_angle, null);
			viewHolder.angleName=(TextView) convertView.findViewById(R.id.angleNameshow);
			viewHolder.angleAngle=(TextView) convertView.findViewById(R.id.angleAngleshow);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.angleName.setText(mList.get(position).getName());
		viewHolder.angleAngle.setText(mList.get(position).getAngle()+"бу");
		return convertView;
	}
	
	ViewHolder viewHolder;

	private static class ViewHolder{
		TextView angleName;
		TextView angleAngle;
	}
}
