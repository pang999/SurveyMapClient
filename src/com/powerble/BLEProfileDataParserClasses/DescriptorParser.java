package com.powerble.BLEProfileDataParserClasses;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import  com.surveymapclient.activity.R;
import com.powerble.Util;
import com.powerble.CommonUtils.Constants;
import com.powerble.CommonUtils.GattAttributes;

public class DescriptorParser {
	private static final int CASE_IND_ENABLED_NOTIFY_DISABLED = 2;
	private static final int CASE_IND_ENABLED_NOTIFY_ENABLED = 3;
	private static final int CASE_NOTIFY_DISABLED_IND_DISABLED = 0;
	private static final int CASE_NOTIFY_ENABLED_IND_DISABLED = 1;

	public static String getClientCharacteristicConfiguration(
			BluetoothGattDescriptor descriptor, Context context) {
		String valueConverted = Util.VERSION_NAME;
		switch (descriptor.getValue()[0]) {
		case CASE_NOTIFY_DISABLED_IND_DISABLED:
			return context.getResources().getString(
					R.string.descriptor_notification_disabled)
					+ "\n"
					+ context.getResources().getString(
							R.string.descriptor_indication_disabled);
		case CASE_NOTIFY_ENABLED_IND_DISABLED:
			return context.getResources().getString(
					R.string.descriptor_notification_enabled)
					+ "\n"
					+ context.getResources().getString(
							R.string.descriptor_indication_disabled);
		case CASE_IND_ENABLED_NOTIFY_DISABLED:
			return context.getResources().getString(
					R.string.descriptor_indication_enabled)
					+ "\n"
					+ context.getResources().getString(
							R.string.descriptor_notification_disabled);
		case CASE_IND_ENABLED_NOTIFY_ENABLED:
			return context.getResources().getString(
					R.string.descriptor_indication_enabled)
					+ "\n"
					+ context.getResources().getString(
							R.string.descriptor_notification_enabled);
		default:
			return valueConverted;
		}
	}

	public static HashMap<String, String> getCharacteristicExtendedProperties(
			BluetoothGattDescriptor descriptor, Context context) {
		String reliableWriteStatus;
		String writableAuxillaryStatus;
		HashMap<String, String> valuesMap = new HashMap();
		byte reliableWriteBit = descriptor.getValue()[0];
		byte writableAuxillaryBit = descriptor.getValue()[1];
		if ((reliableWriteBit & 1) != 0) {
			reliableWriteStatus = context.getResources().getString(
					R.string.descriptor_reliablewrite_enabled);
		} else {
			reliableWriteStatus = context.getResources().getString(
					R.string.descriptor_reliablewrite_disabled);
		}
		if ((writableAuxillaryBit & 1) != 0) {
			writableAuxillaryStatus = context.getResources().getString(
					R.string.descriptor_writableauxillary_enabled);
		} else {
			writableAuxillaryStatus = context.getResources().getString(
					R.string.descriptor_writableauxillary_disabled);
		}
		valuesMap.put(Constants.FIRST_BIT_KEY_VALUE, reliableWriteStatus);
		valuesMap.put(Constants.SECOND_BIT_KEY_VALUE, writableAuxillaryStatus);
		return valuesMap;
	}

	public static String getCharacteristicUserDescription(
			BluetoothGattDescriptor descriptor) {
		return new String(descriptor.getValue(), Charset.forName("UTF-8"));
	}

	public static String getServerCharacteristicConfiguration(
			BluetoothGattDescriptor descriptor, Context context) {
		return (descriptor.getValue()[0] & 1) != 0 ? context.getResources()
				.getString(R.string.descriptor_broadcast_enabled) : context
				.getResources().getString(
						R.string.descriptor_broadcast_disabled);
	}

	public static ArrayList<String> getReportReference(
			BluetoothGattDescriptor descriptor) {
		ArrayList<String> reportReferencevalues = new ArrayList(2);
		byte[] array = descriptor.getValue();
		String str = "";
		String reportType = "";
		if (array != null && array.length == 2) {
			reportReferencevalues.add(str);
			reportReferencevalues.add(reportType);
		} else if (array != null && array.length == 1) {
			// reportReferencevalues.add(ReportAttributes.lookupReportReferenceID(Util.VERSION_NAME
			// + array[0]));
			reportReferencevalues.add(reportType);
		}
		return reportReferencevalues;
	}

	public static String getCharacteristicPresentationFormat(
			BluetoothGattDescriptor descriptor, Context context) {
		String namespaceValue;
		String str = Util.VERSION_NAME;
		String lookCharacteristicPresentationFormat = GattAttributes
				.lookCharacteristicPresentationFormat(String.valueOf(descriptor
						.getValue()[0]));
		String exponentValue = String.valueOf(descriptor.getValue()[1]);
		String unitValue = String.valueOf((descriptor.getValue()[2] & 255)
				| (descriptor.getValue()[3] << 8));
		if (String.valueOf(descriptor.getValue()[4]).equalsIgnoreCase("1")) {
			namespaceValue = context.getResources().getString(
					R.string.descriptor_bluetoothSIGAssignedNo);
		} else {
			namespaceValue = context.getResources().getString(
					R.string.descriptor_reservedforFutureUse);
		}
		return context.getResources().getString(R.string.descriptor_format)
				+ "\n" + context.getResources().getString(R.string.exponent)
				+ exponentValue + "\n"
				+ context.getResources().getString(R.string.unit) + unitValue
				+ "\n" + context.getResources().getString(R.string.namespace)
				+ namespaceValue + "\n"
				+ context.getResources().getString(R.string.description)
				+ String.valueOf(descriptor.getValue()[5]);
	}
}
