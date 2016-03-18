package com.surveymapclient.view.fragment;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("NewApi")
public class EditLineNameDialogFragment extends DialogFragment implements OnClickListener{

	EditText editname;
	Button deletename,confirmname;
	
	public static EditLineNameDialogFragment newIntance(){
		EditLineNameDialogFragment editeAndDelDialog=new EditLineNameDialogFragment();
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
		View view=inflater.inflate(R.layout.fragment_linename_dialog, null);
		editname=(EditText) view.findViewById(R.id.editlinetextname);
		deletename=(Button) view.findViewById(R.id.deleteName);
		confirmname=(Button) view.findViewById(R.id.comfirmName);
		deletename.setOnClickListener(this);
		confirmname.setOnClickListener(this);
		builder.setView(view);
		builder.setCancelable(true);
		AlertDialog dialog=builder.create();		
		return dialog;
	}

	//��onCreate�����öԻ���ķ�����Ե�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);//���setCancelable()�в���Ϊtrue�������dialog���ǲ�����activity�Ŀհ׻��߰����ؼ��������cancel��״̬�������onCancel()��onDismiss()�������Ϊfalse���򰴿հ״��򷵻ؼ��޷�Ӧ��ȱʡΪtrue 
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
		if (id == R.id.comfirmName) {
			String name="";
			if (!editname.getText().toString().equals("")&&""!=editname.getText().toString()) {
				name=editname.getText().toString();
			}else {
				name="text";
			}
			daActivity.EditLineName(name);
			dismiss();
		} else if (id == R.id.dialogdelete) {
			daActivity.EditLineName("");
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
