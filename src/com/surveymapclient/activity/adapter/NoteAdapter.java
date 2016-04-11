package com.surveymapclient.activity.adapter;

import java.util.List;

import com.surveymapclient.activity.R;
import com.surveymapclient.common.FileUtils;
import com.surveymapclient.entity.NoteBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {

	private  LayoutInflater layoutInflater;
	List<NoteBean> mList;
	
	public NoteAdapter(Context context,List<NoteBean> list) {
		// TODO Auto-generated constructor stub
		this.mList=list;
		layoutInflater = LayoutInflater.from(context);
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
			convertView=layoutInflater.inflate(R.layout.listview_note, null);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.dependent_img);
			viewHolder.audiott=(TextView) convertView.findViewById(R.id.timeandtext);
			viewHolder.audiourl=(TextView) convertView.findViewById(R.id.audiourl);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		String text=mList.get(position).getText();
		String time=mList.get(position).getAudioTime();
		if (text!=null) {
			viewHolder.audiott.setText("文本注释："+text);
			viewHolder.audiourl.setVisibility(View.INVISIBLE);
		}
		if (time!=null) {
			viewHolder.audiott.setText("时长:"+time);
			viewHolder.audiourl.setText("文件地址:"+FileUtils.getM4aFilePath(mList.get(position).getAudioUrl()));
		}
		return convertView;
	}
	private ViewHolder viewHolder;

	private static class ViewHolder{
		ImageView img;
		TextView audiott;
		TextView audiourl;
	}
}
