package com.powerble.BLEConnectionServices;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import  com.surveymapclient.activity.R;
import com.powerble.Util;
import com.powerble.BLEProfileDataParserClasses.DescriptorParser;
import com.powerble.CommonUtils.BLELogger;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.Constants;
import com.powerble.CommonUtils.GattAttributes;
import com.powerble.CommonUtils.UUIDDatabase;
import com.powerble.data.BLEDataPackage;

/**
 * @Description(描述): 蓝牙通信核心服务！！，提供大部分蓝牙通信业务
 * @Package(包名): com.powerble.BLEConnectionServices
 * @ClassName(类名): BluetoothLeService
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午2:43:52
 * @version(版本): V1.0
 */
public class BluetoothLeService extends Service {
	public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public static final String ACTION_GATT_CHARACTERISTIC_ERROR = "com.example.bluetooth.le.ACTION_GATT_CHARACTERISTIC_ERROR";
	public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public static final String ACTION_GATT_CONNECTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
	public static final String ACTION_GATT_CONNECT_OTA = "com.example.bluetooth.le.ACTION_GATT_CONNECT_OTA";
	public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public static final String ACTION_GATT_DISCONNECTED_CAROUSEL = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED_CAROUSEL";
	public static final String ACTION_GATT_DISCONNECTED_OTA = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED_OTA";
	private static final String ACTION_GATT_DISCONNECTING = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTING";
	public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public static final String ACTION_GATT_SERVICES_DISCOVERED_OTA = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED_OTA";
	public static final String ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL = "com.example.bluetooth.le.ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL";
	public static final String ACTION_OTA_DATA_AVAILABLE = "com.cysmart.bluetooth.le.ACTION_OTA_DATA_AVAILABLE";
	private static final String ACTION_PAIRING_REQUEST = "com.example.bluetooth.le.PAIRING_REQUEST";
	public static final String ACTION_PAIR_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	public static final String ACTION_WRITE_COMPLETED = "android.bluetooth.device.action.ACTION_WRITE_COMPLETED";
	public static final String ACTION_WRITE_FAILED = "android.bluetooth.device.action.ACTION_WRITE_FAILED";
	public static final String ACTION_WRITE_SUCCESS = "android.bluetooth.device.action.ACTION_WRITE_SUCCESS";
	private static final int STATE_BONDED = 5;
	public static final int STATE_CONNECTED = 2;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_DISCONNECTING = 4;
	public static BluetoothAdapter mBluetoothAdapter;
	private static String mBluetoothDeviceAddress;
	private static String mBluetoothDeviceName;
	public static BluetoothGatt mBluetoothGatt;
	private static int mConnectionState;
	private static Context mContext;
	public static boolean mDisableNotificationFlag;
	public static boolean mEnableGlucoseFlag;
	public static boolean mEnableRDKNotificationFlag;
	public static ArrayList<BluetoothGattCharacteristic> mEnabledCharacteristics;
	private static final BluetoothGattCallback mGattCallback;
	public static ArrayList<BluetoothGattCharacteristic> mGlucoseCharacteristics;
	private static boolean mOtaExitBootloaderCmdInProgress;
	public static ArrayList<BluetoothGattCharacteristic> mRDKCharacteristics;
	private final IBinder mBinder;
	private BluetoothManager mBluetoothManager;
	public boolean mBound;

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	public BluetoothLeService() {
		this.mBinder = new LocalBinder();
	}

	static {
		mEnabledCharacteristics = new ArrayList();
		mRDKCharacteristics = new ArrayList();
		mGlucoseCharacteristics = new ArrayList();
		mDisableNotificationFlag = false;
		mEnableRDKNotificationFlag = false;
		mEnableGlucoseFlag = false;
		mConnectionState = 0;
		mOtaExitBootloaderCmdInProgress = false;
		mGattCallback = new BluetoothGattCallback() {
			@Override
			public void onConnectionStateChange(BluetoothGatt gatt, int status,
					int newState) {
				String intentAction;
				BLELogger.i("onConnectionStateChange");
				if (newState == 2) {
					intentAction = ACTION_GATT_CONNECTED;
					synchronized (mGattCallback) {
						mConnectionState = STATE_CONNECTED;
					}
					BluetoothLeService.broadcastConnectionUpdate(intentAction);
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ mBluetoothDeviceName
							+ "|"
							+ mBluetoothDeviceAddress
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_connection_established));
				} else if (newState == 0) {
					intentAction = ACTION_GATT_DISCONNECTED;
					synchronized (mGattCallback) {
						mConnectionState = STATE_DISCONNECTED;
					}
					BluetoothLeService.broadcastConnectionUpdate(intentAction);
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ mBluetoothDeviceName
							+ "|"
							+ mBluetoothDeviceAddress
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_connection_disconnected));
				}
				if (newState == 1) {
					intentAction = ACTION_GATT_CONNECTING;
					synchronized (mGattCallback) {
						mConnectionState = STATE_CONNECTING;
					}
					BluetoothLeService.broadcastConnectionUpdate(intentAction);
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ mBluetoothDeviceName
							+ "|"
							+ mBluetoothDeviceAddress
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_connection_establishing));
				} else if (newState == 3) {
					intentAction = ACTION_GATT_DISCONNECTING;
					synchronized (mGattCallback) {
						mConnectionState = STATE_DISCONNECTING;
					}
					BluetoothLeService.broadcastConnectionUpdate(intentAction);
				}
			}

			@Override
			public void onServicesDiscovered(BluetoothGatt gatt, int status) {
				if (status == 0) {
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ mBluetoothDeviceName
							+ "|"
							+ mBluetoothDeviceAddress
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_service_discovery_status)
							+ mContext.getResources().getString(
									R.string.dl_status_success));
					BluetoothLeService
							.broadcastConnectionUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				} else if (status == 5 || status == 15) {
					BluetoothLeService.bondDevice();
					BluetoothLeService
							.broadcastConnectionUpdate(ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL);
				} else {
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ mBluetoothDeviceName
							+ "|"
							+ mBluetoothDeviceAddress
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_service_discovery_status)
							+ mContext.getResources().getString(
									R.string.dl_status_failure) + status);
					BluetoothLeService
							.broadcastConnectionUpdate(ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL);
				}
			}

			@Override
			public void onDescriptorWrite(BluetoothGatt gatt,
					BluetoothGattDescriptor descriptor, int status) {
				String serviceName = GattAttributes.lookupUUID(descriptor
						.getCharacteristic().getService().getUuid(), descriptor
						.getCharacteristic().getService().getUuid().toString());
				String characteristicName = GattAttributes.lookupUUID(
						descriptor.getCharacteristic().getUuid(), descriptor
								.getCharacteristic().getUuid().toString());
				String descriptorName = GattAttributes.lookupUUID(
						descriptor.getUuid(), descriptor.getUuid().toString());
				if (status == 0) {
					String dataLog = mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ serviceName
							+ "|"
							+ characteristicName
							+ "|"
							+ descriptorName
							+ "] "
							+ mContext
									.getResources()
									.getString(
											R.string.dl_characteristic_write_request_status)
							+ mContext.getResources().getString(
									R.string.dl_commaseparator) + "[00]";
					mContext.sendBroadcast(new Intent(ACTION_WRITE_SUCCESS));
					BLELogger.datalog(dataLog);
					if (descriptor.getValue() != null) {
						BluetoothLeService.addRemoveData(descriptor);
					}
					if (mDisableNotificationFlag) {
						BluetoothLeService.disableAllEnabledCharacteristics();
					} else if (mEnableRDKNotificationFlag) {
						if (mRDKCharacteristics.size() > 0) {
							mRDKCharacteristics.remove(STATE_DISCONNECTED);
							BluetoothLeService.enableAllRDKCharacteristics();
						}
					} else if (mEnableGlucoseFlag
							&& mGlucoseCharacteristics.size() > 0) {
						mGlucoseCharacteristics.remove(STATE_DISCONNECTED);
						BluetoothLeService.enableAllGlucoseCharacteristics();
					}
				} else if (status == 5 || status == 15) {
					BluetoothLeService.bondDevice();
					mContext.sendBroadcast(new Intent(ACTION_WRITE_FAILED));
				} else {
					BLELogger
							.datalog(mContext.getResources().getString(
									R.string.dl_commaseparator)
									+ "["
									+ serviceName
									+ "|"
									+ characteristicName
									+ "|"
									+ descriptorName
									+ "] "
									+ mContext
											.getResources()
											.getString(
													R.string.dl_characteristic_write_request_status)
									+ mContext.getResources().getString(
											R.string.dl_status_failure)
									+ status);
					mDisableNotificationFlag = false;
					mEnableRDKNotificationFlag = false;
					mEnableGlucoseFlag = false;
					mContext.sendBroadcast(new Intent(ACTION_WRITE_FAILED));
				}
			}

			@Override
			public void onDescriptorRead(BluetoothGatt gatt,
					BluetoothGattDescriptor descriptor, int status) {
				String serviceName = GattAttributes.lookupUUID(descriptor
						.getCharacteristic().getService().getUuid(), descriptor
						.getCharacteristic().getService().getUuid().toString());
				String characteristicName = GattAttributes.lookupUUID(
						descriptor.getCharacteristic().getUuid(), descriptor
								.getCharacteristic().getUuid().toString());
				String descriptorName = GattAttributes.lookupUUID(
						descriptor.getUuid(), descriptor.getUuid().toString());
				String descriptorValue = " "
						+ BLEUtils.ByteArraytoHex(descriptor.getValue()) + " ";
				if (status == 0) {
					UUID descriptorUUID = descriptor.getUuid();
					Intent intent = new Intent(ACTION_DATA_AVAILABLE);
					Bundle mBundle = new Bundle();
					mBundle.putByteArray(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE,
							descriptor.getValue());
					mBundle.putInt(
							Constants.EXTRA_BYTE_DESCRIPTOR_INSTANCE_VALUE,
							descriptor.getCharacteristic().getInstanceId());
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ serviceName
							+ "|"
							+ characteristicName
							+ "|"
							+ descriptorName
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_characteristic_read_response)
							+ mContext.getResources().getString(
									R.string.dl_commaseparator)
							+ "["
							+ descriptorValue + "]");
					mBundle.putString(
							Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_UUID,
							descriptor.getUuid().toString());
					mBundle.putString(
							Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID,
							descriptor.getCharacteristic().getUuid().toString());
					if (descriptorUUID
							.equals(UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG)) {
						mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
								DescriptorParser
										.getClientCharacteristicConfiguration(
												descriptor, mContext));
					} else if (descriptorUUID
							.equals(UUIDDatabase.UUID_CHARACTERISTIC_EXTENDED_PROPERTIES)) {
						HashMap<String, String> receivedValuesMap = DescriptorParser
								.getCharacteristicExtendedProperties(
										descriptor, mContext);
						String reliableWriteStatus = receivedValuesMap
								.get(Constants.FIRST_BIT_KEY_VALUE);
						String writeAuxillaryStatus = receivedValuesMap
								.get(Constants.SECOND_BIT_KEY_VALUE);
						mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
								reliableWriteStatus + "\n"
										+ writeAuxillaryStatus);
					} else if (descriptorUUID
							.equals(UUIDDatabase.UUID_CHARACTERISTIC_USER_DESCRIPTION)) {
						mBundle.putString(
								Constants.EXTRA_DESCRIPTOR_VALUE,
								DescriptorParser
										.getCharacteristicUserDescription(descriptor));
					} else if (descriptorUUID
							.equals(UUIDDatabase.UUID_SERVER_CHARACTERISTIC_CONFIGURATION)) {
						mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
								DescriptorParser
										.getServerCharacteristicConfiguration(
												descriptor, mContext));
					} else if (descriptorUUID
							.equals(UUIDDatabase.UUID_REPORT_REFERENCE)) {
						ArrayList<String> reportReferencealues = DescriptorParser
								.getReportReference(descriptor);
						if (reportReferencealues.size() == 2) {
							String reportReference = reportReferencealues
									.get(0);
							String reportReferenceType = reportReferencealues
									.get(1);
							mBundle.putString(
									Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_ID,
									reportReference);
							mBundle.putString(
									Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_TYPE,
									reportReferenceType);
							mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
									reportReference + "\n"
											+ reportReferenceType);
						}
					} else if (descriptorUUID
							.equals(UUIDDatabase.UUID_CHARACTERISTIC_PRESENTATION_FORMAT)) {
						mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
								DescriptorParser
										.getCharacteristicPresentationFormat(
												descriptor, mContext));
					}
					intent.putExtras(mBundle);
					mContext.sendBroadcast(intent);
					return;
				}
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ mBluetoothDeviceName
						+ "|"
						+ mBluetoothDeviceAddress
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_read_request_status)
						+ mContext.getResources().getString(
								R.string.dl_status_failure) + status);
			}

			@Override
			public void onCharacteristicWrite(BluetoothGatt gatt,
					BluetoothGattCharacteristic characteristic, int status) {
				String serviceName = GattAttributes.lookupUUID(characteristic
						.getService().getUuid(), characteristic.getService()
						.getUuid().toString());
				String characteristicName = GattAttributes.lookupUUID(
						characteristic.getUuid(), characteristic.getUuid()
								.toString());
				String str = Util.VERSION_NAME;
				if (status == 0) {
					str = mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ serviceName
							+ "|"
							+ characteristicName
							+ "] "
							+ mContext
									.getResources()
									.getString(
											R.string.dl_characteristic_write_request_status)
							+ mContext.getResources().getString(
									R.string.dl_status_success);
					BLELogger.datalog(str);
				} else {
					str = mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ serviceName
							+ "|"
							+ characteristicName
							+ "] "
							+ mContext
									.getResources()
									.getString(
											R.string.dl_characteristic_write_request_status)
							+ mContext.getResources().getString(
									R.string.dl_status_failure) + status;
					Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_ERROR);
					intent.putExtra(
							Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE,
							Util.VERSION_NAME + status);
					mContext.sendBroadcast(intent);
					BLELogger.datalog(str);
				}
				BLELogger.d("CYSMART", str);
				synchronized (mGattCallback) {
					boolean isExitBootloaderCmd = mOtaExitBootloaderCmdInProgress;
					if (mOtaExitBootloaderCmdInProgress) {
						mOtaExitBootloaderCmdInProgress = false;
					}
				}
				// if (isExitBootloaderCmd) {
				// BluetoothLeService.onOtaExitBootloaderComplete(status);
				// }
			}

			@Override
			public void onCharacteristicRead(BluetoothGatt gatt,
					BluetoothGattCharacteristic characteristic, int status) {
				String serviceName = GattAttributes.lookupUUID(characteristic
						.getService().getUuid(), characteristic.getService()
						.getUuid().toString());
				String characteristicName = GattAttributes.lookupUUID(
						characteristic.getUuid(), characteristic.getUuid()
								.toString());
				String characteristicValue = " "
						+ BLEUtils.ByteArraytoHex(characteristic.getValue())
						+ " ";
				if (status == 0) {
					BLELogger.datalog(mContext.getResources().getString(
							R.string.dl_commaseparator)
							+ "["
							+ serviceName
							+ "|"
							+ characteristicName
							+ "] "
							+ mContext.getResources().getString(
									R.string.dl_characteristic_read_response)
							+ mContext.getResources().getString(
									R.string.dl_commaseparator)
							+ "["
							+ characteristicValue + "]");
					Util.e("ble service", "notify updata1");
					BluetoothLeService.broadcastNotifyUpdate(characteristic);
					return;
				}
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ mBluetoothDeviceName
						+ "|"
						+ mBluetoothDeviceAddress
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_read_request_status)
						+ mContext.getResources().getString(
								R.string.dl_status_failure) + status);
				if (status == 5 || status == 15) {
					BluetoothLeService.bondDevice();
				}
			}

			// //
			@Override
			public void onCharacteristicChanged(BluetoothGatt gatt,
					BluetoothGattCharacteristic characteristic) {
				String serviceName = GattAttributes.lookupUUID(characteristic
						.getService().getUuid(), characteristic.getService()
						.getUuid().toString());
				String characteristicName = GattAttributes.lookupUUID(
						characteristic.getUuid(), characteristic.getUuid()
								.toString());
				BLELogger
						.datalog(mContext.getResources().getString(
								R.string.dl_commaseparator)
								+ "["
								+ serviceName
								+ "|"
								+ characteristicName
								+ "] "
								+ mContext
										.getResources()
										.getString(
												R.string.dl_characteristic_notification_response)
								+ mContext.getResources().getString(
										R.string.dl_commaseparator)
								+ "[ "
								+ BLEUtils.ByteArraytoHex(characteristic
										.getValue()) + " ]");
				Util.e("ble service", "notify updata2");
				BluetoothLeService.broadcastNotifyUpdate(characteristic);
			}

			public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
				BLELogger
						.datalog(String.format(
								mContext.getResources().getString(
										R.string.exchange_mtu_rsp),
								new Object[] {
										mBluetoothDeviceName,
										mBluetoothDeviceAddress,
										mContext.getResources().getString(
												R.string.exchange_mtu),
										Integer.valueOf(mtu),
										Integer.valueOf(status) }));
			}
		};
	}

	public static void exchangeGattMtu(int mtu) {
		int i = STATE_DISCONNECTED;
		int retry = STATE_BONDED;
		boolean status = false;
		while (!status && retry > 0) {
			// status = mBluetoothGatt.requestMtu(mtu);
			retry--;
		}
		Resources res = mContext.getResources();
		String string = res.getString(R.string.exchange_mtu_request);
		Object[] objArr = new Object[5];
		objArr[0] = mBluetoothDeviceName;
		objArr[1] = mBluetoothDeviceAddress;
		objArr[2] = res.getString(R.string.exchange_mtu);
		objArr[3] = Integer.valueOf(mtu);
		if (!status) {
			i = 1;
		}
		objArr[4] = Integer.valueOf(i);
		BLELogger.datalog(String.format(string, objArr));
	}

	public static String getmBluetoothDeviceAddress() {
		return mBluetoothDeviceAddress;
	}

	public static String getmBluetoothDeviceName() {
		return mBluetoothDeviceName;
	}

	private static void broadcastConnectionUpdate(String action) {
		BLELogger.i("action :" + action);
		mContext.sendBroadcast(new Intent(action));
	}

	private static void broadcastWritwStatusUpdate(String action) {
		mContext.sendBroadcast(new Intent(action));
	}

	/**
	 * @Description（描述）: 04-21 00:28:30.977: E/pang(4120): save dataLog------ ,
	 *                   [0003cdd0-0000-1000-8000-00805f9b0131|0003cdd1-0000-
	 *                   1000-8000-00805f9b0131] Notification received with
	 *                   value , [ 99 6A 1A 68 00 00 00 08 86 BF ] 04-21
	 *                   00:28:30.977: E/pang(4120): ble service------notify
	 *                   updata2 04-21 00:28:30.977: E/pang(4120): broad
	 *                   update:------data:>>>>>>99 6A 1A 68 00 00 00 08 86 BF
	 *                   04-21 00:28:30.977: E/pang(4120): Gattdetail key
	 *                   receive!------actioin:com.example.bluetooth.le.
	 *                   ACTION_DATA_AVAILABLE 04-21 00:28:30.977: E/pang(4120):
	 *                   Gattdetail key receive!------HexValue:99 6A 1A 68 00 00
	 *                   00 08 86 BF 04-21 00:28:30.987: E/pang(4120): save
	 *                   dataLog------ ,
	 *                   [0003cdd0-0000-1000-8000-00805f9b0131|0003
	 *                   cdd1-0000-1000-8000-00805f9b0131] Notification received
	 *                   with value , [ 97 DB 55 18 2D 74 86 BF 97 DB 55 18 05
	 *                   E2 0E 00 00 00 4F ] 04-21 00:28:30.987: E/pang(4120):
	 *                   ble service------notify updata2 04-21 00:28:30.987:
	 *                   E/pang(4120): broad update:------data:>>>>>>97 DB 55 18
	 *                   2D 74 86 BF 97 DB 55 18 05 E2 0E 00 00 00 4F 04-21
	 *                   00:28:30.987: E/pang(4120): Gattdetail key
	 *                   receive!------
	 *                   actioin:com.example.bluetooth.le.ACTION_DATA_AVAILABLE
	 * 
	 * 
	 *                   数据分两次返回
	 * @param characteristic
	 *            void
	 * @author Pang
	 * @date 2016-4-21 上午12:28:17
	 */

	static String tempStr;

	private static void broadcastNotifyUpdate(
			BluetoothGattCharacteristic characteristic) {
		String s = BLEUtils.ByteArraytoHex(characteristic.getValue());
		Util.e("broad update:", "data:>>>>>>" + s);

		BLEDataPackage package1;
		if (s != null && s.contains("99 6A")) {
			tempStr = s;
			package1 = null;
			return;
		}

		else {
			tempStr = tempStr + s;
			package1 = new BLEDataPackage(tempStr);
			tempStr = "";
		}
		if (package1 == null)
			return;
		Intent intent = new Intent(ACTION_DATA_AVAILABLE);
		Bundle mBundle = new Bundle();
		Util.e("reciive :", "uuid:" + characteristic.getUuid().toString()
				+ " \nINSTANCE_VALUE:" + characteristic.getInstanceId()
				+ "\nSERVICE_UUID_VALUE:"
				+ characteristic.getService().getUuid().toString()
				+ "\nSERVICE_INSTANCE_VALUE:"
				+ characteristic.getService().getInstanceId());

		package1.setUUid(characteristic.getUuid().toString());
		package1.setServiceUUid(characteristic.getService().getUuid()
				.toString());
		mBundle.putSerializable(Constants.EXTRA_DATA_PACKAGE, package1);
		intent.putExtras(mBundle);
		mContext.sendBroadcast(intent);
	}

	private static void onOtaExitBootloaderComplete(int status) {
		Bundle bundle = new Bundle();
		bundle.putByteArray(Constants.EXTRA_BYTE_VALUE,
				new byte[] { (byte) status });
		Intent intentOTA = new Intent(ACTION_OTA_DATA_AVAILABLE);
		intentOTA.putExtras(bundle);
		mContext.sendBroadcast(intentOTA);
	}

	public static void connect(String address, String devicename,
			Context context) {
		mContext = context;
		if (mBluetoothAdapter != null && address != null) {
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			if (device != null) {
				mBluetoothGatt = device.connectGatt(context, false,
						mGattCallback);
				if (BLEUtils.getBooleanSharedPreference(mContext,
						Constants.PREF_PAIR_CACHE_STATUS)) {
					refreshDeviceCache(mBluetoothGatt);
				}
				mBluetoothDeviceAddress = address;
				mBluetoothDeviceName = devicename;
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ devicename
						+ "|"
						+ address
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_connection_request));
			}
		}
	}

	public static void reconnect() {
		BLELogger.e("<--Reconnecting device-->");
		BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(mBluetoothDeviceAddress);
		if (device != null) {
			mBluetoothGatt = null;

			mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ mBluetoothDeviceName
					+ "|"
					+ mBluetoothDeviceAddress
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_connection_request));
		}
	}

	public static void reDiscoverServices() {
		BLELogger.e("<--Rediscovering services-->");
		BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(mBluetoothDeviceAddress);
		if (device != null) {
			if (mBluetoothGatt != null) {
				mBluetoothGatt.disconnect();
			}
			mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ mBluetoothDeviceName
					+ "|"
					+ mBluetoothDeviceAddress
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_connection_request));
		}
	}

	public static boolean refreshDeviceCache(BluetoothGatt gatt) {
		BluetoothGatt localBluetoothGatt = gatt;
		try {
			Method localMethod = localBluetoothGatt.getClass().getMethod(
					"refresh", new Class[0]);
			if (localMethod != null) {
				return ((Boolean) localMethod.invoke(localBluetoothGatt,
						new Object[0])).booleanValue();
			}
		} catch (Exception e) {
			BLELogger.i("An exception occured while refreshing device");
		}
		return false;
	}

	public static void disconnect() {
		BLELogger.i("disconnect called");
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			if (BLEUtils.getBooleanSharedPreference(mContext,
					Constants.PREF_PAIR_CACHE_STATUS)) {
				refreshDeviceCache(mBluetoothGatt);
			}
			mBluetoothGatt.disconnect();
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ mBluetoothDeviceName
					+ "|"
					+ mBluetoothDeviceAddress
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_disconnection_request));
			close();
		}
	}

	public static void discoverServices() {
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			mBluetoothGatt.discoverServices();
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ mBluetoothDeviceName
					+ "|"
					+ mBluetoothDeviceAddress
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_service_discovery_request));
		}
	}

	public static void readCharacteristic(
			BluetoothGattCharacteristic characteristic) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			mBluetoothGatt.readCharacteristic(characteristic);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ serviceName
					+ "|"
					+ characteristicName
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_characteristic_read_request));
		}
	}

	public static void readDescriptor(BluetoothGattDescriptor descriptor) {
		String serviceName = GattAttributes.lookupUUID(descriptor
				.getCharacteristic().getService().getUuid(), descriptor
				.getCharacteristic().getService().getUuid().toString());
		String characteristicName = GattAttributes.lookupUUID(descriptor
				.getCharacteristic().getUuid(), descriptor.getCharacteristic()
				.getUuid().toString());
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			mBluetoothGatt.readDescriptor(descriptor);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ serviceName
					+ "|"
					+ characteristicName
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_characteristic_read_request));
		}
	}

	public static void writeCharacteristicNoresponse(
			BluetoothGattCharacteristic characteristic, byte[] byteArray) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		String characteristicValue = BLEUtils.ByteArraytoHex(byteArray);
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			characteristic.setValue(byteArray);
			mBluetoothGatt.writeCharacteristic(characteristic);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ serviceName
					+ "|"
					+ characteristicName
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_characteristic_write_request)
					+ mContext.getResources().getString(
							R.string.dl_commaseparator)
					+ "[ "
					+ characteristicValue + " ]");
		}
	}

	public static void writeOTABootLoaderCommand(
			BluetoothGattCharacteristic characteristic, byte[] value,
			boolean isExitBootloaderCmd) {
		synchronized (mGattCallback) {
			writeOTABootLoaderCommand(characteristic, value);
			if (isExitBootloaderCmd) {
				mOtaExitBootloaderCmdInProgress = true;
			}
		}
	}

	public static void writeOTABootLoaderCommand(
			BluetoothGattCharacteristic characteristic, byte[] value) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		String characteristicValue = BLEUtils.ByteArraytoHex(value);
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			boolean status;
			int counter = 0;
			characteristic.setValue(value);
			int counter2 = Constants.TEXT_SIZE_HDPI;
			while (true) {
				characteristic.setWriteType(STATE_CONNECTED);
				status = mBluetoothGatt.writeCharacteristic(characteristic);
				if (!status) {
					Log.v("CYSMART", "writeCharacteristic() status: False");
					try {
						Log.v("CYSMART OTA SLEEP>>>>", Util.VERSION_NAME
								+ STATE_DISCONNECTED);
						int i = 0 + 1;
						Thread.sleep(100, STATE_DISCONNECTED);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (status) {
					break;
				}
				counter = counter2 - 1;
				if (counter2 <= 0) {
					break;
				}
				counter2 = counter;
			}
			counter2 = counter;
			if (status) {
				String dataLog = mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ serviceName
						+ "|"
						+ characteristicName
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_write_request)
						+ mContext.getResources().getString(
								R.string.dl_commaseparator)
						+ "[ "
						+ characteristicValue + " ]";
				BLELogger.datalog(dataLog);
				Log.v("CYSMART", dataLog);
				return;
			}
			Log.v("CYSMART", "writeOTABootLoaderCommand failed!");
		}
	}

	private static String getHexValue(byte[] array) {
		StringBuffer sb = new StringBuffer();
		int len$ = array.length;
		for (int i$ = STATE_DISCONNECTED; i$ < len$; i$++) {
			sb.append(String.format("%02x",
					new Object[] { Byte.valueOf(array[i$]) }));
		}
		return Util.VERSION_NAME + sb;
	}

	public static void writeCharacteristicGattDb(
			BluetoothGattCharacteristic characteristic, byte[] byteArray) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		String characteristicValue = BLEUtils.ByteArraytoHex(byteArray);
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			characteristic.setValue(byteArray);
			mBluetoothGatt.writeCharacteristic(characteristic);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ serviceName
					+ "|"
					+ characteristicName
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_characteristic_write_request)
					+ mContext.getResources().getString(
							R.string.dl_commaseparator)
					+ "[ "
					+ characteristicValue + " ]");
		}
	}

	public static final boolean writeCharacteristic(
			BluetoothGattCharacteristic characteristic) {
		BluetoothGatt gatt = mBluetoothGatt;
		if (gatt == null || characteristic == null
				|| (characteristic.getProperties() & 12) == 0) {
			return false;
		}
		BLELogger.v("Writing characteristic " + characteristic.getUuid());
		BLELogger.d("gatt.writeCharacteristic(" + characteristic.getUuid()
				+ ")");
		return gatt.writeCharacteristic(characteristic);
	}

	public static void writeCharacteristicRGB(
			BluetoothGattCharacteristic characteristic, int red, int green,
			int blue, int intensity) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			byte[] valueByte = new byte[] { (byte) red, (byte) green,
					(byte) blue, (byte) intensity };
			characteristic.setValue(valueByte);
			String characteristicValue = BLEUtils.ByteArraytoHex(valueByte);
			mBluetoothGatt.writeCharacteristic(characteristic);
			BLELogger.datalog(mContext.getResources().getString(
					R.string.dl_commaseparator)
					+ "["
					+ serviceName
					+ "|"
					+ characteristicName
					+ "] "
					+ mContext.getResources().getString(
							R.string.dl_characteristic_write_request)
					+ mContext.getResources().getString(
							R.string.dl_commaseparator)
					+ "[ "
					+ characteristicValue + " ]");
		}
	}

	public static void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		String descriptorName = GattAttributes.lookupUUID(
				UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG,
				GattAttributes.CLIENT_CHARACTERISTIC_CONFIG);
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			if (characteristic.getDescriptor(UUID
					.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
				BluetoothGattDescriptor descriptor;
				if (enabled) {
					descriptor = characteristic
							.getDescriptor(UUID
									.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
					descriptor
							.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
					mBluetoothGatt.writeDescriptor(descriptor);
					BLELogger
							.datalog(mContext.getResources().getString(
									R.string.dl_commaseparator)
									+ "["
									+ serviceName
									+ "|"
									+ characteristicName
									+ "|"
									+ descriptorName
									+ "] "
									+ mContext
											.getResources()
											.getString(
													R.string.dl_characteristic_write_request)
									+ mContext.getResources().getString(
											R.string.dl_commaseparator)
									+ "["
									+ BLEUtils
											.ByteArraytoHex(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
									+ "]");
				} else {
					descriptor = characteristic
							.getDescriptor(UUID
									.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
					descriptor
							.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
					mBluetoothGatt.writeDescriptor(descriptor);
					BLELogger
							.datalog(mContext.getResources().getString(
									R.string.dl_commaseparator)
									+ "["
									+ serviceName
									+ "|"
									+ characteristicName
									+ "|"
									+ descriptorName
									+ "] "
									+ mContext
											.getResources()
											.getString(
													R.string.dl_characteristic_write_request)
									+ mContext.getResources().getString(
											R.string.dl_commaseparator)
									+ "["
									+ BLEUtils
											.ByteArraytoHex(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
									+ "]");
				}
			}
			mBluetoothGatt.setCharacteristicNotification(characteristic,
					enabled);
			if (enabled) {
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ serviceName
						+ "|"
						+ characteristicName
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_start_notification));
			} else {
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ serviceName
						+ "|"
						+ characteristicName
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_stop_notification));
			}
		}
	}

	public static void setCharacteristicIndication(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		String serviceName = GattAttributes.lookupUUID(characteristic
				.getService().getUuid(), characteristic.getService().getUuid()
				.toString());
		String characteristicName = GattAttributes.lookupUUID(
				characteristic.getUuid(), characteristic.getUuid().toString());
		String descriptorName = GattAttributes.lookupUUID(
				UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG,
				GattAttributes.CLIENT_CHARACTERISTIC_CONFIG);
		if (mBluetoothAdapter != null && mBluetoothGatt != null) {
			if (characteristic.getDescriptor(UUID
					.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
				BluetoothGattDescriptor descriptor;
				if (enabled) {
					descriptor = characteristic
							.getDescriptor(UUID
									.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
					descriptor
							.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
					mBluetoothGatt.writeDescriptor(descriptor);
					BLELogger
							.datalog(mContext.getResources().getString(
									R.string.dl_commaseparator)
									+ "["
									+ serviceName
									+ "|"
									+ characteristicName
									+ "|"
									+ descriptorName
									+ "] "
									+ mContext
											.getResources()
											.getString(
													R.string.dl_characteristic_write_request)
									+ mContext.getResources().getString(
											R.string.dl_commaseparator)
									+ "["
									+ BLEUtils
											.ByteArraytoHex(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
									+ "]");
				} else {
					descriptor = characteristic
							.getDescriptor(UUID
									.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
					descriptor
							.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
					mBluetoothGatt.writeDescriptor(descriptor);
					BLELogger
							.datalog(mContext.getResources().getString(
									R.string.dl_commaseparator)
									+ "["
									+ serviceName
									+ "|"
									+ characteristicName
									+ "|"
									+ descriptorName
									+ "] "
									+ mContext
											.getResources()
											.getString(
													R.string.dl_characteristic_write_request)
									+ mContext.getResources().getString(
											R.string.dl_commaseparator)
									+ "["
									+ BLEUtils
											.ByteArraytoHex(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
									+ "]");
				}
			}
			mBluetoothGatt.setCharacteristicNotification(characteristic,
					enabled);
			if (enabled) {
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ serviceName
						+ "|"
						+ characteristicName
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_start_indication));
			} else {
				BLELogger.datalog(mContext.getResources().getString(
						R.string.dl_commaseparator)
						+ "["
						+ serviceName
						+ "|"
						+ characteristicName
						+ "] "
						+ mContext.getResources().getString(
								R.string.dl_characteristic_stop_indication));
			}
		}
	}

	public static List<BluetoothGattService> getSupportedGattServices() {
		return mBluetoothGatt == null ? null : mBluetoothGatt.getServices();
	}

	public static int getConnectionState() {
		int i;
		synchronized (mGattCallback) {
			i = mConnectionState;
		}
		return i;
	}

	public static boolean getBondedState() {
		return Boolean.valueOf(
				mBluetoothAdapter.getRemoteDevice(mBluetoothDeviceAddress)
						.getBondState() == 12).booleanValue();
	}

	public static void bondDevice() {
		try {
			BLELogger.e("Pair initates status-->"
					+ (Class.forName("android.bluetooth.BluetoothDevice")
							.getMethod("createBond", new Class[0]).invoke(
							mBluetoothGatt.getDevice(), new Object[0])));
		} catch (Exception e) {
			BLELogger.e("Exception Pair" + e.getMessage());
		}
	}

	public static void addRemoveData(BluetoothGattDescriptor descriptor) {
		switch (descriptor.getValue()[0]) {
		case STATE_DISCONNECTED:
			removeEnabledCharacteristic(descriptor.getCharacteristic());
			BLELogger.e("Removed characteristic");
		case STATE_CONNECTING:
			BLELogger.e("added notify characteristic");
			addEnabledCharacteristic(descriptor.getCharacteristic());
		case STATE_CONNECTED:
			BLELogger.e("added indicate characteristic");
			addEnabledCharacteristic(descriptor.getCharacteristic());
		default:
			break;
		}
	}

	public static void addEnabledCharacteristic(
			BluetoothGattCharacteristic bluetoothGattCharacteristic) {
		if (!mEnabledCharacteristics.contains(bluetoothGattCharacteristic)) {
			mEnabledCharacteristics.add(bluetoothGattCharacteristic);
		}
	}

	public static void removeEnabledCharacteristic(
			BluetoothGattCharacteristic bluetoothGattCharacteristic) {
		if (mEnabledCharacteristics.contains(bluetoothGattCharacteristic)) {
			mEnabledCharacteristics.remove(bluetoothGattCharacteristic);
		}
	}

	public static void disableAllEnabledCharacteristics() {
		if (mEnabledCharacteristics.size() > 0) {
			mDisableNotificationFlag = true;
			BluetoothGattCharacteristic bluetoothGattCharacteristic = mEnabledCharacteristics
					.get(STATE_DISCONNECTED);
			BLELogger.e("Disabling characteristic--"
					+ bluetoothGattCharacteristic.getUuid());
			setCharacteristicNotification(bluetoothGattCharacteristic, false);
			return;
		}
		mDisableNotificationFlag = false;
	}

	public static void enableAllRDKCharacteristics() {
		if (mRDKCharacteristics.size() > 0) {
			mEnableRDKNotificationFlag = true;
			BluetoothGattCharacteristic bluetoothGattCharacteristic = mRDKCharacteristics
					.get(STATE_DISCONNECTED);
			BLELogger.e("enabling characteristic--"
					+ bluetoothGattCharacteristic.getInstanceId());
			setCharacteristicNotification(bluetoothGattCharacteristic, true);
			return;
		}
		BLELogger.e("All RDK Chara enabled");
		mEnableRDKNotificationFlag = false;
		broadcastWritwStatusUpdate(ACTION_WRITE_COMPLETED);
	}

	public static void enableAllGlucoseCharacteristics() {
		if (mGlucoseCharacteristics.size() > 0) {
			mEnableGlucoseFlag = true;
			BluetoothGattCharacteristic bluetoothGattCharacteristic = mGlucoseCharacteristics
					.get(STATE_DISCONNECTED);
			BLELogger.e("enabling characteristic--"
					+ bluetoothGattCharacteristic);
			if (bluetoothGattCharacteristic.getUuid().equals(
					UUIDDatabase.UUID_RECORD_ACCESS_CONTROL_POINT)) {
				setCharacteristicIndication(bluetoothGattCharacteristic, true);
				BLELogger.e("RACP Indicate");
				return;
			}
			setCharacteristicNotification(bluetoothGattCharacteristic, true);
			return;
		}
		BLELogger.e("All Gluocse Char enabled");
		mEnableGlucoseFlag = false;
		broadcastWritwStatusUpdate(ACTION_WRITE_COMPLETED);
	}

	public static void close() {
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		this.mBound = true;
		return this.mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		this.mBound = false;
		close();
		return super.onUnbind(intent);
	}

	public boolean initialize() {
		boolean z = false;
		if (this.mBluetoothManager == null) {
			this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
			if (this.mBluetoothManager == null) {
				return false;
			}
		}
		mBluetoothAdapter = this.mBluetoothManager.getAdapter();
		if (mBluetoothAdapter != null) {
			Object obj = STATE_CONNECTING;
		} else {
			z = false;
		}
		return z;
	}

	@Override
	public void onCreate() {
		if (!initialize()) {
			BLELogger.d("Service not initialized");
		}
	}
}
