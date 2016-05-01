package com.powerble.BLEProfileDataParserClasses;

import java.util.ArrayList;

import android.bluetooth.BluetoothGattCharacteristic;

import com.powerble.Util;
import com.powerble.CommonUtils.BLELogger;

public class CSCParser {
	private static final int ARRAYLIST_INDEX_0 = 0;
	private static final int ARRAYLIST_INDEX_1 = 1;
	private static final int ARRAYLIST_INDEX_2 = 2;
	private static final int ARRAYLIST_INDEX_3 = 3;
	private static final int ARRAYLIST_INDEX_4 = 4;
	private static final byte CRANK_REVOLUTION_DATA_PRESENT = (byte) 2;
	private static final float FLOAT_CONST_1000 = 1000.0f;
	private static final float FLOAT_CONST_1000000 = 1000000.0f;
	private static final float FLOAT_CONST_1024 = 1024.0f;
	private static final float FLOAT_CONST_60 = 60.0f;
	private static final int FORMAT_TYPE_18 = 18;
	private static final int FORMAT_TYPE_20 = 20;
	private static final int WHEEL_CONST = 65535;
	private static final byte WHEEL_REVOLUTIONS_DATA_PRESENT = (byte) 1;
	private static ArrayList<String> mCSCInfo;
	private static String mCyclingCadence;
	private static String mCyclingDistance;
	private static String mCyclingExtraDistance;
	private static String mCyclingExtraSpeed;
	private static int mFirstWheelRevolutions;
	private static String mGearRatio;
	private static int mLastCrankEventTime;
	private static int mLastCrankRevolutions;
	private static int mLastWheelEventTime;
	private static int mLastWheelRevolutions;
	private static float mWheelCadence;

	static {
		mCSCInfo = new ArrayList();
		mFirstWheelRevolutions = -1;
		mLastWheelRevolutions = -1;
		mLastWheelEventTime = -1;
		mWheelCadence = -1.0f;
		mLastCrankRevolutions = -1;
		mLastCrankEventTime = -1;
	}

	public static ArrayList<String> getCyclingSpeednCadence(
			BluetoothGattCharacteristic characteristic) {
		boolean wheelRevPresent;
		boolean crankRevPreset = true;
		int flags = characteristic.getValue()[0];
		int offset = 0 + 1;
		if ((flags & 1) > 0) {
			wheelRevPresent = true;
		} else {
			wheelRevPresent = false;
		}
		if ((flags & 2) <= 0) {
			crankRevPreset = false;
		}
		if (wheelRevPresent) {
			int offsetComp = offset + 4;
			offset = offsetComp + 2;
			onWheelMeasurementReceived(
					characteristic.getIntValue(FORMAT_TYPE_20, offset)
							.intValue(),
					characteristic.getIntValue(FORMAT_TYPE_18, offsetComp)
							.intValue());
		}
		if (crankRevPreset) {
			onCrankMeasurementReceived(
					characteristic.getIntValue(FORMAT_TYPE_18, offset)
							.intValue(),
					characteristic.getIntValue(FORMAT_TYPE_18, offset + 2)
							.intValue());
		}
		return mCSCInfo;
	}

	private static void onWheelMeasurementReceived(int wheelRevolutions,
			int lastWheelEventTime) {
		double WHEEL_CIRCUMFERENCE = 6.28d * 4;
		if (mFirstWheelRevolutions < 0) {
			mFirstWheelRevolutions = wheelRevolutions;
		}
		if (mLastWheelEventTime != lastWheelEventTime) {
			if (mLastWheelRevolutions >= 0) {
				float timeDifference;
				if (lastWheelEventTime < mLastWheelEventTime) {
					timeDifference = ((65535 + lastWheelEventTime) - mLastWheelEventTime) / 1024.0f;
				} else {
					timeDifference = (lastWheelEventTime - mLastWheelEventTime) / 1024.0f;
				}
				float totalDistance = ((wheelRevolutions) * ((float) WHEEL_CIRCUMFERENCE)) / 1000.0f;
				float distance = ((wheelRevolutions - mFirstWheelRevolutions) * ((float) WHEEL_CIRCUMFERENCE)) / 1000.0f;
				float speed = (((float) ((wheelRevolutions - mLastWheelRevolutions) * WHEEL_CIRCUMFERENCE)) / 1000.0f)
						/ timeDifference;
				mWheelCadence = (60.0f * (wheelRevolutions - mLastWheelRevolutions))
						/ timeDifference;
				mCyclingDistance = Util.VERSION_NAME + totalDistance;
				mCSCInfo.add(ARRAYLIST_INDEX_0, mCyclingDistance);
				mCyclingExtraSpeed = Util.VERSION_NAME + speed;
				mCSCInfo.add(ARRAYLIST_INDEX_3, mCyclingExtraSpeed);
				mCyclingExtraDistance = Util.VERSION_NAME + distance;
				mCSCInfo.add(ARRAYLIST_INDEX_4, mCyclingExtraDistance);
				BLELogger.d("WheelValues are " + mCyclingDistance + " "
						+ mCyclingExtraSpeed + " " + mCyclingExtraDistance);
			}
			mLastWheelRevolutions = wheelRevolutions;
			mLastWheelEventTime = lastWheelEventTime;
		}
	}

	private static void onCrankMeasurementReceived(int crankRevolutions,
			int lastCrankEventTime) {
		if (mLastCrankEventTime != lastCrankEventTime) {
			if (mLastCrankRevolutions >= 0) {
				float timeDifference;
				if (lastCrankEventTime < mLastCrankEventTime) {
					timeDifference = ((65535 + lastCrankEventTime) - mLastCrankEventTime) / 1024.0f;
				} else {
					timeDifference = (lastCrankEventTime - mLastCrankEventTime) / 1024.0f;
				}
				float crankCadence = (60.0f * (crankRevolutions - mLastCrankRevolutions))
						/ timeDifference;
				if (crankCadence > 0.0f) {
					mGearRatio = Util.VERSION_NAME
							+ (mWheelCadence / crankCadence);
					mCSCInfo.add(ARRAYLIST_INDEX_2, mGearRatio);
					mCyclingCadence = Util.VERSION_NAME + ((int) crankCadence);
					mCSCInfo.add(ARRAYLIST_INDEX_1, mCyclingCadence);
					BLELogger.d("Crank Values are " + mGearRatio + " "
							+ mCyclingCadence);
				}
			}
			mLastCrankRevolutions = crankRevolutions;
			mLastCrankEventTime = lastCrankEventTime;
		}
	}
}
