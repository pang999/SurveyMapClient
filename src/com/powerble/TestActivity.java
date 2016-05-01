package com.powerble;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.Constants;
import com.powerble.data.BLEDataPackage;
import com.surveymapclient.activity.R;

public class TestActivity extends Activity {
	BroadcastReceiver mGattUpdateReceiver;

	BLERequestUtil util;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		util = new BLERequestUtil(this);
		util.readSigleData();
		// UIbuttonvisibility();

		mGattUpdateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				Bundle extras = intent.getExtras();
				if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
					if (extras.containsKey(Constants.EXTRA_DATA_PACKAGE)) {
						BLEDataPackage pkg = (BLEDataPackage) extras
								.getSerializable(Constants.EXTRA_DATA_PACKAGE);
						
						String s="data:\n"+"data1:"+pkg.getData1()+" "+pkg.getDataType1()+"\n"
								+"data2:"+pkg.getData2()+" "+pkg.getDataType2()+"\n"
								+"data3:"+pkg.getData3()+" "+pkg.getDataType3()+"\n"
								+"data4:"+pkg.getData4()+" "+pkg.getDataType4()+"\n"
								;
					/*	Toast.makeText(TestActivity.this,
								,Toast.LENGTH_LONG).show();*/
						((TextView)findViewById(R.id.tv1)).setText(s);
					//	pkg.
						Util.e("pkg uuid:", pkg.getServiceUUid());
					}
				}
			}

		};

		registerReceiver(mGattUpdateReceiver,
				BLEUtils.makeGattUpdateIntentFilter());

	}

	public void onClickSend(View v) {

		// send("0x99 0x6A 0x03 0x50 0x01");

		// 99 6A 03 50 01
		// crc:99 6A 03 50 01 29
		
		
		
	util.sendRequest(Constants.REQUEST_HISTORY_DATA);
//util.sendRequest(Constants.REQUEST_ACK);
		// b_out_b------66 9A 04 67 EF FE 67 2D
		// Gattdetail key receive!------HexValue:99 6A 03 50 01 29

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
	}

}
