package com.surveymapclient.activity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.LineBean;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AttributeLineActivity extends Activity{
	
	EditText lineName,lineLength,lineAngle,lineDescripte;
	TextView lineColor,lineStyle,lineWidth;
	boolean isfull=false;
	int linecolor=0;
	private float width=0;	
	LineBean line;
	private int type=-1;
	public static int BACKLINE=0;
	Bundle getbundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_list);
		lineName=(EditText) findViewById(R.id.editlineName);
		lineLength=(EditText) findViewById(R.id.editlineLength);
		lineAngle=(EditText) findViewById(R.id.editlineAngle);
		lineColor=(TextView) findViewById(R.id.editlineColor);
		lineWidth=(TextView) findViewById(R.id.editlineWidth);
		lineStyle=(TextView) findViewById(R.id.editlineStyle);
		lineDescripte=(EditText) findViewById(R.id.editlineDescripte);
		line=new LineBean();
		getbundle=this.getIntent().getExtras();
		line=(LineBean) getbundle.getSerializable("Line");
		type=getbundle.getInt("TYPE");
		lineName.setText(line.getName());
		lineLength.setText(line.getLength()+"");
		lineAngle.setText(line.getAngle()+"");
		if (line.getPaintColor()==Color.RED) {
			lineColor.setText("��ɫ");
			linecolor=Color.RED;
		}else if (line.getPaintColor()==Color.BLUE) {
			lineColor.setText("��ɫ");
			linecolor=Color.BLUE;
		}else if (line.getPaintColor()==Color.GREEN) {
			lineColor.setText("��ɫ");
			linecolor=Color.GREEN;
		}else if (line.getPaintColor()==Color.BLACK) {
			lineColor.setText("��ɫ");
			linecolor=Color.BLACK;
		}	
//		int linecolor=line.getPaintColor();
//		String ssString=null;
//		if (linecolor==Color.RED) {
//			ssString="��ɫ";
//		}else if (linecolor==Color.BLUE) {
//			ssString="��ɫ";
//		}else if (linecolor==Color.GREEN) {
//			ssString="��ɫ";
//		}
//		Logger.i("ɫ����", "----------->color="+ssString+",width="+couple.getWidth()+",style="+couple.isFull());
		lineWidth.setText(line.getPaintWidth()+"");
		width=line.getPaintWidth();
		if (line.isPaintIsFull()) {
			lineStyle.setText("ʵ��");
			isfull=true;
		}else {
			lineStyle.setText("����");
			isfull=false;
		}		
		lineDescripte.setText(line.getDescripte());
	}
	
	public void AttributeBack(View v){
		finish();
	}

	public void EditComplete(View v){
		LineBean backline=new LineBean();	
		Bundle bundle = new Bundle();
		backline.setName(lineName.getText().toString());
		backline.setLength(Double.parseDouble(lineLength.getText().toString()));
		backline.setPaintColor(linecolor);
		backline.setPaintIsFull(isfull);
		backline.setPaintWidth(width);
		backline.setDescripte(lineDescripte.getText().toString());
		backline.setAngle(Double.parseDouble(lineAngle.getText().toString()));
		bundle.putSerializable("BackLine", backline);
		if (type==2) {
			BACKLINE=Contants.LINEATTRIBUTEBACK;
			bundle.putInt("Index", getbundle.getInt("Index"));
			Intent intent=new Intent();			
			intent.putExtras(bundle);
			intent.setClass(this, DefineActivity.class);
			startActivity(intent);
		}else {
			AttributeLineActivity.this.setResult(RESULT_OK, this.getIntent()
					.putExtras(bundle));
		}
		Logger.i("Activity����", "LINEATTRIBUTEBACK");
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeLineActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] linestyle = {"ʵ��", "����"};
		builder.setTitle("ѡ��ʵ/����");
        //    ����һ���������б�ѡ����
		builder.setItems(linestyle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				if (linestyle[i]=="ʵ��") {
					isfull=true;
				}else if (linestyle[i]=="����") {
					isfull=false;
				}
				lineStyle.setText(linestyle[i]);
			}
		});
		builder.show();
	}
	public void onSetWidth(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeLineActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] w = {"2", "4", "6","8"};
		builder.setTitle("ѡ��ֱ�ߴ�С");
        //    ����һ���������б�ѡ����
		builder.setItems(w, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				String m=w[i];
				if (m=="2") {
					width=2;
				}else if (m=="4") {
					width=4;
				}else if (m=="6") {
					width=6;
				}else if (m=="8") {
					width=8;
				}			
				lineWidth.setText(m);
			}
		});
		builder.show();
	}
	public void onSetColor(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeLineActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] scolor = {"��ɫ", "��ɫ", "��ɫ","��ɫ"};
		builder.setTitle("ѡ��һ����ɫ");
        //    ����һ���������б�ѡ����
		builder.setItems(scolor, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub				
				if (scolor[i]=="��ɫ") {
					linecolor=Color.RED;
				}else if (scolor[i]=="��ɫ") {
					linecolor=Color.BLUE;
				}else if (scolor[i]=="��ɫ") {
					linecolor=Color.GREEN;
				}else if (scolor[i]=="��ɫ") {
					linecolor=Color.BLACK;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
