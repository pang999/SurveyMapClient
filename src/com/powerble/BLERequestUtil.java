package com.powerble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;

import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.CRC8Checker;
import com.surveymapclient.activity.base.MyApplication;

/**
 * @Description(描述): 蓝牙业务请求类，对数据请求进行封装，对设备发起数据请求
 * @Package(包名): com.powerble
 * @ClassName(类名): BLERequestUtil
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午2:49:59
 * @version(版本): V1.0
 */
public class BLERequestUtil {
	private final MyApplication mApplication;
	private BroadcastReceiver mGattUpdateReceiver;

	private BluetoothGattCharacteristic mNotifyCharacteristic;

	private BluetoothGattCharacteristic mReadCharacteristic;

	public BLERequestUtil(Activity activity) {

		this.mApplication = (MyApplication) activity.getApplication();

	}

	public void readSigleData() {

		mReadCharacteristic = this.mApplication
				.getBluetoothgattcharacteristic();

		mApplication.setBluetoothgattcharacteristic(mApplication
				.getGattCharacteristics().get(0));

		mNotifyCharacteristic = this.mApplication
				.getBluetoothgattcharacteristic();
		prepareBroadcastDataNotify(this.mNotifyCharacteristic);

	}
/**
 * @Description（描述）:example send ACK :sendRequest("0x66 0x9A 0x04 0x67 0xEF 0xFE");
 *  @param hexRequest void
 * @author Pang
 * @date 2016-4-21 下午10:21:49
 */
	public void sendRequest(String hexRequest) {
		mApplication.setBluetoothgattcharacteristic(mApplication
				.getGattCharacteristics().get(1));
		mReadCharacteristic = mApplication.getBluetoothgattcharacteristic();
		prepareBroadcastDataRead(mReadCharacteristic);
		send(hexRequest);
	}

	/** write data to ble example:0x23 0x45 0x67 0x89 */
	private void send(String result) {
		byte[] convertedBytes = BLEUtils.convertingTobyteArray(result);
		/* displayHexValue(convertedBytes); */
		/* displayASCIIValue(this.mHexValue.getText().toString()); */

		byte b = CRC8Checker.calcCrc8(convertedBytes, 3, convertedBytes.length);

		String b_source = BLEUtils.ByteArraytoHex(convertedBytes);
		String b_crc = Integer.toHexString(0x00ff & b);

		String out = b_source + b_crc;
		byte[] out_b = BLEUtils.hexStr2Byte(out);
		Util.e("b_sour", b_source);
		Util.e("b_crc", b_crc);
		Util.e("b_out", out);
		Util.e("b_out_b", BLEUtils.ByteArraytoHex(out_b));

		writeCharaValue(out_b);
	}

	private void writeCharaValue(byte[] value) {
		/* displayTimeandDate(); */
		try {
			BluetoothLeService.writeCharacteristicGattDb(
					this.mReadCharacteristic, value);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	private void prepareBroadcastDataRead(
			BluetoothGattCharacteristic gattCharacteristic) {

		if ((gattCharacteristic.getProperties() | 2) > 0) {
			BluetoothLeService.readCharacteristic(gattCharacteristic);
		}
	}

	@SuppressLint("NewApi")
	private void prepareBroadcastDataNotify(
			BluetoothGattCharacteristic gattCharacteristic) {

		if ((gattCharacteristic.getProperties() | 16) > 0) {
			BluetoothLeService.setCharacteristicNotification(
					gattCharacteristic, true);
		}
	}

}