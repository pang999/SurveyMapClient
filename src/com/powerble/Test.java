package com.powerble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.view.View;

import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.CRC8Checker;
import com.surveymapclient.activity.R;

public class Test extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
	}

	public void onClickSend(View v) {
		send("0x66 0x9A 0x4 0x67 0xEF 0xFE 0x67");
	}

	@SuppressLint("NewApi")
	void prepareBroadcastDataRead(BluetoothGattCharacteristic gattCharacteristic) {
		Util.e("notif", "8");
		if ((gattCharacteristic.getProperties() | 2) > 0) {
			BluetoothLeService.readCharacteristic(gattCharacteristic);
		}
	}

	boolean getGattCharacteristicsPropertices(int characteristics,
			int characteristicsSearch) {
		return (characteristics & characteristicsSearch) == characteristicsSearch;
	}

	/** write data to ble example:0x23 0x45 0x67 0x89 */
	public void send(String result) {
		byte[] convertedBytes = BLEUtils.convertingTobyteArray(result);

		byte b = CRC8Checker.calcCrc8(convertedBytes, 2, convertedBytes.length);
		/*
		 * byte [] b1=new byte[convertedBytes.length+8];
		 * 
		 * 
		 * for(int i=0;i<b1.length;i++){
		 * if(i<convertedBytes.length)b1[i]=convertedBytes[i]; else }
		 */
		String b_source = BLEUtils.ByteArraytoHex(convertedBytes);
		String b_crc = Integer.toHexString(0x00ff & b);

		String out = b_source + b_crc;
		byte[] out_b = BLEUtils.hexStr2Byte(out);
		Util.e("b_sour", b_source);
		Util.e("b_crc", b_crc);
		Util.e("b_out", out);
		Util.e("b_out_b", BLEUtils.ByteArraytoHex(out_b));
		// 0110 0110-1001 1010-0000 0100-0110 0111-1110 1111-1111 1110-0110 0111

		/* displayHexValue(convertedBytes); */
		/* displayASCIIValue(this.mHexValue.getText().toString()); */
		// writeCharaValue(convertedBytes);
	}

	private void writeCharaValue(byte[] value) {
		/* displayTimeandDate(); */
		try {
			// BluetoothLeService.writeCharacteristicGattDb(this.mReadCharacteristic,
			// value);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private String asciiToHex(String asciiValue) {
		char[] chars = asciiValue.toCharArray();
		StringBuffer hex = new StringBuffer();
		for (char c : chars) {
			hex.append(Integer.toHexString(c));
		}
		return hex.toString();
	}

	@SuppressLint("NewApi")
	void prepareBroadcastDataNotify(
			BluetoothGattCharacteristic gattCharacteristic) {
		Util.e("notif", "9");
		if ((gattCharacteristic.getProperties() | 16) > 0) {
			BluetoothLeService.setCharacteristicNotification(
					gattCharacteristic, true);
		}
	}

}
