package com.powerble.CommonFragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.powerble.Util;
import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.CommonUtils.BLELogger;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.Constants;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.base.MyApplication;

/**
 * @Description(描述): 设备扫描界面
 * @Package(包名): com.powerble.CommonFragments
 * @ClassName(类名): ProfileScanningFragment
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午2:42:43
 * @version(版本): V1.0
 */
public class ProfileScanningFragment extends Fragment {
	private static final long CONNECTION_TIMEOUT = 10000;
	private static final long DELAY_PERIOD = 500;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD_TIMEOUT = 2000;
	public static boolean isInFragment;
	private static BluetoothAdapter mBluetoothAdapter;
	public static String mDeviceAddress;
	public static String mDeviceName;
	private static ArrayList<BluetoothDevice> mLeDevices;
	public static Button mPairButton;
	private Timer mConnectTimer;
	private boolean mConnectTimerON;
	private Map<String, Integer> mDevRssiValues;
	private final BroadcastReceiver mGattConnectReceiver;
	private LeDeviceListAdapter mLeDeviceListAdapter;
	private final LeScanCallback mLeScanCallback;
	private ListView mProfileListView;
	private ProgressDialog mProgressdialog;
	private TextView mRefreshText;
	private Timer mScanTimer;
	private boolean mScanning;
	private boolean mSearchEnabled;
	private SwipeRefreshLayout mSwipeLayout;
	private final TextWatcher textWatcher;

	@SuppressLint("NewApi")
	class AnonymousClass_7 implements OnActionExpandListener {
		final/* synthetic */EditText val$mEditText;

		AnonymousClass_7(EditText editText) {
			val$mEditText = editText;
		}

		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			View view = getActivity().getCurrentFocus();
			if (view != null) {
				((InputMethodManager) getActivity().getSystemService(
						"input_method")).hideSoftInputFromWindow(
						view.getWindowToken(), 0);
			}
			BLELogger.e("Collapsed");
			return true;
		}

		@Override
		public boolean onMenuItemActionExpand(MenuItem item) {
			BLELogger.e("Expanded");
			val$mEditText.requestFocus();
			View view = getActivity().getCurrentFocus();
			if (view != null) {
				((InputMethodManager) getActivity().getSystemService(
						"input_method")).showSoftInput(view, REQUEST_ENABLE_BT);
			}
			return true;
		}
	}

	private class LeDeviceListAdapter extends BaseAdapter implements Filterable {
		private final ItemFilter mFilter;
		ArrayList<BluetoothDevice> mFilteredDevices;
		private final LayoutInflater mInflator;
		private int rssiValue;

		class AnonymousClass_1 implements OnClickListener {
			final/* synthetic */BluetoothDevice val$device;

			AnonymousClass_1(BluetoothDevice bluetoothDevice) {
				val$device = bluetoothDevice;
			}

			@Override
			public void onClick(View view) {
				mPairButton = (Button) view;
				mDeviceAddress = val$device.getAddress();
				mDeviceName = val$device.getName();
				if (mPairButton
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources().getString(
										R.string.bluetooth_pair))) {
					unpairDevice(val$device);
				} else {
					pairDevice(val$device);
				}
			}
		}

		private class ItemFilter extends Filter {
			private ItemFilter() {
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String mFilterString = constraint.toString().toLowerCase();
				FilterResults mResults = new FilterResults();
				ArrayList<BluetoothDevice> list = mLeDevices;
				int count = list.size();
				ArrayList<BluetoothDevice> nlist = new ArrayList(count);
				int i = 0;
				while (i < count) {
					if (list.get(i).getName() != null
							&& list.get(i).getName().toLowerCase()
									.contains(mFilterString)) {
						nlist.add(list.get(i));
					}
					i++;
				}
				mResults.values = nlist;
				mResults.count = nlist.size();
				return mResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				mFilteredDevices = (ArrayList) results.values;
				clear();
				int count = mFilteredDevices.size();
				for (int i = 0; i < count; i++) {
					mLeDeviceListAdapter.addDevice(mFilteredDevices.get(i),
							mLeDeviceListAdapter.getRssiValue());
					notifyDataSetChanged();
				}
			}
		}

		public LeDeviceListAdapter() {
			mFilteredDevices = new ArrayList();
			mFilter = new ItemFilter();
			mLeDevices = new ArrayList();
			mInflator = getActivity().getLayoutInflater();
		}

		private void addDevice(BluetoothDevice device, int rssi) {
			rssiValue = rssi;
			if (mLeDevices.contains(device)) {
				mDevRssiValues.put(device.getAddress(), Integer.valueOf(rssi));
				return;
			}
			mDevRssiValues.put(device.getAddress(), Integer.valueOf(rssi));
			mLeDevices.add(device);
		}

		public int getRssiValue() {
			return rssiValue;
		}

		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		public void clear() {
			mLeDevices.clear();
			BluetoothDevice b = getApp().getCurrDevice();
			if (b != null)
				mLeDevices.add(b);
		}

		@Override
		public int getCount() {
			return mLeDevices.size();
		}

		@Override
		public Object getItem(int i) {
			return mLeDevices.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, viewGroup,
						false);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view
						.findViewById(R.id.device_address);
				viewHolder.is_connect = (TextView) view
						.findViewById(R.id.is_connect);
				viewHolder.deviceName = (TextView) view
						.findViewById(R.id.device_name);
				viewHolder.deviceRssi = (TextView) view
						.findViewById(R.id.device_rssi);
				viewHolder.pairStatus = (Button) view
						.findViewById(R.id.btn_pair);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			BluetoothDevice device = mLeDevices.get(position);
			BluetoothDevice d = getApp().getCurrDevice();
			if (d != null) {
				if (device.getAddress().equals(d.getAddress())) {
					viewHolder.is_connect.setText("connected");
					viewHolder.is_connect.setTextColor(Color
							.parseColor("#00ff00"));
				}

				else {
					viewHolder.is_connect.setText("no connected");
					viewHolder.is_connect.setTextColor(Color
							.parseColor("#adadad"));
				}

			} else {
				viewHolder.is_connect.setText("no connected");
				viewHolder.is_connect.setTextColor(Color.parseColor("#adadad"));
			}
			String deviceName = device.getName();
			if (deviceName == null || deviceName.length() <= 0) {
				viewHolder.deviceName.setText(R.string.device_unknown);
				viewHolder.deviceName.setSelected(true);
				viewHolder.deviceAddress.setText(device.getAddress());
			} else {
				try {
					viewHolder.deviceName.setText(deviceName);
					viewHolder.deviceAddress.setText("Address: "
							+ device.getAddress());
					byte rssival = (byte) mDevRssiValues.get(
							device.getAddress()).intValue();
					if (rssival != 0) {
						viewHolder.deviceRssi.setText("signal: "
								+ String.valueOf(rssival) + " dbm");
					}
					viewHolder.pairStatus
							.setText(device.getBondState() == 12 ? getActivity()
									.getResources().getString(
											R.string.bluetooth_pair)
									: getActivity().getResources().getString(
											R.string.bluetooth_unpair));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			viewHolder.pairStatus.setOnClickListener(new AnonymousClass_1(
					device));
			return view;
		}

		@Override
		public Filter getFilter() {
			return mFilter;
		}

		class ViewHolder {
			TextView deviceAddress;
			TextView deviceName;
			TextView deviceRssi;
			Button pairStatus;
			TextView is_connect;

		}
	}

	@SuppressLint("NewApi")
	public ProfileScanningFragment() {
		mConnectTimerON = false;
		mSearchEnabled = false;
		mLeScanCallback = new LeScanCallback() {

			class AnonymousClass_1 implements Runnable {
				final/* synthetic */BluetoothDevice val$device;
				final/* synthetic */int val$rssi;

				AnonymousClass_1(BluetoothDevice bluetoothDevice, int i) {
					val$device = bluetoothDevice;
					val$rssi = i;
				}

				@Override
				public void run() {
					Util.e("Scan CallBack", "---xx");
					if (!mSearchEnabled) {
						mLeDeviceListAdapter.addDevice(val$device, val$rssi);
						try {
							mLeDeviceListAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			public void onLeScan(BluetoothDevice device, int rssi,
					byte[] scanRecord) {
				Activity mActivity = getActivity();
				if (mActivity != null) {
					mActivity.runOnUiThread(new AnonymousClass_1(device, rssi));
				}
			}
		};
		mGattConnectReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
					mProgressdialog
							.setMessage(getString(R.string.alert_message_bluetooth_connect));
					if (mScanning) {
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
						mScanning = false;
					}
					mProgressdialog.dismiss();
					mLeDevices.clear();
					if (mConnectTimer != null) {
						mConnectTimer.cancel();
					}
					mConnectTimerON = false;
					updateWithNewFragment();
				} else if (!BluetoothLeService.ACTION_GATT_DISCONNECTED
						.equals(action)) {
				} else {
					if (mConnectTimerON) {
						BluetoothLeService.reconnect();
					} else {
						Toast.makeText(getActivity(),
								R.string.profile_cannot_connect_message, 0)
								.show();
					}
				}
			}
		};
		textWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mLeDeviceListAdapter.notifyDataSetInvalidated();
				mLeDeviceListAdapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
	}

	static {
		mDeviceName = "name";
		mDeviceAddress = "address";
		isInFragment = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		BLELogger.e("Scanning onResume");
		isInFragment = true;
		if (checkBluetoothStatus()) {
			prepareList();
		}
		BLELogger.e("Registering receiver in Profile scannng");
		getActivity().registerReceiver(mGattConnectReceiver,
				BLEUtils.makeGattUpdateIntentFilter());

		if (getApp().getCurrDevice() != null) {
			mLeDeviceListAdapter.addDevice(getApp().getCurrDevice(), 0);
			try {
				mLeDeviceListAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Util.e("fragment", "profilescan");
		View mrootView = inflater.inflate(R.layout.fragment_profile_scan,
				container, false);
		mDevRssiValues = new HashMap();
		mSwipeLayout = (SwipeRefreshLayout) mrootView
				.findViewById(R.id.swipe_container);
		mSwipeLayout.setColorScheme(R.color.dark_blue, R.color.medium_blue,
				R.color.light_blue, R.color.faint_blue);
		mProfileListView = (ListView) mrootView
				.findViewById(R.id.listView_profiles);
		mRefreshText = (TextView) mrootView.findViewById(R.id.no_dev);
		mLeDeviceListAdapter = new LeDeviceListAdapter();
		mProfileListView.setAdapter(mLeDeviceListAdapter);
		mProfileListView.setTextFilterEnabled(true);
		setHasOptionsMenu(true);
		mProgressdialog = new ProgressDialog(getActivity());
		mProgressdialog.setCancelable(false);
		checkBleSupportAndInitialize();
		prepareList();
		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (!mScanning) {
					if (mLeDeviceListAdapter != null) {
						mLeDeviceListAdapter.clear();
						try {
							mLeDeviceListAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mScanning = false;
					scanLeDevice1();
					mScanning = true;
					mSearchEnabled = false;
					mRefreshText.setText(getResources().getString(
							R.string.profile_control_device_scanning));
				}
			}
		});
		BLELogger.createDataLoggerFile(getActivity());
		mProfileListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long l) {

				if (mLeDeviceListAdapter.getCount() > 0) {
					BluetoothDevice device = mLeDeviceListAdapter
							.getDevice(position);
					if (device != null) {
						stopScanLeDevice();
						connectDevice(device, true);
						getApp().setCurrDevices(device);
					}
				}
			}
		});
		return mrootView;
	}

	@SuppressLint("NewApi")
	private void checkBleSupportAndInitialize() {
		if (!getActivity().getPackageManager().hasSystemFeature(
				"android.hardware.bluetooth_le")) {
			Toast.makeText(getActivity(), R.string.device_ble_not_supported, 0)
					.show();
			getActivity().finish();
		}
		mBluetoothAdapter = ((BluetoothManager) getActivity().getSystemService(
				"bluetooth")).getAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getActivity(),
					R.string.device_bluetooth_not_supported, 0).show();
			getActivity().finish();
		}
	}

	private void connectDevice(BluetoothDevice device, boolean isFirstConnect) {
		mDeviceAddress = device.getAddress();
		mDeviceName = device.getName();
		if (BluetoothLeService.getConnectionState() == 0) {
			BLELogger.v("BLE DISCONNECTED STATE");
			BluetoothLeService.connect(mDeviceAddress, mDeviceName,
					getActivity());
			showConnectAlertMessage(mDeviceName, mDeviceAddress);
		} else {
			BLELogger.v("BLE OTHER STATE-->"
					+ BluetoothLeService.getConnectionState());
			Util.e("discon", "---5");
			BluetoothLeService.disconnect();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					BluetoothLeService.connect(mDeviceAddress, mDeviceName,
							getActivity());
					showConnectAlertMessage(mDeviceName, mDeviceAddress);
				}
			}, DELAY_PERIOD);
		}
		if (isFirstConnect) {
			startConnectTimer();
			mConnectTimerON = true;
		}
	}

	@SuppressLint("NewApi")
	private void showConnectAlertMessage(String devicename, String deviceaddress) {
		mProgressdialog.setTitle(getResources().getString(
				R.string.alert_message_connect_title));
		mProgressdialog.setMessage(getResources().getString(
				R.string.alert_message_connect)
				+ "\n"
				+ devicename
				+ "\n"
				+ deviceaddress
				+ "\n"
				+ getResources().getString(R.string.alert_message_wait));
		if (!getActivity().isDestroyed() && mProgressdialog != null) {
			mProgressdialog.show();
		}
	}

	@SuppressLint("NewApi")
	private void scanLeDevice1() {
		if (!mScanning) {
			/** 2s 后停止 */
			new Handler().postDelayed(new Runnable() {

				@SuppressLint("NewApi")
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					mRefreshText.setText("no devices");
					mSwipeLayout.setRefreshing(false);
					if (mLeDeviceListAdapter.getCount() == 0) {
						mRefreshText.setVisibility(View.VISIBLE);

					} else
						mRefreshText.setVisibility(View.GONE);
				}
			}, SCAN_PERIOD_TIMEOUT);

			mScanning = true;
			mRefreshText.setText(getResources().getString(
					R.string.profile_control_device_scanning));
			mBluetoothAdapter.startLeScan(mLeScanCallback);
			mSwipeLayout.setRefreshing(true);
		}
	}

	@SuppressLint("NewApi")
	private void stopScanLeDevice() {
		mScanning = false;
		mSwipeLayout.setRefreshing(false);
		mBluetoothAdapter.stopLeScan(mLeScanCallback);

	}

	public void prepareList() {
		setUpActionBar();
		mLeDeviceListAdapter = new LeDeviceListAdapter();
		mProfileListView.setAdapter(mLeDeviceListAdapter);
		scanLeDevice1();
		mSearchEnabled = false;
	}

	@Override
	public void onPause() {
		BLELogger.e("Scanning onPause");
		isInFragment = false;
		if (mProgressdialog != null && mProgressdialog.isShowing()) {
			mProgressdialog.dismiss();
		}
		BLELogger.e("UN Registering receiver in Profile scannng");
		getActivity().unregisterReceiver(mGattConnectReceiver);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopScanLeDevice();
		isInFragment = false;
		if (mLeDeviceListAdapter != null) {
			mLeDeviceListAdapter.clear();
		}
		if (mLeDeviceListAdapter != null) {
			try {
				mLeDeviceListAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mSwipeLayout.setRefreshing(false);
	}

	private void updateWithNewFragment() {
		if (mLeDeviceListAdapter != null) {
			mLeDeviceListAdapter.clear();
			try {
				mLeDeviceListAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// explor();

		FragmentManager fragmentManager = getFragmentManager();
		ServiceDiscoveryFragment serviceDiscoveryFragment = new ServiceDiscoveryFragment();
		fragmentManager
				.beginTransaction()
				.remove(getFragmentManager().findFragmentByTag(
						Constants.PROFILE_SCANNING_FRAGMENT_TAG)).commit();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container, serviceDiscoveryFragment,
						Constants.SERVICE_DISCOVERY_FRAGMENT_TAG).commit();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 0) {
			getActivity().finish();
		} else if (requestCode != 1) {
		} else {
			if (resultCode == -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.device_bluetooth_on),
						0).show();
				mLeDeviceListAdapter = new LeDeviceListAdapter();
				mProfileListView.setAdapter(mLeDeviceListAdapter);
				scanLeDevice1();
				return;
			}
			getActivity().finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	void setUpActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		if (actionBar != null) {
			actionBar.setIcon(new ColorDrawable(getResources().getColor(
					17170445)));
		}
		if (actionBar != null) {
			actionBar.setTitle(R.string.profile_scan_fragment);
		}
	}

	private void pairDevice(BluetoothDevice device) {
		try {
			device.getClass().getMethod("createBond", (Class[]) null)
					.invoke(device, (Object[]) null);
		} catch (Exception e) {
			if (mProgressdialog != null && mProgressdialog.isShowing()) {
				mProgressdialog.dismiss();
			}
		}
	}

	private void unpairDevice(BluetoothDevice device) {
		try {
			device.getClass().getMethod("removeBond", (Class[]) null)
					.invoke(device, (Object[]) null);
		} catch (Exception e) {
			if (mProgressdialog != null && mProgressdialog.isShowing()) {
				mProgressdialog.dismiss();
			}
		}
	}

	public boolean checkBluetoothStatus() {
		if (mBluetoothAdapter.isEnabled()) {
			return true;
		}
		startActivityForResult(new Intent(
				"android.bluetooth.adapter.action.REQUEST_ENABLE"),
				REQUEST_ENABLE_BT);
		return false;
	}

	private void startConnectTimer() {
		mConnectTimer = new Timer();
		mConnectTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mProgressdialog.dismiss();
				BLELogger.v("CONNECTION TIME OUT");
				mConnectTimerON = false;
				Util.e("discon", "---1");
				BluetoothLeService.disconnect();
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getActivity(),
									R.string.profile_cannot_connect_message, 0)
									.show();
							if (mLeDeviceListAdapter != null) {
								mLeDeviceListAdapter.clear();
							}
							if (mLeDeviceListAdapter != null) {
								try {
									mLeDeviceListAdapter.notifyDataSetChanged();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							scanLeDevice1();
							mScanning = true;
						}
					});
				}
			}
		}, CONNECTION_TIMEOUT);
	}

	public void explor() {
		MyApplication app = (MyApplication) getActivity()
				.getApplication();
		app.setBluetoothgattcharacteristic(app.getGattCharacteristics().get(1));
		String uuidWrite = "0003cdd2-0000-1000-8000-00805f9b0131";

		Bundle bundle = new Bundle();
		/*
		 * bundle.putString(Constants.GATTDB_SELECTED_SERVICE,"Unknown Service");
		 * bundle.putString(Constants.GATTDB_SELECTED_CHARACTERISTICE,
		 * uuidWrite);
		 */
		FragmentManager fragmentManager = getFragmentManager();
		// Fragment gattDetailsfragment = new GattDetailsFragment().create();
		// gattDetailsfragment.setArguments(bundle);
		// fragmentManager.beginTransaction().add((int) R.id.container,
		// gattDetailsfragment).addToBackStack(null).commit();

	}

	/**
	 * if(((CySmartApplication)getActivity().getApplication()).
	 * getBluetoothgattcharacteristic()==null)
	 * 
	 * @return
	 */

	public MyApplication getApp() {
		return (MyApplication) getActivity().getApplication();
	}
}
