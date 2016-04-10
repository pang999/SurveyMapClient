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
	
	 /** 拖拉照片模式 */
    private static final int MODE_DRAG = 1;
    /** 放大缩小照片模式 */
    private static final int MODE_ZOOM = 2;
    /**  不支持Matrix */ 
    private static final int MODE_UNABLE=3;
    /**   最大缩放级别*/ 
    float mMaxScale=6;
    /**   双击时的缩放级别*/ 
    float mDobleClickScale=2;
    private int mMode = 0;// 
    /**  缩放开始时的手指间距 */ 
    private float mStartDis;
    /**   当前Matrix*/ 
    private Matrix mCurrentMatrix = new Matrix(); 
    /** 用于记录开始时候的坐标位置 */
    private PointF startPoint = new PointF();
    
    /**  模板Matrix，用以初始化 */ 
    private Matrix mMatrix=new Matrix();
    /**  图片长度*/ 
    private float mImageWidth;
    /**  图片高度 */ 
    private float mImageHeight;
    LinesModel linesModel;    
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;// 画布的画笔
    private Bitmap  mBitmap;
    private Paint   mPaint;// 真实的画笔
    private Bitmap background;
    
    
	public MatrixImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
//		MatrixTouchListener mListener=new MatrixTouchListener();
//        setOnTouchListener(mListener);
        //背景设置为balck
//		mBitmap=new Bi
		initCanvas();
//		this.background=CameraActivity.smallBitmap;
//		Logger.i("执行顺序", "MatrixImageView__smallBitmap="+CameraActivity.smallBitmap);
//		Logger.i("执行顺序", "MatrixImageView");
//		mBitmap
//        setBackgroundColor(Color.BLACK);
        //将缩放类型设置为FIT_CENTER，表示把图片按比例扩大/缩小到View的宽度，居中显示
        setScaleType(ScaleType.FIT_CENTER);
        linesModel=new LinesModel();
        
        invalidate();

	}
	//初始化画布
    public void initCanvas(){
         
        mPaint = ViewContans.generatePaint(Color.RED, 5,true);  
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);    
        //画布大小 
        mBitmap = Bitmap.createBitmap(Contants.sreenWidth*3, Contants.screenHeight*3, 
            Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中      
        mCanvas.drawColor(Color.WHITE);
//        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
         
    }
    public void setimagebitmap(Bitmap bm){
//    	this.mBitmap=bm;
    	this.background=bm;
		Logger.i("执行顺序", "background="+background);
		Logger.i("执行顺序", "MatrixImageView");
    }
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Logger.i("执行顺序", "draw");
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
        //设置完图片后，获取该图片的坐标变换矩阵
        mMatrix.set(getImageMatrix());
        float[] values=new float[9];
        mMatrix.getValues(values);
        //图片宽度为屏幕宽度除缩放倍数
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
			//设置拖动模式
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

		 /** 拖拉照片模式 */
        private static final int MODE_DRAG = 1;
        /** 放大缩小照片模式 */
        private static final int MODE_ZOOM = 2;
        /**  不支持Matrix */ 
        private static final int MODE_UNABLE=3;
        /**   最大缩放级别*/ 
        float mMaxScale=6;
        /**   双击时的缩放级别*/ 
        float mDobleClickScale=2;
        private int mMode = 0;// 
        /**  缩放开始时的手指间距 */ 
        private float mStartDis;
        /**   当前Matrix*/ 
        private Matrix mCurrentMatrix = new Matrix(); 
        /** 用于记录开始时候的坐标位置 */
        private PointF startPoint = new PointF();
        
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			float rx=event.getX();
			float ry=event.getY();
			switch (event.getActionMasked()) {
			
			case MotionEvent.ACTION_DOWN:
				//设置拖动模式
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
				//避免和双击冲突,大于10f才算是拖动
				if (Math.sqrt(dx*dx+dy*dy)>10f) {
					startPoint.set(event.getX(), event.getY());
					//在当前基础上移动
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
         *  判断缩放级别是否是改变过
         *  @return   true表示非初始值,false表示初始值
         */
		private boolean isZoomChanged(){
			float[] values=new float[9];
			getImageMatrix().getValues(values);
			//获取当前X轴缩放级别
			float scale=values[Matrix.MSCALE_X];
			//获取模板的X轴缩放级别，两者做比较
			mMatrix.getValues(values);		
			return scale!=values[Matrix.MSCALE_X];
		}
		/**  
         *  和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
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
         *和当前矩阵对比，检验dx，使图像移动后不会超出ImageView边界
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
         *  设置缩放Matrix
         *  @param event   
         */
        private void setZoomMatrix(MotionEvent event) {
            //只有同时触屏两个点的时候才执行
            if(event.getPointerCount()<2) return;
            float endDis = distance(event);// 结束距离
            if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                float scale = endDis / mStartDis;// 得到缩放倍数
                mStartDis=endDis;//重置距离
                mCurrentMatrix.set(getImageMatrix());//初始化Matrix
                float[] values=new float[9];
                mCurrentMatrix.getValues(values);

                scale = checkMaxScale(scale, values);
                setImageMatrix(mCurrentMatrix);    
            }
        }
        /**  
         *  检验scale，使图像缩放后不会超出最大倍数
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
         *   重置Matrix
         */
		private void resetmatrix(){
			if (checkReset()) {
				mCurrentMatrix.set(mMatrix);
				setImageMatrix(mCurrentMatrix);
			}
		}
		/**  
         *  判断是否需要重置
         *  @return  当前缩放级别小于模板缩放级别时，重置 
         */
		private boolean checkReset(){
			float[] values=new float[9];
			getImageMatrix().getValues(values);
			//获取当前X轴缩放级别
			float scale=values[Matrix.MSCALE_X];
			//获取模板的X轴缩放级别，两者做比较
			mMatrix.getValues(values);			
			return scale<values[Matrix.MSCALE_X];
			
		}
		/**  
	     *  判断是否支持Matrix
	     */
		private void isMatrixEnable(){
			//当加载出错时，不可缩放
			if (getScaleType()!=ScaleType.CENTER) {
				setScaleType(ScaleType.MATRIX);			
			}else {
				 mMode=MODE_UNABLE;//设置为不支持手势
			}
		}
		 /**  
         *  计算两个手指间的距离
         *  @param event
         *  @return   
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /**  
         *   双击时触发
         */
        public void onDoubleClick(){
            float scale=isZoomChanged()?1:mDobleClickScale;
            mCurrentMatrix.set(mMatrix);//初始化Matrix
            mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);    
            setImageMatrix(mCurrentMatrix);
        }
	}
	public void setDragMatrix(MotionEvent event){
		if (isZoomChanged()) {
			float dx=event.getX()-startPoint.x;
			float dy=event.getY()-startPoint.y;
			//避免和双击冲突,大于10f才算是拖动
			if (Math.sqrt(dx*dx+dy*dy)>10f) {
				startPoint.set(event.getX(), event.getY());
				//在当前基础上移动
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
     *  判断缩放级别是否是改变过
     *  @return   true表示非初始值,false表示初始值
     */
	private boolean isZoomChanged(){
		float[] values=new float[9];
		getImageMatrix().getValues(values);
		//获取当前X轴缩放级别
		float scale=values[Matrix.MSCALE_X];
		//获取模板的X轴缩放级别，两者做比较
		mMatrix.getValues(values);		
		return scale!=values[Matrix.MSCALE_X];
	}
	/**  
     *  和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
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
     *和当前矩阵对比，检验dx，使图像移动后不会超出ImageView边界
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
     *  设置缩放Matrix
     *  @param event   
     */
    private void setZoomMatrix(MotionEvent event) {
        //只有同时触屏两个点的时候才执行
        if(event.getPointerCount()<2) return;
        float endDis = distance(event);// 结束距离
        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
            float scale = endDis / mStartDis;// 得到缩放倍数
            mStartDis=endDis;//重置距离
            mCurrentMatrix.set(getImageMatrix());//初始化Matrix
            float[] values=new float[9];
            mCurrentMatrix.getValues(values);

            scale = checkMaxScale(scale, values);
            setImageMatrix(mCurrentMatrix);    
        }
    }
    /**  
     *  检验scale，使图像缩放后不会超出最大倍数
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
     *   重置Matrix
     */
	private void resetmatrix(){
		if (checkReset()) {
			mCurrentMatrix.set(mMatrix);
			setImageMatrix(mCurrentMatrix);
		}
	}
	/**  
     *  判断是否需要重置
     *  @return  当前缩放级别小于模板缩放级别时，重置 
     */
	private boolean checkReset(){
		float[] values=new float[9];
		getImageMatrix().getValues(values);
		//获取当前X轴缩放级别
		float scale=values[Matrix.MSCALE_X];
		//获取模板的X轴缩放级别，两者做比较
		mMatrix.getValues(values);			
		return scale<values[Matrix.MSCALE_X];
		
	}
	/**  
     *  判断是否支持Matrix
     */
	private void isMatrixEnable(){
		//当加载出错时，不可缩放
		if (getScaleType()!=ScaleType.CENTER) {
			setScaleType(ScaleType.MATRIX);			
		}else {
			 mMode=MODE_UNABLE;//设置为不支持手势
		}
	}
	 /**  
     *  计算两个手指间的距离
     *  @param event
     *  @return   
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**  
     *   双击时触发
     */
    public void onDoubleClick(){
        float scale=isZoomChanged()?1:mDobleClickScale;
        mCurrentMatrix.set(mMatrix);//初始化Matrix
        mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);    
        setImageMatrix(mCurrentMatrix);
    }
}
