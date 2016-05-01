package com.powerble.BLEProfileDataParserClasses;

import java.util.Calendar;
import java.util.Locale;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * @Description(描述): 时间解析者
 * @Package(包名): com.powerble.BLEProfileDataParserClasses
 * @ClassName(类名): DateTimeParser
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午2:46:13
 * @version(版本): V1.0
 */
public class DateTimeParser {
	public static String parse(BluetoothGattCharacteristic characteristic) {
		return parse(characteristic, 0);
	}

	static String parse(BluetoothGattCharacteristic characteristic, int offset) {
		int year = characteristic.getIntValue(18, offset).intValue();
		int month = characteristic.getIntValue(17, offset + 2).intValue();
		int day = characteristic.getIntValue(17, offset + 3).intValue();
		int hours = characteristic.getIntValue(17, offset + 4).intValue();
		int minutes = characteristic.getIntValue(17, offset + 5).intValue();
		int seconds = characteristic.getIntValue(17, offset + 6).intValue();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day, hours, minutes, seconds);
		return String.format(Locale.US, "%1$te %1$tb %1$tY, %1$tH:%1$tM:%1$tS",
				new Object[] { calendar });
	}
}
