package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.entity.PolygonBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PolygonAdapter extends BaseAdapter {

	private  LayoutInflater layoutInflater;
	List<PolygonBean> mList;
	
	public PolygonAdapter(Context context,List<PolygonBean> list) {
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
			viewHolder =new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.listview_polygon, null);
			viewHolder.polygonName=(TextView) convertView.findViewById(R.id.polygonNameshow);
			viewHolder.polygonArea=(TextView) convertView.findViewById(R.id.polygonAreashow);
			viewHolder.polygonLine=(TextView) convertView.findViewById(R.id.polygonLinesshow);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.polygonName.setText(mList.get(position).getPolyName());
		viewHolder.polygonArea.setText(mList.get(position).getPolyArea()+"m²");
		viewHolder.polygonLine.setText(mList.size()+"");
		return convertView;
	}
	private ViewHolder viewHolder;

	private static class ViewHolder{
		TextView polygonName;
		TextView polygonArea;
		TextView polygonLine;
	}

}
