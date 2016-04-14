package com.surveymapclient.dialog;

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
public class EditTextDialog extends DialogFragment implements OnClickListener{

	EditText editname;
	Button deletename,confirmname;
	static int mType=-1;
	
	public static EditTextDialog newIntance(int type){
		mType=type;
		EditTextDialog editeAndDelDialog=new EditTextDialog();
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

	//在onCreate中设置对话框的风格、属性等
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);//如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true 
        this.setCancelable(true); 
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0; 
        setStyle(style,theme);  
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.comfirmName:
			String name="";
			if (!editname.getText().toString().equals("")&&""!=editname.getText().toString()) {
				name=editname.getText().toString();
			}else {
				name="Text";				
			}
			if (mType==0) {
				DefineActivity daActivity=(DefineActivity) getActivity();
				daActivity.ChangeTextContent(name);
			}			
			dismiss();
			break;

		case R.id.deleteName:
			if (mType==0) {
				DefineActivity daActivity=(DefineActivity) getActivity();
				daActivity.RemoveTextIndex();
			}
			dismiss();
			break;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
