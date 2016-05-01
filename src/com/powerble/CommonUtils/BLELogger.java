package com.powerble.CommonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;
import android.support.v4.media.TransportMediator;
import android.util.Log;

import  com.surveymapclient.activity.R;
import com.powerble.Util;

public class BLELogger {
	private static Context mContext;
	private static File mDataLoggerDirectory;
	private static File mDataLoggerFile;
	private static File mDataLoggerOldFile;
	private static String mLogTag = "CySmart Android";
	private static boolean mLogflag = true;

	public static void d(String message) {
		show(1, mLogTag, message);
	}

	public static void d(String tag, String message) {
		show(2, tag, message);
	}

	public static void w(String message) {
		show(3, mLogTag, message);
	}

	public static void i(String message) {
		show(TransportMediator.FLAG_KEY_MEDIA_PLAY, mLogTag, message);
	}

	public static void e(String message) {
		show(4, mLogTag, message);
	}

	public static void v(String message) {
		show(5, mLogTag, message);
	}

	public static void datalog(String message) {
		saveLogData(message);
		Util.e("save dataLog", message);
	}

	private static void show(int type, String tag, String msg) {
		if (msg.length() > 4000) {
			Log.i("Length ", msg.length() + Util.VERSION_NAME);
			while (msg.length() > 4000) {
				show(type, tag, msg.substring(0, 4000));
				msg = msg.substring(4000, msg.length());
			}
		}
		Log.e(tag, msg);
	}

	private static void show(Exception exception) {
		try {
			if (mLogflag) {
				exception.printStackTrace();
			}
		} catch (NullPointerException e) {
			show(e);
		}
	}

	public static boolean enableLog() {
		mLogflag = true;
		return mLogflag;
	}

	public static boolean disableLog() {
		mLogflag = false;
		return mLogflag;
	}

	public static void createDataLoggerFile(Context context) {
		mContext = context;
		try {
			mDataLoggerDirectory = new File(
					Environment.getExternalStorageDirectory()
							+ File.separator
							+ context.getResources().getString(
									R.string.dl_directory));
			if (!mDataLoggerDirectory.exists()) {
				mDataLoggerDirectory.mkdirs();
			}
			mDataLoggerFile = new File(mDataLoggerDirectory.getAbsoluteFile()
					+ File.separator
					+ BLEUtils.GetDate()
					+ context.getResources().getString(
							R.string.dl_file_extension));
			if (!mDataLoggerFile.exists()) {
				mDataLoggerFile.createNewFile();
			}
			deleteOLDFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteOLDFiles() {
		File[] allFilesList = mDataLoggerDirectory.listFiles();
		long cutoff = System.currentTimeMillis() - 604800000;
		for (File currentFile : allFilesList) {
			if (currentFile.lastModified() < cutoff) {
				currentFile.delete();
			}
		}
		mDataLoggerOldFile = new File(mDataLoggerDirectory.getAbsoluteFile()
				+ File.separator + BLEUtils.GetDateSevenDaysBack()
				+ mContext.getResources().getString(R.string.dl_file_extension));
		if (mDataLoggerOldFile.exists()) {
			mDataLoggerOldFile.delete();
		}
	}

	private static void saveLogData(String message) {
		mDataLoggerFile = new File(mDataLoggerDirectory.getAbsoluteFile()
				+ File.separator + BLEUtils.GetDate()
				+ mContext.getResources().getString(R.string.dl_file_extension));
		if (!mDataLoggerFile.exists()) {
			try {
				mDataLoggerFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		message = BLEUtils.GetTimeandDate() + message;
		try {
			BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(mDataLoggerFile, true), "UTF-8"));
			fbw.write(message);
			fbw.newLine();
			fbw.flush();
			fbw.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}
}
