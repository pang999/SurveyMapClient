package com.powerble.data;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.powerble.Util;
import com.powerble.CommonFragments.ProfileScanningFragment;

/**
 * @Description(描述): 蓝牙数据包，对蓝牙数据包进行封装
 * @Package(包名): com.cypress.cysmart.data
 * @ClassName(类名): BLEDataPackage
 * @author(作者): Pang
 * @date(时间): 2016-4-21 下午1:55:07
 * @version(版本): V1.0
 */
public class BLEDataPackage implements Serializable {
	/** 单次测量 */
	private final String MEASURE_TYPE_SINGLE = "single";
	/** 面积测量 */
	private final String MEASURE_TYPE_AREA = "area";
	/** 体积测量 */
	private final String MEASURE_TYPE_CUBE = "cube";
	/** 勾股1 */
	private final String MEASURE_TYPE_GOUGU1 = "gougu1";
	/** 勾股2 */
	private final String MEASURE_TYPE_GOUGU2 = "gougu2";
	/** 勾股3 */
	private final String MEASURE_TYPE_GOUGU3 = "gougu3";
	/** 连续测量 */
	private final String MEASURE_TYPE_SERIES = "series";
	/** 墙面积测量 */
	private final String MEASURE_TYPE_WALL_AREA = "wall_area";

	private final String DATA_TYPE_M = "m";
	private final String DATA_TYPE_M2 = "㎡";
	private final String DATA_TYPE_M3 = "m³";
	private final String DATA_TYPE_C = "°";

	private String uuid;
	private String serviceuuid;
	private final String mAddress;
	private int size;// 数据包大小
	private int id;// 当前编号
	private int recordNum;// 记录总数
	private String measureType;
	private double data1 = 0;
	private double data2 = 0;
	private double data3 = 0;
	private double data4 = 0;

	private String dataType1="";
	private String dataType2="";
	private String dataType3="";
	private String dataType4="";

	public BLEDataPackage(String hexdata) {
		parseHead(hexdata);
		mAddress = ProfileScanningFragment.mDeviceAddress;
	}

	public void setUUid(String s) {
		this.uuid = s;
	}

	public String getUUid() {
		return uuid + "";
	}

	public void setServiceUUid(String s) {
		this.serviceuuid = s;
	}

	public String getServiceUUid() {
		return serviceuuid + "";
	}

	/**
	 * @Description（描述）: 解析数据头
	 * @param s
	 *            void
	 * @author Pang
	 * @date 2016-4-21 下午2:44:57
	 */
	private void parseHead(String s) {
		Util.e(">>>>>>>>>>>>.hex", s);
		String[] s1 = s.split("\\s+");// 按空格拆分

		int length = s1.length;
		if (length >= 3) {
			size = Integer.parseInt(s1[2], 16);
		} else
			size = 0;
		// 当前编号
		if (length >= 5)
			id = Integer.parseInt(s1[4], 16);
		else
			id = 0;
		if (length >= 6)
			recordNum = Integer.parseInt(s1[5], 16);
		else
			recordNum = 0;
		if (length >= 7) {
			if (s1[6].equals("00")) {// 单词测量
				measureType = MEASURE_TYPE_SINGLE;
			} else if (s1[6].equals("01")) {
				measureType = MEASURE_TYPE_AREA;
			} else if (s1[6].equals("02")) {
				measureType = MEASURE_TYPE_CUBE;

			} else if (s1[6].equals("03")) {
				measureType = MEASURE_TYPE_GOUGU1;

			} else if (s1[6].equals("04")) {
				measureType = MEASURE_TYPE_GOUGU2;
			} else if (s1[6].equals("05")) {
				measureType = MEASURE_TYPE_GOUGU3;

			} else if (s1[6].equals("06")) {
				measureType = MEASURE_TYPE_SERIES;

			} else if (s1[6].equals("07")) {
				measureType = MEASURE_TYPE_WALL_AREA;

			}

		}

		if (length >= 13) {
			long t = Long.parseLong(s1[11] + s1[10] + s1[9] + s1[8], 16);
			if (s1[12].equals("00"))
				dataType1 = DATA_TYPE_M;
			else if (s1[12].equals("01"))
				dataType1 = DATA_TYPE_M2;
			else if (s1[12].equals("02"))
				dataType1 = DATA_TYPE_M3;
			else if (s1[12].equals("03"))
				dataType1 = DATA_TYPE_C;
			double d = t * 0.0001;

			String tString = d + "";
			Util.e("data", "data1:" +s1[11] + s1[10] + s1[9] + s1[8]+"<>"+ tString);
			
				data1 = Double.parseDouble(tString);
		
			data1=format(data1);
		
		}

		if (length >= 18) {
			long t = Long.parseLong(s1[16] + s1[15] + s1[14] + s1[13], 16);
			if (s1[17].equals("00"))
				dataType2 = DATA_TYPE_M;
			else if (s1[17].equals("01"))
				dataType2 = DATA_TYPE_M2;
			else if (s1[17].equals("02"))
				dataType2 = DATA_TYPE_M3;
			else if (s1[17].equals("03"))
				dataType2 = DATA_TYPE_C;

			double d = t * 0.0001;

			String tString = d + "";
			Util.e("data", "data2:"+s1[16] + s1[15] + s1[14] + s1[13]+"<>"+tString);
			
				data2 = Double.parseDouble(tString);
		
			data2=format(data2);
			
		}

		if (length >= 23) {
			long t = Long.parseLong(s1[21] + s1[20] + s1[19] + s1[18], 16);
			if (s1[22].equals("00"))
				dataType3 = DATA_TYPE_M;
			else if (s1[22].equals("01"))
				dataType3 = DATA_TYPE_M2;
			else if (s1[22].equals("02"))
				dataType3 = DATA_TYPE_M3;
			else if (s1[22].equals("03"))
				dataType3 = DATA_TYPE_C;

			double d = t * 0.0001;

			String tString = d + "";
			Util.e("data", "data3:" +s1[21] + s1[20] + s1[19] + s1[18]+"<>"+ tString);
			
				data3 = Double.parseDouble(tString);
			
			data3=format(data3);
		
		}

		if (length >= 28) {
			long t = Long.parseLong(s1[26] + s1[25] + s1[24] + s1[23], 16);
			if (s1[27].equals("00"))
				dataType4 = DATA_TYPE_M;
			else if (s1[27].equals("01"))
				dataType4 = DATA_TYPE_M2;
			else if (s1[27].equals("02"))
				dataType4 = DATA_TYPE_M3;
			else if (s1[27].equals("03"))
				dataType4 = DATA_TYPE_C;

			double d = t * 0.0001;

			String tString = d + "";
			Util.e("data", "data4:"+s1[26] + s1[25] + s1[24] + s1[23]+"<>" +tString);
		
				data4 = Double.parseDouble(tString);
			
			data4=format(data4);
		
		}

		// 屏显映射1011 从上到下124行有，第三行无
		if (length >= 8) {
			// int i = Integer.parseInt(s1[7], 16);
			String s3 = Integer.toBinaryString(Integer.valueOf(s1[7], 16));
			/*
			 * Util.e("xian0", s3); if (s3.length() >= 4) s3 = s3.substring(3);
			 * 
			 */
			Util.e("xian", s3);
			if(s3.length()>=4){
				char [] c123=s3.toCharArray();
				if(c123[0]=='0')	data4 = 0.0;
				if(c123[0]=='0')	data3 = 0.0;
				if(c123[0]=='0')	data2 = 0.0;
				if(c123[0]=='0')	data1 = 0.0;
			}
			
				
			
		}
		
		
		
		Util.e("parse", toString());

	}

	public String getRemoteAddress() {
		return mAddress + "";
	}

	public void setSize(int s) {
		this.size = s;
	}

	public int getSize() {
		return size;
	}

	public void setId(int i) {
		id = i;
	}

	public int getId() {
		return id;
	}

	public void setMeasureType(String type) {
		measureType = type;
	}

	public String getMeasureType() {
		return measureType + "";
	}

	
	///////////////////////数据
	public void setData1(double l) {
		data1 = l;
	}

	public double getData1() {
		return data1;
	}

	public void setData2(double l) {
		data2 = l;
	}

	public double getData2() {
		return data2;
	}

	public void setData3(double l) {
		data3 = l;
	}

	public double getData3() {
		return data3;
	}

	public void setData4(double l) {
		data4 = l;
	}

	public double getData4() {
		return data4;
	}



	
	//////////////////单位
	
	
	
	public void setDataType1(String s) {
		dataType1=s;
	}

	public String  getDataType1() {
		return dataType1;
	}

	public void setDataType2(String s) {
		dataType2=s;
	}

	public String  getDataType2() {
		return dataType2;
	}
	public void setDataType3(String s) {
		dataType3=s;
	}

	public String  getDataType3() {
		return dataType3;
	}
	public void setDataType4(String s) {
		dataType4=s;
	}

	public String  getDataType4() {
		return dataType4;
	}

	
	
	
	
	
	@Override
	public String  toString(){
		String s ="";
		s="Head: "+"0x99 0x6A"
		+"\nLength: "+size
		+"\nCommend: "+"0x68"
		+"\nId: "+id
		+"\nRecordNum: "+recordNum
		+"\nMesureType: "+measureType
		+"\nData1: "+data1+" "+dataType1
		+"\nData2: "+data2+" "+dataType2
		+"\nData3: "+data3+" "+dataType3
		
		+"\nData4: "+data4+" "+dataType4;
		
		
		return s;
	}
	
	
	/**
	 * @Description（描述）:   四舍五入取两位小数
	 *   void
	 * @author Pang
	 * @date 2016-4-21 下午9:47:16
	 */
	private  double format(double d){
double d1=0.0;

String s=d+"";
//if(s.length()<=7){//6位000.000对测距仪足够了
	 DecimalFormat df = new DecimalFormat("#.000");   
  d1= Double.parseDouble(df.format(d)) ; 

if(d1>500)d1=0.0;//数据太大，不合理

return d1;
	}
	
}