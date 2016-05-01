package com.surveymapclient.activity;

import java.io.Serializable;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.db.DBHelper;
import com.surveymapclient.db.OperateData;
import com.surveymapclient.dialog.CameraEditeAndDelDialog;
import com.surveymapclient.dialog.ExitSaveSketchDialog;
import com.surveymapclient.dialog.ExitSaveSketchDialog.DialogFragmentClickImpl;
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
import com.surveymapclient.view.CameraBitMapView.TypeChangeListener;
import com.surveymapclient.view.LocationView;
import com.surveymapclient.view.MorePopupWindow;
import com.surveymapclient.view.NotePopupWindow;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class CameraActivity extends Activity
		implements TypeChangeListener, DialogCallBack, VibratorCallBack, OnClickListener, DialogFragmentClickImpl {

	// 控件
	private EditText edittitle;
	private ImageView btndefineback, btneditNote, btnrectangle, btnmoveoperator, btncoordinate, btnangle, btnsingle,
			btncontinuous;
	private static LocationView locationview;

	Bitmap bitmap;
	private CameraBitMapView cameraBitMapView;
	public static Bitmap smallBitmap;
	LineBean couple;
	private Button dataMove;
	int index;
	int screenWidth;
	int screenHeight;
	public static int TYPE = 0;
	LineBean line;
	PolygonBean polygon;
	RectangleBean rectangle;
	CoordinateBean coordinate;
	AngleBean angle;
	TextBean textBean;
	// 数据库操作
	private DBHelper helper;
	private String imgurl = "";
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
		helper = DBHelper.getInstance(this);
		initView();
		// 震动效果的系统服务
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = getIntent().getExtras();
			if (bundle.getInt("TYPE") == 0) {
				long key = bundle.getLong("KEY");
				String path = bundle.getString("imgUrl");
				imgurl=path;
				cameraBitMapView.setimagebitmap(path);
				 edittitle.setText(bundle.getString("Title"));
				cameraBitMapView.AddAllDataFromActivity(OperateData.searchLine(key, helper),
						OperateData.searchPolygon(key, helper), OperateData.searchRectangle(key, helper),
						OperateData.searchAngle(key, helper), OperateData.searchCoordinate(key, helper),
						OperateData.searchText(key, helper), OperateData.searchAudio(key, helper));

				
				OperateData.dedeleLine(helper.searchDataLine(key), helper);
				OperateData.deletePolygon(helper.searchDataPolygon(key), helper);
				OperateData.deleteRectangle(helper.searchDataRectangle(key), helper);
				OperateData.deleteCoordinate(helper.searchDataCoordinate(key), helper);
				OperateData.deleteAngle(helper.searchDataAngle(key), helper);
				OperateData.deleteText(helper.searchDataText(key), helper);
				OperateData.deleteAudio(helper.searchDataAudio(key), helper);

			} else {
				int where = bundle.getInt("Where");
				if (where == 0) {
					String fileName = bundle.getString(TestCameraActivity.KEY_FILENAME);
					Logger.i("图片的路径", "fileName=" + fileName);
					String path = getFileStreamPath(fileName).getAbsolutePath();
					Logger.i("图片的路径", "path=" + path);
					imgurl = path;
					cameraBitMapView.setimagebitmap(path);
				} else if (where == 1) {
					String fileName = bundle.getString(TestCameraActivity.KEY_FILENAME);
					imgurl = fileName;
					cameraBitMapView.setimagebitmap(fileName);
				}
			}
		}
	}

	private void initView() {
		// top
		btndefineback = (ImageView) findViewById(R.id.defineBack);
		edittitle = (EditText) findViewById(R.id.camera_editTitle);
		locationview = (LocationView) findViewById(R.id.locationview);
		btnmoveoperator = (ImageView) findViewById(R.id.btnmoveoperator);
		btnmoveoperator.setOnClickListener(this);
//		 edittitle.setOnClickListener(this);
		btndefineback.setOnClickListener(this);
		// center
		cameraBitMapView = (CameraBitMapView) findViewById(R.id.CameraBitMapView);
		dataMove = (Button) findViewById(R.id.camera_data_move);
		dataMove.setOnTouchListener(mTouchListener);
		// bottom
		btnsingle = (ImageView) findViewById(R.id.camera_type_single);
		btncontinuous = (ImageView) findViewById(R.id.camera_type_continuous);
		btnrectangle = (ImageView) findViewById(R.id.camera_type_rectangle);
		btncoordinate = (ImageView) findViewById(R.id.camera_type_coordinate);
		btnangle = (ImageView) findViewById(R.id.camera_type_angle);
		btneditNote = (ImageView) findViewById(R.id.camera_annotation);
		btnsingle.setOnClickListener(this);
		btncontinuous.setOnClickListener(this);
		btnrectangle.setOnClickListener(this);
		btncoordinate.setOnClickListener(this);
		btnangle.setOnClickListener(this);
		btneditNote.setOnClickListener(this);
		cameraBitMapView.setOnTypeChangeListener(this);
	}

	public void onRecall(View v) {
		// cameraBitMapView.UnDo();
	}

	public static void LocationXY(int x, int y) {
		locationview.LocationSketch(x, y);
	}

	@Override
	public void onVibratorCallBack() {
		// TODO Auto-generated method stub
		/*
		 * 震动的方式
		 */
		// vibrator.vibrate(2000);//振动两秒
		// 第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
		// 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
		// 下边是可以使震动有规律的震动 -1：表示不重复 0：循环的震动
		long[] pattern = { 10, 50 };
		vibrator.vibrate(pattern, -1);
	}

	public void Remove() {
		cameraBitMapView.RemoveIndexLine(index);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Contants.LINEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				LineBean lineBeans = (LineBean) bundle.getSerializable("BackLine");
				cameraBitMapView.ChangeLineAttribute(index, lineBeans);
				IToast.show(this, "CameraLine");
			}
			if (requestCode == Contants.RECTATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				RectangleBean rectangleBean = (RectangleBean) bundle.getSerializable("BackRectangle");
				cameraBitMapView.ChangeRectangleAttribute(index, rectangleBean);
				Logger.i("BackRectangle", rectangleBean.getRectName());
			}
			if (requestCode == Contants.COORDATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				CoordinateBean coordinateBean = (CoordinateBean) bundle.getSerializable("BackCoordinate");
				cameraBitMapView.ChangeCoordinateAttribute(index, coordinateBean);
			}
			if (requestCode == Contants.ANGLEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				AngleBean angleBean = (AngleBean) bundle.getSerializable("BackAngle");
				cameraBitMapView.ChangeAngleAttribute(index, angleBean);
			}
			if (requestCode == Contants.AUDIOATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
				if (bundle.getInt("BackAudio") == 0) {
					cameraBitMapView.setAudioOnView(bundle.getString("AudioUrl"), bundle.getInt("AudioLen"));
				}
				if (bundle.getInt("BackAudio") == 1) {
					cameraBitMapView.RemoveIndexAudio(index);
				}
			}
		}
	}

	public void onDatalist() {
		Intent intent = new Intent();
		intent.putExtra("LineList", (Serializable) cameraBitMapView.BackLinelist());
		intent.putExtra("RectList", (Serializable) cameraBitMapView.BackRectlist());
		intent.putExtra("PolyList", (Serializable) cameraBitMapView.BackPolylist());
		intent.putExtra("CoorList", (Serializable) cameraBitMapView.BackCoorlist());
		intent.putExtra("AngleList", (Serializable) cameraBitMapView.BackAnglelist());
		intent.putExtra("TextList", (Serializable) cameraBitMapView.BackTextlist());
		intent.putExtra("AudioList", (Serializable) cameraBitMapView.BackAudiolist());
		intent.setClass(this, CameraDataListActivity.class);
		startActivity(intent);
	}

	public void HistoryData() {
		Intent intent = new Intent();
		intent.setClass(this, HistoryActivity.class);
		startActivity(intent);
	}

	public void ChangeTextContent(String text) {
		cameraBitMapView.ChangeTextContent(index, text);
	}

	public void RemoveLineIndex() {
		cameraBitMapView.RemoveIndexLine(index);
	}

	public void RemovePolygonIndex() {
		cameraBitMapView.RemoveIndexPolygon(index);
	}

	public void RemoveRectangleIndex() {
		cameraBitMapView.RemoveIndexRectangle(index);
	}

	public void RemoveCoordinateIndex() {
		cameraBitMapView.RemoveIndexCoordinate(index);
	}

	public void RemoveAngleIndex() {
		cameraBitMapView.RemoveIndexAngle(index);
	}

	public void RemoveTextIndex() {
		cameraBitMapView.RemoveIndexText(index);
	}

	public void showEditeView() {
		cameraBitMapView.setManyTextOnView();
	}

	public void showTapeDialog() {
		Intent intent = new Intent(this, AudioRecorderActivity.class);
		intent.putExtra("TYPE", 0);
		startActivityForResult(intent, Contants.AUDIOATTRIBUTEBACK);
	}

	// 直线属性显示及编辑
	public void SendLineData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Line", line);
		bundle.putInt("TYPE", 1);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, AttributeLineActivity.class);
		startActivityForResult(intent, Contants.LINEATTRIBUTEBACK);
	}

	public void SendPolygonData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Polygon", (Serializable) polygon);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, AttributePolygonActivity.class);
		startActivityForResult(intent, Contants.POLYGONATTRIBUTEBACK);
	}

	public void SendRectangleData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Rectangle", rectangle);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, AttributeRectangleActivity.class);
		startActivityForResult(intent, Contants.RECTATTRIBUTEBACK);
	}

	public void SendCoordinateData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Coordinate", coordinate);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, AttributeCoordinateActivity.class);
		startActivityForResult(intent, Contants.COORDATTRIBUTEBACK);
	}

	public void SendAngleData() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Angle", angle);
		intent.putExtras(bundle);
		intent.setClass(CameraActivity.this, AttributeAngleActivity.class);
		startActivityForResult(intent, Contants.ANGLEATTRIBUTEBACK);
	}

	@SuppressLint("NewApi")
	@Override
	public void onDialogCallBack(LineBean couplePoint, int i,int type) {
		// TODO Auto-generated method stub
		this.line = couplePoint;
		this.index = i;
		CameraEditeAndDelDialog eadd = CameraEditeAndDelDialog.newIntance(0);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(PolygonBean polygon, int i) {
		// TODO Auto-generated method stub
		this.polygon = polygon;
		this.index = i;
		CameraEditeAndDelDialog eadd = CameraEditeAndDelDialog.newIntance(1);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(RectangleBean rectangle, int i) {
		// TODO Auto-generated method stub
		this.rectangle = rectangle;
		this.index = i;
		CameraEditeAndDelDialog eadd = CameraEditeAndDelDialog.newIntance(2);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(CoordinateBean coordinate, int i) {
		// TODO Auto-generated method stub
		this.coordinate = coordinate;
		this.index = i;
		CameraEditeAndDelDialog eadd = CameraEditeAndDelDialog.newIntance(3);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(AngleBean angleLine, int i) {
		// TODO Auto-generated method stub
		this.angle = angleLine;
		this.index = i;
		CameraEditeAndDelDialog eadd = CameraEditeAndDelDialog.newIntance(4);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		eadd.show(ft, "");
	}

	@Override
	public void onDialogCallBack(TextBean textBean, int i) {
		// TODO Auto-generated method stub
		this.textBean = textBean;
		this.index = i;
		CameraEditeAndDelDialog etd = CameraEditeAndDelDialog.newIntance(5);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.camera_type_single:
			TYPE = Contants.SINGLE;
			btnsingle.setSelected(true);

			btncontinuous.setSelected(false);

			btnrectangle.setSelected(false);

			btncoordinate.setSelected(false);

			btnangle.setSelected(false);
			break;
		case R.id.camera_type_continuous:
			TYPE = Contants.CONTINU;
			btnsingle.setSelected(false);

			btncontinuous.setSelected(true);

			btnrectangle.setSelected(false);

			btncoordinate.setSelected(false);

			btnangle.setSelected(false);
			break;
		case R.id.camera_type_rectangle:
			TYPE = Contants.RECTANGLE;
			btnsingle.setSelected(false);

			btncontinuous.setSelected(false);

			btnrectangle.setSelected(true);

			btncoordinate.setSelected(false);

			btnangle.setSelected(false);
			break;
		case R.id.camera_type_coordinate:
			TYPE = Contants.COORDINATE;
			btnsingle.setSelected(false);

			btncontinuous.setSelected(false);

			btnrectangle.setSelected(false);

			btncoordinate.setSelected(true);

			btnangle.setSelected(false);
			break;
		case R.id.camera_type_angle:
			TYPE = Contants.ANGLE;
			btnsingle.setSelected(false);

			btncontinuous.setSelected(false);

			btnrectangle.setSelected(false);

			btncoordinate.setSelected(false);

			btnangle.setSelected(true);
			break;
		case R.id.camera_annotation:
			NotePopupWindow notePopupWindow = new NotePopupWindow(CameraActivity.this, 1);
			notePopupWindow.showPopupWindow(btneditNote);
			break;
		case R.id.defineBack:
			showAlertDialog();
			break;
		case R.id.btnmoveoperator:
			MorePopupWindow movePopupWindow = new MorePopupWindow(CameraActivity.this, 1);
			movePopupWindow.showPopupWindow(btnmoveoperator);
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
				if (cameraBitMapView.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
					cameraBitMapView.LineChangeToText();
				} else {
					cameraBitMapView.LineNoChangeToText();
				}
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				if (cameraBitMapView.SetLineText((int) event.getRawX(), (int) event.getRawY())) {
					// Logger.i("设置线段", "设置成功了！");
					if (v == dataMove) {
						cameraBitMapView.SetwriteLineText("2.985");
						dataMove.setVisibility(View.INVISIBLE);
					}
					cameraBitMapView.LineNoChangeToText();
				}
				break;
			}
			return false;
		}
	};

	private void showAlertDialog() {
		ExitSaveSketchDialog ess = ExitSaveSketchDialog.newInstance();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ess.show(ft, "");
	}

	@Override
	public void doPositiveClick() {
		// TODO Auto-generated method stub
		long key = System.currentTimeMillis();
		OperateData.insertLine(key, cameraBitMapView.BackLinelist(), helper);
		OperateData.insertPolygon(key, cameraBitMapView.BackPolylist(), helper);
		OperateData.insertRectangle(key, cameraBitMapView.BackRectlist(), helper);
		OperateData.insertCoordinate(key, cameraBitMapView.BackCoorlist(), helper);
		OperateData.insertAngle(key, cameraBitMapView.BackAnglelist(), helper);
		OperateData.insertText(key, cameraBitMapView.BackTextlist(), helper);
		OperateData.insertAudio(key, cameraBitMapView.BackAudiolist(), helper);
		OperateData.insertModule(key, edittitle.getText().toString(), imgurl, 1, helper);
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
		btnrectangle.setSelected(false);
		btncoordinate.setSelected(false);
		btnangle.setSelected(false);
	}
}
