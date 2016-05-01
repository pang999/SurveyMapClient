package com.powerble;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.ExploreByTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.powerble.BLEConnectionServices.BluetoothLeService.LocalBinder;
import com.powerble.CommonFragments.ProfileScanningFragment;
import com.powerble.CommonFragments.ServiceDiscoveryFragment;
import com.powerble.CommonUtils.BLELogger;
import com.powerble.CommonUtils.BLEUtils;
import com.powerble.CommonUtils.Constants;
import com.surveymapclient.activity.R;

public class BLEHomePageActivity extends FragmentActivity {
	private static final int DRAWER_ABOUT = 2;
	private static final int DRAWER_BLE = 0;
	public static Boolean mApplicationInBackground;
	private static BluetoothLeService mBluetoothLeService;
	public static FrameLayout mContainerView;
	// private static DrawerLayout mParentView;
	private boolean BLUETOOTH_STATUS_FLAG;
	private String Paired;
	private String Unpaired;
	private final InputStream attachment;
	String attachmentFileName;
	private AlertDialog mAlert;
	private final BroadcastReceiver mBondStateReceiver;
	// private NavigationDrawerFragment mNavigationDrawerFragment;
	private final ServiceConnection mServiceConnection;
	private ProgressDialog mpdia;

	class AnonymousClass_8 implements OnClickListener {
		final/* synthetic */File val$sourceLocation;
		final/* synthetic */File val$targetLocation;

		AnonymousClass_8(File file, File file2) {
			this.val$sourceLocation = file;
			this.val$targetLocation = file2;
		}

		@Override
		public void onClick(DialogInterface dialog, int id) {
			try {
				BLEHomePageActivity.this.copyDirectory(this.val$sourceLocation,
						this.val$targetLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public BLEHomePageActivity() {
		this.mServiceConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName,
					IBinder service) {
				mBluetoothLeService = ((LocalBinder) service).getService();
				if (!mBluetoothLeService.initialize()) {
					BLELogger.d("Service not initialized");
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				mBluetoothLeService = null;
			}
		};
		this.attachmentFileName = "attachment.cyacd";
		this.BLUETOOTH_STATUS_FLAG = true;
		this.attachment = null;
		this.mBondStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action
						.equals("android.bluetooth.device.action.BOND_STATE_CHANGED")) {
					int state = intent.getIntExtra(
							"android.bluetooth.device.extra.BOND_STATE",
							ExploreByTouchHelper.INVALID_ID);
					int bondState = intent.getIntExtra(
							"android.bluetooth.device.extra.BOND_STATE", -1);
					int previousBondState = intent
							.getIntExtra(
									"android.bluetooth.device.extra.PREVIOUS_BOND_STATE",
									-1);
					if (state == 11) {
						BLELogger
								.datalog(BLEHomePageActivity.this.getResources()
										.getString(R.string.dl_commaseparator)
										+ "["
										+ ProfileScanningFragment.mDeviceName
										+ "|"
										+ ProfileScanningFragment.mDeviceAddress
										+ "] "
										+ BLEHomePageActivity.this
												.getResources()
												.getString(
														R.string.dl_connection_pairing_request));
						BLEUtils.bondingProgressDialog(BLEHomePageActivity.this,
								BLEHomePageActivity.this.mpdia, true);
					} else if (state == 12) {
						BLELogger.e("HomePageActivitypageActivity--->Bonded");
						BLEUtils.stopDialogTimer();
						if (ProfileScanningFragment.mPairButton != null) {
							ProfileScanningFragment.mPairButton
									.setText(BLEHomePageActivity.this.Paired);
							if (bondState == 12 && previousBondState == 11) {
								Toast.makeText(
										BLEHomePageActivity.this,
										BLEHomePageActivity.this.getResources()
												.getString(
														R.string.toast_paired),
										0).show();
							}
						}
						BLELogger.datalog(BLEHomePageActivity.this.getResources()
								.getString(R.string.dl_commaseparator)
								+ "["
								+ ProfileScanningFragment.mDeviceName
								+ "|"
								+ ProfileScanningFragment.mDeviceAddress
								+ "] "
								+ BLEHomePageActivity.this.getResources()
										.getString(
												R.string.dl_connection_paired));
						BLEUtils.bondingProgressDialog(BLEHomePageActivity.this,
								BLEHomePageActivity.this.mpdia, false);
					} else if (state == 10) {
						BLELogger
								.e("HomePageActivitypageActivity--->Not Bonded");
						BLEUtils.stopDialogTimer();
						if (ProfileScanningFragment.mPairButton != null) {
							ProfileScanningFragment.mPairButton
									.setText(BLEHomePageActivity.this.Unpaired);
							if (bondState == 10 && previousBondState == 12) {
								Toast.makeText(
										BLEHomePageActivity.this,
										BLEHomePageActivity.this
												.getResources()
												.getString(
														R.string.toast_unpaired),
										0).show();
							} else {
								Toast.makeText(
										BLEHomePageActivity.this,
										BLEHomePageActivity.this
												.getResources()
												.getString(
														R.string.dl_connection_pairing_unsupported),
										1).show();
							}
						}
						BLELogger
								.datalog(BLEHomePageActivity.this.getResources()
										.getString(R.string.dl_commaseparator)
										+ "["
										+ ProfileScanningFragment.mDeviceName
										+ "|"
										+ ProfileScanningFragment.mDeviceAddress
										+ "] "
										+ BLEHomePageActivity.this
												.getResources()
												.getString(
														R.string.dl_connection_pairing_unsupported));
						BLEUtils.bondingProgressDialog(BLEHomePageActivity.this,
								BLEHomePageActivity.this.mpdia, false);
					} else {
						BLELogger.e("Error received in pair-->" + state);
					}
				} else if (action
						.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
					BLELogger.i("BluetoothAdapter.ACTION_STATE_CHANGED.");
					if (intent.getIntExtra(
							"android.bluetooth.adapter.extra.STATE", -1) == 10) {
						BLELogger.i("BluetoothAdapter.STATE_OFF");
						if (BLEHomePageActivity.this.BLUETOOTH_STATUS_FLAG) {
							BLEHomePageActivity.this
									.connectionLostBluetoothalertbox(Boolean
											.valueOf(true));
						}
					} else if (intent.getIntExtra(
							"android.bluetooth.adapter.extra.STATE", -1) == 12) {
						BLELogger.i("BluetoothAdapter.STATE_ON");
						if (BLEHomePageActivity.this.BLUETOOTH_STATUS_FLAG) {
							BLEHomePageActivity.this
									.connectionLostBluetoothalertbox(Boolean
											.valueOf(false));
						}
					}
				} else if (action
						.equals(BluetoothLeService.ACTION_PAIR_REQUEST)) {
					BLELogger.e("Pair request received");
					BLELogger.e("HomePageActivitypageActivity--->Pair Request");
					BLEUtils.stopDialogTimer();
				}
			}
		};
	}

	static {
		mApplicationInBackground = Boolean.valueOf(false);
	}

	private static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isTablet(this)) {
			BLELogger.d("Tablet");
			setRequestedOrientation(-1);
		} else {
			BLELogger.d("Phone");
			setRequestedOrientation(1);
		}
		setContentView(R.layout.activity_main);
		this.Paired = getResources().getString(R.string.bluetooth_pair);
		this.Unpaired = getResources().getString(R.string.bluetooth_unpair);
		// mParentView = (DrawerLayout) findViewById(R.id.drawer_layout);
		mContainerView = (FrameLayout) findViewById(R.id.container);
		this.mpdia = new ProgressDialog(this);
		this.mpdia.setCancelable(false);
		this.mAlert = new Builder(this).create();
		this.mAlert.setMessage(getResources().getString(
				R.string.alert_message_bluetooth_reconnect));
		this.mAlert.setCancelable(false);
		this.mAlert.setTitle(getResources().getString(R.string.app_name));
		this.mAlert.setButton(-1,
				getResources().getString(R.string.alert_message_exit_ok),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intentActivity = BLEHomePageActivity.this
								.getIntent();
						BLEHomePageActivity.this.finish();
						BLEHomePageActivity.this.overridePendingTransition(
								R.anim.slide_left, R.anim.push_left);
						BLEHomePageActivity.this.startActivity(intentActivity);
						BLEHomePageActivity.this.overridePendingTransition(
								R.anim.slide_right, R.anim.push_right);
					}
				});
		this.mAlert.setCanceledOnTouchOutside(false);
		getTitle();
		// this.mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
		// (DrawerLayout) findViewById(R.id.drawer_layout));
		if (!BLEUtils.ifContainsSharedPreference(this,
				Constants.PREF_PAIR_CACHE_STATUS)) {
			BLEUtils.setBooleanSharedPreference(this,
					Constants.PREF_PAIR_CACHE_STATUS, true);
		}
		startService(new Intent(getApplicationContext(),
				BluetoothLeService.class));
		displayView(new ProfileScanningFragment(),
				Constants.PROFILE_SCANNING_FRAGMENT_TAG);

	}

	public void connectionLostBluetoothalertbox(Boolean status) {
		if (status.booleanValue()) {
			this.mAlert.show();
		} else if (this.mAlert != null && this.mAlert.isShowing()) {
			this.mAlert.dismiss();
		}
	}

	@Override
	protected void onPause() {
		getIntent().setData(null);
		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentById(R.id.container);

		mApplicationInBackground = Boolean.valueOf(true);
		this.BLUETOOTH_STATUS_FLAG = false;
		unregisterReceiver(this.mBondStateReceiver);

		super.onPause();
	}

	@Override
	protected void onResume() {
		BLELogger.e("onResume-->activity");

		mApplicationInBackground = Boolean.valueOf(false);
		this.BLUETOOTH_STATUS_FLAG = true;
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
		intentFilter
				.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
		intentFilter
				.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
		intentFilter.addAction(BluetoothLeService.ACTION_PAIR_REQUEST);
		registerReceiver(this.mBondStateReceiver, intentFilter);
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		BLELogger.e("newIntent");
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onBackPressed() {
		Fragment currentFragment = getSupportFragmentManager()
				.findFragmentById(R.id.container);
		if (currentFragment instanceof ProfileScanningFragment) {
			finish();
		} else if ((currentFragment instanceof ServiceDiscoveryFragment)) {

			// /////////////////////////////////////////////////////////退到扫描界面就disconnect
			/*
			 * if (BluetoothLeService.getConnectionState() == 2 ||
			 * BluetoothLeService.getConnectionState() == 1 ||
			 * BluetoothLeService.getConnectionState() == 4) { Util.e("discon",
			 * "---2"); BluetoothLeService.disconnect(); Toast.makeText(this,
			 * "BLESevice is disconnect", 0).show(); }
			 */

			finish();
			overridePendingTransition(R.anim.slide_right, R.anim.push_right);
			startActivity(getIntent());
			overridePendingTransition(R.anim.slide_left, R.anim.push_left);
			super.onBackPressed();
		} else {
			BLEUtils.setUpActionBar(this,
					getResources().getString(R.string.profile_control_fragment));
			super.onBackPressed();
		}
	}

	public void onNavigationDrawerItemSelected(int position) {
		BLELogger.e("onNavigationDrawerItemSelected " + position);
		switch (position) {
		case Constants.ZOOM_AXIS_XY:
			if (BluetoothLeService.getConnectionState() == 2
					|| BluetoothLeService.getConnectionState() == 1
					|| BluetoothLeService.getConnectionState() == 4) {
				Util.e("discon", "---3");
				BluetoothLeService.disconnect();
			}
			Intent intent = getIntent();
			finish();
			overridePendingTransition(R.anim.slide_left, R.anim.push_left);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right, R.anim.push_right);
		case DRAWER_ABOUT:
			// displayView(new AboutFragment(), Constants.ABOUT_FRAGMENT_TAG);
		default:
			break;
		}
	}

	void displayView(Fragment fragment, String tag) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment, tag).commit();
	}

	/* JADX WARNING: inconsistent code. */
	/* Code decompiled incorrectly, please refer to instructions dump. */

	public boolean fileExists(String name, File file) {
		File[] list = file.listFiles();
		if (list != null) {
			for (File fil : list) {
				if (fil.isDirectory()) {
					fileExists(name, fil);
				} else if (name.equalsIgnoreCase(fil.getName())) {
					BLELogger.e("File>>" + fil.getName());
					return true;
				}
			}
		}
		return false;
	}

	public void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}
			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
			return;
		}
		InputStream in = new FileInputStream(sourceLocation.getAbsolutePath());
		OutputStream out = new FileOutputStream(
				targetLocation.getAbsolutePath());
		byte[] buf = new byte[1024];
		while (true) {
			int len = in.read(buf);
			if (len > 0) {
				out.write(buf, 0, len);
			} else {
				in.close();
				out.close();
				getIntent().setData(null);
				return;
			}
		}
	}

	void alertbox() {
		Builder builder = new Builder(this);
		builder.setMessage(
				getResources().getString(R.string.alert_message_exit))
				.setCancelable(false)
				.setTitle(getResources().getString(R.string.app_name))
				.setPositiveButton(
						getResources()
								.getString(R.string.alert_message_exit_ok),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								BLEHomePageActivity.this.finish();
								BLEHomePageActivity.this.stopService(new Intent(
										BLEHomePageActivity.this
												.getApplicationContext(),
										BluetoothLeService.class));
							}
						})
				.setNegativeButton(
						getResources().getString(
								R.string.alert_message_exit_cancel),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}

	private void unpairDevice(BluetoothDevice device) {
		try {
			device.getClass().getMethod("removeBond", (Class[]) null)
					.invoke(device, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showWarningMessage() {
		Builder builder = new Builder(this);
		builder.setMessage(getString(R.string.alert_message_clear_cache))
				.setTitle(getString(R.string.alert_title_clear_cache))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.alert_message_exit_ok),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								if (BluetoothLeService.mBluetoothGatt != null) {
									BluetoothLeService
											.refreshDeviceCache(BluetoothLeService.mBluetoothGatt);
								}
								Util.e("discon", "---4");
								BluetoothLeService.disconnect();
								Toast.makeText(
										BLEHomePageActivity.this.getBaseContext(),
										"BLE Service is disconnect", 0).show();
								Intent HomePageActivityPage = BLEHomePageActivity.this
										.getIntent();
								BLEHomePageActivity.this.finish();
								BLEHomePageActivity.this
										.overridePendingTransition(
												R.anim.slide_right,
												R.anim.push_right);
								BLEHomePageActivity.this
										.startActivity(HomePageActivityPage);
								BLEHomePageActivity.this
										.overridePendingTransition(
												R.anim.slide_left,
												R.anim.push_left);
							}
						})
				.setNegativeButton(
						getString(R.string.alert_message_exit_cancel), null);
		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public int scan = 0;

	
	
	
	
	
	
	
	
	
	public void onClickBack(View v){
		finish();
	}
	
	
	
	
	
}
