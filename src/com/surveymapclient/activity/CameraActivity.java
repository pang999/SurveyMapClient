package com.surveymapclient.activity;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.ImageTools;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.view.CameraBitMapView;
import com.surveymapclient.view.DefineView;
import com.surveymapclient.view.HistPopupWindow;
import com.surveymapclient.view.LocationView;
import com.surveymapclient.view.NotePopupWindow;
import com.surveymapclient.view.fragment.CameraEditeAndDelDialog;
import com.surveymapclient.view.fragment.EditeAndDelDialog;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.MotionEvent;
public class CameraActivity extends Activity implements DialogCallBack,VibratorCallBack,OnClickListener{
	
	//控件
	private Button textView;
	private EditText edittitle;
	private ImageView btndefineback,btneditNote,btnhistoryItem,btnrectangle,
			btncoordinate,btnangle,btnsingle,btncontinuous;
	private static LocationView locationview;
	private RelativeLayout topLinearlaout;
	
    Bitmap bitmap;
	private CameraBitMapView cameraBitMapView;
	public static Bitmap smallBitmap ;
	CouplePointLineBean couple;
	private Button dataMove;
    int index;
    int screenWidth;
    int screenHeight;
    private Context mContext = null;
    public static int TYPE=0;
	/**
     * 手机振动器
     */
    private Vibrator vibrator;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);   
		initView();	
		// 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Intent intent=getIntent();
        if(intent!=null)
        {
			Logger.i("执行顺序", "smallBitmap="+smallBitmap);
			Bundle bundle=getIntent().getExtras();			
			int where=bundle.getInt("Where");
			if (where==0) {
				String fileName = bundle.getString(TestCameraActivity.KEY_FILENAME);
				Logger.i("图片的路径","fileName="+ fileName);
				String path=getFileStreamPath(fileName).getAbsolutePath();
				Logger.i("图片的路径","path="+ path);
				cameraBitMapView.setimagebitmap(path);
			}else if (where==1){
				String fileName = bundle.getString(TestCameraActivity.KEY_FILENAME);
				cameraBitMapView.setimagebitmap(fileName);
			}
        }      
	}
	private void initView(){
		mContext=this;
		//top		
		btndefineback=(ImageView) findViewById(R.id.camera_defineBack);
		btnhistoryItem=(ImageView) findViewById(R.id.camera_tohistory);
		edittitle=(EditText) findViewById(R.id.camera_editTitle);
		topLinearlaout=(RelativeLayout) findViewById(R.id.camera_topLinearlayout);
		locationview=(LocationView) findViewById(R.id.camera_locationview);
		edittitle.setOnClickListener(this);		
		btndefineback.setOnClickListener(this);			
		//center
		cameraBitMapView = (CameraBitMapView) findViewById(R.id.CameraBitMapView);
		dataMove=(Button) findViewById(R.id.camera_data_move);
		textView=(Button) findViewById(R.id.camera_center_move);	
		dataMove.setOnTouchListener(mTouchListener);
		textView.setOnTouchListener(mTouchListener);
		//bottom
		btnsingle=(ImageView) findViewById(R.id.camera_type_single);
		btncontinuous=(ImageView) findViewById(R.id.camera_type_continuous);
		btnrectangle=(ImageView) findViewById(R.id.camera_type_rectangle);
		btncoordinate=(ImageView) findViewById(R.id.camera_type_coordinate);
		btnangle=(ImageView) findViewById(R.id.camera_type_angle);
		btneditNote=(ImageView) findViewById(R.id.camera_annotation);
		btnsingle.setOnClickListener(this);
		btncontinuous.setOnClickListener(this);
		btnrectangle.setOnClickListener(this);
		btncoordinate.setOnClickListener(this);
		btnangle.setOnClickListener(this);
		btneditNote.setOnClickListener(this);
	}
	public void onRecall(View v){
		cameraBitMapView.UnDo();
	}
	public void onAnnotation(View v){
		NotePopupWindow notePopupWindow=new NotePopupWindow(CameraActivity.this);
		notePopupWindow.showPopupWindow(btneditNote);
	}
	public void onListLines(View v){
		Intent intent=new Intent();
		intent.setClass(this, ListLinesActivity.class);
		startActivity(intent);
	}
	public void onHistory(View v){
		HistPopupWindow histPopupWindow=new HistPopupWindow(CameraActivity.this);
		histPopupWindow.showPopupWindow(btnhistoryItem);
	}
	public static void LocationXY(int x,int y){
		locationview.LocationSketch(x, y);
	}
	@Override
	public void onVibratorCallBack() {
		// TODO Auto-generated method stub
		/*
         * 震动的方式
         */
        // vibrator.vibrate(2000);//振动两秒
		//第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。 
		// 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400 
        // 下边是可以使震动有规律的震动   -1：表示不重复 0：循环的震动
        long[] pattern = {10,50};
        vibrator.vibrate(pattern, -1);
	}
	@SuppressLint("NewApi")
	@Override
	public void onDialogCallBack(CouplePointLineBean couplePoint, int i) {
		// TODO Auto-generated method stub
		this.couple=couplePoint;
		this.index=i;
		Toast.makeText(this, "短按事件发来的消息 x="+couplePoint.getStartPoint().x, Toast.LENGTH_SHORT).show();
		CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}
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
		bundle.putInt("i", 1);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, LineAttributeActivity.class);
		startActivity(intent);
	}
	public void Remove(){
		cameraBitMapView.RemoveIndexLine(index);
	}
	
	private OnTouchListener mTouchListener=new OnTouchListener() {
		
		int lastX, lastY;  
		  
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

                if (right > screenWidth) {  
                    right = screenWidth;  
                    left = right - v.getWidth();  
                }  

                if (top < 0) {  
                    top = 0;  
                    bottom = top + v.getHeight();  
                }  

                if (bottom > screenHeight) {  
                    bottom = screenHeight;  
                    top = bottom - v.getHeight();  
                }  

                v.layout(left, top, right, bottom);  
                if (cameraBitMapView.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
					cameraBitMapView.LineChangeToText();						
				}else {
					cameraBitMapView.LineNoChangeToText();
				}
                         
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  

                break;  
            case MotionEvent.ACTION_UP:  
            	if (cameraBitMapView.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
            		dataMove.setVisibility(View.INVISIBLE);
            		textView.setVisibility(View.INVISIBLE);
            		cameraBitMapView.SetwriteLineText("Text");
            		cameraBitMapView.LineNoChangeToText();            		
				}
                break;  
            }  
            return false;  
        }  
	};
	@Override
	public void onDestroy() {
		super.onDestroy();	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.camera_type_single:
			TYPE=Contants.SINGLE;
			break;
		case R.id.camera_type_continuous:
			TYPE=Contants.CONTINU;
			break;
		case R.id.camera_type_rectangle:
			TYPE=Contants.RECTANGLE;
			break;
		case R.id.camera_type_coordinate:
			TYPE=Contants.COORDINATE;
			break;
		case R.id.camera_type_angle:
			TYPE=Contants.ANGLE;
			break;
		case R.id.camera_defineBack:
			IToast.show(this, "保存图片成功");
			break;
		}
	}
}
