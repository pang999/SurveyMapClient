package com.surveymapclient.activity;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.view.DefineView;
import com.surveymapclient.view.HistPopupWindow;
import com.surveymapclient.view.LocationView;
import com.surveymapclient.view.NotePopupWindow;
import com.surveymapclient.view.fragment.EditLineNameDialogFragment;
import com.surveymapclient.view.fragment.EditeAndDelDialog;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DefineActivity extends Activity implements DialogCallBack,VibratorCallBack,OnClickListener{

	//控件
	private DefineView defineview;
	private EditText edittitle;
	private ImageView btndefineback,btneditNote,btnhistoryItem,btnrectangle,
			btncoordinate,btnangle,btnsingle,btncontinuous;
	
	private static LocationView locationview;
	private RelativeLayout topLinearlaout;
    Bitmap bitmap;
	boolean isdrag=true;
	private Context mContext = null;
	private Button dataMove;
	public static int TYPE=0;
	/**
     * 手机振动器
     */
    private Vibrator vibrator;
    
    CouplePointLineBean couple;
    int index;
   
    public static int TopHeaght;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_define);		
		initView();
		// 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mContext=this;
        TopHeaght=100;
		
	}
	private void initView(){
		//top		
		btndefineback=(ImageView) findViewById(R.id.defineBack);
		btnhistoryItem=(ImageView) findViewById(R.id.tohistory);
		edittitle=(EditText) findViewById(R.id.editTitle);
		topLinearlaout=(RelativeLayout) findViewById(R.id.topLinearlayout);
		locationview=(LocationView) findViewById(R.id.locationview);
		edittitle.setOnClickListener(this);		
		btndefineback.setOnClickListener(this);			
		//center
		defineview= (DefineView) findViewById(R.id.defineview);
		dataMove=(Button) findViewById(R.id.data_move);
		dataMove.setOnTouchListener(mTouchListener);
		//bottom
		btnsingle=(ImageView) findViewById(R.id.type_single);
		btncontinuous=(ImageView) findViewById(R.id.type_continuous);
		btnrectangle=(ImageView) findViewById(R.id.type_rectangle);
		btncoordinate=(ImageView) findViewById(R.id.type_coordinate);
		btnangle=(ImageView) findViewById(R.id.type_angle);
		btneditNote=(ImageView) findViewById(R.id.annotation);
		btnsingle.setOnClickListener(this);
		btncontinuous.setOnClickListener(this);
		btnrectangle.setOnClickListener(this);
		btncoordinate.setOnClickListener(this);
		btnangle.setOnClickListener(this);
		btneditNote.setOnClickListener(this);
		

	}
	//重置中心点
	public void LocationSketch(View v){
		defineview.LocationXY();
	}

	public void onReCall(View v){
		defineview.UnDo();
	}

	public void onListLines(View v){
		Intent intent=new Intent();
		intent.setClass(this, ListLinesActivity.class);
		startActivity(intent);
	}
	public void onHistory(View v){
		HistPopupWindow histPopupWindow=new HistPopupWindow(DefineActivity.this);
		histPopupWindow.showPopupWindow(btnhistoryItem);
	}
	
	public static void LocationXY(int x,int y){
		locationview.LocationSketch(x, y);
	}
	@SuppressLint("NewApi")
	@Override
	public void onDialogCallBack(CouplePointLineBean couplePoint,int i) {
		// TODO Auto-generated method stub
		this.couple=couplePoint;
		this.index=i;
		EditeAndDelDialog eadd=EditeAndDelDialog.newIntance();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}
	@Override
	public void onVibratorCallBack() {
		// TODO Auto-generated method stub
		// 震动的方式
        // vibrator.vibrate(2000);//振动两秒
		//第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。 
		// 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400 
        // 下边是可以使震动有规律的震动   -1：表示不重复 0：循环的震动
        long[] pattern = {10,50};
        vibrator.vibrate(pattern, -1);
	}
	//直线属性显示及编辑
	public void SendData(){
		Intent intent=new Intent();
		Bundle bundle=new Bundle();
		bundle.putString("name", couple.getName());
		bundle.putString("descripte", couple.getDescripte());
		bundle.putDouble("length", couple.getLength());
		bundle.putDouble("angle", couple.getAngle());
		bundle.putFloat("width", couple.getWidth());
		bundle.putInt("color", couple.getColor());
		bundle.putBoolean("style", couple.isFull());
		bundle.putInt("i", index);
		intent.putExtras(bundle);
		intent.setClass(DefineActivity.this, LineAttributeActivity.class);
		startActivityForResult(intent, Contants.LINEATTRIBUTEBACK);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==Contants.LINEATTRIBUTEBACK) {
			if (resultCode==RESULT_OK) {
				Bundle bundle = data.getExtras();	
				defineview.ChangeLineAttribute(bundle.getInt("backIndex"), bundle.getString("backName"),
						bundle.getDouble("backAngle"), bundle.getInt("backColor"),
						bundle.getDouble("backLenght"), bundle.getBoolean("backStyle"),
						bundle.getFloat("backWidth"), bundle.getString("backDesc"));			
			}
			
		}
	}
	public void Remove(){
		defineview.RemoveIndexLine(index);
	}

	public void showEditeView(){
		defineview.setManyTextOnView();	
	}
	public void EditLineName(String text){
		if (!text.equals("")&&""!=text) {
			defineview.SetwriteLineText(text,-25);		
		}		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.type_single:
			defineview.ZoomCanvas(1);
			TYPE=Contants.SINGLE;
			break;
		case R.id.type_continuous:
			defineview.ZoomCanvas(1);
			TYPE=Contants.CONTINU;
			break;
		case R.id.type_rectangle:
			defineview.ZoomCanvas(1);
			TYPE=Contants.RECTANGLE;
			break;
		case R.id.type_coordinate:
			defineview.ZoomCanvas(1);
			TYPE=Contants.COORDINATE;
			break;
		case R.id.type_angle:
			defineview.ZoomCanvas(1);
			TYPE=Contants.ANGLE;
			break;
		case R.id.defineBack:
			IToast.show(this, "保存图片成功");
			break;
		case R.id.annotation:
			IToast.show(this, "hvoifdh");
			TYPE=Contants.TEXT;
			NotePopupWindow notePopupWindow=new NotePopupWindow(DefineActivity.this);
			notePopupWindow.showPopupWindow(btneditNote);
			break;
		}		
	}
	private OnTouchListener mTouchListener=new OnTouchListener() {
		
		int lastX, lastY;  
		  
        @SuppressLint("NewApi")
		public boolean onTouch(View v, MotionEvent event) {  
            // TODO Auto-generated method stub  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:            	
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                int dx = (int) event.getRawX() - lastX;  
                int dy = (int) event.getRawY() - lastY;  

                int left = v.getLeft() + dx;  
                int top = v.getTop() + dy;  
                int right = v.getRight() + dx;  
                int bottom = v.getBottom() + dy;  

                if (left < 0) {  
                    left = 0;  
                    right = left + v.getWidth();  
                }  
                if (right > Contants.sreenWidth) {  
                    right = Contants.sreenWidth;  
                    left = right - v.getWidth();  
                }  
                if (top < 0) {  
                    top = 0;  
                    bottom = top + v.getHeight();  
                }  
                if (bottom > Contants.screenHeight-50) {  
                    bottom = Contants.screenHeight-50;  
                    top = bottom - v.getHeight();  
                }  
                v.layout(left, top, right, bottom);  
                if (defineview.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
					Logger.i("设置线段", "设置成功了！");
					defineview.LineChangeToText();						
				}else {
					defineview.LineNoChangeToText();
				}                        
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  
                break;  
            case MotionEvent.ACTION_UP:  
            	if (defineview.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
//					Logger.i("设置线段", "设置成功了！");            		           		
            		if (v==dataMove) {
            			defineview.SetwriteLineText("2.985m",10);
            			dataMove.setVisibility(View.INVISIBLE);
    				}     		
            		defineview.LineNoChangeToText();          		
				}
                break;  
            }  
            return false;  
        }  
	};
}
