package com.surveymapclient.activity;

import com.surveymapclient.common.IToast;
import com.surveymapclient.common.ImageTools;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.view.CameraBitMapView;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.MotionEvent;
public class CameraActivity extends Activity implements DialogCallBack,VibratorCallBack{
	
    Bitmap bitmap;
	private CameraBitMapView cameraBitMapView;
	public static Bitmap smallBitmap ;
	CouplePointLineBean couple;
	private Button dataMove;
	private Button textView;
    int index;
    int screenWidth;
    int screenHeight;
    private Context mContext = null;
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
		cameraBitMapView = (CameraBitMapView) findViewById(R.id.CameraBitMapView);
		dataMove=(Button) findViewById(R.id.data_move);
		dataMove.setOnTouchListener(mTouchListener);
		textView=(Button) findViewById(R.id.center_move);
		textView.setOnTouchListener(mTouchListener);
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		screenWidth = dm.widthPixels;  
		screenHeight= dm.heightPixels - 50; 
		mContext=this;
        Intent intent=getIntent();
        if(intent!=null)
        {
//            bitmap=intent.getParcelableExtra("bitmap");
//            
//			smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth()*5, bitmap.getHeight()*5 );
			Logger.i("执行顺序", "smallBitmap="+smallBitmap);
//			String url=intent.getExtras().getString("bitmap");
			Bundle bundle=getIntent().getExtras();
//			String fileName = intent.getStringExtra(
//					TestCameraActivity.KEY_FILENAME);
			
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
        // 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}
	public void DanLianXian(View v){
		if (cameraBitMapView.isDan) {
			cameraBitMapView.isDuo=false;
			cameraBitMapView.isDrag=true;
			cameraBitMapView.isDan=false;
		}else {
//			imageview.ZoomCanvas(1);
			cameraBitMapView.isDuo=false;
			cameraBitMapView.isDrag=false;
			cameraBitMapView.isDan=true;
		}
	}
	public void DuoLianXian(View v){
		if (cameraBitMapView.isDuo) {
			cameraBitMapView.isDuo=false; 
			cameraBitMapView.isDrag=true;
			cameraBitMapView.isDan=false;
		}else {
//			imageview.ZoomCanvas(1);
			cameraBitMapView.isDuo=true;
			cameraBitMapView.isDrag=false;
			cameraBitMapView.isDan=false;
		}
	}
	public void CheHui(View v){
		cameraBitMapView.UnDo();
	}
	public void ZhuSi(View v){
		showEditePopupWindow(v);
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
	
	public void showEditePopupWindow(View view){
		// 一个自定义的布局，作为显示的内容
		View contentView=LayoutInflater.from(mContext).inflate(R.layout.popupeditewindow, null);
		Button edittext=(Button) contentView.findViewById(R.id.editetext);
		Button tape=(Button) contentView.findViewById(R.id.tape);
		edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IToast.show(mContext, "文字编辑");
				textView.setVisibility(View.VISIBLE);
				
//				textView.layout(Contants.sreenWidth/2, 400, 400, 500);

//				textView.setOnTouchListener(mTouchListener);
			}
		});
		tape.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IToast.show(mContext, "录音");
			}
		});
		final PopupWindow popupWindow=new PopupWindow(contentView, 
				200, 200, true);
//		popupWindow.setTouchable(true);
//		popupWindow.setTouchInterceptor(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.i("mengdd", "onTouch : ");
//
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//
//        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.meimu));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
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
					Logger.i("设置线段", "设置成功了！");
					cameraBitMapView.LineChangeToText();						
				}else {
					cameraBitMapView.LineNoChangeToText();
				}
                         
                lastX = (int) event.getRawX();  
                lastY = (int) event.getRawY();  

                break;  
            case MotionEvent.ACTION_UP:  
            	if (cameraBitMapView.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
//					Logger.i("设置线段", "设置成功了！");
            		dataMove.setVisibility(View.INVISIBLE);
            		textView.setVisibility(View.INVISIBLE);
            		cameraBitMapView.SetwriteLineText("Text");
            		cameraBitMapView.LineNoChangeToText();
//					v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            		
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
}
