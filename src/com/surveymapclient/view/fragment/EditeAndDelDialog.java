package com.surveymapclient.view.fragment;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.LineAttributeActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.CouplePointLineBean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class EditeAndDelDialog extends DialogFragment implements OnClickListener{

	CouplePointLineBean couplePointLine;
	int index;
	ImageView edit;
	ImageView delete;
	public static EditeAndDelDialog newIntance(){
		EditeAndDelDialog editeAndDelDialog=new EditeAndDelDialog();
		Bundle bundle=new Bundle();

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
		DefineActivity daActivity=(DefineActivity) getActivity();
		int id = v.getId();
		if (id == R.id.dialogedit) {
			daActivity.SendData();
			dismiss();
		} else if (id == R.id.dialogdelete) {
			daActivity.Remove();
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
