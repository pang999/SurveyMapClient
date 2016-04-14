package com.surveymapclient.Dialog;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class ExitSaveSketchDialog extends DialogFragment {
	
	 public interface DialogFragmentClickImpl {
	        void doPositiveClick();
	 
	        void doNegativeClick();
	 }
	 
	 public static ExitSaveSketchDialog newInstance(){
		 ExitSaveSketchDialog eDialog=new ExitSaveSketchDialog();
		 return eDialog;
	 }
	 public ExitSaveSketchDialog() {
		// TODO Auto-generated constructor stub
	}
	 @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity())
				.setIcon(com.surveymapclient.activity.R.drawable.ic_launcher)
				.setTitle("保存画图")
				.setMessage("确定保存所画的的画板")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DialogFragmentClickImpl impl=(DialogFragmentClickImpl) getActivity();
						impl.doPositiveClick();
					}
				}).setNegativeButton(android.R.string.cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						DialogFragmentClickImpl impl=(DialogFragmentClickImpl) getActivity();
						impl.doNegativeClick();
					}
				})
				.create();
	}

}
