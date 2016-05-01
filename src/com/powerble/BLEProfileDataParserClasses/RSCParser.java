package com.powerble.BLEProfileDataParserClasses;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.bluetooth.BluetoothGattCharacteristic;

import com.powerble.Util;
import com.powerble.CommonUtils.BLELogger;

public class RSCParser {
	private static final int ARRAYLIST_INDEX_0 = 0;
	private static final int ARRAYLIST_INDEX_1 = 1;
	public static final int EIGTH_BITMASK = 128;
	public static final int FIFTH_BITMASK = 16;
	private static final int FIRST_BITMASK = 1;
	private static final float FLOAT_CONST_10 = 10.0f;
	private static final float FLOAT_CONST_1000 = 1000.0f;
	private static final float FLOAT_CONST_256 = 256.0f;
	private static final float FLOAT_CONST_3D6 = 3.6f;
	private static final float FLOAT_CONST_N1F = -1.0f;
	private static final int FORMAT_TYPE_17 = 17;
	private static final int FORMAT_TYPE_18 = 18;
	private static final int FORMAT_TYPE_20 = 20;
	public static final int FOURTH_BITMASK = 8;
	private static final byte INSTANTANEOUS_STRIDE_LENGTH_PRESENT = (byte) 1;
	public static final int SECOND_BITMASK = 2;
	public static final int SEVENTH_BITMASK = 64;
	public static final int SIXTH_BITMASK = 32;
	public static final int THIRD_BITMASK = 4;
	private static final byte TOTAL_DISTANCE_PRESENT = (byte) 2;
	private static final byte WALKING_OR_RUNNING_STATUS_BITS = (byte) 4;
	private static ArrayList<String> mRscInfo;

	static {
		mRscInfo = new ArrayList();
	}

	public static ArrayList<String> getRunningSpeednCadence(
			BluetoothGattCharacteristic characteristic) {
		boolean running;
		boolean strideFlag;
		boolean totalDistFlag;
		boolean runOrWalkFlag;
		byte flags = characteristic.getValue()[0];
		int offset = 0 + 1;
		boolean islmPresent = (flags & 1) > 0;
		boolean tdPreset = (flags & 2) > 0;
		if ((flags & 4) > 0) {
			running = true;
		} else {
			running = false;
		}
		if (islmPresent) {
			strideFlag = true;
		} else {
			strideFlag = false;
		}
		if (tdPreset) {
			totalDistFlag = true;
		} else {
			totalDistFlag = false;
		}
		if (running) {
			runOrWalkFlag = true;
		} else {
			runOrWalkFlag = false;
		}
		BLELogger.i("Running value received "
				+ characteristic.getIntValue(18, offset).intValue());
		float speedValue = 3.6f * (characteristic.getIntValue(18, offset)
				.floatValue() / 256.0f);
		BLELogger.i("Running value shown " + speedValue);
		mRscInfo.add(0, Util.VERSION_NAME + speedValue);
		int offsetInc = offset + 2;
		int offsetValue = characteristic.getIntValue(17, offsetInc).intValue();
		int offsetIncT = offsetInc + 1;
		float floatConst = FLOAT_CONST_N1F;
		if (strideFlag) {
			floatConst = characteristic.getIntValue(18, offsetIncT).intValue();
			offsetIncT += 2;
		}
		float distanceValue = FLOAT_CONST_N1F;
		if (totalDistFlag) {
			distanceValue = ((characteristic.getIntValue(20, offsetIncT)
					.intValue()) / 10.0f) / 1000.0f;
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMinimumFractionDigits(2);
			formatter.setMaximumFractionDigits(3);
			mRscInfo.add(1, Util.VERSION_NAME + formatter.format(distanceValue));
		}
		BLELogger.d("Running Values are " + speedValue + " " + offsetValue + " "
				+ distanceValue + " " + floatConst + " "
				+ (runOrWalkFlag ? FIRST_BITMASK : ARRAYLIST_INDEX_0));
		return mRscInfo;
	}
}
