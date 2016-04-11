package com.surveymapclient.activity;

import java.io.Serializable;

<<<<<<< HEAD
import com.surveymapclient.Dialog.CameraEditeAndDelDialog;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.ImageTools;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.view.CameraBitMapView;
import com.surveymapclient.view.DefineView;
import com.surveymapclient.view.MovePopupWindow;
import com.surveymapclient.view.LocationView;
import com.surveymapclient.view.NotePopupWindow;
<<<<<<< HEAD
=======
import com.surveymapclient.view.fragment.CameraEditeAndDelDialog;
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4

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
	LineBean couple;
	private Button dataMove;
    int index;
    int screenWidth;
    int screenHeight;
    private Context mContext = null;
    public static int TYPE=0;
    LineBean line;
    PolygonBean polygon;
    RectangleBean rectangle;
    CoordinateBean coordinate;
    AngleBean angle;
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
		btndefineback=(ImageView) findViewById(R.id.defineBack);
		btnhistoryItem=(ImageView) findViewById(R.id.tohistory);
		edittitle=(EditText) findViewById(R.id.editTitle);
		topLinearlaout=(RelativeLayout) findViewById(R.id.topLinearlayout);
		locationview=(LocationView) findViewById(R.id.locationview);
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
		intent.putExtra("LineList",(Serializable)cameraBitMapView.BackLinelist());
		intent.putExtra("RectList",(Serializable) cameraBitMapView.BackRectlist());
		intent.putExtra("PolyList",(Serializable) cameraBitMapView.BackPolylist());
		intent.putExtra("CoorList", (Serializable)cameraBitMapView.BackCoorlist());
		intent.putExtra("AngleList",(Serializable) cameraBitMapView.BackAnglelist());
		intent.setClass(this, DataListActivity.class);
		startActivity(intent);
	}
	public void onHistory(View v){
		MovePopupWindow histPopupWindow=new MovePopupWindow(CameraActivity.this);
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
		case R.id.defineBack:
			IToast.show(this, "保存图片成功");
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			if (requestCode==Contants.LINEATTRIBUTEBACK) {	
				Bundle bundle = data.getExtras();				
				LineBean lineBeans=(LineBean) bundle.getSerializable("BackLine");
				cameraBitMapView.ChangeLineAttribute(index, lineBeans);			
			}
			if (requestCode==Contants.RECTATTRIBUTEBACK) {
				Bundle bundle=data.getExtras();
				RectangleBean rectangleBean=(RectangleBean) bundle.getSerializable("BackRectangle");
				cameraBitMapView.ChangeRectangleAttribute(index,rectangleBean);
				Logger.i("BackRectangle", rectangleBean.getRectName());
			}
			if (requestCode==Contants.COORDATTRIBUTEBACK) {
				Bundle bundle=data.getExtras();
				CoordinateBean coordinateBean= (CoordinateBean) bundle.getSerializable("BackCoordinate");
				cameraBitMapView.ChangeCoordinateAttribute(index, coordinateBean);
			}
			if (requestCode==Contants.ANGLEATTRIBUTEBACK) {
				Bundle bundle=data.getExtras();
				AngleBean angleBean=(AngleBean) bundle.getSerializable("BackAngle");
				cameraBitMapView.ChangeAngleAttribute(index, angleBean);
			}
		}
	}
	//直线属性显示及编辑
		public void SendLineData(){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("Line", line);
			bundle.putInt("TYPE", 1);
			intent.putExtras(bundle);
			intent.setClass(CameraActivity.this, AttributeLineActivity.class);
			startActivityForResult(intent, Contants.LINEATTRIBUTEBACK);
		}
		public void SendPolygonData(){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("Polygon", (Serializable)polygon);
			intent.putExtras(bundle);
			intent.setClass(CameraActivity.this, AttributePolygonActivity.class);
			startActivityForResult(intent, Contants.POLYGONATTRIBUTEBACK);
		}
		public void SendRectangleData(){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("Rectangle", rectangle);
			intent.putExtras(bundle);
			intent.setClass(CameraActivity.this, AttributeRectangleActivity.class);
			startActivityForResult(intent, Contants.RECTATTRIBUTEBACK);
		}
		public void SendCoordinateData(){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("Coordinate", coordinate);
			intent.putExtras(bundle);
			intent.setClass(CameraActivity.this, AttributeCoordinateActivity.class);
			startActivityForResult(intent, Contants.COORDATTRIBUTEBACK);
		}
		public void SendAngleData(){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("Angle", angle);
			intent.putExtras(bundle);
			intent.setClass(CameraActivity.this, AttributeAngleActivity.class);
			startActivityForResult(intent, Contants.ANGLEATTRIBUTEBACK);
		}
		@SuppressLint("NewApi")
		@Override
		public void onDialogCallBack(LineBean couplePoint,int i) {
			// TODO Auto-generated method stub
			this.line=couplePoint;
			this.index=i;
			CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance(0);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			eadd.show(ft, "");
		}
//		List<LineBean> list=new ArrayList<LineBean>();
		@Override
		public void onDialogCallBack(PolygonBean polygon, int i) {
			// TODO Auto-generated method stub
			this.polygon=polygon;
			this.index=i;
			CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance(1);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			eadd.show(ft, "");
		}
		@Override
		public void onDialogCallBack(RectangleBean rectangle, int i) {
			// TODO Auto-generated method stub
			this.rectangle=rectangle;
			this.index=i;
			CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance(2);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			eadd.show(ft, "");
		}
		
		@Override
		public void onDialogCallBack(CoordinateBean coordinate, int i) {
			// TODO Auto-generated method stub
			this.coordinate=coordinate;
			this.index=i;
			CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance(3);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			eadd.show(ft, "");
		}
		@Override
		public void onDialogCallBack(AngleBean angleLine, int i) {
			// TODO Auto-generated method stub
			this.angle=angleLine;
			this.index=i;
			CameraEditeAndDelDialog eadd=CameraEditeAndDelDialog.newIntance(4);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			eadd.show(ft, "");
		}
		@Override
		public void onDialogCallBack(TextBean textBean, int i) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onDialogCallBack(AudioBean audioBean, int i) {
			// TODO Auto-generated method stub
			
		}
}
