package com.powerble.BLEProfileDataParserClasses;

import android.bluetooth.BluetoothGattCharacteristic;

import com.powerble.CommonUtils.BLELogger;

import java.util.ArrayList;


public class CapSenseParser {
    public static int getCapSenseProximity(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(17, 0).intValue();
    }

    public static int getCapSenseSlider(BluetoothGattCharacteristic characteristic) {
        return characteristic.getIntValue(17, 0).intValue();
    }

    public static ArrayList<Integer> getCapSenseButtons(BluetoothGattCharacteristic characteristic) {
        ArrayList<Integer> mButtonParams = new ArrayList();
        int buttonCount = characteristic.getIntValue(17, 0).intValue();
        int buttonStatus1 = characteristic.getIntValue(17, 1).intValue();
   //     int buttonStatus2 = characteristic.getIntValue(17, Zoom.ZOOM_AXIS_Y).intValue();
        int buttonStatus2 =1;
        BLELogger.i("Button count" + buttonCount + "Button status " + buttonStatus1 + "Button status " + buttonStatus2);
        mButtonParams.add(Integer.valueOf(buttonCount));
        mButtonParams.add(Integer.valueOf(buttonStatus1));
        mButtonParams.add(Integer.valueOf(buttonStatus2));
        return mButtonParams;
    }
}
