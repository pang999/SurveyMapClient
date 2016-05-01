package com.powerble.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.media.TransportMediator;
import android.view.View;
import android.widget.Toast;

import com.powerble.Util;
import com.powerble.BLEConnectionServices.BluetoothLeService;
import com.surveymapclient.activity.R;

public class BLEUtils {
    private static final String SHARED_PREF_NAME = "CySmart Shared Preference";
    private static ProgressDialog mProgressDialog;
    private static Timer mTimer;

    @SuppressLint("NewApi")
	public static String getManufacturerNameString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getModelNumberString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getSerialNumberString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getHardwareRevisionString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getFirmwareRevisionString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getSoftwareRevisionString(BluetoothGattCharacteristic characteristic) {
        return characteristic.getStringValue(0);
    }

    @SuppressLint("NewApi")
	public static String getPNPID(BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        StringBuilder stringBuilder = new StringBuilder(data.length);
        if (data != null && data.length > 0) {
            int len$ = data.length;
            for (int i$ = 0; i$ < len$; i$++) {
                stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(data[i$])}));
            }
        }
        return String.valueOf(stringBuilder);
    }

    @SuppressLint("NewApi")
	public static String getSYSID(BluetoothGattCharacteristic characteristic) {
        byte[] data = characteristic.getValue();
        StringBuilder stringBuilder = new StringBuilder(data.length);
        if (data != null && data.length > 0) {
            int len$ = data.length;
            for (int i$ = 0; i$ < len$; i$++) {
                stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(data[i$])}));
            }
        }
        return String.valueOf(stringBuilder);
    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
     //   intentFilter.addAction(BootLoaderUtils.ACTION_OTA_STATUS);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTING);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED_CAROUSEL);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED_OTA);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECT_OTA);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED_OTA);
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CHARACTERISTIC_ERROR);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_SUCCESS);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_FAILED);
        intentFilter.addAction(BluetoothLeService.ACTION_PAIR_REQUEST);
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.extra.BOND_STATE");
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_COMPLETED);
        return intentFilter;
    }

    public static String ByteArraytoHex(byte[] bytes) {
        if (bytes == null) {
            return Util.VERSION_NAME;
        }
        StringBuilder sb = new StringBuilder();
        int len$ = bytes.length;
        for (int i$ = 0; i$ < len$; i$++) {
            sb.append(String.format("%02X ", new Object[]{Byte.valueOf(bytes[i$])}));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[(len / 2)];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), TransportMediator.FLAG_KEY_MEDIA_PAUSE) << 4) + Character.digit(s.charAt(i + 1), TransportMediator.FLAG_KEY_MEDIA_PAUSE));
        }
        return data;
    }

    public static String getMSB(String string) {
        StringBuilder msbString = new StringBuilder();
        for (int i = string.length(); i > 0; i -= 2) {
            msbString.append(string.substring(i - 2, i));
        }
        return msbString.toString();
    }

    public static String BytetoBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 8);
        for (int i = 0; i < bytes.length * 8; i++) {
            sb.append(((bytes[i / 8] << (i % 8)) & 128) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    
    
    
  /*  
    public static String BytetoBinary(byte byte1) {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i <  8; i++) {
            sb.append(((bytes[i / 8] << (i % 8)) & 128) == 0 ? '0' : '1');
        }
        return sb.toString();
    }*/

    
    
    
    
    
    public static byte[] convertingTobyteArray(String result) {
        String[] splited = result.split("\\s+");
        byte[] valueByte = new byte[splited.length];
        for (int i = 0; i < splited.length; i++) {
            if (splited[i].length() > 2) {
                valueByte[i] = (byte) convertstringtobyte(splited[i].split("x")[1]);
             
            }
        }
        return valueByte;
    }
    
    
    
    
    public static byte[] hexStr2Byte(String result) {
        String[] splited = result.split("\\s+");
        byte[] valueByte = new byte[splited.length];
        for (int i = 0; i < splited.length; i++) {
            if (splited[i].length() > 0) {
                valueByte[i] = (byte) convertstringtobyte(splited[i]);
             
            }
        }
        return valueByte;
    }

    private static int convertstringtobyte(String string) {
        return Integer.parseInt(string, TransportMediator.FLAG_KEY_MEDIA_PAUSE);
    }

    @SuppressLint("NewApi")
	public static String byteToASCII(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (byte byteChar : array) {
            if (byteChar < 32 || byteChar >= 127) {
                sb.append(String.format("%d ", new Object[]{Integer.valueOf(byteChar & 255)}));
            } else {
                sb.append(String.format("%c", new Object[]{Byte.valueOf(byteChar)}));
            }
        }
        return sb.toString();
    }

    @SuppressLint("NewApi")
	public static String getBatteryLevel(BluetoothGattCharacteristic characteristics) {
        return String.valueOf(characteristics.getIntValue(17, 0).intValue());
    }

    @SuppressLint("NewApi")
	public static String getAlertLevel(BluetoothGattCharacteristic characteristics) {
        return String.valueOf(characteristics.getIntValue(17, 0).intValue());
    }

    @SuppressLint("NewApi")
	public static int getTransmissionPower(BluetoothGattCharacteristic characteristics) {
        return characteristics.getIntValue(33, 0).intValue();
    }

    public static String GetDateFromLong(long date) {
        Date currentDate = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar.getInstance().setTimeInMillis(date);
        return currentDate.toString();
    }

    public static String GetDateFromMilliseconds() {
        return new SimpleDateFormat("dd MMM yyyy").format(Calendar.getInstance().getTime());
    }

    public static String GetDate() {
        return new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
    }

    public static int getTimeInSeconds() {
        return (int) System.currentTimeMillis();
    }

    public static String GetDateSevenDaysBack() {
        DateFormat formatter = new SimpleDateFormat("dd_MMM_yyyy");
        Calendar calendar = Calendar.getInstance();
      //  calendar.add(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT, -7);
        return formatter.format(calendar.getTime());
    }

    public static String GetTimeFromMilliseconds() {
        return new SimpleDateFormat("HH:mm ss SSS").format(Calendar.getInstance().getTime());
    }

    public static String GetTimeandDate() {
        return new SimpleDateFormat("[dd-MMM-yyyy|HH:mm:ss]").format(Calendar.getInstance().getTime());
    }

    public static String GetTimeandDateUpdate() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static final void setStringSharedPreference(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static final String getStringSharedPreference(Context context, String key) {
        return context != null ? context.getSharedPreferences(SHARED_PREF_NAME, 0).getString(key, Util.VERSION_NAME) : Util.VERSION_NAME;
    }

    public static final void setIntSharedPreference(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static final int getIntSharedPreference(Context context, String key) {
        return context != null ? context.getSharedPreferences(SHARED_PREF_NAME, 0).getInt(key, 0) : 0;
    }

    public static final void setBooleanSharedPreference(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static final boolean getBooleanSharedPreference(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, 0).getBoolean(key, false);
    }

    public static final void setInitialBooleanSharedPreference(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static final boolean getInitialBooleanSharedPreference(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, 0).getBoolean(key, true);
    }

    public static final boolean ifContainsSharedPreference(Context context, String key) {
        return context.getSharedPreferences(SHARED_PREF_NAME, 0).contains(key);
    }

    public static void screenShotMethod(View view) {
        if (view != null) {
            View v1 = view;
            v1.setDrawingCacheEnabled(true);
            v1.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, Constants.GRAPH_MARGIN_100, bytes);
            try {
                FileOutputStream fo = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CySmart" + File.separator + "file.jpg"));
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static void bondingProgressDialog(Activity context, ProgressDialog pDialog, boolean status) {
        mProgressDialog = pDialog;
        if (status) {
            mProgressDialog.setTitle(context.getResources().getString(R.string.alert_message_bonding_title));
            mProgressDialog.setMessage(context.getResources().getString(R.string.alert_message_bonding_message));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mTimer = setDialogTimer();
            return;
        }
        mProgressDialog.dismiss();
    }

    public static Timer setDialogTimer() {
        BLELogger.e("Started Timer");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
			public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        }, 20000);
        return timer;
    }

    public static void stopDialogTimer() {
        if (mTimer != null) {
            BLELogger.e("Stopped Timer");
            mTimer.cancel();
        }
    }

    @SuppressLint("NewApi")
	public static void setUpActionBar(Activity context, String title) {
        ActionBar actionBar = context.getActionBar();
     /*   actionBar.setIcon(context.getResources().getDrawable(R.drawable.logo));
        actionBar.setTitle(title);*/
    }

    public static final boolean checkNetwork(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void toast(Activity context, String text) {
        Toast.makeText(context, text.toString(), 1).show();
    }
}
