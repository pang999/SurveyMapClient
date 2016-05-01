package com.powerble;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonFragments.ProfileScanningFragment;
import com.powerble.CommonFragments.ServiceDiscoveryFragment;
import com.powerble.CommonUtils.BLELogger;

public class BLEStatusReceiver extends BroadcastReceiver {
	static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(intent
				.getAction())) {
			BLELogger.e("onReceive--"
					+ BLEHomePageActivity.mApplicationInBackground);
			if (!BLEHomePageActivity.mApplicationInBackground.booleanValue()) {
				Toast.makeText(context, "BLE Service is disconnect!!!", 0)
						.show();
				/*
				 * if (OTAFirmwareUpgradeFragment.mFileupgradeStarted) {
				 * Utils.setStringSharedPreference(context,
				 * Constants.PREF_OTA_FILE_ONE_NAME, "Default");
				 * Utils.setStringSharedPreference(context,
				 * Constants.PREF_OTA_FILE_TWO_PATH, "Default");
				 * Utils.setStringSharedPreference(context,
				 * Constants.PREF_OTA_FILE_TWO_NAME, "Default");
				 * Utils.setStringSharedPreference(context,
				 * Constants.PREF_BOOTLOADER_STATE, "Default");
				 * Utils.setIntSharedPreference(context,
				 * Constants.PREF_PROGRAM_ROW_NO, 0); }
				 */
				if (!ProfileScanningFragment.isInFragment
						&& !ServiceDiscoveryFragment.isInServiceFragment
						&& !BLEHomePageActivity.mApplicationInBackground
								.booleanValue()) {
					BLELogger.e("Not in PSF and SCF");
					if (BluetoothLeService.getConnectionState() == 0) {
						Toast.makeText(context, "BLE is disconnect", 0).show();
						Intent homePage = new Intent(context,
								BLEHomePageActivity.class);
						homePage.setFlags(335544320);
						context.startActivity(homePage);
					}
				}
			}
		}
	}
}
