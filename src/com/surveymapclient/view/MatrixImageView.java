package com.surveymapclient.view;

import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.model.LinesModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MatrixImageView extends ImageView {

	private final static String TAG="MatrixImageView";
	
	 /** ������Ƭģʽ */
    private static final int MODE_DRAG = 1;
    /** �Ŵ���С��Ƭģʽ */
    private static final int MODE_ZOOM = 2;
    /**  ��֧��Matrix */ 
    private static final int MODE_UNABLE=3;
    /**   ������ż���*/ 
    float mMaxScale=6;
    /**   ˫��ʱ�����ż���*/ 
    float mDobleClickScale=2;
    private int mMode = 0;// 
    /**  ���ſ�ʼʱ����ָ��� */ 
    private float mStartDis;
    /**   ��ǰMatrix*/ 
    private Matrix mCurrentMatrix = new Matrix(); 
    /** ���ڼ�¼��ʼʱ�������λ�� */
    private PointF startPoint = new PointF();
    
    /**  ģ��Matrix�����Գ�ʼ�� */ 
    private Matrix mMatrix=new Matrix();
    /**  ͼƬ����*/ 
    private float mImageWidth;
    /**  ͼƬ�߶� */ 
    private float mImageHeight;
    LinesModel linesModel;    
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;// �����Ļ���
    private Bitmap  mBitmap;
    private Paint   mPaint;// ��ʵ�Ļ���
    private Bitmap background;
    
    
	public MatrixImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
//		MatrixTouchListener mListener=new MatrixTouchListener();
//        setOnTouchListener(mListener);
        //��������Ϊbalck
//		mBitmap=new Bi
		initCanvas();
//		this.background=CameraActivity.smallBitmap;
//		Logger.i("ִ��˳��", "MatrixImageView__smallBitmap="+CameraActivity.smallBitmap);
//		Logger.i("ִ��˳��", "MatrixImageView");
//		mBitmap
//        setBackgroundColor(Color.BLACK);
        //��������������ΪFIT_CENTER����ʾ��ͼƬ����������/��С��View�Ŀ�ȣ�������ʾ
        setScaleType(ScaleType.FIT_CENTER);
        linesModel=new LinesModel();
        
        invalidate();

	}
	//��ʼ������
    public void initCanvas(){
         
        mPaint = ViewContans.generatePaint(Color.RED, 5);  
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);    
        //������С 
        mBitmap = Bitmap.createBitmap(Contants.sreenWidth*3, Contants.screenHeight*3, 
            Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap);  //����mCanvas���Ķ���������������mBitmap��      
        mCanvas.drawColor(Color.WHITE);
//        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
         
    }
    public void setimagebitmap(Bitmap bm){
//    	this.mBitmap=bm;
    	this.background=bm;
		Logger.i("ִ��˳��", "background="+background);
		Logger.i("ִ��˳��", "MatrixImageView");
    }
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Logger.i("ִ��˳��", "draw");
		if (background!=null) {
			canvas.drawBitmap(background, 50, 300, mBitmapPaint);
		}
		linesModel.DrawLine(canvas);
	}
	
    @Override
    public void setImageBitmap(Bitmap bm) {
        // TODO Auto-generated method stub
        super.setImageBitmap(bm);
        this.mBitmap=bm;
        //������ͼƬ�󣬻�ȡ��ͼƬ������任����
        mMatrix.set(getImageMatrix());
        float[] values=new float[9];
        mMatrix.getValues(values);
        //ͼƬ���Ϊ��Ļ��ȳ����ű���
        mImageWidth=getWidth()/values[Matrix.MSCALE_X];
        mImageHeight=(getHeight()-values[Matrix.MTRANS_Y]*2)/values[Matrix.MSCALE_Y];
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	// TODO Auto-generated method stub
		float rx=event.getX();
		float ry=event.getY();
		switch (event.getActionMasked()) {
		
		case MotionEvent.ACTION_DOWN:
			//�����϶�ģʽ
			mMode=MODE_DRAG;
			startPoint.set(event.getX(), event.getY());
			isMatrixEnable();
//    					linesModel.Line_touch_down(rx, ry);
			break;
		
		case MotionEvent.ACTION_CANCEL:
			resetmatrix();
			break;
		case MotionEvent.ACTION_MOVE:
//    					linesModel.Line_touch_move(rx, ry);
            if (mMode == MODE_ZOOM) {
                setZoomMatrix(event);
            }else if (mMode==MODE_DRAG) {
                setDragMatrix(event);
            }
            break;
		case MotionEvent.ACTION_UP:
//    					linesModel.Line_touch_up(rx, ry, mCanvas);
			break;
        case MotionEvent.ACTION_POINTER_DOWN:
            if(mMode==MODE_UNABLE) return true;
            mMode=MODE_ZOOM;
            mStartDis = distance(event);
            break;
		default:
			break;
		}
		invalidate();
		return true;
    }
    
	public class MatrixTouchListener implements OnTouchListener{

		 /** ������Ƭģʽ */
        private static final int MODE_DRAG = 1;
        /** �Ŵ���С��Ƭģʽ */
        private static final int MODE_ZOOM = 2;
        /**  ��֧��Matrix */ 
        private static final int MODE_UNABLE=3;
        /**   ������ż���*/ 
        float mMaxScale=6;
        /**   ˫��ʱ�����ż���*/ 
        float mDobleClickScale=2;
        private int mMode = 0;// 
        /**  ���ſ�ʼʱ����ָ��� */ 
        private float mStartDis;
        /**   ��ǰMatrix*/ 
        private Matrix mCurrentMatrix = new Matrix(); 
        /** ���ڼ�¼��ʼʱ�������λ�� */
        private PointF startPoint = new PointF();
        
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			float rx=event.getX();
			float ry=event.getY();
			switch (event.getActionMasked()) {
			
			case MotionEvent.ACTION_DOWN:
				//�����϶�ģʽ
				mMode=MODE_DRAG;
				startPoint.set(event.getX(), event.getY());
				isMatrixEnable();
//				linesModel.Line_touch_down(rx, ry);
				break;
			
			case MotionEvent.ACTION_CANCEL:
				resetmatrix();
				break;
			case MotionEvent.ACTION_MOVE:
//				linesModel.Line_touch_move(rx, ry);
                if (mMode == MODE_ZOOM) {
                    setZoomMatrix(event);
                }else if (mMode==MODE_DRAG) {
                    setDragMatrix(event);
                }
                break;
			case MotionEvent.ACTION_UP:
//				linesModel.Line_touch_up(rx, ry, mCanvas);
				break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(mMode==MODE_UNABLE) return true;
                mMode=MODE_ZOOM;
                mStartDis = distance(event);
                break;
			default:
				break;
			}
			invalidate();
			return true;
		}
		
		public void setDragMatrix(MotionEvent event){
			if (isZoomChanged()) {
				float dx=event.getX()-startPoint.x;
				float dy=event.getY()-startPoint.y;
				//�����˫����ͻ,����10f�������϶�
				if (Math.sqrt(dx*dx+dy*dy)>10f) {
					startPoint.set(event.getX(), event.getY());
					//�ڵ�ǰ�������ƶ�
					mCurrentMatrix.set(getImageMatrix());
					float[] values=new float[9];
					mCurrentMatrix.getValues(values);
					dx=checkDxBound(values,dx);
                    dy=checkDyBound(values,dy);    
                    mCurrentMatrix.postTranslate(dx, dy);
                    setImageMatrix(mCurrentMatrix);
				}
			}
		}
		 /**  
         *  �ж����ż����Ƿ��Ǹı��
         *  @return   true��ʾ�ǳ�ʼֵ,false��ʾ��ʼֵ
         */
		private boolean isZoomChanged(){
			float[] values=new float[9];
			getImageMatrix().getValues(values);
			//��ȡ��ǰX�����ż���
			float scale=values[Matrix.MSCALE_X];
			//��ȡģ���X�����ż����������Ƚ�
			mMatrix.getValues(values);		
			return scale!=values[Matrix.MSCALE_X];
		}
		/**  
         *  �͵�ǰ����Աȣ�����dy��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
         *  @param values
         *  @param dy
         *  @return   
         */
		private float checkDyBound(float[] values,float dy){
			float height=getHeight();
			if (mImageHeight*values[Matrix.MSCALE_Y]<height) {
				return 0;
			}
			if(values[Matrix.MTRANS_Y]+dy>0)
                dy=-values[Matrix.MTRANS_Y];
            else if(values[Matrix.MTRANS_Y]+dy<-(mImageHeight*values[Matrix.MSCALE_Y]-height))
                dy=-(mImageHeight*values[Matrix.MSCALE_Y]-height)-values[Matrix.MTRANS_Y];
            return dy;
		}
		/**  
         *�͵�ǰ����Աȣ�����dx��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
         *  @param values
         *  @param dx
         *  @return   
         */
        private float checkDxBound(float[] values,float dx) {
            float width=getWidth();
            if(mImageWidth*values[Matrix.MSCALE_X]<width)
                return 0;
            if(values[Matrix.MTRANS_X]+dx>0)
                dx=-values[Matrix.MTRANS_X];
            else if(values[Matrix.MTRANS_X]+dx<-(mImageWidth*values[Matrix.MSCALE_X]-width))
                dx=-(mImageWidth*values[Matrix.MSCALE_X]-width)-values[Matrix.MTRANS_X];
            return dx;
        }
        /**  
         *  ��������Matrix
         *  @param event   
         */
        private void setZoomMatrix(MotionEvent event) {
            //ֻ��ͬʱ�����������ʱ���ִ��
            if(event.getPointerCount()<2) return;
            float endDis = distance(event);// ��������
            if (endDis > 10f) { // ������ָ��£��һ���ʱ�����ش���10
                float scale = endDis / mStartDis;// �õ����ű���
                mStartDis=endDis;//���þ���
                mCurrentMatrix.set(getImageMatrix());//��ʼ��Matrix
                float[] values=new float[9];
                mCurrentMatrix.getValues(values);

                scale = checkMaxScale(scale, values);
                setImageMatrix(mCurrentMatrix);    
            }
        }
        /**  
         *  ����scale��ʹͼ�����ź󲻻ᳬ�������
         *  @param scale
         *  @param values
         *  @return   
         */
        private float checkMaxScale(float scale, float[] values) {
            if(scale*values[Matrix.MSCALE_X]>mMaxScale) 
                scale=mMaxScale/values[Matrix.MSCALE_X];
            mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);
            return scale;
        }
		/**  
         *   ����Matrix
         */
		private void resetmatrix(){
			if (checkReset()) {
				mCurrentMatrix.set(mMatrix);
				setImageMatrix(mCurrentMatrix);
			}
		}
		/**  
         *  �ж��Ƿ���Ҫ����
         *  @return  ��ǰ���ż���С��ģ�����ż���ʱ������ 
         */
		private boolean checkReset(){
			float[] values=new float[9];
			getImageMatrix().getValues(values);
			//��ȡ��ǰX�����ż���
			float scale=values[Matrix.MSCALE_X];
			//��ȡģ���X�����ż����������Ƚ�
			mMatrix.getValues(values);			
			return scale<values[Matrix.MSCALE_X];
			
		}
		/**  
	     *  �ж��Ƿ�֧��Matrix
	     */
		private void isMatrixEnable(){
			//�����س���ʱ����������
			if (getScaleType()!=ScaleType.CENTER) {
				setScaleType(ScaleType.MATRIX);			
			}else {
				 mMode=MODE_UNABLE;//����Ϊ��֧������
			}
		}
		 /**  
         *  ����������ָ��ľ���
         *  @param event
         *  @return   
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** ʹ�ù��ɶ���������֮��ľ��� */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /**  
         *   ˫��ʱ����
         */
        public void onDoubleClick(){
            float scale=isZoomChanged()?1:mDobleClickScale;
            mCurrentMatrix.set(mMatrix);//��ʼ��Matrix
            mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);    
            setImageMatrix(mCurrentMatrix);
        }
	}
	public void setDragMatrix(MotionEvent event){
		if (isZoomChanged()) {
			float dx=event.getX()-startPoint.x;
			float dy=event.getY()-startPoint.y;
			//�����˫����ͻ,����10f�������϶�
			if (Math.sqrt(dx*dx+dy*dy)>10f) {
				startPoint.set(event.getX(), event.getY());
				//�ڵ�ǰ�������ƶ�
				mCurrentMatrix.set(getImageMatrix());
				float[] values=new float[9];
				mCurrentMatrix.getValues(values);
				dx=checkDxBound(values,dx);
                dy=checkDyBound(values,dy);    
                mCurrentMatrix.postTranslate(dx, dy);
                setImageMatrix(mCurrentMatrix);
			}
		}
	}
	 /**  
     *  �ж����ż����Ƿ��Ǹı��
     *  @return   true��ʾ�ǳ�ʼֵ,false��ʾ��ʼֵ
     */
	private boolean isZoomChanged(){
		float[] values=new float[9];
		getImageMatrix().getValues(values);
		//��ȡ��ǰX�����ż���
		float scale=values[Matrix.MSCALE_X];
		//��ȡģ���X�����ż����������Ƚ�
		mMatrix.getValues(values);		
		return scale!=values[Matrix.MSCALE_X];
	}
	/**  
     *  �͵�ǰ����Աȣ�����dy��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
     *  @param values
     *  @param dy
     *  @return   
     */
	private float checkDyBound(float[] values,float dy){
		float height=getHeight();
		if (mImageHeight*values[Matrix.MSCALE_Y]<height) {
			return 0;
		}
		if(values[Matrix.MTRANS_Y]+dy>0)
            dy=-values[Matrix.MTRANS_Y];
        else if(values[Matrix.MTRANS_Y]+dy<-(mImageHeight*values[Matrix.MSCALE_Y]-height))
            dy=-(mImageHeight*values[Matrix.MSCALE_Y]-height)-values[Matrix.MTRANS_Y];
        return dy;
	}
	/**  
     *�͵�ǰ����Աȣ�����dx��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
     *  @param values
     *  @param dx
     *  @return   
     */
    private float checkDxBound(float[] values,float dx) {
        float width=getWidth();
        if(mImageWidth*values[Matrix.MSCALE_X]<width)
            return 0;
        if(values[Matrix.MTRANS_X]+dx>0)
            dx=-values[Matrix.MTRANS_X];
        else if(values[Matrix.MTRANS_X]+dx<-(mImageWidth*values[Matrix.MSCALE_X]-width))
            dx=-(mImageWidth*values[Matrix.MSCALE_X]-width)-values[Matrix.MTRANS_X];
        return dx;
    }
    /**  
     *  ��������Matrix
     *  @param event   
     */
    private void setZoomMatrix(MotionEvent event) {
        //ֻ��ͬʱ�����������ʱ���ִ��
        if(event.getPointerCount()<2) return;
        float endDis = distance(event);// ��������
        if (endDis > 10f) { // ������ָ��£��һ���ʱ�����ش���10
            float scale = endDis / mStartDis;// �õ����ű���
            mStartDis=endDis;//���þ���
            mCurrentMatrix.set(getImageMatrix());//��ʼ��Matrix
            float[] values=new float[9];
            mCurrentMatrix.getValues(values);

            scale = checkMaxScale(scale, values);
            setImageMatrix(mCurrentMatrix);    
        }
    }
    /**  
     *  ����scale��ʹͼ�����ź󲻻ᳬ�������
     *  @param scale
     *  @param values
     *  @return   
     */
    private float checkMaxScale(float scale, float[] values) {
        if(scale*values[Matrix.MSCALE_X]>mMaxScale) 
            scale=mMaxScale/values[Matrix.MSCALE_X];
        mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);
        return scale;
    }
	/**  
     *   ����Matrix
     */
	private void resetmatrix(){
		if (checkReset()) {
			mCurrentMatrix.set(mMatrix);
			setImageMatrix(mCurrentMatrix);
		}
	}
	/**  
     *  �ж��Ƿ���Ҫ����
     *  @return  ��ǰ���ż���С��ģ�����ż���ʱ������ 
     */
	private boolean checkReset(){
		float[] values=new float[9];
		getImageMatrix().getValues(values);
		//��ȡ��ǰX�����ż���
		float scale=values[Matrix.MSCALE_X];
		//��ȡģ���X�����ż����������Ƚ�
		mMatrix.getValues(values);			
		return scale<values[Matrix.MSCALE_X];
		
	}
	/**  
     *  �ж��Ƿ�֧��Matrix
     */
	private void isMatrixEnable(){
		//�����س���ʱ����������
		if (getScaleType()!=ScaleType.CENTER) {
			setScaleType(ScaleType.MATRIX);			
		}else {
			 mMode=MODE_UNABLE;//����Ϊ��֧������
		}
	}
	 /**  
     *  ����������ָ��ľ���
     *  @param event
     *  @return   
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** ʹ�ù��ɶ���������֮��ľ��� */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**  
     *   ˫��ʱ����
     */
    public void onDoubleClick(){
        float scale=isZoomChanged()?1:mDobleClickScale;
        mCurrentMatrix.set(mMatrix);//��ʼ��Matrix
        mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);    
        setImageMatrix(mCurrentMatrix);
    }
}
