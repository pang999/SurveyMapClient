package com.surveymapclient.dialog;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;
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
	@SuppressLint("InflateParams")
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
	
	//��onCreate�����öԻ���ķ�����Ե�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//���setCancelable()�в���Ϊtrue�������dialog���ǲ�����activity�Ŀհ׻��߰����ؼ��������cancel��״̬�������onCancel()��onDismiss()�������Ϊfalse���򰴿հ״��򷵻ؼ��޷�Ӧ��ȱʡΪtrue 
        this.setCancelable(true); 
        //��������dialog����ʾ�����styleΪSTYLE_NO_TITLE��������ʾtitle���ź����ǣ���û����DialogFragment���ҵ�����title���ݵķ�����themeΪ0����ʾ��ϵͳѡ����ʵ�theme��
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
				daActivity.SendPolygonLineData();
			}
			dismiss();
		} else if (id == R.id.dialogdelete) {
			if (type==0) {
				daActivity.RemoveLineIndex();
			}if (type==1) {
				daActivity.RemovePolygonIndex();
			}if (type==2) {
				daActivity.RemovePolygonLineIndex();
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