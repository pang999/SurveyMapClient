package com.surveymapclient.activity;

import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.LineBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AttributeAngleActivity extends Activity {

	EditText angleName,angle,angleDescripte;
	TextView lineColor,lineStyle,lineWidth;
	boolean isfull=false;
	int linecolor=0;
	private float width=0;	
	AngleBean anglebean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_angle);
		angleName=(EditText) findViewById(R.id.editangleName);
		angle=(EditText) findViewById(R.id.editangle);
		angleDescripte=(EditText) findViewById(R.id.editangleDescripte);
		lineColor=(TextView) findViewById(R.id.editanglelineColor);
		lineWidth=(TextView) findViewById(R.id.editanglelineWidth);
		lineStyle=(TextView) findViewById(R.id.editanglelineStyle);
		anglebean=new AngleBean();
		Bundle bundle=this.getIntent().getExtras();
		anglebean=(AngleBean) bundle.getSerializable("Angle");
		angleName.setText(anglebean.getName());
		angle.setText(anglebean.getAngle()+"");
		angleDescripte.setText(anglebean.getDescripte());
		int c=anglebean.getPaintColor();
		if (c==Color.RED) {
			lineColor.setText("��ɫ");
			linecolor=Color.RED;
		}else if (c==Color.BLUE) {
			lineColor.setText("��ɫ");
			linecolor=Color.BLUE;
		}else if (c==Color.GREEN) {
			lineColor.setText("��ɫ");
			linecolor=Color.GREEN;
		}else if (c==Color.BLACK) {
			lineColor.setText("��ɫ");
			linecolor=Color.BLACK;
		}
		lineWidth.setText(anglebean.getPaintWidth()+"");
		width=anglebean.getPaintWidth();
		if (anglebean.isPaintIsFull()) {
			lineStyle.setText("ʵ��");
			isfull=true;
		}else {
			lineStyle.setText("����");
			isfull=false;
		}		
	}
	
	public void AttributeBack(View v){
		finish();
	}

	public void EditComplete(View v){
		AngleBean backangle=new AngleBean();
		Bundle bundle = new Bundle();
		backangle.setName(angleName.getText().toString());
		backangle.setAngle(Double.parseDouble(angle.getText().toString()));
		backangle.setDescripte(angleDescripte.getText().toString());
		backangle.setPaintColor(linecolor);
		backangle.setPaintWidth(width);
		backangle.setPaintIsFull(isfull);
		bundle.putSerializable("BackAngle", backangle);
		AttributeAngleActivity.this.setResult(RESULT_OK, this.getIntent()
					.putExtras(bundle));
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
				}else if(scolor[i]=="��ɫ"){
					linecolor=Color.BLACK;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
