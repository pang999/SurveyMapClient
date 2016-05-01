package com.powerble.BLEProfileDataParserClasses;

import android.bluetooth.BluetoothGattCharacteristic;

import com.powerble.CommonUtils.BLELogger;

public class SensorHubParser {
    public static final int EIGTH_BITMASK = 128;
    public static final int FIFTH_BITMASK = 16;
    private static final int FIRST_BITMASK = 1;
    public static final int FOURTH_BITMASK = 8;
    public static final int SECOND_BITMASK = 2;
    public static final int SEVENTH_BITMASK = 64;
    public static final int SIXTH_BITMASK = 32;
    public static final int THIRD_BITMASK = 4;

    public static int getAcceleroMeterXYZReading(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(18, 0).intValue();
    }

    public static float getThermometerReading(BluetoothGattCharacteristic characteristic) {
        return characteristic.getFloatValue(52, 0).floatValue();
    }

    public static int getBarometerReading(BluetoothGattCharacteristic characteristic) {
        int mPressure = characteristic.getIntValue(18, 0).intValue();
        BLELogger.w("pressure " + mPressure);
        return mPressure;
    }

    public static int getSensorScanIntervalReading(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(17, 0).intValue();
    }

    public static int getSensorTypeReading(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(17, 0).intValue();
    }

    public static int getFilterConfiguration(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(17, 0).intValue();
    }

    public static int getThresholdValue(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(18, 0).intValue();
    }
}
