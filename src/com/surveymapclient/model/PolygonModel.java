package com.surveymapclient.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CouplePointLineBean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Looper;
import android.provider.MediaStore.Video;

public class PolygonModel {
	
	//�����߶λ�ȡ����
    private List<PointF> continuousPoints=new ArrayList<PointF>();
    //�����߶λ����ֹ����
    private List<PointF> linePoint=new ArrayList<PointF>();
    //�����߶λ������
    public List<CouplePointLineBean> continuouslist=new ArrayList<CouplePointLineBean>();
    private float mX, mY;// ��ʱ������
    private static final float TOUCH_TOLERANCE = 4;
    
    //ÿ��10����ȡ��
  	private static int space=10;
    
	private Path mPath;
	
	private Paint mPaint=ViewContans.generatePaint(Color.RED, 4);
	
	private LinesModel linesModel=new LinesModel();
	//����Ӱ
    List<PointF> shadowpoints;
    Path mshadowpath; 
		
	public void DrawPath(Canvas canvas){
		if (mPath!=null) {
			canvas.drawPath(mPath, mPaint);
		}
	}
	
	public void Continuous_touch_down(float x,float y){
		// ÿ��down��ȥ����newһ��Path 
		mPath=new Path();
		continuousPoints.clear();
		linePoint.clear();
		continuouslist.clear();
		continuousPoints.add(new PointF(x, y));
		mPath.reset();
		mPath.moveTo(x, y);
		mX=x;
		mY=y;	
	}
	
	public void Continuous_touch_move(float x,float y){
		continuousPoints.add(new PointF(x, y));
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        //�������������ֵ�Ż���·��
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	// ��x1,y1��x2,y2��һ�����������ߣ���ƽ��(ֱ����mPath.lineToҲ�ǿ��Ե�)
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
	}
	public void Continuous_touch_up(float x,float y,Canvas canvas){
		drawpolygon(0, 0);
		int count =linePoint.size();
		for (int i = 0; i < count; i++) {
			if (i==count-1) {
				linesModel.AddLineParams(continuouslist,ViewContans.AdsorbPoint((int)linePoint.get(i).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i).y), ViewContans.AdsorbPoint((int)linePoint.get(0).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(0).y));
			}else {
				linesModel.AddLineParams(continuouslist,ViewContans.AdsorbPoint((int)linePoint.get(i).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i).y), ViewContans.AdsorbPoint((int)linePoint.get(i+1).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i+1).y));
			}		
		}
		for (int i = 0; i < continuouslist.size(); i++) {
        	float sx=continuouslist.get(i).getStartPoint().x;
        	float sy=continuouslist.get(i).getStartPoint().y;
        	float ex=continuouslist.get(i).getStopPoint().x;
        	float ey=continuouslist.get(i).getStopPoint().y;
        	if (sx==ex&&sy==ey) {
				continuouslist.remove(i);
			}	
		}
		linesModel.DrawLinesOnBitmap(continuouslist, canvas);	
        mPath = null;// �����ÿ�	
		
	}
	public void Continuous_camera_touch_up(float x,float y,Canvas canvas){
		drawpolygon(0, 0);
		int count =linePoint.size();
		for (int i = 0; i < count; i++) {
			if (i==count-1) {
				linesModel.AddLineParams(continuouslist,linePoint.get(i).x, 
						linePoint.get(i).y, linePoint.get(0).x, 
						linePoint.get(0).y);
			}else {
				linesModel.AddLineParams(continuouslist,linePoint.get(i).x, 
						linePoint.get(i).y, linePoint.get(i+1).x, 
						linePoint.get(i+1).y);
			}		
		}
		for (int i = 0; i < continuouslist.size(); i++) {
        	float sx=continuouslist.get(i).getStartPoint().x;
        	float sy=continuouslist.get(i).getStartPoint().y;
        	float ex=continuouslist.get(i).getStopPoint().x;
        	float ey=continuouslist.get(i).getStopPoint().y;
        	if (sx==ex&&sy==ey) {
				continuouslist.remove(i);
			}	
		}
		linesModel.DrawLinesOnBitmap(continuouslist, canvas);	
        mPath = null;// �����ÿ�	
		
	}
	private void drawpolygon(int i,int j){
			
		List<PointF> currePoint=new ArrayList<PointF>();
		try {
			currePoint.add(continuousPoints.get(i));
			currePoint.add(continuousPoints.get(space*(j+1) + i));
			currePoint.add(continuousPoints.get(space*(j+1) + space + i));
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			return;
		}
		if (Angle(currePoint)<150) {
			linePoint.add(continuousPoints.get(i));
			linePoint.add(continuousPoints.get(space*(j+1) + i));
			linePoint.add(continuousPoints.get(space*(j+1) + space + i));
			drawpolygon((space*(j+1) + space + i), 0);
		}else {
			drawpolygon(i, j+1);
		}
			
	}
	
	private List<CouplePointLineBean> jiLines=new ArrayList<CouplePointLineBean>();
	boolean goFoot=false;
	boolean goHead=true;
	private boolean isFromFor=false;
	private boolean isFromBack=false;
	private boolean isFromCrack=false;
	private List<Integer> getInt=new ArrayList<Integer>(); 
	public void DrawShadowArea(List<CouplePointLineBean> lines,Canvas canvas){
		jiLines.clear();
		for (int i = 0; i < lines.size(); i++) {
    		int sxi=(int) lines.get(i).getStartPoint().x;
			int syi=(int) lines.get(i).getStartPoint().y;
			int exi=(int) lines.get(i).getStopPoint().x;
			int eyi=(int) lines.get(i).getStopPoint().y;
			for (int j = 0; j < lines.size(); j++) {
				if (j!=i) {				  
					int sxj=(int) lines.get(j).getStartPoint().x;
					int syj=(int) lines.get(j).getStartPoint().y;
					int exj=(int) lines.get(j).getStopPoint().x;
					int eyj=(int) lines.get(j).getStopPoint().y;
					Logger.i("ѡ���ص���", "�ɹ���");
					boolean isHead=((sxi==sxj)&&(syi==syj))||((sxi==exj)&&(syi==eyj));
					if (isHead) { 
						for (int k = 0; k < lines.size(); k++) {
							if (k!=j&&k!=i) {
								int nsxj=(int) lines.get(k).getStartPoint().x;
								int nsyj=(int) lines.get(k).getStartPoint().y;
								int nexj=(int) lines.get(k).getStopPoint().x;
								int neyj=(int) lines.get(k).getStopPoint().y;
								boolean isFoot=((exi==nsxj)&&(eyi==nsyj))||((exi==nexj)&&(eyi==neyj));
								if (isFoot) {
									Logger.i("ͷβ���", "��   i= "+i);
//									Logger.i("ͷβ���", "��   j= "+j);
//									Logger.i("ͷβ���", "��   k= "+k);									
									jiLines.add(new CouplePointLineBean(new PointF((float)sxi, (float)syi), new PointF((float)exi,(float)eyi)));
									
								}
							}
						}
					}
				}
			}			
		}
		Logger.i("�������", "jiLines����="+jiLines.size());
		if (jiLines.size()>=3) {
			jisuang(jiLines,0,canvas);
		}  		
	}
	
	private void jisuang(List<CouplePointLineBean> lines,int i,Canvas canvas){	
		int n=0;
		int ex=(int) lines.get(0).getStopPoint().x;
	 	int ey=(int) lines.get(0).getStopPoint().y;
		int sxi=(int) lines.get(i).getStartPoint().x;
		int syi=(int) lines.get(i).getStartPoint().y;
		int exi = 0;
		int eyi = 0;
		if (i!=0) {
			 exi=(int) lines.get(i).getStopPoint().x;
			 eyi=(int) lines.get(i).getStopPoint().y;
		    	Logger.i("����յ�", "jiLines-->i="+i+": ("+sxi+","+syi+")  ,  ("+exi+","+eyi+")");
		}
    	Logger.i("����յ�", "jiLines-->i="+i+": ("+sxi+","+syi+")  ,  ("+ex+","+ey+")");
		boolean isBack=((ex==sxi)&&(ey==syi))||((ex==exi)&&(ey==eyi));
    	for (int j = 1; j< lines.size(); j++) {
			if (j!=i) {
				int sxj=(int) lines.get(j).getStartPoint().x;
				int syj=(int) lines.get(j).getStartPoint().y;
				int exj=(int) lines.get(j).getStopPoint().x;
				int eyj=(int) lines.get(j).getStopPoint().y;
//				Logger.i("�ڼ�����", "--j-->"+j);
				boolean isHead=false;
				boolean isFoot=false;
				if (goHead) {
					Logger.i("�ڼ�����", "--goHead-->"+j);
					if ((sxi==sxj)&&(syi==syj)) {
						Logger.i("�ڼ�����", "-ss-isHead-->"+j);
//						isHead=true;
//						goHead=false;
//						goFoot=true;
					}else if ((sxi==exj)&&(syi==eyj)) {
						Logger.i("�ڼ�����", "-se-isHead-->"+j);
						goHead=true;
//						goFoot=false;
						getInt.add(i);
						n=j;
						isFromFor=true;
						break;
					}else if (isBack) {
						Logger.i("�ڼ�����", "--isFromBack-->"+n);
						getInt.add(i);
						isFromBack=true;
						shadowpoints=new ArrayList<PointF>();
						for (int k = 0; k < getInt.size(); k++) {
//							Logger.i("�ڼ�����", "--->"+getInt.get(k).intValue());
							shadowpoints.add(new PointF(lines.get(getInt.get(k).intValue()).getStartPoint().x, lines.get(getInt.get(k).intValue()).getStartPoint().y));
							shadowpoints.add(new PointF(lines.get(getInt.get(k).intValue()).getStopPoint().x, lines.get(getInt.get(k).intValue()).getStopPoint().y));
						}								
						ShadowArea(canvas);		
						
//						for (int k = 0; k < getInt.size(); k++) {
//							lines.remove(getInt.get(k).intValue());
							lines.removeAll(getInt);
//							Logger.i("��getInt����", "--getInt-->"+getInt.get(k).intValue());
//						}
						Logger.i("�������", "lines����="+lines.size());	
//						if (lines.size()>=3) {
//							jisuang(lines, 0, canvas);
//						}
						break;
					}
				}
//				else if (goFoot) {
//					if (i!=0) {
//						if ((exi==sxj)&&(eyi==syj)) {
//							isFoot=true;
//							goHead=false;
//							goFoot=true;
//						}else if ((exi==exj)&&(eyi==eyj)) {
//							isFoot=true;
//							goHead=true;
//							goFoot=false;
//						}
//					}
//				}		 
//				if (isHead) {
//					isHead=false;
//					getInt.add(i);
//					n=j;
//					isFromFor=true;
//					Logger.i("�ڼ�����", "--isHead-->"+n);
//					break;
//				}else if (isFoot) {
//					isFoot=false;
//					getInt.add(i);
//					n=j;
//					isFromFor=true;
//					Logger.i("�ڼ�����", "--isFoot-->"+n);
//					break;
//				}
//				else if (isBack) { 		
//					Logger.i("�ڼ�����", "--isFromBack-->"+n);
//					getInt.add(i);
//					shadowpoints=new ArrayList<PointF>();
//					for (int k = 0; k < getInt.size(); k++) {
//						Logger.i("�ڼ�����", "--->"+getInt.get(k).intValue());
//						shadowpoints.add(new PointF(jiLines.get(getInt.get(k).intValue()).getStartPoint().x, jiLines.get(getInt.get(k).intValue()).getStartPoint().y));
//						shadowpoints.add(new PointF(jiLines.get(getInt.get(k).intValue()).getStopPoint().x, jiLines.get(getInt.get(k).intValue()).getStopPoint().y));
//					}					
//					for (int k = 0; k < getInt.size(); k++) {
//						jiLines.remove(getInt.get(k).intValue());
//					}				
//					ShadowArea(canvas);
//					getInt.clear();
//					isFromBack=true;
//					break;
//				}
//					else {
//					jiLines.remove(i);
//					isFromCrack=true;
//					break;
//				}	
			}		
		}
		if (isFromFor) {
			isFromFor=false;
			int m=getInt.get(getInt.size()-1).intValue();
			Logger.i("�ڼ�����", "--isFromFor-->n="+n+",m="+m);
			jisuang(lines,n,canvas);
		}			
	}
	
	public void ShadowArea(Canvas canvas){
	   	Logger.i("����Ӱ����", ""+shadowpoints.size());
	   	mshadowpath=new Path();
    	if (shadowpoints.size()>0) {
    		Paint p = new Paint();
            p.setAntiAlias(true);
            p.setDither(true);
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.FILL);
            p.setStrokeJoin(Paint.Join.ROUND);
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(10); 
        	p.setAlpha(0x40);           	
        	mshadowpath.moveTo(shadowpoints.get(0).x, shadowpoints.get(0).y);
        	for (int i = 1; i < shadowpoints.size(); i++) {    									        		        	
        		mshadowpath.lineTo(shadowpoints.get(i).x, shadowpoints.get(i).y);	             	
			}
        	canvas.drawPath(mshadowpath, p);  
        	shadowpoints.clear();
        	mshadowpath.reset();
		}
	}
	
	public static double Angle(List<PointF> points){		
		double a=distance(points.get(0), points.get(1));
		double b=distance(points.get(1), points.get(2));
		double c=distance(points.get(2), points.get(0));
		// ���㻡�ȱ�ʾ�Ľ�
		double A=Math.acos((b * b + a * a - c * c) / (2.0 * b * a));
		A=Math.toDegrees(A);
		// ��ʽ�����ݣ�������λС��
		return A;
	}
	
	private static double distance(PointF p1,PointF p2){
		float x = p1.x - p2.x;  
	    float y = p1.y - p2.y;  
	    return Math.sqrt(x * x + y * y);  		
	}
	
	
}
