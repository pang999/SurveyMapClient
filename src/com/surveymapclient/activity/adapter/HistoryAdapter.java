package com.surveymapclient.activity.adapter;

import com.surveymapclient.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private  LayoutInflater layoutInflater;
	
	public HistoryAdapter(Context context) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView=layoutInflater.inflate(R.layout.listview_history, null);
			viewHolder.historyName=(TextView) convertView.findViewById(R.id.historylist);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.historyName.setText("测得墙体的数据");
		return convertView;
	}
	private ViewHolder viewHolder;

	private static class ViewHolder{
		ImageView imageView;
		TextView historyName;
	}
	
}
