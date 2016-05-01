package com.powerble.BLEProfileDataParserClasses;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import  com.surveymapclient.activity.R;
import com.powerble.CommonUtils.BLELogger;

public class BloodPressureParser {
    public static String getSystolicBloodPressure(BluetoothGattCharacteristic characteristic) {
      String pressure = "";//String.format("%3.3f", new Object[]{Float.valueOf(characteristic.getFloatValue(BootLoaderCommands.GET_FLASH_SIZE, 1).floatValue())});
        BLELogger.i("Systolic Pressure>>>>" +pressure);
        return pressure;
    }

    public static String getSystolicBloodPressureUnit(BluetoothGattCharacteristic characteristic, Context context) {
        return BloodPressureUnitsFlagSet(characteristic.getValue()[0]) ? context.getResources().getString(R.string.blood_pressure_kPa) : context.getResources().getString(R.string.blood_pressure_mmHg);
    }

    public static String getDiaStolicBloodPressure(BluetoothGattCharacteristic characteristic) {
        //String pressure = String.format("%3.3f", new Object[]{Float.valueOf(characteristic.getFloatValue(BootLoaderCommands.GET_FLASH_SIZE, WearableExtender.SIZE_MEDIUM).floatValue())});
       // Logger.i("Diastolic Pressure>>>>" + );
        return "";
    }

    public static String getDiaStolicBloodPressureUnit(BluetoothGattCharacteristic characteristic, Context context) {
        return BloodPressureUnitsFlagSet(characteristic.getValue()[0]) ? context.getResources().getString(R.string.blood_pressure_kPa) : context.getResources().getString(R.string.blood_pressure_mmHg);
    }

    private static boolean BloodPressureUnitsFlagSet(byte flags) {
        return (flags & 1) != 0;
    }
}
