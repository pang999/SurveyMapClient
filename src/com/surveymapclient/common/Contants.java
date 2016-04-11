package com.surveymapclient.common;

import android.os.Environment;

public class Contants {

	public static final int sreenWidth=720;
	public static final int screenHeight=1280;
	
	public static final int DRAG=1;
	public static final int CONTINU=2;
	public static final int SINGLE=3;
	public static final int RECTANGLE=4;
	public static final int COORDINATE=5;
	public static final int ANGLE=6;
	public static final int TEXT=7;
	public static final int COORDINATE_AXIS=8;
	public static final int ANGLE_AXIS=9;
<<<<<<< HEAD
	public static final int CONTINU_XETEND=21;

=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	
	public static final int IS_CONTINUES=10;
	public static final int IS_ONE_LINE=11;
	public static final int IS_TWO_LINES=12;
	
	public static final int LINEATTRIBUTEBACK=13;
	public static final int RECTATTRIBUTEBACK=14;
	public static final int COORDATTRIBUTEBACK=15;
	public static final int ANGLEATTRIBUTEBACK=16;
	public static final int POLYGONATTRIBUTEBACK=17;
	public static final int TEXTATTRIBUTEBACK=18;
	public static final int AUDIOATTRIBUTEBACK=19;
	public static final int AUDIO=20;
	/**
	 * 根目录
	 */
	public static String fish_saying_root = "/surveymap/";
	
	/**
	 * 获取音频保存目录
	 */
	public static String AUDIO_DIRECTORY = Environment.getExternalStorageDirectory() +
			fish_saying_root + "/audio_record";
	
	/**
	 * 录制音频初始后缀
	 */
	public static final String PCM_SUFFIX = ".pcm";
	
	/**
	 * 转换成aac音频后缀
	 */
	public static final String AAC_SUFFIX = ".aac";
	
	/**
	 * 转换成m4a音频后缀
	 */
	public static final String M4A_SUFFIX = ".m4a";
	
}
