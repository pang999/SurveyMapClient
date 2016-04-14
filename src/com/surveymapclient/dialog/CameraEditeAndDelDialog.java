package com.surveymapclient.dialog;

import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.common.Contants;
import com.surveymapclient.entity.LineBean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class CameraEditeAndDelDialog extends DialogFragment implements OnClickListener{

	static int type;
	int index;
	ImageView edit;
	ImageView delete;
	public static CameraEditeAndDelDialog newIntance(int t){
		CameraEditeAndDelDialog editeAndDelDialog=new CameraEditeAndDelDialog();
		Bundle bundle=new Bundle();
		type=t;
		editeAndDelDialog.setArguments(bundle);
		return editeAndDelDialog;
		
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        // Get the layout inflater  
        LayoutInflater inflater = getActivity().getLayoutInflater(); 
        View view=inflater.inflate(R.layout.fragment_dialog, null);
		edit=(ImageView) view.findViewById(R.id.dialogedit);
		edit.setOnClickListener(this);
		delete=(ImageView) view.findViewById(R.id.dialogdelete);
		delete.setOnClickListener(this);
		builder.setView(view);
		builder.setCancelable(true);
		AlertDialog dialog=builder.create();		
		return dialog;
	}
	
	//在onCreate中设置对话框的风格、属性等
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true 
        this.setCancelable(true); 
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0; 
        setStyle(style,theme);  
       

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CameraActivity cameraActivity=(CameraActivity) getActivity();
		int id = v.getId();
		if (id == R.id.dialogedit) {
			if (type==0) {
				cameraActivity.SendLineData();
			}	
			if (type==1) {
				cameraActivity.SendPolygonData();
			}
			if (type==2) {
				cameraActivity.SendRectangleData();
			}
			if (type==3) {
				cameraActivity.SendCoordinateData();
			}
			if (type==4) {
				cameraActivity.SendAngleData();
			}
			dismiss();
		} else if (id == R.id.dialogdelete) {
			cameraActivity.Remove();
			dismiss();
		} else {
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
