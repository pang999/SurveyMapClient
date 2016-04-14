package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.db.greendao.Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private  LayoutInflater layoutInflater;
	private List<Module> mList;
	
	public HistoryAdapter(Context context,List<Module> list) {
		// TODO Auto-generated constructor stub
		layoutInflater = LayoutInflater.from(context);
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
			viewHolder = new ViewHolder();
			convertView=layoutInflater.inflate(R.layout.listview_history, null);
			viewHolder.historyName=(TextView) convertView.findViewById(R.id.historylist);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.img_type);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.historyName.setText(mList.get(position).getName());
		if (mList.get(position).getImgUrl()!=null&&!"".equals(mList.get(position).getImgUrl())) {
			viewHolder.imageView.setBackgroundResource(R.drawable.meinv);
		}else {
			viewHolder.imageView.setBackgroundResource(R.drawable.wangge);
		}
		return convertView;
	}
	
	private ViewHolder viewHolder;

	private static class ViewHolder{
		ImageView imageView;
		TextView historyName;
	}
	
}
