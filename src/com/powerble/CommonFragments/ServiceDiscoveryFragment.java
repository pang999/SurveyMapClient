package com.powerble.CommonFragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.powerble.TestActivity;
import com.powerble.Util;
import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLELogger;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.GattAttributes;
import com.powerble.CommonUtils.UUIDDatabase;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.base.MyApplication;

/**
 * @Description(描述): 获取设备服务
 * @Package(包名): com.powerble.CommonFragmentsout
 * @ClassName(类名): ServiceDiscoveryFragment
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午2:43:02
 * @version(版本): V1.0
 */
public class ServiceDiscoveryFragment extends Fragment {
	private static final long DELAY_PERIOD = 500;
	private static final String LIST_UUID = "UUID";
	private static final long SERVICE_DISCOVERY_TIMEOUT = 10000;
	public static boolean isInServiceFragment;
	static ArrayList<HashMap<String, BluetoothGattService>> mGattServiceData;
	static ArrayList<HashMap<String, BluetoothGattService>> mGattServiceFindMeData;
	private static ArrayList<HashMap<String, BluetoothGattService>> mGattServiceMasterData;
	static ArrayList<HashMap<String, BluetoothGattService>> mGattServiceProximityData;
	static ArrayList<HashMap<String, BluetoothGattService>> mGattServiceSensorHubData;
	private static ArrayList<HashMap<String, BluetoothGattService>> mGattdbServiceData;
	private MyApplication mApplication;
	private TextView mNoserviceDiscovered;
	private ProgressDialog mProgressDialog;
	private final BroadcastReceiver mServiceDiscoveryListner;
	private Timer mTimer;
	private LinearLayout ll_top;
	private TextView tv_address, tv_service, tv_uuid;

	public ServiceDiscoveryFragment() {
		mServiceDiscoveryListner = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
						.equals(action)) {
					BLELogger.e("Service discovered");
					if (mTimer != null) {
						mTimer.cancel();
					}
					prepareGattServices(BluetoothLeService
							.getSupportedGattServices());
					if (VERSION.SDK_INT >= 21) {
						BluetoothLeService
								.exchangeGattMtu(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
					}
				} else if (BluetoothLeService.ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL
						.equals(action)) {
					mProgressDialog.dismiss();
					if (mTimer != null) {
						mTimer.cancel();
					}
					showNoServiceDiscoverAlert();
				}
			}
		};
	}

	static {
		mGattServiceData = new ArrayList();
		mGattServiceFindMeData = new ArrayList();
		mGattServiceProximityData = new ArrayList();
		mGattServiceSensorHubData = new ArrayList();
		mGattdbServiceData = new ArrayList();
		mGattServiceMasterData = new ArrayList();
		isInServiceFragment = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Util.e("fragment", "discovery");

		View rootView = inflater.inflate(
				R.layout.servicediscovery_temp_fragment, container, false);
		mNoserviceDiscovered = (TextView) rootView
				.findViewById(R.id.no_service_text);

		ll_top = (LinearLayout) rootView.findViewById(R.id.ll_top);
		tv_address = (TextView) rootView.findViewById(R.id.tv_address);
		tv_service = (TextView) rootView.findViewById(R.id.tv_service);
		mProgressDialog = new ProgressDialog(getActivity());
		mTimer = showServiceDiscoveryAlert(false);
		mApplication = (MyApplication) getActivity().getApplication();
		tv_uuid = (TextView) rootView.findViewById(R.id.tv_uuid);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				BLELogger.e("Discover service called");
				if (BluetoothLeService.getConnectionState() == 2) {
					BluetoothLeService.discoverServices();
				}
			}
		}, DELAY_PERIOD);
		setHasOptionsMenu(true);
		return rootView;
	}

	private Timer showServiceDiscoveryAlert(boolean isReconnect) {
		mProgressDialog.setTitle("Service Discovery");
		if (isReconnect) {
			mProgressDialog
					.setMessage(getString(R.string.progress_message_reconnect));
		} else {
			mProgressDialog.setMessage("Discovering services. Please wait.");
		}
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(0);
		mProgressDialog.show();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
					mNoserviceDiscovered.post(new Runnable() {
						@Override
						public void run() {
							mNoserviceDiscovered.setVisibility(0);
						}
					});
				}
			}
		}, SERVICE_DISCOVERY_TIMEOUT);
		return timer;
	}

	private void prepareGattServices(List<BluetoothGattService> gattServices) {
		if (isSensorHubPresent(gattServices)) {
			prepareSensorHubData(gattServices);
			Util.e("ok", "----1");
		} else {
			Util.e("ok", "----2");
			prepareData(gattServices);
		}
	}

	@SuppressLint("NewApi")
	boolean isSensorHubPresent(List<BluetoothGattService> gattServices) {
		boolean present = false;
		for (BluetoothGattService gattService : gattServices) {
			if (gattService.getUuid().equals(
					UUIDDatabase.UUID_BAROMETER_SERVICE)) {
				present = true;
			}
		}
		return present;
	}

	@SuppressLint("NewApi")
	private void prepareSensorHubData(List<BluetoothGattService> gattServices) {
		boolean mGattSet = false;
		boolean mSensorHubSet = false;
		if (gattServices != null) {
			mGattServiceData.clear();
			mGattServiceMasterData.clear();
			mGattServiceSensorHubData.clear();
			for (BluetoothGattService gattService : gattServices) {
				HashMap<String, BluetoothGattService> mCurrentServiceData = new HashMap();
				UUID uuid = gattService.getUuid();
				if (uuid.equals(UUIDDatabase.UUID_LINK_LOSS_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_TRANSMISSION_POWER_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_IMMEDIATE_ALERT_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_BAROMETER_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_ACCELEROMETER_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_ANALOG_TEMPERATURE_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_BATTERY_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_DEVICE_INFORMATION_SERVICE)) {
					mCurrentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(mCurrentServiceData);
					if (!mGattServiceSensorHubData
							.contains(mCurrentServiceData)) {
						mGattServiceSensorHubData.add(mCurrentServiceData);
					}
					if (!mSensorHubSet
							&& uuid.equals(UUIDDatabase.UUID_BAROMETER_SERVICE)) {
						mSensorHubSet = true;
						mGattServiceData.add(mCurrentServiceData);
					}
				} else if (uuid
						.equals(UUIDDatabase.UUID_GENERIC_ACCESS_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_GENERIC_ATTRIBUTE_SERVICE)) {
					mCurrentServiceData.put(LIST_UUID, gattService);
					mGattdbServiceData.add(mCurrentServiceData);
					if (!mGattSet) {
						mGattSet = true;
						mGattServiceData.add(mCurrentServiceData);
					}
				} else {
					mCurrentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(mCurrentServiceData);
					mGattServiceData.add(mCurrentServiceData);
				}
			}
			mApplication.setGattServiceMasterData(mGattServiceMasterData);
			if (mGattdbServiceData.size() > 0) {
				updateWithNewFragment();
				return;
			}
			BLELogger.e("No service found");
			mProgressDialog.dismiss();
			showNoServiceDiscoverAlert();
		}
	}

	// ///////////////////////////////////////////////////////////此时ble初始化完成！！！！可以通信
	@SuppressLint("NewApi")
	private void updateWithNewFragment() {
		mProgressDialog.dismiss();
		/*
		 * mProgressDialog.dismiss(); FragmentManager fragmentManager =
		 * getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.container, new
		 * ProfileControlFragment
		 * ().create(BluetoothLeService.getmBluetoothDeviceName(),
		 * BluetoothLeService.getmBluetoothDeviceAddress()),
		 * Constants.PROFILE_CONTROL_FRAGMENT_TAG).commit();
		 */

		/*
		 * bundle.putString(Constants.GATTDB_SELECTED_SERVICE,"Unk Service");
		 * bundle.putString(Constants.GATTDB_SELECTED_CHARACTERISTICE,
		 * uuidWrite)
		 */
		String uuid = "unknown uuid";
		if (mApplication.getBluetoothgattcharacteristic() != null)
			uuid = mApplication.getBluetoothgattcharacteristic().getUuid()
					.toString();

		if (ll_top != null)
			ll_top.setVisibility(View.VISIBLE);
		BluetoothGattService sv = (BluetoothGattService) ((HashMap) mApplication
				.getGattServiceMasterData().get(0)).get("UUID");

		String servicename = GattAttributes.lookupUUID(
				sv.getUuid(),
				getResources().getString(
						R.string.profile_control_unknown_service));
		if (tv_address != null)
			tv_address.setText("设备地址: "
					+ ProfileScanningFragment.mDeviceAddress);
		if (tv_service != null)
			tv_service.setText("服务名称：" + servicename);

		if (tv_uuid != null)
			tv_uuid.setText("UUID: " + uuid);
		explor();

	}

	@SuppressLint("NewApi")
	private void prepareData(List<BluetoothGattService> gattServices) {
		boolean mFindmeSet = false;
		boolean mProximitySet = false;
		boolean mGattSet = false;
		if (gattServices != null) {
			mGattServiceData.clear();
			mGattServiceFindMeData.clear();
			mGattServiceMasterData.clear();
			for (BluetoothGattService gattService : gattServices) {
				HashMap<String, BluetoothGattService> currentServiceData = new HashMap();
				UUID uuid = gattService.getUuid();
				if (uuid.equals(UUIDDatabase.UUID_IMMEDIATE_ALERT_SERVICE)) {
					currentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(currentServiceData);
					if (!mGattServiceFindMeData.contains(currentServiceData)) {
						mGattServiceFindMeData.add(currentServiceData);
					}
					if (!mFindmeSet) {
						mFindmeSet = true;
						mGattServiceData.add(currentServiceData);
					}
				} else if (uuid.equals(UUIDDatabase.UUID_LINK_LOSS_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_TRANSMISSION_POWER_SERVICE)) {
					currentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(currentServiceData);
					if (!mGattServiceProximityData.contains(currentServiceData)) {
						mGattServiceProximityData.add(currentServiceData);
					}
					if (!mProximitySet) {
						mProximitySet = true;
						mGattServiceData.add(currentServiceData);
					}
				} else if (uuid
						.equals(UUIDDatabase.UUID_GENERIC_ACCESS_SERVICE)
						|| uuid.equals(UUIDDatabase.UUID_GENERIC_ATTRIBUTE_SERVICE)) {
					currentServiceData.put(LIST_UUID, gattService);
					mGattdbServiceData.add(currentServiceData);
					if (!mGattSet) {
						mGattSet = true;
						mGattServiceData.add(currentServiceData);
					}
				} else if (!uuid.equals(UUIDDatabase.UUID_HID_SERVICE)) {
					currentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(currentServiceData);
					mGattServiceData.add(currentServiceData);
				} else if (VERSION.SDK_INT < 21) {
					BLELogger.e("Kitkat RDK device found");
					List<BluetoothGattCharacteristic> allCharacteristics = gattService
							.getCharacteristics();
					List<BluetoothGattCharacteristic> RDKCharacteristics = new ArrayList();
					List<BluetoothGattDescriptor> RDKDescriptors = new ArrayList();
					for (BluetoothGattCharacteristic characteristic : allCharacteristics) {
						if (characteristic.getUuid().equals(
								UUIDDatabase.UUID_REP0RT)) {
							RDKCharacteristics.add(characteristic);
						}
					}
					for (BluetoothGattCharacteristic rdkcharacteristic : RDKCharacteristics) {
						for (BluetoothGattDescriptor descriptor : rdkcharacteristic
								.getDescriptors()) {
							RDKDescriptors.add(descriptor);
						}
					}
					if (RDKDescriptors.size() == RDKCharacteristics.size() * 2) {
						int pos = 0;
						for (int descPos = 0; descPos < RDKCharacteristics
								.size(); descPos++) {
							BluetoothGattCharacteristic rdkcharacteristic2 = RDKCharacteristics
									.get(descPos);
							BLELogger.e("Pos-->" + pos);
							BLELogger.e("Pos+1-->" + (pos + 1));
							BluetoothGattDescriptor clientdescriptor = RDKDescriptors
									.get(pos);
							BluetoothGattDescriptor reportdescriptor = RDKDescriptors
									.get(pos + 1);
							if (!rdkcharacteristic2.getDescriptors().contains(
									clientdescriptor)) {
								rdkcharacteristic2
										.addDescriptor(clientdescriptor);
							}
							if (!rdkcharacteristic2.getDescriptors().contains(
									reportdescriptor)) {
								rdkcharacteristic2
										.addDescriptor(reportdescriptor);
							}
							pos = (pos + 1) + 1;
						}
					}
					currentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(currentServiceData);
					mGattServiceData.add(currentServiceData);
				} else {
					currentServiceData.put(LIST_UUID, gattService);
					mGattServiceMasterData.add(currentServiceData);
					mGattServiceData.add(currentServiceData);
				}
			}
			mApplication.setGattServiceMasterData(mGattServiceMasterData);

			Util.e("service discovery", "---ss");

			BluetoothLeService.mEnabledCharacteristics = new ArrayList();

			mGattServiceData = mApplication.getGattServiceMasterData();
			List mModifiedServiceData = new ArrayList();
			int i = 0;
			while (i < mGattServiceData.size()) {
				if (!((BluetoothGattService) ((HashMap) mGattServiceData.get(i))
						.get("UUID")).getUuid().equals(
						UUIDDatabase.UUID_GENERIC_ATTRIBUTE_SERVICE)
						&& !((BluetoothGattService) ((HashMap) mGattServiceData
								.get(i)).get("UUID")).getUuid().equals(
								UUIDDatabase.UUID_GENERIC_ACCESS_SERVICE)) {
					mModifiedServiceData.add(mGattServiceData.get(i));
				}
				i++;
			}
			BluetoothGattService mService = (BluetoothGattService) ((HashMap) mModifiedServiceData
					.get(0)).get("UUID");
			List<BluetoothGattCharacteristic> mGattCharacteristics = mService
					.getCharacteristics();
			String selectedServiceName = GattAttributes.lookupUUID(
					mService.getUuid(),
					getResources().getString(
							R.string.profile_control_unknown_service));
			mApplication.setGattCharacteristics(mGattCharacteristics);

			if (mGattdbServiceData.size() > 0) {
				updateWithNewFragment();
				return;
			}
			mProgressDialog.dismiss();
			showNoServiceDiscoverAlert();
		}
	}

	private void showNoServiceDiscoverAlert() {
		if (mNoserviceDiscovered != null) {
			mNoserviceDiscovered.setVisibility(0);
		}

		if (ll_top != null)
			ll_top.setVisibility(View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
		isInServiceFragment = false;
		getActivity().unregisterReceiver(mServiceDiscoveryListner);
	}

	@Override
	public void onResume() {
		super.onResume();
		BLELogger.e("Service discovery onResume");
		isInServiceFragment = true;
		getActivity().registerReceiver(mServiceDiscoveryListner,
				BLEUtils.makeGattUpdateIntentFilter());
		BLEUtils.setUpActionBar(getActivity(),
				getResources().getString(R.string.profile_control_fragment));
	}

	public void explor() {
		// new CommunicationInit(getActivity());
		getActivity().startActivity(
				new Intent(getActivity(), TestActivity.class));
		if (1 == 1)
			return;
		String uuidWrite = "0003cdd1-0000-1000-8000-0";
		/*
		 * Bundle bundle = new Bundle();
		 * bundle.putString(Constants.GATTDB_SELECTED_SERVICE,"Unk Service");
		 * bundle.putString(Constants.GATTDB_SELECTED_CHARACTERISTICE,
		 * uuidWrite); FragmentManager fragmentManager = getFragmentManager();
		 * Fragment gattDetailsfragment = new GattDetailsFragment().create();
		 * gattDetailsfragment.setArguments(bundle);
		 * fragmentManager.beginTransaction().add((int) R.id.container,
		 * gattDetailsfragment).addToBackStack(null).commit();
		 */
	}
}
