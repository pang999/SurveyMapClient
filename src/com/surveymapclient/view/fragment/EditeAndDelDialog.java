package com.surveymapclient.view.fragment;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.AttributeLineActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.common.IToast;
import com.surveymapclient.entity.LineBean;

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

	static int type;
	ImageView edit;
	ImageView delete;
	public static EditeAndDelDialog newIntance(int t){
		EditeAndDelDialog editeAndDelDialog=new EditeAndDelDialog();
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
	
<<<<<<< HEAD
	//ÔÚonCreateÖÐÉèÖÃ¶Ô»°¿òµÄ·ç¸ñ¡¢ÊôÐÔµÈ
=======
	//ï¿½ï¿½onCreateï¿½ï¿½ï¿½ï¿½ï¿½Ã¶Ô»ï¿½ï¿½ï¿½Ä·ï¿½ï¿½ï¿½ï¿½ï¿½Ôµï¿½
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
<<<<<<< HEAD
		//Èç¹ûsetCancelable()ÖÐ²ÎÊýÎªtrue£¬Èôµã»÷dialog¸²¸Ç²»µ½µÄactivityµÄ¿Õ°×»òÕß°´·µ»Ø¼ü£¬Ôò½øÐÐcancel£¬×´Ì¬¼ì²âÒÀ´ÎonCancel()ºÍonDismiss()¡£Èç²ÎÊýÎªfalse£¬Ôò°´¿Õ°×´¦»ò·µ»Ø¼üÎÞ·´Ó¦¡£È±Ê¡Îªtrue 
        this.setCancelable(true); 
        //¿ÉÒÔÉèÖÃdialogµÄÏÔÊ¾·ç¸ñ£¬ÈçstyleÎªSTYLE_NO_TITLE£¬½«±»ÏÔÊ¾title¡£ÒÅº¶µÄÊÇ£¬ÎÒÃ»ÓÐÔÚDialogFragmentÖÐÕÒµ½ÉèÖÃtitleÄÚÈÝµÄ·½·¨¡£themeÎª0£¬±íÊ¾ÓÉÏµÍ³Ñ¡ÔñºÏÊÊµÄtheme¡£
=======
		//ï¿½ï¿½ï¿½setCancelable()ï¿½Ð²ï¿½ï¿½ï¿½Îªtrueï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½dialogï¿½ï¿½ï¿½Ç²ï¿½ï¿½ï¿½ï¿½ï¿½activityï¿½Ä¿Õ°×»ï¿½ï¿½ß°ï¿½ï¿½ï¿½ï¿½Ø¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½cancelï¿½ï¿½×´Ì¬ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½onCancel()ï¿½ï¿½onDismiss()ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Îªfalseï¿½ï¿½ï¿½ò°´¿Õ°×´ï¿½ï¿½ò·µ»Ø¼ï¿½ï¿½Þ·ï¿½Ó¦ï¿½ï¿½È±Ê¡Îªtrue 
        this.setCancelable(true); 
        //ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½dialogï¿½ï¿½ï¿½ï¿½Ê¾ï¿½ï¿½ï¿½ï¿½ï¿½styleÎªSTYLE_NO_TITLEï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ê¾titleï¿½ï¿½ï¿½Åºï¿½ï¿½ï¿½ï¿½Ç£ï¿½ï¿½ï¿½Ã»ï¿½ï¿½ï¿½ï¿½DialogFragmentï¿½ï¿½ï¿½Òµï¿½ï¿½ï¿½ï¿½ï¿½titleï¿½ï¿½ï¿½ÝµÄ·ï¿½ï¿½ï¿½ï¿½ï¿½themeÎª0ï¿½ï¿½ï¿½ï¿½Ê¾ï¿½ï¿½ÏµÍ³Ñ¡ï¿½ï¿½ï¿½ï¿½Êµï¿½themeï¿½ï¿½
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0; 
        setStyle(style,theme);  

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DefineActivity daActivity=(DefineActivity) getActivity();
		int id = v.getId();
		if (id == R.id.dialogedit) {
			if (type==0) {
				daActivity.SendLineData();
			}	
			if (type==1) {
				daActivity.SendPolygonData();
			}
			if (type==2) {
				daActivity.SendRectangleData();
			}
			if (type==3) {
				daActivity.SendCoordinateData();
			}
			if (type==4) {
				daActivity.SendAngleData();
			}
			dismiss();
		} else if (id == R.id.dialogdelete) {
			if (type==0) {
				daActivity.RemoveLineIndex();
			}
			if (type==2) {
				daActivity.RemoveRectangleIndex();
			}
			if (type==3) {
				daActivity.RemoveCoordinateIndex();
			}
			if (type==4) {
				daActivity.RemoveAngleIndex();
			}
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
