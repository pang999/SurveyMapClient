package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.surveymapclient.common.Logger;
<<<<<<< HEAD
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;

import android.graphics.PointF;

public class LineAndPolygonModel {
	
	public List<PolygonBean> backpolylist=new ArrayList<PolygonBean>();
	
	LinesModel listModel=new LinesModel();
	PolygonModel polygonModel=new PolygonModel();
	
	private List<LineBean> jilist=new ArrayList<LineBean>();
	boolean goFoot=false;
	boolean goHead=true;
	private boolean isFromFor=false;
	private   List<Integer> getInt=new ArrayList<Integer>(); 
	public  List<Integer> GetPolyInt=new ArrayList<Integer>();
	public List<Integer> setInt=new ArrayList<Integer>();
	
	public void CalculatePolyFromlist(List<LineBean> list){
		jilist.clear();
		for (int i = 0; i < list.size(); i++) {
    		int sxi=(int) list.get(i).getStartX();
			int syi=(int) list.get(i).getStartY();
			int exi=(int) list.get(i).getEndX();
			int eyi=(int) list.get(i).getEndY();
			for (int j = 0; j < list.size(); j++) {
				if (j!=i) {				  
					int sxj=(int) list.get(j).getStartX();
					int syj=(int) list.get(j).getStartY();
					int exj=(int) list.get(j).getEndX();
					int eyj=(int) list.get(j).getEndY();
					boolean isHead=((sxi==sxj)&&(syi==syj))||((sxi==exj)&&(syi==eyj));
					if (isHead) { 
						for (int k = 0; k < list.size(); k++) {
							if (k!=j&&k!=i) {
								int sxk=(int) list.get(k).getStartX();
								int syk=(int) list.get(k).getStartY();
								int exk=(int) list.get(k).getEndX();
								int eyk=(int) list.get(k).getEndY();
								boolean isFoot=((exi==sxk)&&(eyi==syk))||((exi==exk)&&(eyi==eyk));
								if (isFoot) {
									Logger.i("头尾相接", "第   i= "+i);
									LineBean line=new LineBean();
									line.setStartX((float)sxi);
									line.setStartY((float)syi);
									line.setEndX((float)exi);
									line.setEndY((float)eyi);
									jilist.add(line);	
									setInt.add(i);
								}
							}
						}
					}
				}
			}			
		}
		if (jilist.size()>=3) {
			CalculateLines(jilist,0);
		}
		
	}
	public void ClearInt(){
		GetPolyInt.clear();	
		setInt.clear();
		getInt.clear();		
		pointFs.clear();
		backpolylist.clear();
	}
	List<PointF> pointFs=new ArrayList<PointF>();
	public void CalculateLines(List<LineBean> list,int i){	
		int n=0;
		int ex=(int) list.get(0).getEndX();
	 	int ey=(int) list.get(0).getEndY();
		int sxi=(int) list.get(i).getStartX();
		int syi=(int) list.get(i).getStartY();
		int exi = 0;
		int eyi = 0;
		if (i!=0) {
			 exi=(int) list.get(i).getEndX();
			 eyi=(int) list.get(i).getEndY();
		}
		boolean isBack=((ex==sxi)&&(ey==syi))||((ex==exi)&&(ey==eyi));
    	for (int j = 1; j< list.size(); j++) {   		
			if (j!=i) {
				int sxj=(int) list.get(j).getStartX();
				int syj=(int) list.get(j).getStartY();
				int exj=(int) list.get(j).getEndX();
				int eyj=(int) list.get(j).getEndY();
				if (goHead) {
					if ((sxi==sxj)&&(syi==syj)) {
						goHead=false;
						goFoot=true;
						isFromFor=true;
						getInt.add(i);
						pointFs.add(new PointF(sxi, syi));
						n=j;						
						break;
					}else if ((sxi==exj)&&(syi==eyj)) {
						goHead=true;
						goFoot=false;						
						isFromFor=true;
						getInt.add(i);
						pointFs.add(new PointF(sxi, syi));
						n=j;						
						break;
					}else if (isBack) {
						pointFs.add(new PointF(ex, ey));
						getInt.add(i);
						List<LineBean> llList=new ArrayList<LineBean>();
						PolygonBean polygonBean=new PolygonBean();	
						int count =pointFs.size();
						for (int k = 0;k < count; k++) {
							if (k==count-1) {
								polygonModel.GetLines(llList,pointFs.get(k).x, 
										pointFs.get(k).y, pointFs.get(0).x, 
										pointFs.get(0).y);
							}else {
								polygonModel.GetLines(llList,pointFs.get(k).x, 
										pointFs.get(k).y, pointFs.get(k+1).x, 
										pointFs.get(k+1).y);
							}
							
						}
						for (int k = 0; k < llList.size(); k++) {
				        	float sx0=llList.get(k).getStartX();
				        	float sy0=llList.get(k).getStartY();
				        	float ex0=llList.get(k).getEndX();
				        	float ey0=llList.get(k).getEndY();
				        	if (sx0==ex0&&sy0==ey0) {
				        		llList.remove(k);
							}	
						}
						polygonBean.setPolyLine(llList);
						backpolylist.add(polygonBean);	
						Collections.sort(getInt);
						for (int k = getInt.size()-1; k >=0 ; k--) {
							GetPolyInt.add(setInt.get(getInt.get(k).intValue()));
						}						
						break;
					}
				}else if (goFoot) {
					if ((exi==sxj)&&(eyi==syj)) {
						goHead=false;
						goFoot=true;
						isFromFor=true;
						getInt.add(i);
						pointFs.add(new PointF(exi, eyi));
						n=j;						
						break;
					}else if ((exi==exj)&&(eyi==eyj)) {
						goHead=true;
						goFoot=false;
						isFromFor=true;
						getInt.add(i);
						pointFs.add(new PointF(exi, eyi));
						n=j;						
						break;
					}else if (isBack) {
						pointFs.add(new PointF(ex, ey));
						getInt.add(i);
						List<LineBean> llList=new ArrayList<LineBean>();
						PolygonBean polygonBean=new PolygonBean();						
						int count =pointFs.size();
						for (int k = 0;k < count; k++) {
							if (k==count-1) {
								polygonModel.GetLines(llList,pointFs.get(k).x, 
										pointFs.get(k).y, pointFs.get(0).x, 
										pointFs.get(0).y);
							}else {
								polygonModel.GetLines(llList,pointFs.get(k).x, 
										pointFs.get(k).y, pointFs.get(k+1).x, 
										pointFs.get(k+1).y);
							}
							
						}
						for (int k = 0; k < llList.size(); k++) {
				        	float sx0=llList.get(k).getStartX();
				        	float sy0=llList.get(k).getStartY();
				        	float ex0=llList.get(k).getEndX();
				        	float ey0=llList.get(k).getEndY();
				        	if (sx0==ex0&&sy0==ey0) {
				        		llList.remove(k);
							}	
						}
						polygonBean.setPolyLine(llList);
						backpolylist.add(polygonBean);	
						Collections.sort(getInt);
						for (int k = getInt.size()-1; k >=0 ; k--) {
							GetPolyInt.add(setInt.get(getInt.get(k).intValue()));
						}						
						break;
					}
				}								
=======
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;

public class LineAndPolygonModel {
	
	public List<LineBean> backlinelist=new ArrayList<LineBean>();
	public List<PolygonBean> backpolylist=new ArrayList<PolygonBean>();
	
	LinesModel linesModel=new LinesModel();
	PolygonModel polygonModel=new PolygonModel();
	
	private List<LineBean> jiLines=new ArrayList<LineBean>();
	boolean goFoot=false;
	boolean goHead=true;
	private boolean isFromFor=false;
//	private boolean isFromBack=false;
//	private boolean isFromCrack=false;
	private List<Integer> getInt=new ArrayList<Integer>(); 
//	public void CalculatePolyFromLines(List<LineBean> lines){
//		jiLines.clear();
//		for (int i = 0; i < lines.size(); i++) {
//    		int sxi=(int) lines.get(i).getStartX();
//			int syi=(int) lines.get(i).getStartY();
//			int exi=(int) lines.get(i).getEndX();
//			int eyi=(int) lines.get(i).getEndY();
//			for (int j = 0; j < lines.size(); j++) {
//				if (j!=i) {				  
//					int sxj=(int) lines.get(j).getStartX();
//					int syj=(int) lines.get(j).getStartY();
//					int exj=(int) lines.get(j).getEndX();
//					int eyj=(int) lines.get(j).getEndY();
//					Logger.i("选择重叠点", "成功了");
//					boolean isHead=((sxi==sxj)&&(syi==syj))||((sxi==exj)&&(syi==eyj));
//					if (isHead) { 
//						for (int k = 0; k < lines.size(); k++) {
//							if (k!=j&&k!=i) {
//								int nsxj=(int) lines.get(k).getStartX();
//								int nsyj=(int) lines.get(k).getStartY();
//								int nexj=(int) lines.get(k).getEndX();
//								int neyj=(int) lines.get(k).getEndY();
//								boolean isFoot=((exi==nsxj)&&(eyi==nsyj))||((exi==nexj)&&(eyi==neyj));
//								if (isFoot) {
//									Logger.i("头尾相接", "第   i= "+i);
//									LineBean line=new LineBean();
//									line.setStartX((float)sxi);
//									line.setStartY((float)syi);
//									line.setEndX((float)exi);
//									line.setEndY((float)eyi);
//									jiLines.add(line);									
//								}
//							}
//						}
//					}
//				}
//			}			
//		}
//		Logger.i("相接总数", "jiLines总数="+jiLines.size());
//		if (jiLines.size()>=3) {
//			jisuang(jiLines,0);
//		}  		
//	}
	
	public void jisuang(List<LineBean> lines,int i){	
		if (lines.size()==1) {
			backlinelist.add(lines.get(0));
		}
		int n=0;
		int ex=(int) lines.get(0).getEndX();
	 	int ey=(int) lines.get(0).getEndY();
		int sxi=(int) lines.get(i).getStartX();
		int syi=(int) lines.get(i).getStartY();
		int exi = 0;
		int eyi = 0;
		if (i!=0) {
			 exi=(int) lines.get(i).getEndX();
			 eyi=(int) lines.get(i).getEndY();
		    	Logger.i("起点终点", "jiLines-->i="+i+": ("+sxi+","+syi+")  ,  ("+exi+","+eyi+")");
		}
    	Logger.i("起点终点", "jiLines-->i="+i+": ("+sxi+","+syi+")  ,  ("+ex+","+ey+")");
		boolean isBack=((ex==sxi)&&(ey==syi))||((ex==exi)&&(ey==eyi));
    	for (int j = 1; j< lines.size(); j++) {   		
			if (j!=i) {
				int sxj=(int) lines.get(j).getStartX();
				int syj=(int) lines.get(j).getStartY();
				int exj=(int) lines.get(j).getEndX();
				int eyj=(int) lines.get(j).getEndY();
				if (goHead) {
					Logger.i("第几个数", "--goHead-->"+j);
			    	Logger.i("起点终点", "lines-->i="+i+": ("+sxi+","+syi+")  ,  ("+ex+","+ey+")");
			    	Logger.i("起点终点", "lines-->j="+j+": ("+sxj+","+syj+")  ,  ("+exj+","+eyj+")");
					if ((sxi==sxj)&&(syi==syj)) {
						Logger.i("第几个数", "-ss-goHead-->"+j);
						goHead=false;
						goFoot=true;
						getInt.add(i);
						n=j;
						isFromFor=true;
					}else if ((sxi==exj)&&(syi==eyj)) {
						Logger.i("第几个数", "-se-goHead-->"+j);
						goHead=true;
						goFoot=false;
						getInt.add(i);
						n=j;
						isFromFor=true;
						break;
					}else if (isBack) {
						Logger.i("第几个数", "--isFromBack-->"+n);
						getInt.add(i);
						for (int k = getInt.size()-1; k >=0 ; k--) {						
							polygonModel.GetLines(backlinelist, lines.get(getInt.get(k).intValue()).getStartX(),
									lines.get(getInt.get(k).intValue()).getStartY(),
									lines.get(getInt.get(k).intValue()).getEndX(),
									lines.get(getInt.get(k).intValue()).getEndY());
						}	
						Collections.sort(getInt);
						for (int k = getInt.size()-1; k >=0 ; k--) {
							Logger.i("第几个数", "--sort-->"+getInt.get(k).intValue());
							lines.remove(getInt.get(k).intValue());
						}
						getInt.clear();
//						if (lines.size()>=3) {
//							jisuang(lines, 0);
//						}
						this.backlinelist=lines;
						Logger.i("相接总数", "lines总数="+lines.size());	
						break;
					}else {
						backlinelist.add(lines.get(i));
						lines.remove(i);
						if (lines.size()>0) {
							jisuang(lines, 0);
						}
					}
				}else if (goFoot) {
					Logger.i("第几个数", "--goFoot-->"+j);
					if ((exi==sxj)&&(eyi==syj)) {
						Logger.i("第几个数", "-es-goFoot-->"+j);
						goHead=false;
						goFoot=true;
						getInt.add(i);
						n=j;
						isFromFor=true;
						break;
					}else if ((exi==exj)&&(eyi==eyj)) {
						Logger.i("第几个数", "-ee-goFoot-->"+j);
						goHead=true;
						goFoot=false;
						getInt.add(i);
						n=j;
						isFromFor=true;
						break;
					}else if (isBack) {
						Logger.i("第几个数", "--isFromBack-->"+n);
						getInt.add(i);
						List<LineBean> llList=new ArrayList<LineBean>();
						PolygonBean polygonBean=new PolygonBean();
						Collections.sort(getInt);
						for (int k = 0; k <getInt.size(); k++) {						
							polygonModel.GetLines(llList, lines.get(getInt.get(k).intValue()).getStartX(),
							lines.get(getInt.get(k).intValue()).getStartY(),
							lines.get(getInt.get(k).intValue()).getEndX(),
							lines.get(getInt.get(k).intValue()).getEndY());
						}
						polygonBean.setPolyLine(llList);
						backpolylist.add(polygonBean);						
						for (int k = getInt.size()-1; k >=0 ; k--) {
							Logger.i("第几个数", "--sort-->"+getInt.get(k).intValue());
							lines.remove(getInt.get(k).intValue());
						}
						backlinelist=lines;
						getInt.clear();
						Logger.i("lines总数", "backlinelist总数="+backlinelist.size());	
						break;
					}else {
						backlinelist.add(lines.get(i));
						lines.remove(i);
						if (lines.size()>0) {
							jisuang(lines, 0);
						}
					}
				}	
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			}		
		}
		if (isFromFor) {
			isFromFor=false;
<<<<<<< HEAD
			CalculateLines(list,n);
		}	
	}
=======
			int m=getInt.get(getInt.size()-1).intValue();
			Logger.i("第几个数", "--isFromFor-->n="+n+",m="+m);
			jisuang(lines,n);
		}			
	}

>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
}
