package com.surveymapclient.activity;

import java.io.Serializable;
import java.util.Date;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.db.DBHelper;
import com.surveymapclient.db.OperateData;
import com.surveymapclient.dialog.EditTextDialog;
import com.surveymapclient.dialog.EditeAndDelDialog;
import com.surveymapclient.dialog.ExitSaveSketchDialog;
import com.surveymapclient.dialog.ExitSaveSketchDialog.DialogFragmentClickImpl;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.impl.CallBackData;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.pdf.PDFCreater;
import com.surveymapclient.pdf.PDFSharer;
import com.surveymapclient.view.DefineView;
import com.surveymapclient.view.DefineView.TypeChangeListener;
import com.surveymapclient.view.DefineViewPopupWindow;
import com.surveymapclient.view.MorePopupWindow;
import com.surveymapclient.view.LocationView;
import com.surveymapclient.view.NotePopupWindow;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DefineActivity extends Activity implements TypeChangeListener,
		DialogCallBack, VibratorCallBack, OnClickListener,CallBackData,
		DialogFragmentClickImpl {

	// 控件
	private DefineView defineview;
	private EditText edittitle;
	private ImageView btndefineback, btneditNote, btnmoveoperator,btnrecall,
			btnsingle, btncontinuous;
	private static LocationView locationview;
	DialogFragmentClickImpl impl;
	Bitmap bitmap;
	boolean isdrag = true;
	private Button dataMove;
	public static int TYPE = 0;
	// 数据库操作
	private DBHelper helper;
	/**
	 * 手机振动器
	 */
	private Vibrator vibrator;

	LineBean line;
	PolygonBean polygon;
//	RectangleBean rectangle;
//	CoordinateBean coordinate;
//	AngleBean angle;
	TextBean textBean;
	PDFSharer sharer;
	int index;

	public static int TopHeaght;
	private DefineViewPopupWindow window;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_define);
		helper = DBHelper.getInstance(this);
		sharer=new PDFSharer(this);		
		initView();
		window=new DefineViewPopupWindow(this);
		// 震动效果的系统服务
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		TopHeaght = 100;
		// initData();
		Bundle bundle = this.getIntent().getExtras();
		if (bundle.getInt("TYPE") == 0) {
			long key = bundle.getLong("KEY");
			edittitle.setText(bundle.getString("Title"));
			defineview.AddAllDataFromActivity(
					OperateData.searchLine(key, helper),
					OperateData.searchPolygon(key, helper),
					OperateData.searchText(key, helper),
					OperateData.searchAudio(key, helper));
			window.AddAllDataFromActivity(
					OperateData.searchLine(key, helper),
					OperateData.searchPolygon(key, helper),
					OperateData.searchText(key, helper),
					OperateData.searchAudio(key, helper));
			OperateData.dedeleLine(helper.searchDataLine(key), helper);
			OperateData.deletePolygon(helper.searchDataPolygon(key), helper);
			OperateData
					.deleteRectangle(helper.searchDataRectangle(key), helper);
			OperateData.deleteCoordinate(helper.searchDataCoordinate(key),
					helper);
			OperateData.deleteAngle(helper.searchDataAngle(key), helper);
			OperateData.deleteText(helper.searchDataText(key), helper);
			OperateData.deleteAudio(helper.searchDataAudio(key), helper);
			
		}
		
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		// top
		btndefineback = (ImageView) findViewById(R.id.defineBack);
		btnmoveoperator = (ImageView) findViewById(R.id.btnmoveoperator);
		edittitle = (EditText) findViewById(R.id.editTitle);
		locationview = (LocationView) findViewById(R.id.locationview);
//		edittitle.setOnClickListener(this);
		btndefineback.setOnClickListener(this);
		// center
		defineview = (DefineView) findViewById(R.id.defineview);
		dataMove = (Button) findViewById(R.id.data_move);
		dataMove.setOnTouchListener(mTouchListener);
		// bottom
		btnsingle = (ImageView) findViewById(R.id.type_single);
		btncontinuous = (ImageView) findViewById(R.id.type_continuous);
//		btnrectangle = (ImageView) findViewById(R.id.type_rectangle);
//		btncoordinate = (ImageView) findViewById(R.id.type_coordinate);
//		btnangle = (ImageView) findViewById(R.id.type_angle);
		btnrecall=(ImageView) findViewById(R.id.recall);
		btneditNote = (ImageView) findViewById(R.id.annotation);
		btnsingle.setOnClickListener(this);
		btncontinuous.setOnClickListener(this);
//		btnrectangle.setOnClickListener(this);
//		btncoordinate.setOnClickListener(this);
//		btnangle.setOnClickListener(this);
		btneditNote.setOnClickListener(this);
		btnrecall.setOnClickListener(this);
		defineview.setOnTypeChangeListener(this);

	}

	// 重置中心点
	public void LocationSketch(View v) {
		defineview.LocationXY();
	}

	public void onDatalist() {
		Intent intent = new Intent();
		intent.putExtra("LineList", (Serializable) defineview.BackLinelist());
		intent.putExtra("PolyList", (Serializable) defineview.BackPolylist());
//		intent.putExtra("TextList", (Serializable) defineview.BackTextlist());
//		intent.putExtra("AudioList", (Serializable) defineview.BackAudiolist());
		intent.setClass(this, DefineDataListActivity.class);
		startActivity(intent);
	}

	@SuppressLint("SdCardPath")
	public void ShareData() {
		 sharer.showProgressDialog();		
		 String pngurl=defineview.SavaBitmap();	
		 String pdfname=new Date().getTime()+".pdf";
		 new PDFCreater("/sdcard/surveymap/pdf/",pdfname).createPDF(pngurl,
		 defineview.BackLinelist(),
		 defineview.BackPolylist(),
		 defineview.BackTextlist(),
		 defineview.BackAudiolist());
		 sharer.dismissProgressDialog();
		 sharer.share(this,"/sdcard/surveymap/pdf/"+ pdfname);

	}
	
	
	
	@SuppressLint("SdCardPath")
	public void onMoreOperator(View v) {
		MorePopupWindow movePopupWindow = new MorePopupWindow(
				DefineActivity.this,0);
		movePopupWindow.showPopupWindow(btnmoveoperator);
	}

	public void HistoryData() {
		Intent intent = new Intent();
		intent.setClass(this, HistoryActivity.class);
		startActivity(intent);
	}

	public static void LocationXY(int x, int y) {
		locationview.LocationSketch(x, y);
	}

	@Override
	public void onVibratorCallBack() {
		// TODO Auto-generated method stub
		// 震动的方式
		// vibrator.vibrate(2000);//振动两秒
		// 第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
		// 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
		// 下边是可以使震动有规律的震动 -1：表示不重复 0：循环的震动
		long[] pattern = { 10, 50 };
		vibrator.vibrate(pattern, -1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Contants.LINEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				LineBean lineBeans = (LineBean) bundle
						.getSerializable("BackLine");
				defineview.ChangeLineAttribute(index, lineBeans);
			}	
			if (requestCode==Contants.POLYGONLINEATTRIBUTEBACK) {
				Logger.i("ChangePolygon", "ChangePolygonLineAttribute");
				Bundle bundle = data.getExtras();
				LineBean polyline = (LineBean) bundle
						.getSerializable("BackLine");
				defineview.ChangePolygonLineAttribute(index, polyline);
			}
			if (requestCode == Contants.AUDIOATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				if (bundle.getInt("BackAudio") == 0) {
					defineview.setAudioOnView(bundle.getString("AudioUrl"),
							bundle.getInt("AudioLen"));
				}
				if (bundle.getInt("BackAudio") == 1) {
					defineview.RemoveIndexAudio(index);
				}
			}
			
		}
	}

	public void ChangeTextContent(String text) {
		defineview.ChangeTextContent(index, text);
		defineview.BackLocation();
	}

	public void RemoveLineIndex() {
		defineview.RemoveIndexLine(index);
	}

	public void RemovePolygonIndex(){
		defineview.RemoveIndexPolygon(index);
	}
	public void RemovePolygonLineIndex(){
		defineview.RemoveIndexPolygonLine(index);
	}
	public void RemoveTextIndex() {
		defineview.RemoveIndexText(index);
	}

	public void showEditeView() {
		defineview.setManyTextOnView();
	}

	public void showTapeDialog() {
		Intent intent = new Intent(this, AudioRecorderActivity.class);
		intent.putExtra("TYPE", 0);
		startActivityForResult(intent, Contants.AUDIOATTRIBUTEBACK);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.type_single:
			defineview.ZoomCanvas(1);
			TYPE = Contants.SINGLE;

			btnsingle.setSelected(true);

			btncontinuous.setSelected(false);

			break;
		case R.id.type_continuous:
			defineview.ZoomCanvas(1);
			TYPE = Contants.CONTINU;

			btnsingle.setSelected(false);

			btncontinuous.setSelected(true);

			break;

		case R.id.defineBack:
			showAlertDialog();
			break;
		case R.id.recall:
			IToast.show(this, "保存图片");
			break;
		case R.id.annotation:
			NotePopupWindow notePopupWindow = new NotePopupWindow(
					DefineActivity.this,0);
			notePopupWindow.showPopupWindow(btneditNote);
			break;
		}
	}

	private OnTouchListener mTouchListener = new OnTouchListener() {

		int lastX, lastY;

		@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
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
				if (bottom > Contants.screenHeight - 50) {
					bottom = Contants.screenHeight - 50;
					top = bottom - v.getHeight();
				}
				v.layout(left, top, right, bottom);
				if (defineview.SetTextToLine((int) event.getRawX(),
						(int) event.getRawY())) {
					Logger.i("设置线段", "设置成功了！");
					defineview.LineChangeToText();
				} else {
					defineview.LineNoChangeToText();
				}
				if (defineview.SetTextToPolygonLine((int)event.getRawX(), 
						(int)event.getRawY())) {
					defineview.PolygonLineChangeToText();
				}else {
					defineview.PolygonLineNoChangeToText();
				}
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				if (defineview.SetTextToLine((int) event.getRawX(),
						(int) event.getRawY())) {
					// Logger.i("设置线段", "设置成功了！");
					if (v == dataMove) {
						defineview.SetwriteLineText("2.985");
						dataMove.setVisibility(View.INVISIBLE);
					}
					defineview.LineNoChangeToText();
				}
				if (defineview.SetTextToPolygonLine((int) event.getRawX(),
						(int) event.getRawY())) {
					if (v==dataMove) {
						defineview.SetwritePolygonLineText("2.985");
						defineview.setVisibility(View.INVISIBLE);
					}
					defineview.PolygonLineNoChangeToText();
				}
				break;
			}
			return false;
		}
	};

	// 直线属性显示及编辑
	public void SendLineData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Line", line);
		bundle.putInt("TYPE", 1);
		intent.putExtras(bundle);
		intent.setClass(DefineActivity.this, AttributeLineActivity.class);
		startActivityForResult(intent, Contants.LINEATTRIBUTEBACK);
	}
	public void SendPolygonLineData(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Line", line);
		bundle.putInt("TYPE", 1);
		intent.putExtras(bundle);
		intent.setClass(DefineActivity.this, AttributeLineActivity.class);
		startActivityForResult(intent, Contants.POLYGONLINEATTRIBUTEBACK);
	}

	public void SendPolygonData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Polygon", (Serializable) polygon);
		intent.putExtras(bundle);
		intent.setClass(DefineActivity.this, AttributePolygonActivity.class);
		startActivityForResult(intent, Contants.POLYGONATTRIBUTEBACK);
	}

	@SuppressLint("NewApi")
	@Override
	public void onDialogCallBack(LineBean couplePoint, int i,int type) {
		// TODO Auto-generated method stub
		this.line = couplePoint;
		this.index = i;
		EditeAndDelDialog eadd = EditeAndDelDialog.newIntance(type);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(PolygonBean polygon, int i) {
		// TODO Auto-generated method stub
		this.polygon = polygon;
		this.index = i;
		EditeAndDelDialog eadd = EditeAndDelDialog.newIntance(1);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(RectangleBean rectangle, int i) {
		// TODO Auto-generated method stub
//		this.rectangle = rectangle;
//		this.index = i;
//		EditeAndDelDialog eadd = EditeAndDelDialog.newIntance(2);
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(CoordinateBean coordinate, int i) {
		// TODO Auto-generated method stub
//		this.coordinate = coordinate;
//		this.index = i;
//		EditeAndDelDialog eadd = EditeAndDelDialog.newIntance(3);
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(AngleBean angleLine, int i) {
		// TODO Auto-generated method stub
//		this.angle = angleLine;
//		this.index = i;
//		EditeAndDelDialog eadd = EditeAndDelDialog.newIntance(4);
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(TextBean textBean, int i) {
		// TODO Auto-generated method stub
		this.textBean = textBean;
		this.index = i;
		EditTextDialog etd = EditTextDialog.newIntance(0);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		etd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(AudioBean audioBean, int i) {
		// TODO Auto-generated method stub
		this.index = i;
		Intent intent = new Intent(this, AudioRecorderActivity.class);
		intent.putExtra("TYPE", 1);
		intent.putExtra("URL", audioBean.getUrl());
		intent.putExtra("Len", audioBean.getLenght());
		Logger.i("录音", "onDialogCallBack");
		startActivityForResult(intent, Contants.AUDIOATTRIBUTEBACK);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Logger.i("activity生命周期", "onRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Logger.i("activity生命周期", "onStart");
	}

	private void showAlertDialog() {
		ExitSaveSketchDialog ess = ExitSaveSketchDialog.newInstance();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ess.show(ft, "");
	}

	@Override
	public void doPositiveClick() {
		// TODO Auto-generated method stub
		long key = System.currentTimeMillis();
		OperateData.insertLine(key, defineview.BackLinelist(), helper);
		OperateData.insertPolygon(key, defineview.BackPolylist(), helper);
		OperateData.insertText(key, defineview.BackTextlist(), helper);
		OperateData.insertAudio(key, defineview.BackAudiolist(), helper);
		OperateData.insertModule(key, edittitle.getText().toString(), null, 0,helper);
		finish();
	}

	@Override
	public void doNegativeClick() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAlertDialog();
		}
		return false;
	}

	@Override
	public void onTypeChange() {
		// TODO Auto-generated method stub
		btnsingle.setSelected(false);
		btncontinuous.setSelected(false);
	}

	@Override
	public void onCallBackDown(float x, float y) {
		// TODO Auto-generated method stub
		window.DrawDown(x, y);
	}

	@Override
	public void onCallBackMove(float x, float y) {
		// TODO Auto-generated method stub
		window.showAtLocation(defineview, Gravity.NO_GRAVITY,0,150);
		window.DrawMove(x, y);
	}

	@Override
	public void onCallBackUp(float x, float y) {
		// TODO Auto-generated method stub
		window.DrawUp(x,y);
		window.dismiss();
	}

	@Override
	public void onCallBackLong(boolean islong,boolean isyes) {
		// TODO Auto-generated method stub
		window.DownlongTime(islong,isyes);
	}

	@Override
	public void onCallBackDeleteLine(int i) {
		// TODO Auto-generated method stub
		window.DeleteLine(i);
	}

	@Override
	public void onCallBackMoveLine(LineBean lineBean) {
		// TODO Auto-generated method stub
		window.MoveLineUp(lineBean);
	}

	@Override
	public void onCallBackDeletePolygon(int i) {
		// TODO Auto-generated method stub
		window.DeletePolygon(i);
	}

	@Override
	public void onCallBackMovePolygon(PolygonBean polygonBean) {
		// TODO Auto-generated method stub
		window.MovePolygonUp(polygonBean);
	}

	@Override
	public void onCallBackDeletePolygonLine(int n, int i) {
		// TODO Auto-generated method stub
		window.DeletePolygonLine(n, i);
	}

	@Override
	public void onCallBackLineAttribute(int index, LineBean lineBean) {
		// TODO Auto-generated method stub
		window.ChangeLineAttribute(index, lineBean);
	}

	@Override
	public void onCallBaclPolygonLineAttribute(int n, int index, LineBean line) {
		// TODO Auto-generated method stub
		window.ChangePolygonLineAttribute(n, index, line);
	}

	@Override
	public void onCallBackText(float x, float y) {
		// TODO Auto-generated method stub
		window.setManyTextOnView(x, y);
	}

	@Override
	public void onCallBackAudio(float x, float y, String url, int len) {
		// TODO Auto-generated method stub
		window.setAudoiOnView(x, y, url, len);
	}

	@Override
	public void onCallBackDeleteText(int i) {
		// TODO Auto-generated method stub
		window.DeleteText(i);
	}

	@Override
	public void onCallBackDeleteAudio(int i) {
		// TODO Auto-generated method stub
		window.DeleteAudio(i);
	}

	@Override
	public void onCallBackMoveTextUp(TextBean textBean) {
		// TODO Auto-generated method stub
		window.MoveTextUp(textBean);
	}

	@Override
	public void onCallBackMoveAudioUp(AudioBean audioBean) {
		// TODO Auto-generated method stub
		window.MoveAudioUp(audioBean);
	}

	@Override
	public void onCallBackChangeTextContent(int i, String text) {
		// TODO Auto-generated method stub
		window.ChangeTextContent(i, text);
	}


}
