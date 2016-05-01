package com.surveymapclient.activity.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.surveymapclient.db.greendao.DaoMaster;
import com.surveymapclient.db.greendao.DaoMaster.OpenHelper;
import com.surveymapclient.db.greendao.DaoSession;

public class MyApplication extends Application {

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	public static SQLiteDatabase db;
	//数据库名，表名是自动被创建的      
    public static final String DB_NAME = "surveymap.db"; 
    
    public static DaoMaster getDaoMaster(Context context) {
    	if (daoMaster==null) {
			OpenHelper helper=new DaoMaster.DevOpenHelper(context, DB_NAME, null);
			daoMaster=new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}
    
    public static DaoSession getDaoSession(Context context){
    	if (daoSession==null) {
			if (daoMaster==null) {
				daoMaster=getDaoMaster(context);
			}
			daoSession=daoMaster.newSession();
		}
    	return daoSession;
    }
    
    public static SQLiteDatabase getSQLiteDatabase(Context context){
    	if (daoSession==null) {
			if (daoMaster==null) {
				daoMaster=getDaoMaster(context);
			}
			db=daoMaster.getDatabase();
		}
    	return db;
    }
    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
    	this.mGattServiceMasterData = new ArrayList();
    }
    
//    //各个平台的配置，建议放在全局Application或者程序入口
//    {
//        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
//    }
    
    
    
    
    
    
    
    //////////////////////////BLE蓝牙相关
    
    private BluetoothGattDescriptor mBluetoothGattDescriptor;
	private BluetoothGattCharacteristic mBluetoothgattcharacteristic;
	private List<BluetoothGattCharacteristic> mGattCharacteristics;
	private ArrayList<HashMap<String, BluetoothGattService>> mGattServiceMasterData;



	public BluetoothGattCharacteristic getBluetoothgattcharacteristic() {
		return this.mBluetoothgattcharacteristic;
	}

	public void setBluetoothgattcharacteristic(
			BluetoothGattCharacteristic bluetoothgattcharacteristic) {
		this.mBluetoothgattcharacteristic = bluetoothgattcharacteristic;
	}

	public BluetoothGattDescriptor getBluetoothgattDescriptor() {
		return this.mBluetoothGattDescriptor;
	}

	public void setBluetoothgattdescriptor(
			BluetoothGattDescriptor bluetoothGattDescriptor) {
		this.mBluetoothGattDescriptor = bluetoothGattDescriptor;
	}

	public List<BluetoothGattCharacteristic> getGattCharacteristics() {
		return this.mGattCharacteristics;
	}

	public void setGattCharacteristics(
			List<BluetoothGattCharacteristic> gattCharacteristics) {
		this.mGattCharacteristics = gattCharacteristics;
	}

	public ArrayList<HashMap<String, BluetoothGattService>> getGattServiceMasterData() {
		return this.mGattServiceMasterData;
	}

	public void setGattServiceMasterData(
			ArrayList<HashMap<String, BluetoothGattService>> gattServiceMasterData) {
		this.mGattServiceMasterData = gattServiceMasterData;
	}

	private BluetoothDevice currDevice;

	public BluetoothDevice getCurrDevice() {
		return currDevice;
	}

	public void setCurrDevices(BluetoothDevice d) {
		this.currDevice = d;
	}
    
    
    
    
    
}
