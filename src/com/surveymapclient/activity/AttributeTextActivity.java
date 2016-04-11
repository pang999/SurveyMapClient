package com.surveymapclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AttributeTextActivity extends Activity {

	
	EditText editname;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_linename_dialog);
		editname=(EditText) findViewById(R.id.editlinetextname);
		Bundle bundle=this.getIntent().getExtras();
		editname.setText(bundle.getString("Text"));
	}
	
	public void OnDelete(View view){
		Bundle bundle=new Bundle();
		bundle.putString("BackText", "Delete");
		AttributeTextActivity.this.setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
	public void OnComfirm(View view){
		Bundle bundle=new Bundle();
		bundle.putString("BackText", editname.getText().toString());
		AttributeTextActivity.this.setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
}
